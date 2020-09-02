package org.goskyer.sshcore;

import org.apache.sshd.client.ClientBuilder;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.config.hosts.HostConfigEntry;
import org.apache.sshd.client.future.ConnectFuture;

import java.io.Closeable;
import java.io.IOException;

public class ApacheSSHClient implements Closeable {

    public static SshClient Client;

    public ApacheSSHClient() {
        init();
    }

    public void init() {
        // TODO add more detailed configuration
        ClientBuilder builder = new ClientBuilder();
        Client = builder.build(true);
        Client.start();
    }

    public ApacheSSHConnection connect(String host, int port, String username, String password, long timeout) throws IOException {

        if (Client == null || Client.isClosing() || Client.isClosed()) {
            throw new IllegalStateException("ssh client is closed.");
        }

        if (!Client.isStarted()) {
            throw new IllegalStateException("ssh client is not ready.");
        }

        SSHConnConfig config = new SSHConnConfig();
        config.setHost(host);
        config.setPort(port);
        config.setUsername(username);
        config.setPassword(password);
        config.setConnectTimeout(timeout);

        ApacheSSHConnection connection = new ApacheSSHConnection(config);
        connection.init();

        return connection;
    }


    @Override
    public void close() throws IOException {
        Client.close();
        Client = null;
    }

}
