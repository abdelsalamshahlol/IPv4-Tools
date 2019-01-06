/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
///Custom Class to iterate through ip addresses from start to end
package scripts;

public class IPAddress {

    private int value;

    public IPAddress(int value) {
        this.value = value;
    }

    public IPAddress(String stringValue) {
        String[] parts = stringValue.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException();
        }
        value
                = (Integer.parseInt(parts[0], 10) << (8 * 3)) & 0xFF000000
                | (Integer.parseInt(parts[1], 10) << (8 * 2)) & 0x00FF0000
                | (Integer.parseInt(parts[2], 10) << (8 * 1)) & 0x0000FF00
                | (Integer.parseInt(parts[3], 10) << (8 * 0)) & 0x000000FF;
    }

    public int getOctet(int i) {

        if (i < 0 || i >= 4) {
            throw new IndexOutOfBoundsException();
        }

        return (value >> (i * 8)) & 0x000000FF;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 3; i >= 0; --i) {
            sb.append(getOctet(i));
            if (i != 0) {
                sb.append(".");
            }
        }

        return sb.toString();

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IPAddress) {
            return value == ((IPAddress) obj).value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value;
    }

    public int getValue() {
        return value;
    }

    public IPAddress next() {
        return new IPAddress(value + 1);
    }

    /**
     * @param args
     */
//    public static void main(String[] args) {
//
//        IPAddress ip1 = new IPAddress("192.168.0.1");
//
//        System.out.println("ip1 = " + ip1);
//
//        IPAddress ip2 = new IPAddress("192.169.5.255");
//
//        System.out.println("ip2 = " + ip2);
//
//        System.out.println("Looping:");
//
//        do {
//
//            ip1 = ip1.next();
//
//            System.out.println(ip1);
//
//        } while (!ip1.equals(ip2));
//
//    }
}
