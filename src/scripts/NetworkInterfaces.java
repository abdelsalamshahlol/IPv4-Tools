/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


/**
 *
 * @author Abdelsalam Shahlol
 */
//This class looks for network interfaces in the device and return a list
public class NetworkInterfaces {
    public static Enumeration<NetworkInterface> displayInterfaceInformation() throws SocketException {
        
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        return nets;
    }
}
