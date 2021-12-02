package de.cloud.gommzy.cloud.minecraft;

import java.util.HashMap;

public class LobbySystem {
    public static HashMap<String,Server> lobbys = new HashMap<>();

    public static void start() {
        new Thread(() -> {
            while (true) {
                try {

                    if (lobbys.size() == 0) {
                        Server lobby = ServerManager.autoCreateServer("public-lobby","public-lobby", ServerManager.ServerType.SPIGOT); // ordner können in dem thread nicht gelesen werden
                        if (lobby != null) {
                            System.out.println("[LOBBY] Lobby started "+lobby.serverName);
                            lobbys.put(lobby.serverName, lobby);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
