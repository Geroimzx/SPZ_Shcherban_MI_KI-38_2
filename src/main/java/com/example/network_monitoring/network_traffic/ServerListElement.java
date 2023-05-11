package com.example.network_monitoring.network_traffic;

/**
 * Елемент списку серверів, що використовується для моніторингу мережевого трафіку.
 */
public class ServerListElement {
    /**
     * Адреса сервера.
     */
    private String serverAddress;

    /**
     * Час відповіді сервера.
     */
    private long responseTime;

    /**
     * Якість сервера.
     */
    private String quality;

    /**
     * Конструктор класу ServerListElement.
     *
     * @param serverAddress адреса сервера
     */
    public ServerListElement(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Отримує адресу сервера.
     *
     * @return адреса сервера
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Встановлює адресу сервера.
     *
     * @param serverAddress адреса сервера
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Отримує час відповіді сервера.
     *
     * @return час відповіді сервера
     */
    public long getResponseTime() {
        return responseTime;
    }

    /**
     * Встановлює час відповіді сервера.
     *
     * @param responseTime час відповіді сервера
     */
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * Отримує якість сервера.
     *
     * @return якість сервера
     */
    public String getQuality() {
        return quality;
    }

    /**
     * Встановлює якість сервера.
     *
     * @param quality якість сервера
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * Перерахування, що визначає можливі значення якості сервера.
     */
    public enum qualityEnum {
        unavailable,
        bad,
        good,
        excellent
    }
}