package org.goskyer.sshcore;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class ReceiveStream extends OutputStream {

    int baseCap = 1 << 8;
    int cap = baseCap;
    int length = 0;
    byte[] buffer = new byte[cap];

    volatile long lastedWriteTime = System.currentTimeMillis();

    public String read() {
        String content = null;
        if (System.currentTimeMillis() - lastedWriteTime > 100) {
            if (length > 0) {
                content = new String(buffer, 0, length);
                length = 0;
                cap = 1 << 8;
                buffer = new byte[baseCap];
            }
        }
        return content;
    }

    @Override
    public void write(int b) throws IOException {
        lastedWriteTime = System.currentTimeMillis();
        if (length == cap) {
            cap = cap << 1;
            buffer = Arrays.copyOf(buffer, cap);
        }
        buffer[length] = (byte) b;
        length++;
    }

}
