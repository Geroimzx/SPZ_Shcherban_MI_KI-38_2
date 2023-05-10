package com.example.network_monitoring.network_device;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class NetworkDevice {
    private String name;
    private InetAddress ipAddress;
    private String macAddress;
    private String type;
    private boolean isUp;
    private boolean isLoopback;

    public NetworkDevice(String name, InetAddress ipAddress, String macAddress, String type, boolean isUp, boolean isLoopback) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.type = type;
        this.isUp = isUp;
        this.isLoopback = isLoopback;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsUp() {
        return isUp;
    }

    public void setIsRun(boolean isRun) {
        this.isUp = isRun;
    }

    public boolean getIsLoopback() {
        return isLoopback;
    }

    public void setIsLoopback(boolean loopback) {
        isLoopback = loopback;
    }

    public static ArrayList<NetworkDevice> getLocalDevices() throws SocketException {
        ArrayList<NetworkDevice> devices = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            String name = null;
            InetAddress ipAddress = null;
            String macAddress = null;

            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> addresses = ni.getInetAddresses();

            name = ni.getDisplayName();


            if (addresses.hasMoreElements()) {
                ipAddress = addresses.nextElement();
            }

            byte[] mac = ni.getHardwareAddress();
            if (mac != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                }
                macAddress = sb.toString();
            }

            NetworkDevice device = new NetworkDevice(name, ipAddress, macAddress, ni.isVirtual() ? "Virtual" : "Physical", ni.isUp(), ni.isLoopback());
            devices.add(device);
        }
        return devices;
    }
}
