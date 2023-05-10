package com.example.network_monitoring.network_traffic;


public class ServerListElement {
    private String serverAddress;
    private long responseTime;
    private String quality;

    public ServerListElement(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public enum qualityEnum{
        unavailable,
        bad,
        good,
        excellent
    };
}
