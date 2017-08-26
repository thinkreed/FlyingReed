package think.reed.refitshopmodule.mediacodec;

import android.os.Environment;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

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
    private String m3u8URL = "http://live.xmcdn.com/live/252/64.m3u8?transcode=ts";
    private List<String> mBufferPathList = new ArrayList<>();
    private List<String> mDownLoadedList = new ArrayList<>();
    private CopyOnWriteArrayList<String> mFileList;
    private static final String pathHead = Environment.getExternalStorageDirectory().getPath();

    public M3U8DownLoadThread(CopyOnWriteArrayList<String> fileList) {
        mFileList = fileList;
    }

    @Override
    public void run() {
        while (mBufferPathList != null) {
            if (isStartDownload) {
                File file = new File(pathHead + "/fm");
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
                        if (mBufferPathList.size() < 4) {
                            downloadM3U8(m3u8URL);
                        }
                    }
                    mBufferPathList.clear();
                    mDownLoadedList.clear();
                    mFileList.clear();
                    File[] files = file.listFiles();
                    for (File file0 : files) {
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
        m3u8URL = checkPath(path);
        isStartDownload = true;
    }

    private String checkPath(String urlPath) {
        if (!urlPath.startsWith("http")) {
            urlPath = "http:" + urlPath;
        }
//        if (urlPath.contains("transcode=ts")) {
//            urlPath = urlPath.replace("transcode=ts", "transcode=aac");
//        }
        return urlPath;
    }

    private String resolvePath(String url, String fileName) {
        int index = url.lastIndexOf(".");
        String suffix = url.substring(index);
        return pathHead + "/fm/" + fileName + suffix;
    }

    private void downloadM3U8(String urlPath) {

        URL url;

        isDownloadStatus = true;
        HttpURLConnection conn;
        String savePath = resolvePath(urlPath, "test");
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
                if (!str.startsWith("#")) {
                    count++;
//                    if (!mBufferPathList.contains(str) && !mDownLoadedList.contains(str)) {
//                        mBufferPathList.add(checkPath(str));
//                        Log.i("thinkreed", "add + " + str);
//                    } else {
//                        Log.w("thinkreed", "重复 + " + str);
//                    }
                    mBufferPathList.add(checkPath(str));
                }
            }
            if (mDownLoadedList.size() > 5) {
                mDownLoadedList.remove(0);
            }

            while (isDownloadStatus && mFileList.size() > 4) {
                try {
                    EventBus.getDefault().post(new BufferReadyEvent());
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

        String savePath = resolvePath(songUrl, String.valueOf(fileName));
        File file = new File(savePath);
        if (file.exists()) {
            file.delete();
        }
        int totalSize;
        isDownloadStatus = true;
        HttpURLConnection conn;
        try {
            url = new URL(songUrl);
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
                    Log.i("thinkreed", savePath + "下载完成");
                }
            }
            if (downLoadBytes > 0) {
                mFileList.add(savePath);
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
