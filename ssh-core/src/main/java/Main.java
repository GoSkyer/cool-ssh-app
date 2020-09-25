import org.goskyer.sshcore.ApacheSSHClient;
import org.goskyer.sshcore.ApacheSSHConnection;
import org.goskyer.sshcore.ReceiveStream;
import org.goskyer.sshcore.SendStream;

import java.io.IOException;

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
        ReceiveStream receiver = new ReceiveStream();
        SendStream sender = new SendStream();
        boolean isOpenSucceed = false;

        // 独立线程保持ssh连接
        new Thread("ssh-connection-thread") {

            @Override
            public void run() {
                try {
                    ApacheSSHConnection connection = client.connect(user.ip, user.port, user.name, user.pwd, 10_000);
                    connection.openChannel(sender, receiver, System.err);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }

                try {
                    client.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }

            }

        }.start();

        // 模拟数据读取，每秒60次
        new Thread("view-loop-thread") {

            @Override
            public void run() {
                while (true) {

                    String content = receiver.read();

                    if (content != null) {
                        System.out.print(content);
                    }

                    try {
                        sleep(16);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e.getMessage());
                    }

                }
            }

        }.start();

        // 模拟用户输入指令
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sender.write("ls");
            }
        }).start();

        for (; ; ) {
            Thread.sleep(100);
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
