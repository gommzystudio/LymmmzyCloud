package de.cloud.gommzy;

import de.cloud.gommzy.client.socket.Client;
import de.cloud.gommzy.cloud.minecraft.Minecraft;
import de.cloud.gommzy.cloud.socket.SocketServer;
import de.cloud.gommzy.config.Config;
import de.cloud.gommzy.files.FolderUtil;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new Config();
        if (Config.getString("client").equals("true")) {
            FolderUtil.createFolder("temp",true);
            Client client = new Client(Config.getString("cloudip"),Integer.parseInt(Config.getString("cloudport")));
            client.login();
        } else {
            SocketServer server =  new SocketServer(Integer.parseInt(Config.getString("cloudport")));
            server.start();
            Minecraft.startMinecraft();
        }
    }
}
