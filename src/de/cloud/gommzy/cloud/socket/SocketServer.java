package de.cloud.gommzy.cloud.socket;

import java.net.ServerSocket;

public class SocketServer {
    ServerSocket serverSocket;
    int port;

    public SocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            this.serverSocket = new java.net.ServerSocket(port);
            new ClientWaiter(this.serverSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
