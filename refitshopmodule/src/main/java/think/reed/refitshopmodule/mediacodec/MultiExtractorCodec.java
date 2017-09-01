package think.reed.refitshopmodule.mediacodec;

import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

/**
 * multi extractor codec, smooth switch tiny media files
 * Created by thinkreed on 2017/8/21.
 */

public class MultiExtractorCodec {

    private byte[] mBuf;

    private boolean mIsNeedSwitch = false;

    private MediaCodec.BufferInfo mInfo = new MediaCodec.BufferInfo();

    private CodecSwitcher mSwitcher;

    public MultiExtractorCodec(CopyOnWriteArrayList<String> bufferList) {
        mSwitcher = new CodecSwitcher(bufferList);
        mBuf = new byte[1024 * 20];
    }

    public CodecSwitcher getSwitcher() {
        return mSwitcher;
    }

    public int getAudioData(Pair p, int pos) {

        p.setData(mBuf);
        p.setSize(0);

        int ret = fillInputBuffer();

        if (ret < 0) {
            return -1;
        }
        return processOutputBuffer(p, pos);
    }

    private int fillInputBuffer() {
        if (mSwitcher.getCurComponent().getMediaCodec() == null) {
            Log.d("thinkreed", "int output decoder is null");
            return -1;
        }
        int inputBufferId = mSwitcher.getCurComponent().getMediaCodec().dequeueInputBuffer(10000);
        if (inputBufferId >= 0) {
            ByteBuffer destBuffer;
            if (android.os.Build.VERSION.SDK_INT
                    < android.os.Build.VERSION_CODES.LOLLIPOP) {
                destBuffer = mSwitcher.getCurComponent().getInputBuffers()[inputBufferId];
            } else {
                destBuffer = mSwitcher.getCurComponent().getMediaCodec().getInputBuffer(inputBufferId);
            }
            if (destBuffer == null) {
                return -1;
            }
            int chunkSize = mSwitcher.getCurComponent().getExtractor().readSampleData(destBuffer, 0);
            if (chunkSize < 0) {
                mIsNeedSwitch = true;
            } else {
                long presentationTimeUs = mSwitcher.getCurComponent().getExtractor().getSampleTime();
                mSwitcher.getCurComponent().getMediaCodec().queueInputBuffer(inputBufferId, 0, chunkSize,
                        presentationTimeUs, 0);
                mSwitcher.getCurComponent().getExtractor().advance();
            }

            return 0;
        } else if (mIsNeedSwitch) {
            Log.d("thinkreed", "input buffer id is " + inputBufferId);
            mSwitcher.switchCodecComponent();
            mIsNeedSwitch = false;
            return -1;
        }
        Log.d("thinkreed", "last ,error -5");
        return -1;
    }

    private int processOutputBuffer(Pair p, int pos) {
        if (mSwitcher.getCurComponent().getMediaCodec() == null) {
            return -1;
        }
        int dataSize = 0;
        int outputBufferId = mSwitcher.getCurComponent().getMediaCodec().dequeueOutputBuffer(mInfo, 10000);
        if (outputBufferId >= 0) {
            ByteBuffer byteBuffer;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                byteBuffer = mSwitcher.getCurComponent().getMediaCodec().getOutputBuffer(outputBufferId);
            } else {
                byteBuffer = mSwitcher.getCurComponent().getOutputBuffers()[outputBufferId];
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
            mSwitcher.getCurComponent().getMediaCodec().releaseOutputBuffer(outputBufferId, false);

            if (bufLen > 0) {
                p.setSize(bufLen);
                dataSize = bufLen;
            }
        } else if (outputBufferId == MediaCodec.INFO_TRY_AGAIN_LATER) {
            Log.d("thinkreed", "try again");
        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
            Log.d("thinkreed", "buffer change");
            mSwitcher.getCurComponent().getMediaCodec().getOutputBuffers();
        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            Log.d("thinkreed", "format change");
            MediaFormat format = mSwitcher.getCurComponent().getMediaCodec().getOutputFormat();
        }
        return dataSize;
    }
}
