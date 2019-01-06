/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import com.maxmind.geoip.LookupService;

import javax.swing.*;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

//custom class to do some IP related tasks
public class IPaddressOperations {

    //Public IP address finder using ipify library
    public String publicIP() {
        //public IP Finder using IPIFY LIBRARY
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
            String publicIP = s.next();
            return publicIP;
        } catch (Exception e) {
            return "Cannot find public IP please check the internet connection !";
        }
    }


    //Local IP address end of range finder
    public String ipRangeEnd(String IP) {
        String firstOcts = IP.substring(0, IP.lastIndexOf("."));
        String IpRangeEnd = firstOcts + ".255";
        return IpRangeEnd;
    }

    public String ipRangeStart(String IP) {
        String firstOcts = IP.substring(0, IP.lastIndexOf("."));
        String IpRangeStart = firstOcts + ".1";
        return IpRangeStart;
    }

    //Country locator using geoIP library
    public String ipGeoLocation(String IP) {
        try {
            String dir = "resources/";
            String dbfile = "resources/GeoIP.dat";
            //String dbfile = "C:\\Users\\Abdelsalam Shahlol\\Documents\\NetBeansProjects\\IP Tools\\resources/GeoIP.dat";
            String location = "";
            System.err.println(dbfile);
            LookupService cl = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);
            location = cl.getCountry(IP).getName() + " " + cl.getCountry(IP).getCode();
            cl.close();
            return location;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "<html>Path to <b>Database</b> is invalid <br>or <b>Database</b> doesn't exsist !</html>", "Error processing " + IP, JOptionPane.ERROR_MESSAGE);
            // return e.getMessage();
            return "";
        }
    }

    //Resolve HOSTNAME to IP address
    public String returnIP(String Hostname) {
        String theIP = "";
        int index = Hostname.indexOf("://");
        if (index != -1) {
            Hostname = Hostname.substring(index + 3);
        }
        index = Hostname.indexOf('/');
        if (index != -1) {
            Hostname = Hostname.substring(0, index);
        }
        Hostname = Hostname.replaceFirst("^www.*?\\.", "");
        try {
            InetAddress ip = InetAddress.getByName(Hostname);
            theIP = ip.getHostAddress().toString();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Can not resolve hostname.", "Alert", JOptionPane.HEIGHT);
            theIP = "";
        }
        return theIP;
    }


    //Get the hostname of the IP address
    public String hostname(String IP) {
        String hostname = "";
        try {
            InetAddress add = InetAddress.getByName(IP);
            if (!IP.equals(add.getHostName())) {
                hostname = add.getHostName();
            }
            if (IP.equals(InetAddress.getLocalHost().getHostAddress())) {
                hostname = InetAddress.getLocalHost().getHostName();
            }
        } catch (Exception ex) {
            Logger.getLogger(IPaddressOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hostname;
    }

}
