package me.alpha432.oyvey.util;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HwidUtil {
    public static String getHardwareInfo() {
        SystemInfo si = new SystemInfo();
        CentralProcessor cpu = si.getHardware().getProcessor();
        ComputerSystem cs = si.getHardware().getComputerSystem();
        GlobalMemory mem = si.getHardware().getMemory();
        StringBuilder sb = new StringBuilder();

        sb.append(System.getProperty("user.name"));
        sb.append(cpu.getProcessorIdentifier().getIdentifier());
        sb.append(cs.getBaseboard().getSerialNumber());
        sb.append(mem.getTotal());

        for (GraphicsCard gc : si.getHardware().getGraphicsCards()) {
            sb.append(gc.getDeviceId());
            sb.append(gc.getName());
        }
        return sb.toString();
    }

    public static String getSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getHWID() {
        return getSHA256(getHardwareInfo());
    }
} 