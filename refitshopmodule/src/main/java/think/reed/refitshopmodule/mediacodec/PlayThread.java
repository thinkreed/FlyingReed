package think.reed.refitshopmodule.mediacodec;

import android.media.AudioTrack;
import android.util.Log;

/**
 * Created by huweijie01 on 2017/8/25.
 */

public class PlayThread extends Thread {

    private AudioTrack mAudioTrack;

    private MultiExtractorCodec mDecoder;

    private Pair p = new Pair();

    private final Object mPlayLock = new Object();

    public PlayThread(AudioTrack audioTrack, MultiExtractorCodec decoder) {
        this.mAudioTrack = audioTrack;
        this.mDecoder = decoder;
    }

    @Override
    public void run() {
        while (true) {
            playCheck();
            int dataSize = mDecoder.getAudioData(p, 12);
            Log.d("thinkreed", "data size is " + dataSize);
            switch (dataSize) {
                case MultiExtractorCodec.ERROR_DEQUEUE_INPUT:
                    break;
                case MultiExtractorCodec.ERROR_DEQUEUE_OUTPUT:
                    break;
                case MultiExtractorCodec.ERROR_END_OF_OUT_STREAM:
                    break;
                case MultiExtractorCodec.ERROR_END_OF_STREAM:
                    break;
                case MultiExtractorCodec.ERROR_TRACK_INDEX:
                    synchronized (mPlayLock) {
                        try {
                            mPlayLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    if (p.getSize() > 0) {
                        mAudioTrack.write(p.getData(), 12, p.getSize());
                    }
                    break;
            }
        }
    }

    private void playCheck() {
        synchronized (mPlayLock) {
            if (mAudioTrack == null || mAudioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
                try {
                    mPlayLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void play() {
        if (mAudioTrack != null && mAudioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
            mAudioTrack.play();
            Log.d("thinkreed", "play invoked");
            synchronized (mPlayLock) {
                mPlayLock.notify();
            }
        }
    }
}
