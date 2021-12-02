package de.cloud.gommzy.files;

import de.cloud.gommzy.cloud.socket.ClientHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class FolderUtil {
    public static void readFolder(String offset, String path, ClientHandler clientHandler, String orginalPath) {
        if (path != null) {
            sendFolder(path.replace(orginalPath,offset), clientHandler);
        } else {
            String temp = "";
            for (String loop : offset.split("/")) {
                if (temp.equals("")) {
                    temp = loop;
                } else {
                    temp = temp + "/" + loop;
                }
                sendFolder(offset, clientHandler);
            }
            path = orginalPath;
        }

        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isDirectory()) {
                    readFolder(offset ,child.getPath(), clientHandler, orginalPath);
                } else {
                    sendFile(child.getPath(),child.getPath().replace(orginalPath,offset), clientHandler);
                }
            }
        }
    }

    public static void copyFolder(String path, String target, String originalPath) {
        createFolder(target+"/"+ path.replace(originalPath,""),false);

        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isDirectory()) {
                    copyFolder(child.getPath(),target,originalPath);
                } else {
                    try {
                        Files.copy(child.toPath(), new File(target+"/"+ child.getPath().replace(originalPath,"")).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void sendFolder(String path, ClientHandler clientHandler) {
        clientHandler.write("createfolder "+path);
    }

    private static void sendFile(String realPath, String path, ClientHandler clientHandler) {
        try {
            File file = new File(realPath);
            byte[] bytearray  = new byte [(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray,0,bytearray.length);
            clientHandler.write("createfile "+path+" "+ Arrays.toString(bytearray));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createFolder(String path, boolean resetWhenExists) {
        try {
            File file = new File(path);
            if (!file.exists()){
                file.mkdirs();
            } else if (resetWhenExists) {
                deleteFolder(file);
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public static void createFile(String path, byte[] content) {
        try {
            File file = new File (path);
            file.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createFile(String path) {
        try {
            File file = new File (path);
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
