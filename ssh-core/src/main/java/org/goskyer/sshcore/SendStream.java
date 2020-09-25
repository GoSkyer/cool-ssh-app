package org.goskyer.sshcore;

import org.goskyer.sshcore.tools.WaitGroup;

import java.io.IOException;
import java.io.InputStream;

public class SendStream extends InputStream {

    private final WaitGroup wg = new WaitGroup();
    private boolean isEnd = false;

    private byte[] buffer = null;
    private int position = 0;

    public void write(String cmd) {
        if (!cmd.endsWith("\n")) {
            cmd += "\n";
        }
        buffer = cmd.getBytes();
        position = 0;
        wg.done();
    }

    @Override
    public int read() throws IOException {

        if (isEnd) {
            isEnd = false;
            return -1;
        }

        if (buffer == null) {
            wg.add(1);
            try {
                wg.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return -1;
            }
        }

        byte b = buffer[position++];

        if (position >= buffer.length) {
            isEnd = true;
            buffer = null;
        }

        return b;
    }

}
