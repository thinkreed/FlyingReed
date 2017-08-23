package think.reed.refitshopmodule.mediacodec;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * multi extractor codec, smooth switch tiny media files
 * Created by thinkreed on 2017/8/21.
 */

public class MultiExtractorCodec {

    public static final int ERROR_TRACK_INDEX = -1;

    public static final int ERROR_END_OF_STREAM = -2;

    public static final int ERROR_DEQUEUE_INPUT = -3;

    private List<String> mPathList;

    private int mCurIndex = -1;

    private int trackIndex = -1;

    private SparseArray<MediaExtractor> mExtractors = new SparseArray<>();

    private MediaCodec mDecoder;

    private ByteBuffer[] mInputBuffers;

    private ByteBuffer[] mOutputBuffers;

    public MultiExtractorCodec() {
        initExtractors();
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
            extractor.setDataSource(mPathList.remove(0));
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

    private MediaCodec configCodec(MediaExtractor extractor) {
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

    private int selectAudioTrack(MediaExtractor extractor) {
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) return i;
        }
        return ERROR_TRACK_INDEX;
    }

    private MediaExtractor getExtractor() {
        return mExtractors.get(mCurIndex);
    }

    private int fillInputBuffer() {
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
                return ERROR_END_OF_STREAM;
            } else {
                long presentationTimeUs = extractor.getSampleTime();
                mDecoder.queueInputBuffer(inputBufferId, 0, chunkSize,
                        presentationTimeUs, 0);
                extractor.advance();
                return chunkSize;
            }
        }
        return ERROR_DEQUEUE_INPUT;
    }

}
