package de.cloud.gommzy.cloud.minecraft;

import de.cloud.gommzy.Main;
import de.cloud.gommzy.cloud.socket.ClientHandler;
import de.cloud.gommzy.files.FolderUtil;

public class Server {
    public String serverName;
    public String template;
    public ClientHandler cloudClientHandler;
    public ClientHandler serverClientHandler;
    public String address;
    public int port;
    public ServerManager.ServerType serverType;

    public Server(String serverName, String template, ClientHandler cloudClientHandler, String address, ServerManager.ServerType serverType) {
        this.serverName = serverName;
        this.template = template;
        this.cloudClientHandler = cloudClientHandler;
        this.address = address;
        this.serverType = serverType;

        FolderUtil.readFolder("temp/"+serverName,null, cloudClientHandler, "templates/"+template);
        cloudClientHandler.write("startserver "+serverName+" "+serverType);
    }

    public void startedServer(int port) {
        this.port = port;
        System.out.println("Started server on "+address+" "+port);
    }
}
