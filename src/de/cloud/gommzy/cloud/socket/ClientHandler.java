package de.cloud.gommzy.cloud.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientHandler {
    private PrintWriter writer;
    private BufferedReader reader;
    private String address;
    private Socket socket;
    private Thread clientReciverThread;

    public ClientHandler(Socket socket) {
        try {
            this.address = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/", "");
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream());
            this.socket = socket;

            final ClientHandler thisInstance = this;
            this.clientReciverThread = new Thread(() -> new ClientReciver(thisInstance,socket));
            clientReciverThread.start();

            System.out.println("[Connection] "+address);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public void unregister() {
        try {
            System.out.println("[Unregister] "+address);
            this.socket.close();
            this.clientReciverThread.interrupt();
            this.clientReciverThread.stop();
        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public String read() {
        try {
            return this.reader.readLine();
        } catch (final Exception exception) {
            unregister();
        }
        return "error";
    }

    public void write(final String msg) {
        this.writer.println(msg);
        this.writer.flush();
    }

    public String getAddress() {
        return address;
    }
}
