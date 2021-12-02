package de.cloud.gommzy.config;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Config {
    public static File config = new File("config.yml");

    public Config() {
        try {
            if (config.createNewFile()) { // if file already exists will do nothing
                PrintWriter writer = new PrintWriter("config.yml", "UTF-8");
                writer.println("{");
                writer.println("    client: \"false\",");
                writer.println("    cloudip: \"162.55.209.195\",");
                writer.println("    cloudport: \"2000\",");
                writer.println("    cloudpassword: \"SeZ59Y6tcfRd7\"");
                writer.println("}");
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(String id) {
        String value = null;
        try {
            String data = "";
            Scanner myReader = new Scanner(config);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                data = data + line;
            }
            JSONObject json = new JSONObject(data);
            value = json.getString(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
