package cn.com.musicone.www.oss.upyun.listener;

public interface ProgressListener {
    void transferred(long transferedBytes, long totalBytes);
}
