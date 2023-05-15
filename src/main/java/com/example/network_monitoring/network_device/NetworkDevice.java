package com.example.network_monitoring.network_device;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Представляє собою мережевий пристрій з його властивостями.
 */
public class NetworkDevice {
    /**
     * Назва мережевого пристрою
     */
    private String name;

    /**
     * IP-адреса мережевого пристрою
     */
    private InetAddress ipAddress;
    /**
     * MAC-адреса мережевого пристрою
     */
    private String macAddress;

    /**
     * Тип мережевого пристрою
     */
    private String type;

    /**
     * Статус активності мережевого пристрою
     */
    private boolean isUp;

    /**
     * Статус петлі мережевого пристрою
     */
    private boolean isLoopback;

    /**
     * Конструктор класу NetworkDevice з вказаними властивостями.
     *
     * @param name        назва мережевого пристрою
     * @param ipAddress     IP-адреса мережевого пристрою
     * @param macAddress    MAC-адреса мережевого пристрою
     * @param type          тип мережевого пристрою
     * @param isUp     true, якщо мережевий пристрій активний, false - в іншому випадку
     * @param isLoopback        true, якщо мережевий пристрій є петлею, false - в іншому випадку
     */
    public NetworkDevice(String name, InetAddress ipAddress, String macAddress, String type, boolean isUp, boolean isLoopback) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.type = type;
        this.isUp = isUp;
        this.isLoopback = isLoopback;
    }

    /**
     * Повертає назву мережевого пристрою.
     *
     * @return назва мережевого пристрою
     */
    public String getName() {
        return name;
    }

    /**
     * Встановлює назву мережевого пристрою.
     *
     * @param name назва мережевого пристрою
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Повертає IP-адресу мережевого пристрою.
     *
     * @return IP-адреса мережевого пристрою
     */
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     * Встановлює IP-адресу мережевого пристрою.
     *
     * @param ipAddress IP-адреса мережевого пристрою
     */
    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Повертає MAC-адресу мережевого пристрою.
     *
     * @return MAC-адреса мережевого пристрою
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Встановлює MAC-адресу мережевого пристрою.
     *
     * @param macAddress MAC-адреса мережевого пристрою
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * Повертає тип мережевого пристрою.
     *
     * @return тип мережевого пристрою
     */
    public String getType() {
        return type;
    }

    /**
     * Встановлює тип мережевого пристрою.
     *
     * @param type тип мережевого пристрою
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Повертає статус активності мережевого пристрою.
     *
     * @return true, якщо мережевий пристрій активний, false - в іншому випадку
     */
    public boolean getIsUp() {
        return isUp;
    }

    /**
     * Встановлює статус активності мережевого пристрою.
     *
     * @param isRun true, якщо мережевий пристрій активний, false - в іншому випадку
     */
    public void setIsRun(boolean isRun) {
        this.isUp = isRun;
    }

    /**
     * Повертає статус петлі мережевого пристрою.
     *
     * @return true, якщо мережевий пристрій є петлею, false - в іншому випадку
     */
    public boolean getIsLoopback() {
        return isLoopback;
    }

    /**
     * Встановлює статус петлі мережевого пристрою.
     *
     * @param loopback true, якщо мережевий пристрій є петлею, false - в іншому випадку
     */
    public void setIsLoopback(boolean loopback) {
        isLoopback = loopback;
    }

    /**
     * Отримує список локальних мережевих пристроїв.
     *
     * @return список локальних мережевих пристроїв
     * @throws SocketException якщо сталася помилка під час отримання мережевих інтерфейсів
     */
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
