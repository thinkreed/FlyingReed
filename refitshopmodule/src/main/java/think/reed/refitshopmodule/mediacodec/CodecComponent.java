package think.reed.refitshopmodule.mediacodec;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;

/**
 * Created by thinkreed on 2017/9/1.
 */

public class CodecComponent {

    private MediaCodec mediaCodec;
    private MediaExtractor extractor;

    private String mMime = "";

    private int mChannelConfig = AudioFormat.CHANNEL_OUT_STEREO;

    private MediaFormat mMediaFormat;

    private int mSampleRate = 32000;

    private ByteBuffer[] mInputBuffers;

    private ByteBuffer[] mOutputBuffers;

    public ByteBuffer[] getInputBuffers() {
        return mInputBuffers;
    }

    public ByteBuffer[] getOutputBuffers() {
        return mOutputBuffers;
    }

    public MediaCodec getMediaCodec() {
        return mediaCodec;
    }

    public AudioTrack getAudioMinBufSizeLocal() {
        return AudioTrack.getMinBufferSize(mSampleRate,
                mChannelConfig, AudioFormat.ENCODING_PCM_16BIT);
        return AudioTrack(AudioManager.STREAM_MUSIC, multiDecoder.sampleRate, multiDecoder.channelConfig,
                AudioFormat.ENCODING_PCM_16BIT, audioMinBufSizeLocal * 2, AudioTrack.MODE_STREAM)
    }

    public MediaExtractor getExtractor() {
        return extractor;
    }

    public void init(String path) {
        configExtractor(path);
        configCodec();
    }

    private int selectAudioTrack(MediaExtractor extractor) {
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
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

    private int configCodec() {


        if (mediaCodec == null) {
            if (mMime == null) {
                return -1;
            }
            try {
                mediaCodec = MediaCodec.createDecoderByType(mMime);
            } catch (IOException e) {
                return -1;
            }
        }

        mediaCodec.stop();

        mediaCodec.configure(mMediaFormat, null, null, 0);
        mediaCodec.start();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            mInputBuffers = mediaCodec.getInputBuffers();
            mOutputBuffers = mediaCodec.getOutputBuffers();
        }
        return 0;
    }

    private int configExtractor(String path) {
        try {
            extractor.release();
            extractor = new MediaExtractor();
            extractor.setDataSource(path);
            return selectAudioTrack(extractor);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
