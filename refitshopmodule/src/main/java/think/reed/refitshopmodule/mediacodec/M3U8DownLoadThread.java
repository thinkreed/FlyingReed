package think.reed.refitshopmodule.mediacodec;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class M3U8DownLoadThread extends Thread {

    public static final int MUSIC_DOWNLOAD_ERROR = -1000;

    private boolean isDownloadStatus = false;
    private boolean isStartDownload = false;
//    private String m3u8URL = "http://ls.qingting.fm/live/386.m3u8";
    private String m3u8URL = "http://open.ls.qingting.fm/live/1140/64k.m3u8";
    private List<String> mBufferPathList = new ArrayList<String>();
    private List<String> mDownLoadedList = new ArrayList<String>();
    private CopyOnWriteArrayList<String> mFileList;
    private static final String pathHead = Environment.getExternalStorageDirectory().getPath();

    public M3U8DownLoadThread(CopyOnWriteArrayList<String> fileList) {
        mFileList = fileList;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (mBufferPathList != null) {
            if (isStartDownload) {
                File file = new File(pathHead + "/thinkreed");
                if (!file.exists()) {
                    file.mkdir();
                }
                int fileName = 1;
                mBufferPathList.clear();
                if (m3u8URL != null) {
                    isStartDownload = false;
                    downloadM3U8(m3u8URL);
                    while (isDownloadStatus) {
                        if (mBufferPathList.size() > 0) {
                            downloadSimpleSong(mBufferPathList.get(0), fileName);
                            mDownLoadedList.add(mBufferPathList.get(0));
                            mBufferPathList.remove(0);
                            fileName++;
                        }
                        if (mBufferPathList.size() < 2) {
                            downloadM3U8(m3u8URL);
                        }
                    }
                    mBufferPathList.clear();
                    mDownLoadedList.clear();
                    mFileList.clear();
                    File[] files = file.listFiles();
                    for (File file0: files) {
                        file0.delete();
                    }
                }
            } else {
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void abortLastDownload() {
        isDownloadStatus = false;
    }

    public void startDownloadSong() {
        isStartDownload = true;
    }

    public void startDownloadSong(String path) {
        m3u8URL = path;
        isStartDownload = true;
    }

    private void downloadM3U8(String urlPath) {

        URL url;

        isDownloadStatus = true;
        HttpURLConnection conn;
        String savePath = pathHead + "/thinkreed/test.m3u8";
        try {
            url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int statusCode = conn.getResponseCode();
            int downLoadBytes = 0;
            if (200 == statusCode) {
                int totalSize = conn.getContentLength();
                if (totalSize <= 0) {
                    return;
                }
                InputStream inStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int count = -1;
                RandomAccessFile saveFile = new RandomAccessFile(savePath, "rwd");
                saveFile.setLength(totalSize);

                while (isDownloadStatus && ((count = inStream.read(buffer, 0, 1024)) != -1)) {
                    if (isDownloadStatus) {
                        saveFile.write(buffer, 0, count);
                        downLoadBytes += count;
                    } else {
                        break;
                    }
                }
                saveFile.close();
                inStream.close();
                if (downLoadBytes >= totalSize) {
                }
            }
            FileReader fileReader = new FileReader(savePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String str;
            int count = 0;
            while ((str = bufferedReader.readLine()) != null) {    //如果之前文件为空，则不执行输出
                if (str.startsWith("#")) {
                    System.out.println(str);
                    continue;
                } else {
                    count++;
                    if (!mBufferPathList.contains(str) && !mDownLoadedList.contains(str)) {
                        mBufferPathList.add(str);
                        Log.i("test", "add + " + str);
                    } else {
                        Log.w("test", "重复 + " + str);
                    }
                }
            }
            if (mDownLoadedList.size() > count) {
                int deleteCount = mDownLoadedList.size() - count;
                for (int i = 0; i < deleteCount; i++) {
                    mDownLoadedList.remove(0);
                }
            }

            if (downLoadBytes > 0) {
            }

            while(isDownloadStatus && mFileList.size() > 2) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void downloadSimpleSong(String songUrl, int fileName) {
        URL url;

        String path = "http:" + songUrl;
        String savePath = pathHead + "/thinkreed/" + fileName + ".aac";
        File file = new File(savePath);
        if (file.exists()) {
            file.delete();
        }
        int totalSize;
        isDownloadStatus = true;
        HttpURLConnection conn;
        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int statusCode = conn.getResponseCode();
            int downLoadBytes = 0;
            if (200 == statusCode) {
                totalSize = conn.getContentLength();
                if (totalSize <= 0) {
                    return;
                }
                InputStream inStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int count = -1;

                RandomAccessFile saveFile = new RandomAccessFile(savePath, "rwd");

                while (isDownloadStatus && ((count = inStream.read(buffer, 0, 1024)) != -1)) {
                    if (isDownloadStatus) {
                        saveFile.write(buffer, 0, count);
                        downLoadBytes += count;
                    } else {
                        break;
                    }
                }
                saveFile.close();
                inStream.close();
                if (downLoadBytes >= totalSize) {
                }
            }
            if (downLoadBytes > 0) {
                // TODO: 上报流量数据，还要区分是否wifi，是否radioSong
                mFileList.add(savePath);
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
