package think.reed.refitshopmodule.mediacodec;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.util.SparseArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * multi extractor codec, smooth switch tiny media files
 * Created by thinkreed on 2017/8/21.
 */

public class MultiExtractorCodec {

    private byte[] mBuf;

    private boolean mIsNeedSwitch = false;

    private MediaCodec.BufferInfo mInfo = new MediaCodec.BufferInfo();

    public MultiExtractorCodec(CopyOnWriteArrayList<String> bufferList) {
        mPathList = bufferList;
        mBuf = new byte[1024 * 20];
    }

    public int getAudioData(Pair p, int pos) {

        if (mPathList.size() < 2) {
            return -1;
        }

        if (mDecoder == null) {

        }

        p.setData(mBuf);
        p.setSize(0);

        int ret = fillInputBuffer();

        if (ret < 0) {
            return -1;
        }
        return processOutputBuffer(p, pos);
    }

    private int fillInputBuffer() {
        if (mDecoder == null) {
            Log.d("thinkreed", "int output decoder is null");
            return -1;
        }
        int inputBufferId = mDecoder.dequeueInputBuffer(10000);
        if (inputBufferId >= 0) {
            ByteBuffer destBuffer;
            if (android.os.Build.VERSION.SDK_INT
                    < android.os.Build.VERSION_CODES.LOLLIPOP) {
                destBuffer = mInputBuffers[inputBufferId];
            } else {
                destBuffer = mDecoder.getInputBuffer(inputBufferId);
            }
            if (destBuffer == null) {
                return -1;
            }
            int chunkSize = mCurExtractor.readSampleData(destBuffer, 0);
            if (chunkSize < 0) {
                mIsNeedSwitch = true;
            } else {
                long presentationTimeUs = mCurExtractor.getSampleTime();
                mDecoder.queueInputBuffer(inputBufferId, 0, chunkSize,
                        presentationTimeUs, 0);
                mCurExtractor.advance();
            }

            return 0;
        } else if (mIsNeedSwitch) {
            Log.d("thinkreed", "input buffer id is " + inputBufferId);

            File file = new File(mPathList.get(0));
            if (file.exists()) file.delete();
            mPathList.remove(0);
            switchExtractor();
            configCodec();
            mIsNeedSwitch = false;
            return -1;
        }
        Log.d("thinkreed", "last ,error -5");
        return -1;
    }

    private int processOutputBuffer(Pair p, int pos) {
        if (mDecoder == null) {
            return -1;
        }
        int dataSize = 0;
        int outputBufferId = mDecoder.dequeueOutputBuffer(mInfo, 10000);
        if (outputBufferId >= 0) {
            ByteBuffer byteBuffer;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                byteBuffer = mDecoder.getOutputBuffer(outputBufferId);
            } else {
                byteBuffer = mOutputBuffers[outputBufferId];
            }
            int bufLen = mInfo.size;
            if (mBuf.length < bufLen + pos) {
                mBuf = new byte[bufLen + pos];
                p.setData(mBuf);
            }
            if (byteBuffer == null) {
                return -1;
            }
            byteBuffer.get(mBuf, pos, bufLen);
            byteBuffer.clear();
            mDecoder.releaseOutputBuffer(outputBufferId, false);

            if (bufLen > 0) {
                p.setSize(bufLen);
                dataSize = bufLen;
            }
        } else if (outputBufferId == MediaCodec.INFO_TRY_AGAIN_LATER) {
            Log.d("thinkreed", "try again");
        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
            Log.d("thinkreed", "buffer change");
            mOutputBuffers = mDecoder.getOutputBuffers();
        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            Log.d("thinkreed", "format change");
            MediaFormat format = mDecoder.getOutputFormat();
        }
        return dataSize;
    }
}
