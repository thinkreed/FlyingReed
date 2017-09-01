package think.reed.refitshopmodule.mediacodec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinkreed on 2017/9/1.
 */

public class CodecSwitcher {

    private List<String> mPathList;

    private List<CodecComponent> mComponents;

    private CodecComponent mCur;

    private int mCurIndex = -1;

    private File mFile;

    public CodecSwitcher(List<String> pathList) {
        mPathList = pathList;
        initComponents();
    }

    public int switchCodecComponent() {
        if (mPathList.size() < 2) {
            return -1;
        }

        if (mCurIndex == -1) {
            mCurIndex += 1;
            mCur = mComponents.get(mCurIndex);
            mCur.init(mPathList.get(0));
            mCurIndex = (mCurIndex + 1) % mComponents.size();
            mComponents.get(mCurIndex).init(mPathList.get(1));
        } else {
            mCur = mComponents.get(mCurIndex);
            mFile = new File(mPathList.get(0));
            if (mFile.exists()) {
                mFile.delete();
            }
            mCurIndex = (mCurIndex + 1) % mComponents.size();
            mComponents.get(mCurIndex).init(mPathList.get(0));
        }

        return 0;
    }

    public CodecComponent getCurComponent() {
        return mCur;
    }

    private void initComponents() {
        mComponents = new ArrayList<>();
        mComponents.add(new CodecComponent());
        mComponents.add(new CodecComponent());
    }
}
