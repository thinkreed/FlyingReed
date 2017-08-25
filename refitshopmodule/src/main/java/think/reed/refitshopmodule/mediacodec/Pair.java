package think.reed.refitshopmodule.mediacodec;

/**
 * Created by huweijie01 on 2017/8/25.
 */

public class Pair {

    private byte[] mData;
    private int mSize;

    public void setData(byte[] data) {
        mData = data;
    }

    public byte[] getData() {
        return mData;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public int getSize() {
        return mSize;
    }
}
