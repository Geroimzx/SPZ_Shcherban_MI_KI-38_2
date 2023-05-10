package com.example.network_monitoring.network_usage;

import javafx.application.Platform;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Представляє графік використання мережі, який відображає використання мережі у вигляді RX та TX байтів на часовому проміжку.
 * Графік реалізований з використанням JavaFX.
 */
public class NetworkUsageGraph extends Tab {
    /**
     * Серія даних для отриманих байтів (Rx) на графіку.
     */
    private XYChart.Series<Number, Number> rxSeries;
    /**
     * Серія даних для надісланих байтів (Tx) на графіку.
     */
    private XYChart.Series<Number, Number> txSeries;
    /**
     * Кількість отриманих байтів (Rx).
     */
    private long rxBytes;
    /**
     * Кількість надісланих байтів (Tx).
     */
    private long txBytes;
    /**
     * Попереднє значення отриманих байтів (Rx).
     */
    private long oldRxBytes = 0L;
    /**
     * Попереднє значення надісланих байтів (Tx).
     */
    private long oldTxBytes = 0L;

    /**
     * Створює новий екземпляр класу NetworkUsageGraph і налаштовує користувацький інтерфейс.
     * Також розпочинає процес оновлення графіку.
     */
    public NetworkUsageGraph() {
        setupUI();
        startUpdating();
    }

    /**
     * Налаштовує графічний інтерфейс користувача для відображення графіка використання мережі.
     * Встановлює вісі X та Y, створює область графіка та встановлює заголовок.
     * Ініціалізує серії даних для отриманих та надісланих байтів.
     * Додає серії даних до графіка та встановлює його вміст.
     */
    private void setupUI() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Час (секунди)");
        yAxis.setLabel("Байти");

        final AreaChart<Number, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setTitle("Використання мережі");

        rxSeries = new XYChart.Series<>();
        rxSeries.setName("Rx (отримані байти)");
        txSeries = new XYChart.Series<>();
        txSeries.setName("Tx (надіслані байти)");

        chart.getData().addAll(rxSeries, txSeries);

        setContent(chart);
    }

    /**
     * Налаштовує графічний інтерфейс користувача для відображення графіка використання мережі.
     * Встановлює вісі X та Y, створює область графіка та встановлює заголовок.
     * Ініціалізує серії даних для отриманих та надісланих байтів.
     * Додає серії даних до графіка та встановлює його вміст.
     */
    private void startUpdating() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    updateNetworkUsage();
                    updateChart();
                });
            }
        }, 0, 1000); // Оновлюємо графік кожну секунду
    }

    /**
     * Оновлює використання мережі.
     * Метод виконує запит до командного рядка для отримання даних про використання мережі.
     * Процес будується за допомогою ProcessBuilder з командою "cmd.exe /c netstat -e".
     * Читає вивід процесу через буферизований читач BufferedReader.
     * Обробляє кожен рядок виводу і перевіряє, чи містить він слово "Bytes".
     * Якщо знаходиться рядок з відповідною інформацією, розбиває його на частини і оновлює значення отриманих і надісланих байтів.
     * Враховує можливі зміни шляхом порівняння з попередніми значеннями.
     * Закриває читач і знищує процес.
     * У разі виникнення виключення IOException, виводить стек виключення.
     */
    private void updateNetworkUsage() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "netstat", "-e");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Bytes")) {
                    String[] parts = line.split("\\s+");
                    if(oldRxBytes == 0 || oldTxBytes == 0) {
                        oldRxBytes = parseBytes(parts[1]);
                        oldTxBytes = parseBytes(parts[2]);
                    }

                    if(parseBytes(parts[1]) - oldRxBytes >= 0 && parseBytes(parts[2]) - oldTxBytes >= 0) {
                        rxBytes = parseBytes(parts[1]) - oldRxBytes;
                        txBytes = parseBytes(parts[2]) - oldTxBytes;

                        oldRxBytes = parseBytes(parts[1]);
                        oldTxBytes = parseBytes(parts[2]);
                    }
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
     * Перетворює рядок, що містить значення байтів, у відповідне довге ціле число.
     * @param bytesStr рядок, що містить значення байтів
     * @return довге ціле число, яке представляє значення байтів
     */
    private long parseBytes(String bytesStr) {
        String value = bytesStr.replaceAll("[^0-9]", "");
        return Long.parseLong(value);
    }

    /**
     * Оновлює графік, додаючи нові дані про використання мережі.
     * Кожне нове значення додається до відповідних серій даних (Rx та Tx) на графіку.
     * Координати X відповідають розміру відповідних серій даних, а координати Y представляють кількість байтів.
     */
    private void updateChart() {
        rxSeries.getData().add(new XYChart.Data<>(rxSeries.getData().size(), rxBytes));
        txSeries.getData().add(new XYChart.Data<>(txSeries.getData().size(), txBytes));
    }
}
