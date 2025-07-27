package me.alpha432.oyvey.auth;

import net.minecraft.client.MinecraftClient;
import org.apache.commons.codec.digest.DigestUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import static me.alpha432.oyvey.OyVey.LOGGER;


public class Auth {
    public static MinecraftClient mc = MinecraftClient.getInstance();
    public static String hwid = getHwid();
    public static String hwidlist = "https://raw.githubusercontent.com/bombcrash/RageX-hwid-list/refs/heads/main/konyalidanbaskasinabastirmam.txt";
    public static String getHwid() {
        String os = System.getenv("os");
        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(os + System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("user.name") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS")));
    }

    public static String getIP() {
        try {
            URL url = new URL("http://checkip.amazonaws.com");
            Scanner scanner = new Scanner(url.openStream(), "UTF-8");
            String ip = scanner.useDelimiter("\\A").next();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(true);
    }

    public static boolean checkAuthentication() {
        boolean isAuthenticated = false;
        System.out.println("[" + LOGGER + "] Authenticating...");
        if (validateAuth()) {
            System.out.println("[" + LOGGER + "] Authentication success.");
            isAuthenticated = true;
        } else {
            System.out.println("[" + LOGGER + "] Authentication failed.");
        }
        return isAuthenticated;
    }


    public static boolean validateAuth() {
        try {
            URL url = new URL(hwidlist);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                String trimmedHwid = hwid.trim();
                System.out.println("[DEBUG] HWID list line: '" + trimmedLine + "' | Local HWID: '" + trimmedHwid + "'");
                if (trimmedLine.equals(trimmedHwid)) {
                    System.out.println("[DEBUG] HWID MATCH!");
                    return true;
                }
            }
            System.out.println("[DEBUG] HWID NOT FOUND! HWID: '" + hwid + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void sendWebhook() throws IOException {
        Webhook webhook = new Webhook("https://discord.com/api/webhooks/1397290720997412944/7yjF0mXvpyURIRfKYAqGNH0OalyHJyMCLfpcJSJsjzUEqg9zAJWyd9WWMIDJ2HXh9hBT");
        Webhook.EmbedObject embed = new Webhook.EmbedObject();
        embed.setTitle("Auth Manager");
        embed.setThumbnail("");
        embed.setDescription("Authentication success - " + mc.getSession().getUsername());
        embed.addField("ID", getHwid(), false);
        embed.setColor(Color.GRAY);
        embed.setFooter(getTime(), (String)null);
        webhook.addEmbed(embed);

        if (checkAuthentication()) {
            webhook.execute();
        } else {
            Webhook webhook2 = new Webhook("https://discord.com/api/webhooks/1397290720997412944/7yjF0mXvpyURIRfKYAqGNH0OalyHJyMCLfpcJSJsjzUEqg9zAJWyd9WWMIDJ2HXh9hBT");
            Webhook.EmbedObject embed2 = new Webhook.EmbedObject();
            embed2.setTitle("Auth Manager");
            embed2.setThumbnail("");
            embed2.setDescription("Authentication failed - " + mc.getSession().getUsername());
            embed2.addField("ID", getHwid(), false);
            embed2.setColor(Color.GRAY);
            embed2.setFooter(getTime(), (String)null);
            webhook2.addEmbed(embed2);
            webhook2.execute();
        }
    }

    public static void sendWebhook2() throws IOException {
        Webhook webhook = new Webhook("https://discord.com/api/webhooks/1397290720997412944/7yjF0mXvpyURIRfKYAqGNH0OalyHJyMCLfpcJSJsjzUEqg9zAJWyd9WWMIDJ2HXh9hBT");
        Webhook.EmbedObject embed = new Webhook.EmbedObject();
        embed.setTitle("Auth Manager");
        embed.setThumbnail("");
        embed.setDescription("! POSSIBLE DUMP ACTION DETECTED !");
        embed.addField("ID", getHwid(), false);
        embed.addField("IP", getIP(), false);
        embed.addField("OS.NAME", System.getProperty("os.name"), false);
        embed.addField("OS.ARCHITECTURE", System.getProperty("os.arch"), false);
        embed.addField("USERNAME", System.getProperty("user.name"), false);
        embed.addField("SYSTEM.ROOT", System.getProperty("SystemRoot"), false);
        embed.addField("HOMEDRIVE", System.getProperty("HOMEDRIVE"), false);
        embed.addField("PROCESSOR.LEVEL", System.getProperty("PROCESSOR_LEVEL"), false);
        embed.addField("PROCESSOR.REVISION", System.getProperty("PROCESSOR_REVISION"), false);
        embed.addField("PROCESSOR.IDENTIFIER", System.getProperty("PROCESSOR_IDENTIFIER"), false);
        embed.addField("PROCESSOR.ARCHITECTURE", System.getProperty("PROCESSOR_ARCHITECTURE"), false);
        embed.addField("PROCESSOR.ARCHITEW6432", System.getProperty("PROCESSOR_ARCHITEW6432"), false);
        embed.addField("PROCESSOR.NUMBER", System.getProperty("NUMBER_OF_PROCESSORS"), false);
        embed.setColor(Color.GRAY);
        embed.setFooter(getTime(), (String) null);
        webhook.addEmbed(embed);
        webhook.execute();
    }

    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}