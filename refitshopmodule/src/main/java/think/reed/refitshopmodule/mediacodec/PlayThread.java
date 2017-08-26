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
            Log.d("thinkreed", "before play check");
            playCheck();
            int dataSize = mDecoder.getAudioData(p, 12);
            Log.d("thinkreed", "data size is " + dataSize);
            if (dataSize < 0) {
                synchronized (mPlayLock) {
                    try {
                        mPlayLock.wait();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            mAudioTrack.write(p.getData(), 12, dataSize);
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
        }
        synchronized (mPlayLock) {
            Log.d("thinkreed", "play notify");
            mPlayLock.notify();
        }
    }
}
