package think.reed.refitshopmodule.mediacodec;

import android.util.Log;
import android.util.SparseIntArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by huweijie01 on 2017/8/20.
 */

public class TsExtractor {

    private SparseIntArray programs = new SparseIntArray();
    private SparseIntArray channels = new SparseIntArray();

    public void extractor(String path) {
        programs.clear();
        try {
            RandomAccessFile ra = new RandomAccessFile(path, "rw");
            byte[] packetBuf = new byte[188];
            ByteBuffer byteBuffer;

            //read a ts packet
            while (ra.read(packetBuf) == 188) {
                //check sync byte
                if ((packetBuf[0] & 0xff) != 0x47) {
                    continue;
                }
                //read pid
                int pidFlags = ((packetBuf[1] & 0xff) << 8) | (packetBuf[2] & 0xff);
                int pid = pidFlags & 0x1fff;
                int payloadStart = (pidFlags >> 14) & 0x1;
                Log.e("thinkreed", "the pid is " + pid);
                int lastFlag = packetBuf[3] & 0xff;
                int counter = lastFlag & 0xf;
                int payloadOff = 0;
                if ((lastFlag & 0x20) != 0) {
                    payloadOff = (packetBuf[4 + payloadOff] & 0xff) + 1;
                }
                if (payloadStart == 1) {
                    payloadOff += (packetBuf[4 + payloadOff] & 0xff) + 1;
                }

                if (payloadStart == 0) {
                    return;
                }
                byteBuffer = ByteBuffer.wrap(packetBuf, 4 + payloadOff,
                        184 - payloadOff);

                if (pid == 0) {

                    parsePAT(byteBuffer);
                } else if (programs.get(pid) != 0) {
                    parsePMT(byteBuffer);
                } else if (channels.get(pid) != 0) {

                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private void parsePMT(ByteBuffer data) {
        byte[] pat = data.array();
        int tableId = pat[0];
        if (tableId != 0x02) {
            return;
        }
        int sectionLength = (pat[1] & 0x0f) << 8 | pat[2];
        int programNum = pat[3] << 8 | pat[4];
        int pcrPid = (pat[8] << 8 | pat[9]) & 0x1fff;
        int programInfoLength = (pat[10] & 0x0f) << 8 | pat[11];

        int pos = 12;
        if (programInfoLength != 0) {
            pos += programInfoLength;
        }
        for (; pos < sectionLength + 2 - 4; ) {
            int streamType = pat[pos];
            int elePID = ((pat[pos + 1] << 8) | pat[pos + 2]) & 0x1fff;
            int esInfoLength = (pat[pos + 3] & 0x0f) << 8 | pat[pos + 4];
            if (esInfoLength != 0) {
                pos += esInfoLength;
            }
            pos += 5;
            channels.put(elePID, streamType);
        }
    }

    private void parsePAT(ByteBuffer data) {
        byte[] pat = data.array();
        int tableId = pat[0];
        if (tableId != 0x00) {
            return;
        }
        int sectionSyntaxIndicator = pat[1] >> 7;
        int sectionLength = (pat[1] & 0x0f) << 8 | pat[2];
        int streamId = pat[3] << 8 | pat[4];
        int currentOrNext = pat[5] & 0x01;
        for (int i = 0; i < sectionLength - 12; i += 4) {
            int programNum = pat[8 + i] << 8 | pat[11 + i];
            if (programNum == 0) {

            } else {
                programs.put(pat[10 + i] << 8 | pat[11 + i], programNum);
            }
        }
    }
}
