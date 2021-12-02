package de.cloud.gommzy.client.socket;

import de.cloud.gommzy.client.avaible.AvaibleLoop;
import de.cloud.gommzy.cloud.socket.ClientHandler;
import de.cloud.gommzy.config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    private BufferedReader reader;
    public PrintWriter writer;
    public Socket socket;
    private Thread clientReciverThread;
    private Thread avaibleLoop;
    public ClientReciver clientReciver;
    private boolean cooldown = false;
    private String ip;
    private int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void login() {
        try {
            logout();
        } catch (Exception ignored) {}
        try {
            if (cooldown) {
                return;
            }
            cooldown = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            cooldown = false;
            socket = new Socket(ip, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            write("login "+ Config.getString("cloudpassword"));
            write("registercloud");
            if (clientReciverThread == null || !clientReciverThread.isAlive()) {
                final Client thisInstance = this;
                clientReciverThread = new Thread(() -> new ClientReciver(thisInstance));
                clientReciverThread.start();
                avaibleLoop =  new Thread(() -> new  AvaibleLoop(this));
                avaibleLoop.start();
            }
            System.out.println("[CLIENT] Successfully connected!");
        } catch (final IOException ignored) {
            System.out.println("[CLIENT] Can't connect! Reconnect in 1s");
            try {
                Thread.sleep(1000);
                cooldown = false;
                login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void logout() {
        try {
            this.clientReciverThread.interrupt();
            this.avaibleLoop.interrupt();
        } catch (Exception ignored) {}
    }

    String read() {
        try {
            return this.reader.readLine();
        } catch (final Exception exception) {
            logout();
        }
        return "error";
    }

    public void write(final String msg) {
        this.writer.println(msg);
        this.writer.flush();
    }
}
