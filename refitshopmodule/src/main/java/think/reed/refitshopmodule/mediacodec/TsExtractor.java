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

    public void extractor(String datah) {
        programs.clear();
        try {
            RandomAccessFile ra = new RandomAccessFile(datah, "rw");
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
                } else if (channels.get(pid, -1) != -1) {
                    parsePES(channels.get(pid), byteBuffer);
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private void fix172Video(ByteBuffer data) {

    }

    private void fix818Video(ByteBuffer data) {

    }

    private void fix172Audio(ByteBuffer data) {

    }

    private void fix818Audio(ByteBuffer data) {

    }

    private void parsePES(int streamType, ByteBuffer data) {
        switch (streamType) {
            case 0x01://iso/iec 11172 video
                fix172Video(data);
                break;
            case 0x02://13818-2 video
                fix818Video(data);
                break;
            case 0x03://11172 audio
                fix172Audio(data);
                break;
            case 0x04://13818-3 audio
                fix818Audio(data);
                break;
            default:
                break;
        }
    }

    private void parsePMT(ByteBuffer data) {
        int tableId = data.get(0);
        if (tableId != 0x02) {
            return;
        }
        int sectionLength = (data.get(1) & 0x0f) << 8 | data.get(2);
        int programNum = data.get(3) << 8 | data.get(4);
        int pcrPid = (data.get(8) << 8 | data.get(9)) & 0x1fff;
        int programInfoLength = (data.get(10) & 0x0f) << 8 | data.get(11);

        int pos = 12;
        if (programInfoLength != 0) {
            pos += programInfoLength;
        }
        for (; pos < sectionLength + 2 - 4; ) {
            int streamType = data.get(pos);
            int elePID = ((data.get(pos + 1 << 8) | data.get(pos + 2))) & 0x1fff;
            int esInfoLength = (data.get(pos + 3) & 0x0f) << 8 | data.get(pos + 4);
            if (esInfoLength != 0) {
                pos += esInfoLength;
            }
            pos += 5;
            channels.put(elePID, streamType);
        }
    }

    private void parsePAT(ByteBuffer data) {
        int off = data.position();
        int tableId = data.get(off);
        if (tableId != 0x00) {
            return;
        }
        int sectionSyntaxIndicator = data.get(off + 1) >> 7;
        int sectionLength = (data.get(off + 1) & 0x0f) << 8 | data.get(off + 2);
        int streamId = data.get(off + 3) << 8 | data.get(off + 4);
        int currentOrNext = data.get(off + 5) & 0x01;
        for (int i = 0; i < sectionLength - 12; i += 4) {
            int programNum = data.get(off + 8 + i) << 8 | data.get(off + 11 + i);
            if (programNum == 0) {

            } else {
                programs.put(data.get(off + 10 + i) << 8 | data.get(off + 11 + i), programNum);
            }
        }
    }
}
