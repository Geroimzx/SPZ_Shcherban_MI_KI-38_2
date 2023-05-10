package com.example.network_monitoring.network_traffic;

import javafx.concurrent.Task;

import java.io.IOException;
import java.net.Socket;

public class ServerCheckTask extends Task<Void> {

    private String serverAddress;
    private long responseTime;

    private String quality;

    private enum qualityEnum{
        bad,
        good,
        excellent
    };

    public ServerCheckTask(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Override
    protected Void call() throws Exception {
        while (true) {
            // Виконання запиту до сервера та вимірювання часу відповіді
            long startTime = System.currentTimeMillis();
            try {
                Socket socket = new Socket(serverAddress, 80);
                socket.close();
            } catch (IOException e) {
                // Обробка помилки підключення до сервера
            }
            responseTime = System.currentTimeMillis() - startTime;
            if (responseTime < 10) {
                quality = qualityEnum.excellent.toString();
            } else if (responseTime < 400) {
                quality = qualityEnum.good.toString();
            } else {
                quality = qualityEnum.bad.toString();
            }
            System.out.println(serverAddress + " " + responseTime +  " " + quality);

            // Очікування 3 секунди перед наступним оновленням
            Thread.sleep(3000);
        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
}
