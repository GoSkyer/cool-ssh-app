package org.goskyer.sshcore;

import org.apache.sshd.client.ClientBuilder;
import org.apache.sshd.client.SshClient;

import java.io.IOException;

public class ApacheSSH implements SSHClient{

    public static SshClient Client;

    public ApacheSSH() {

    }

    public void init() {
        // TODO add more detailed configuration
        ClientBuilder builder = new ClientBuilder();
        Client = builder.build(true);
        Client.start();
    }

}
