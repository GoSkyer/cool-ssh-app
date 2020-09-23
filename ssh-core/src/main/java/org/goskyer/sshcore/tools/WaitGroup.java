package org.goskyer.sshcore.tools;

public class WaitGroup {

    private int count = 0;

    public synchronized void add(int i) {
        count += i;
    }

    public synchronized void done() {
        if (--count == 0) {
            notifyAll();
        }
    }

    public synchronized void await() throws InterruptedException {
        while (count > 0) {
            wait();
        }
    }

}