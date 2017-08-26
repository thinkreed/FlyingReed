package think.reed.refitshopmodule.mediacodec;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.util.SparseArray;

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

    private List<String> mPathList;

    private int mCurIndex = 0;

    private SparseArray<MediaExtractor> mExtractors = new SparseArray<>();

    private MediaCodec mDecoder;

    private ByteBuffer[] mInputBuffers;

    private ByteBuffer[] mOutputBuffers;

    private String mMime = "";

    private int mChannelConfig = AudioFormat.CHANNEL_OUT_STEREO;

    private MediaFormat mMediaFormat;

    private int mSampleRate = 32000;

    private byte[] mBuf;

    private boolean mIsNeedSwitch = false;

    private FileInputStream mWorkFile;

    private MediaExtractor mCurExtractor;

    private MediaCodec.BufferInfo mInfo = new MediaCodec.BufferInfo();

    public MultiExtractorCodec(CopyOnWriteArrayList<String> bufferList) {
        initExtractors();
        mPathList = bufferList;
        mBuf = new byte[1024 * 20];
    }

    public String getMime() {
        return mMime;
    }

    public int getChannelConfig() {
        return mChannelConfig;
    }

    public int getSampleRate() {
        return mSampleRate;
    }

    public int getAudioData(Pair p, int pos) {

        if (mPathList.size() < 2) {
            return -1;
        }

        if (mDecoder == null) {
            mCurExtractor = getExtractor();
            configExtractor(mCurExtractor);
            if (configCodec() < 0) {
                return -1;
            }

        }

        p.setData(mBuf);
        p.setSize(0);

        int ret = fillInputBuffer();

        if (ret < 0) {
            return -1;
        }
        return processOutputBuffer(p, pos);
    }

    private void initExtractors() {
        for (int i = 0; i < 2; i++) {
            mExtractors.put(i, new MediaExtractor());
        }
    }

    private int configExtractor(MediaExtractor extractor) {
        try {
            extractor.setDataSource(mPathList.get(0));
            return selectAudioTrack(extractor);
        } catch (IOException e) {
            Log.d("thinkreed", "io exception");
            return -1;
        }
    }

    private void switchExtractor() {
        Log.d("thinkreed", "switch extractor");
//        mCurIndex = (mCurIndex + 1) % mExtractors.size();
//        mCurExtractor = getExtractor();
        configExtractor(mCurExtractor);
    }

    private int configCodec() {


        if (mDecoder == null) {
            if (mMime == null) {
                return -1;
            }
            try {
                mDecoder = MediaCodec.createDecoderByType(mMime);
            } catch (IOException e) {
                return -1;
            }
        }

        mDecoder.stop();

        Log.d("thinkreed", "the format is " + mMediaFormat);
        mDecoder.configure(mMediaFormat, null, null, 0);
        mDecoder.start();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            mInputBuffers = mDecoder.getInputBuffers();
            mOutputBuffers = mDecoder.getOutputBuffers();
        }
        return 0;
    }

    private int selectAudioTrack(MediaExtractor extractor) {
        Log.d("thinkreed", "select track");
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            Log.e("thinkreed", "the mime is " + mime);
            if (mime.startsWith("audio/")) {
                Log.d("thinkreed", "find audio track " + mime);
                mMime = mime;
                mMediaFormat = format;
                mChannelConfig = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                extractor.selectTrack(i);
                return i;
            }
        }
        return -1;
    }

    private MediaExtractor getExtractor() {
        return mExtractors.get(mCurIndex);
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
            
            mPathList.remove(0);
            switchExtractor();
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
