package com.example.network_monitoring.traffic_limit;

import com.example.network_monitoring.notification.FloatingNotification;
import javafx.application.Platform;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Клас, що представляє обмеження трафіку.
 */
public class TrafficLimit implements Serializable {
    private static final String TRAFFIC_LIMIT_FILE = "traffic_limit.ser";
    private long trafficLimit;
    private long trafficAmount;

    /**
     * Конструктор класу TrafficLimit.
     *
     * @param trafficLimit обмеження трафіку
     */
    public TrafficLimit(long trafficLimit) {
        this.trafficLimit = trafficLimit;
        this.trafficAmount = 0;
    }

    /**
     * Встановлює обмеження трафіку.
     *
     * @param trafficLimit обмеження трафіку
     */
    public void setTrafficLimit(long trafficLimit) {
        this.trafficLimit = trafficLimit;
        saveTrafficLimits();
    }

    /**
     * Повертає обмеження трафіку.
     *
     * @return обмеження трафіку
     */
    public long getTrafficLimit() {
        return trafficLimit;
    }

    /**
     * Встановлює кількість трафіку.
     *
     * @param trafficAmount кількість трафіку
     */
    public void setTrafficAmount(long trafficAmount) {
        this.trafficAmount = trafficAmount;
    }

    /**
     * Встановлює значення трафіку і перевіряє, чи було досягнуто обмеження трафіку.
     *
     * @param amount значення трафіку
     */
    public void setTraffic(long amount) {
        trafficAmount = amount;

        if (trafficAmount >= trafficLimit) {
            new FloatingNotification().showNotification("Ліміт трафіку вичерпано!\n" + trafficAmount + "/" + trafficLimit + " МБ");
        } else if (trafficAmount > trafficLimit*0.9) {
            new FloatingNotification().showNotification("Трафік вичерпано на 90+%\n" + trafficAmount + "/" + trafficLimit + " МБ");
        }

        saveTrafficLimits();
    }

    /**
     * Оновлює значення трафіку з використанням команди "netstat -e".
     * Використовується для оновлення даних статистики мережевого використання.
     */
    private void updateTrafficLabels() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "netstat", "-e");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Bytes")) {
                    String[] parts = line.split("\\s+");
                    double traffic = (parseBytes(parts[1]) + parseBytes(parts[2]))  / Math.pow(1024, 2);
                    setTraffic((long) traffic);
                    break;
                }
            }
            reader.close();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Починає оновлення даних статистики мережевого використання.
     * Використовує Timer для запуску оновлення в фоновому потоці та Platform.runLater() для оновлення в головному потоці JavaFX.
     * Оновлює дані статистики мережевого використання кожні 30 секунд.
     */
    public void startUpdating() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    updateTrafficLabels();
                });
            }
        }, 500, 5000); // Оновлюємо кожні 30 секунд
    }

    /**
     * Парсить рядок, який представляє значення в байтах, і повертає відповідне числове значення.
     *
     * @param bytesStr рядок, що містить значення в байтах
     * @return числове значення в байтах
     */
    private long parseBytes(String bytesStr) {
        String value = bytesStr.replaceAll("[^0-9]", "");
        return Long.parseLong(value);
    }

    /**
     * Зберігає обмеження трафіку в файл.
     */
    protected void saveTrafficLimits() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(TRAFFIC_LIMIT_FILE))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Завантажує обмеження трафіку з файлу.
     *
     * @return об'єкт TrafficLimit, якщо завантаження успішне, в іншому випадку - null
     */
    public static TrafficLimit loadTrafficLimits() {
        TrafficLimit trafficLimit = null;

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(TRAFFIC_LIMIT_FILE))) {
            trafficLimit = (TrafficLimit) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return trafficLimit;
    }
}