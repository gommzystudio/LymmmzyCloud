package de.cloud.gommzy.cloud.cloud;

import de.cloud.gommzy.cloud.minecraft.Server;
import de.cloud.gommzy.cloud.socket.ClientHandler;

public class Cloud {
    public String ip;
    public ClientHandler clientHandler;
    public Server bungee;
    public int avaibleScore  = 100;

    public Cloud(String ip, ClientHandler clientHandler) {
        this.ip = ip;
        this.clientHandler = clientHandler;
    }
}
