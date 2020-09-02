package org.goskyer.sshcore;

import java.io.IOException;

public interface SSHClient {

    SSHConnection connect(String host, int port, String username, String password) throws IOException;

}
