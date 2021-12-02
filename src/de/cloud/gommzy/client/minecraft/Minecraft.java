package de.cloud.gommzy.client.minecraft;

import de.cloud.gommzy.cloud.minecraft.ServerManager;
import de.cloud.gommzy.files.FolderUtil;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Minecraft {
    public static String cloudUUID;

    public static int startServer(String servername, ServerManager.ServerType serverType) {
        try {
            int port = 25565;
            if (serverType == ServerManager.ServerType.SPIGOT) {
                ServerSocket serverSocket = new ServerSocket(0); //find free port
                port = serverSocket.getLocalPort();
                serverSocket.close();
            }

            if (serverType == ServerManager.ServerType.SPIGOT) {
                FolderUtil.copyFolder("jars/spigot","temp/"+servername,"jars/spigot");
            } else {
                FolderUtil.copyFolder("jars/bungee","temp/"+servername,"jars/bungee");
            }

            String[] command = ("java -jar server.jar --nogui").split(" ");
            if (serverType == ServerManager.ServerType.SPIGOT) {
                command = ("java -jar server.jar --nogui --port "+port).split(" ");
            }

            Process p = null;
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File("temp/"+servername));
            p = pb.start();

            return port;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
