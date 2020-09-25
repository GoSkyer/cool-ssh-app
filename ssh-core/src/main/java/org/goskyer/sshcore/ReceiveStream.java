package org.goskyer.sshcore;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReceiveStream extends OutputStream {

    private final Lock lockRW = new ReentrantLock(true);

    private final int baseCap = 1 << 8;
    private int cap = baseCap;
    private int length = 0;
    private byte[] buffer = new byte[cap];

    private volatile long lastedWriteTime = System.currentTimeMillis();

    public String read() {
        String content = null;
        // 避免读到未写入完成的数据
        if (System.currentTimeMillis() - lastedWriteTime > 100) {
            lockRW.lock();
            if (length > 0) {
                content = new String(buffer, 0, length);
                length = 0;
                cap = 1 << 8;
                buffer = new byte[baseCap];
            }
            lockRW.unlock();
        }
        return content;
    }

    @Override
    public void write(int b) throws IOException {
        lockRW.lock();
        lastedWriteTime = System.currentTimeMillis();
        if (length == cap) { // 扩容
            cap = cap << 1;
            buffer = Arrays.copyOf(buffer, cap);
        }
        buffer[length] = (byte) b;
        length++;
        lockRW.unlock();
    }

}
