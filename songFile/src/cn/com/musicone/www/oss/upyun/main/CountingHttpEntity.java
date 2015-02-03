package cn.com.musicone.www.oss.upyun.main;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import com.belladati.httpclientandroidlib.HttpEntity;
import com.belladati.httpclientandroidlib.entity.HttpEntityWrapper;
import cn.com.musicone.www.oss.upyun.listener.ProgressListener;


public class CountingHttpEntity extends HttpEntityWrapper {
	
    static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;
        private long transferred;
        private long totalBytes;
        private long times;

        CountingOutputStream(final OutputStream out, final ProgressListener listener, final long bytesSended, final long totalBytes) {
            super(out);
            this.listener = listener;
            this.transferred = bytesSended;
            this.totalBytes = totalBytes;
        }

        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
        	long tim1 = System.currentTimeMillis();
            out.write(b, off, len);            
            this.transferred += len;
            this.transferred = trick(this.transferred, this.totalBytes);
            this.listener.transferred(this.transferred, this.totalBytes);
            long tim2 = System.currentTimeMillis();
            this.times = tim2 - tim1;
            this.listener.transferred(this.transferred, this.totalBytes, this.times);
        }

        @Override
        public void write(final int b) throws IOException {
        	long tim1 = System.currentTimeMillis();
            out.write(b);
            this.transferred ++;
            this.transferred = trick(this.transferred, this.totalBytes);
            this.listener.transferred(this.transferred, this.totalBytes);
            long tim2 = System.currentTimeMillis();
            this.times = tim2 - tim1;
            this.listener.transferred(this.transferred, this.totalBytes, this.times);
        }
        
        private long trick(long transferred, long total){
        	if (transferred > total) {
        		transferred = total-1000;
        		return transferred >= 0 ? transferred : 0;
        	}
        	return transferred;
        }
        
        public static void changeTransferredBytes(){
        	
        }
    }

    private final ProgressListener listener;
    private final long bytesSended;
    private final long totalBytes;

    public CountingHttpEntity(final HttpEntity entity, final ProgressListener listener, final long bytesSended, final long totalBytes) {
        super(entity);
        this.listener = listener;
        this.bytesSended = bytesSended;
        this.totalBytes = totalBytes;
    }

    @Override
    public void writeTo(final OutputStream out) throws IOException {
        this.wrappedEntity.writeTo(out instanceof CountingOutputStream? out: new CountingOutputStream(out, this.listener, this.bytesSended, this.totalBytes));
    }

}
