/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import macnificent.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Abdelsalam Shahlol
 */
public class MacAddressOperations {
//This is a custom class that uses Windows arp command to get MAC address since JAVA DOESN'T support this yet !

    public static String getMac(String ip) throws IOException {

        String systemInput = "";
        String mac = "";
        Runtime.getRuntime().exec("arp -a");
        if (!InetAddress.getLocalHost().getHostAddress().equals(ip)) {
            Scanner s = new Scanner(Runtime.getRuntime().exec("arp -a " + ip).getInputStream()).useDelimiter("\\A");
            systemInput = s.next();
            Pattern pattern = Pattern.compile("\\s{0,}([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})");
            Matcher matcher = pattern.matcher(systemInput);
            if (matcher.find()) {
                mac = mac + matcher.group().replaceAll("\\s", "").toUpperCase();
            }
        } else {
            NetworkInterface nwi = NetworkInterface.getByInetAddress(InetAddress.getByName(ip));
            byte macbyte[] = nwi.getHardwareAddress();
            StringBuilder sb = new StringBuilder(18);
            for (byte b : macbyte) {
                if (sb.length() > 0) {
                    sb.append('-');
                }
                sb.append(String.format("%02x", b));
            }
            mac = sb.toString().toUpperCase();
        }
        return mac;
    }

    public static String getOUI(String mac) {
        String manufacturer = "";
        try {
            OuiRegistry reg = new OuiRegistry();
            String s = mac;
            MacAddress CheckMac = new MacAddress(s); 
            Oui oui = reg.getOui(CheckMac);
            manufacturer = (oui.getManufacturer() == "" ? "Unknown" : oui.getManufacturer());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return manufacturer;
    }
}
