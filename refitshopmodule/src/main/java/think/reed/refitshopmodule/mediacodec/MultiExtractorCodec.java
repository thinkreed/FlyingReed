package think.reed.refitshopmodule.mediacodec;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

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

    private int mCurIndex = -1;

    private int trackIndex = -1;

    private SparseArray<MediaExtractor> mExtractors = new SparseArray<>();

    private MediaCodec mDecoder;

    private ByteBuffer[] mInputBuffers;

    private ByteBuffer[] mOutputBuffers;

    private String mMime = "";

    private int mChannelConfig = AudioFormat.CHANNEL_OUT_STEREO;

    private MediaFormat mMediaFormat;

    private int mSampleRate = 32000;

    private byte[] mBuf;

    private MediaCodec.BufferInfo mInfo = new MediaCodec.BufferInfo();

    public MultiExtractorCodec(CopyOnWriteArrayList<String> bufferList) {
        initExtractors();
        mPathList = bufferList;
        mBuf = new byte[1024 * 20];
        init();
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
        p.setData(mBuf);
        p.setSize(0);
        switch (fillInputBuffer()) {
            case ERROR_DEQUEUE_INPUT:
                break;
            case ERROR_TRACK_INDEX:
                break;
            case ERROR_END_OF_STREAM:
                mPathList.remove(0);
                if (configCodec(switchExtractor()) == null) {
                    return ERROR_TRACK_INDEX;
                }
                break;
        }
        return processOutputBuffer(p, pos);
    }

    private boolean init() {
        return configCodec(switchExtractor()) != null;
    }

    private void initExtractors() {
        for (int i = 0; i < 2; i++) {
            mExtractors.put(i, new MediaExtractor());
        }
    }

    @Nullable
    private MediaExtractor switchExtractor() {
        try {
            mCurIndex = (mCurIndex + 1) % mExtractors.size();
            MediaExtractor extractor = mExtractors.get(mCurIndex);
            if (mPathList.size() < 2) {
                return null;
            }
            extractor.setDataSource(mPathList.get(0));
            trackIndex = selectAudioTrack(extractor);
            return trackIndex == -1 ? null : extractor;
        } catch (NullPointerException e) {
            return null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

    }

    private int configCodec(MediaExtractor extractor) {
        if (extractor == null) {
            return null;
        }

        MediaFormat format = extractor.getTrackFormat(trackIndex);

        if (mDecoder == null) {
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime == null) {
                return null;
            }
            try {
                mDecoder = MediaCodec.createDecoderByType(mime);
            } catch (IOException e) {
                return null;
            }
        }

        mDecoder.configure(format, null, null, 0);
        mDecoder.start();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            mInputBuffers = mDecoder.getInputBuffers();
            mOutputBuffers = mDecoder.getOutputBuffers();
        }
        return mDecoder;
    }

    private ErrorCode selectAudioTrack(MediaExtractor extractor) {
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                mMime = mime;
                mMediaFormat = format;
                mChannelConfig = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                return i;
            }
        }
        return ErrorCode.ERROR_AUDIO_NOT_FOUND;
    }

    private MediaExtractor getExtractor() {
        return mExtractors.get(mCurIndex);
    }

    private ErrorCode fillInputBuffer() {
        if (mDecoder == null) {
            Log.d("thinkreed", "int output decoder is null");
            return ErrorCode.ERROR_NULL_CODEC;
        }
        int inputBufferId = mDecoder.dequeueInputBuffer(10000);
        if (inputBufferId > 0) {
            ByteBuffer destBuffer;
            if (android.os.Build.VERSION.SDK_INT
                    < android.os.Build.VERSION_CODES.LOLLIPOP) {
                destBuffer = mInputBuffers[inputBufferId];
            } else {
                destBuffer = mDecoder.getInputBuffer(inputBufferId);
            }
            MediaExtractor extractor = getExtractor();
            int chunkSize = extractor.readSampleData(destBuffer, 0);
            if (chunkSize < 0) {
                mDecoder.queueInputBuffer(inputBufferId, 0, 0,
                        0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                return ErrorCode.ERROR_END_OF_INPUT_STREAM;
            } else {
                long presentationTimeUs = extractor.getSampleTime();
                mDecoder.queueInputBuffer(inputBufferId, 0, chunkSize,
                        presentationTimeUs, 0);
                extractor.advance();
                return ErrorCode.SUCCESS;
            }
        }
        Log.d("thinkreed", "last ,error -5");
        return ErrorCode.ERROR_DEQUE_INPUT;
    }

    private ErrorCode processOutputBuffer(Pair p, int pos) {
        if (mDecoder == null) {
            return ErrorCode.ERROR_NULL_CODEC;
        }
        int outputBufferId = mDecoder.dequeueOutputBuffer(mInfo, 10000);
        if (outputBufferId > 0) {
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
                return ErrorCode.ERROR_DEQUE_OUTPUT;
            }
            byteBuffer.get(mBuf, pos, bufLen);
            byteBuffer.clear();

            if (bufLen > 0) {
                p.setSize(bufLen);
            }
            mDecoder.releaseOutputBuffer(outputBufferId, true);
            if ((mInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                return ErrorCode.ERROR_END_OF_OUT_STREAM;
            }
        } else if (outputBufferId == MediaCodec.INFO_TRY_AGAIN_LATER) {

        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
            mOutputBuffers = mDecoder.getOutputBuffers();
        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            mMediaFormat = mDecoder.getOutputFormat();
        }
        return ErrorCode.SUCCESS;
    }

    public enum ErrorCode {
        SUCCESS, ERROR_NULL_EXTRACTOR, ERROR_NULL_CODEC, ERROR_END_OF_OUT_STREAM,
        ERROR_END_OF_INPUT_STREAM, ERROR_DEQUE_OUTPUT, ERROR_DEQUE_INPUT,
        ERROR_AUDIO_NOT_FOUND
    }
}
