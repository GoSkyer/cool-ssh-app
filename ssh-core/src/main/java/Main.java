import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.config.hosts.HostConfigEntry;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.io.NoCloseInputStream;
import org.apache.sshd.common.util.io.NoCloseOutputStream;
import org.goskyer.sshcore.ApacheSSHClient;
import org.goskyer.sshcore.ApacheSSHConnection;

import java.io.IOException;
import java.util.Collections;

public class Main {

    static class User {
        String ip;
        int port;
        String name;
        String pwd;
    }


    public static void main(String[] args) throws Throwable {

        User user = new User();
        user.ip = "115.159.28.229";
        user.port = 22;
        user.name = "lucas";
        user.pwd = "761349852";

        ApacheSSHClient client = new ApacheSSHClient();

        new Thread(() -> {

            try {
                ApacheSSHConnection connection = client.connect(user.ip, user.port, user.name, user.pwd, 10_000);
                connection.openChannel(System.in, System.out, System.err);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            try {
                client.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        }).start();

        while (true) {
            Thread.sleep(1000);
        }


//        try (SshClient client = SshClient.setUpDefaultClient()) {
//
//            client.start();
//
//            HostConfigEntry entry = new HostConfigEntry();
//            entry.setUsername(user.name);
//            entry.setHostName(user.ip);
//            entry.setPort(user.port);
//
//            try (ClientSession session = client.connect(entry).verify(30_000).getSession()) {
//
//                session.addPasswordIdentity(user.pwd);
//                session.auth().verify(30_000);
//
//                try (ClientChannel channel = session.createChannel(ClientChannel.CHANNEL_SHELL)) {
//                    channel.setIn(new NoCloseInputStream(System.in));
//                    channel.setOut(new NoCloseOutputStream(System.out));
//                    channel.setErr(new NoCloseOutputStream(System.err));
//                    channel.open();
//                    channel.waitFor(Collections.singleton(ClientChannelEvent.CLOSED), 0);
//                } finally {
//                    session.close(false);
//                }
//
//            } finally {
//                client.stop();
//            }
//        }
    }

}
