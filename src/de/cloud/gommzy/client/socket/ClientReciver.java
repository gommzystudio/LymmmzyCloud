package de.cloud.gommzy.client.socket;

import de.cloud.gommzy.client.minecraft.Minecraft;
import de.cloud.gommzy.cloud.minecraft.ServerManager;
import de.cloud.gommzy.files.FolderUtil;

import java.lang.reflect.Array;

public class ClientReciver {
    private Client client;

    ClientReciver(Client client) {
        this.client = client;
        client.clientReciver = this;
        try {
            System.out.println("[CLIENT] Thread start ");
            while (!client.socket.isClosed() && client.clientReciver == this) {
                String read = client.read();
                if (read == null || read.equals("") || read.equals("error")) {
                    break;
                }
                String backup = read;
                final String[] args = read.split(" ");
                try {
                    switch (args[0]) {
                        case "createfile": {
                            System.out.println("[CREATE-FILE] "+args[1]);
                            try {
                                String content = read.replace(args[0]+" "+args[1]+" ","");
                                String[] byteValues = content.substring(1, content.length() - 1).split(",");
                                byte[] bytes = new byte[byteValues.length];

                                for (int i=0, len=bytes.length; i<len; i++) {
                                    bytes[i] = Byte.parseByte(byteValues[i].trim());
                                }

                                FolderUtil.createFile(args[1], bytes);
                            } catch (Exception e) {
                                FolderUtil.createFile(args[1]);
                            }
                            break;
                        }
                        case "createfolder": {
                            System.out.println("[CREATE-FOLDER] "+args[1]);
                            FolderUtil.createFolder(args[1],true);
                            break;
                        }
                        case "registercloud": {
                            Minecraft.cloudUUID = args[1];
                            break;
                        }
                        case "startserver": {
                            String servername = args[1];
                            ServerManager.ServerType serverType = ServerManager.ServerType.valueOf(args[2]);
                            int port = Minecraft.startServer(servername,serverType);
                            client.write("startedserver "+servername+" "+port);
                            break;
                        }
                    }
                } catch (Exception exc) {
                    System.out.println("[FAIL] Message: " + backup + " Error: " + exc.getMessage());
                    exc.printStackTrace();
                }
            }
            if (client.socket.isClosed()) {
                System.out.println("[CLIENT] Thread stop #1");
            } else {
                System.out.println("[CLIENT] Thread stop #2");
            }
            System.exit(0);
        } catch (final Exception exc) {
            exc.printStackTrace();
            client.login();
        }
    }
}
