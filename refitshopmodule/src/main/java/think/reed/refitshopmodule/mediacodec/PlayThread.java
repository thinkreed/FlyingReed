package think.reed.refitshopmodule.mediacodec;

import android.media.AudioTrack;

/**
 * Created by huweijie01 on 2017/8/25.
 */

public class PlayThread extends Thread {

    private AudioTrack mAudioTrack;

    private MultiExtractorCodec mDecoder;

    private final Object mPlayLock = new Object();

    public PlayThread(AudioTrack audioTrack, MultiExtractorCodec decoder) {
        this.mAudioTrack = audioTrack;
        this.mDecoder = decoder;
    }

    @Override
    public void run() {
        while (true) {
            playCheck();
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
            synchronized (mPlayLock) {
                mPlayLock.notify();
            }
        }
    }
}
