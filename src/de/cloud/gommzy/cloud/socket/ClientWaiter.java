package de.cloud.gommzy.cloud.socket;

import java.net.ServerSocket;

public class ClientWaiter {
    ClientWaiter(ServerSocket serverSocket) {
        Thread thread = new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    try {
                        System.out.println("Waiting for new client... ");
                        new ClientHandler(serverSocket.accept());
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });
        thread.start();
    }
}
