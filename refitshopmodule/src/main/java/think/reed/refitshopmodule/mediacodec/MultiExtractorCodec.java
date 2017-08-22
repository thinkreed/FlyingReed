package think.reed.refitshopmodule.mediacodec;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.io.IOException;
import java.util.List;

/**
 * multi extractor codec, smooth switch tiny media files
 * Created by thinkreed on 2017/8/21.
 */

public class MultiExtractorCodec {

    private List<String> pathList;

    private int curIndex = -1;

    private SparseArray<MediaExtractor> extractors = new SparseArray<>();

    public MultiExtractorCodec() {
        initExtractors();
    }

    private void initExtractors() {
        for (int i = 0; i < 2; i++) {
            extractors.put(i, new MediaExtractor());
        }
    }

    @Nullable
    private MediaExtractor switchExtractor() {
        try {
            curIndex = (curIndex + 1) % extractors.size();
            MediaExtractor extractor = extractors.get(curIndex);
            extractor.setDataSource(pathList.remove(0));
            return selectAudioTrack(extractor) ? extractor : null;
        } catch (NullPointerException e) {
            return null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

    }

    private boolean selectAudioTrack(MediaExtractor extractor) {
        for (int i = 0; i < extractor.getTrackCount(); i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) return true;
        }
        return false;
    }

}
