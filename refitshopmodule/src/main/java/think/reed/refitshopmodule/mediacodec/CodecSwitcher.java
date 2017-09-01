package think.reed.refitshopmodule.mediacodec;

import java.util.List;

/**
 * Created by thinkreed on 2017/9/1.
 */

public class CodecSwitcher {

    private List<String> mPathList;

    private List<CodecComponent> mComponents;

    public CodecSwitcher(List<String> pathList) {
        mPathList = pathList;
    }
}
