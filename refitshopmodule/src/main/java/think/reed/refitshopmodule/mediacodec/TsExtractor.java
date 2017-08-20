package think.reed.refitshopmodule.mediacodec;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by huweijie01 on 2017/8/20.
 */

public class TsExtractor {

    public void extractor(String path) {
        try {
            RandomAccessFile ra = new RandomAccessFile(path, "rw");
            byte[] packetBuf = new byte[188];
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

                if (pid == 0) {
                    if (payloadStart == 0) {
                        return;
                    }
                    ByteBuffer byteBuffer = ByteBuffer.wrap(packetBuf, 4 + payloadOff,
                            184 - payloadOff);
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
