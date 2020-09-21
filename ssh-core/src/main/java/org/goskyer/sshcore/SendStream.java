package org.goskyer.sshcore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class SendStream extends InputStream {

    byte[] buffer = "cd /\n".getBytes();
    int position = 0;
    boolean isFirst = true;

    

    @Override
    public int read() throws IOException {
        if (position >= buffer.length) {
            return -1;
        }
        return buffer[position++];
    }

}
