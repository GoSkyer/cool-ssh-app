package org.goskyer.sshcore;

import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.config.hosts.HostConfigEntry;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.common.channel.ChannelListener;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class ApacheSSHConnection implements Closeable {

    public static final byte None = 0;
    public static final byte Initialized = 1;
    public static final byte OpenSuccess = 1 << 2;
    public static final byte OpenFailure = 1 << 3;
    public static final byte Closed = 1 << 4;

    private final ChannelListener defaultChannelListener = new ChannelListener() {

        @Override
        public void channelInitialized(Channel channel) {
            status = Initialized;
        }

        @Override
        public void channelOpenSuccess(Channel channel) {
            status = OpenSuccess;
        }

        @Override
        public void channelOpenFailure(Channel channel, Throwable reason) {
            status = OpenFailure;
        }

        @Override
        public void channelStateChanged(Channel channel, String hint) {

        }

        @Override
        public void channelClosed(Channel channel, Throwable reason) {
            status = Closed;
        }
    };

    private SSHConnConfig config;
    private ClientSession session;
    private byte status = None;

    public ApacheSSHConnection(SSHConnConfig config) {
        this.config = config;
    }


    private HostConfigEntry createConfig(String host, int port, String username) {
        HostConfigEntry config = new HostConfigEntry();
        config.setHostName(host);
        config.setPort(port);
        config.setUsername(username);
        return config;
    }

    public void init() throws IOException {

        HostConfigEntry config = createConfig(this.config.getHost(), this.config.getPort(), this.config.getUsername());
        ConnectFuture connect = ApacheSSHClient.Client.connect(config);

        ClientSession session = connect.verify(this.config.getConnectTimeout(), TimeUnit.MILLISECONDS).getSession();

        session.addPasswordIdentity(this.config.getPassword());
        session.auth().await(this.config.getConnectTimeout(), TimeUnit.MILLISECONDS);

        this.session = session;
    }

    public void openChannel(InputStream in, OutputStream out, OutputStream err) throws IOException {

        try (ClientChannel channel = this.session.createChannel(ClientChannel.CHANNEL_SHELL)) {
            channel.addChannelListener(defaultChannelListener);
            channel.setErr(err);
            channel.setOut(out);
            channel.setIn(in);
            channel.open();
            channel.waitFor(Collections.singleton(ClientChannelEvent.CLOSED), -1);
        }

    }

    public void close() throws IOException {
        this.session.close();
    }

    public byte getStatus() {
        return this.status;
    }

}
