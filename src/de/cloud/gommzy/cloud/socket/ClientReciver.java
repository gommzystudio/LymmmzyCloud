package de.cloud.gommzy.cloud.socket;

import de.cloud.gommzy.cloud.cloud.Cloud;
import de.cloud.gommzy.cloud.minecraft.Minecraft;
import de.cloud.gommzy.cloud.minecraft.ServerManager;
import de.cloud.gommzy.config.Config;
import de.cloud.gommzy.files.FolderUtil;

import java.net.Socket;
import java.util.Arrays;

public class ClientReciver {
    private String cloudUUID;

    ClientReciver(ClientHandler clientHandler, Socket socket) {
        try {
            while(socket.isConnected()) {
                String read = clientHandler.read();
                if (read == null) {
                    break;
                }
                new Thread(() -> {
                    final String[] args = read.split(" ");
                    switch (args[0]) {
                        case "login": {
                            final String pswd = args[1];
                            if (!pswd.equals(Config.getString("cloudpassword"))) {
                                System.out.println("[Wrong-Pswd] "+clientHandler.getAddress());
                                clientHandler.unregister();
                            } else {
                                System.out.println("[Register] "+clientHandler.getAddress());
                            }
                            break;
                        }
                        case "registercloud": {
                            Cloud cloud = new Cloud(clientHandler.getAddress(),clientHandler);
                            cloudUUID = Minecraft.addCloud(cloud);
                            clientHandler.write("registercloud "+cloudUUID);
                            break;
                        }
                        case "registerbungee": {
                            ServerManager.setServerClientHanlder(clientHandler,25565);
                            break;
                        }
                        case "registerspigot": {
                            ServerManager.setServerClientHanlder(clientHandler,Integer.parseInt(args[1]));
                            break;
                        }
                        case "startedserver": {
                            ServerManager.servers.get(args[1]).startedServer(Integer.parseInt(args[2]));
                            break;
                        }
                        case "setscore": {
                            Minecraft.clouds.get(cloudUUID).avaibleScore = Integer.parseInt(args[1]);
                            break;
                        }
                    }

                }).start();
           }
            clientHandler.unregister();
        } catch (final Exception exc) {
            exc.printStackTrace();
            clientHandler.unregister();
        }
    }
}
