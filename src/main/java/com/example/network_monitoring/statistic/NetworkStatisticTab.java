package com.example.network_monitoring.statistic;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Клас NetworkStatisticTab представляє собою графік використання мережі, який відображає використання мережі у вигляді RX та TX байтів на часовому проміжку та статистику мережі.
 * Графік реалізований з використанням JavaFX.
 */
public class NetworkStatisticTab extends Tab {
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
     * Мітка для відображення статистика справа.
     */
    private Label rightTextLabel;

    /**
     * Масив міток для відображення знизу.
     */
    private Label[] bottomTextLabels;

    /**
     * Створює новий екземпляр класу NetworkUsageGraph і налаштовує користувацький інтерфейс.
     * Також розпочинає процес оновлення графіку.
     */
    public NetworkStatisticTab() {
        setText("Трафік і статистика мережевих інтерфейсів");

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
        // Створення графіка
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Час (секунди)");
        yAxis.setLabel("Байти");

        final AreaChart<Number, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setTitle("Використання мережі для всіх мережевих інтерфейсів\n\t\t\t(включно з локальними)");

        rxSeries = new XYChart.Series<>();
        rxSeries.setName("Rx (отримані байти)");
        txSeries = new XYChart.Series<>();
        txSeries.setName("Tx (надіслані байти)");

        chart.getData().addAll(rxSeries, txSeries);

        // Створення GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(2);
        gridPane.setVgap(5);

        // Додавання графіка на GridPane
        gridPane.add(chart, 0, 0, 1, 10);

        // Створення Текстового поля 1 справа
        rightTextLabel = new Label("Завантаження...");
        rightTextLabel.setStyle("-fx-font-size: 12px; -fx-font-family: Arial;");
        rightTextLabel.setPrefWidth(350);
        gridPane.add(rightTextLabel, 1, 1);


        NetworkStatistic networkStatistic = new NetworkStatistic();
        // Створюємо контейнер VBox і додаємо в нього мітки та комбінований список
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(networkStatistic.getSentLabel(), networkStatistic.getReceivedLabel(), networkStatistic.getTotalLabel(), networkStatistic.getUnitComboBox());

        gridPane.add(vbox, 2, 1);


        int rowCountGP = gridPane.getRowCount();
        int k = 0;

        // Створення текстових полів знизу
        bottomTextLabels = new Label[8];
        for (int i = 0; i < 8; i++) {
            Label textLabel = new Label("Завантаження...");
            bottomTextLabels[i] = textLabel;
            bottomTextLabels[i].setStyle("-fx-font-size: 12px; -fx-font-family: Arial;");
            bottomTextLabels[i].setPrefWidth(350);
            if(i%2==0) {
                gridPane.add(bottomTextLabels[i], k, rowCountGP);
            } else {
                gridPane.add(bottomTextLabels[i], k, rowCountGP+1);
                k++;
            }
        }

        setContent(gridPane);
    }

    /**
     * Починає оновлення даних мережевого використання та графіка на проміжках часу.
     * Використовує Timer для запуску оновлення в фоновому потоці та Platform.runLater() для оновлення в головному потоці JavaFX.
     * Оновлює дані мережевого використання та графік кожну секунду.
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
     * Процес будується за допомогою ProcessBuilder з командою "cmd.exe /c netstat -e -s".
     * Читає вивід процесу через буферизований читач BufferedReader.
     * Обробляє кожен рядок згідно заданого алгоритму для подальшого виведення користувачеві
     * Якщо знаходиться рядок з відповідною інформацією, розбиває його на частини і оновлює значення.
     * Закриває читач і знищує процес.
     * У разі виникнення виключення IOException, виводить стек виключення.
     */
    private void updateNetworkUsage() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "netstat", "-e", "-s");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();
            StringBuilder sb4 = new StringBuilder();
            StringBuilder sb5 = new StringBuilder();
            StringBuilder sb6 = new StringBuilder();
            StringBuilder sb7 = new StringBuilder();
            StringBuilder sb8 = new StringBuilder();

            String b = "";

            for (int i = 0; i < 126; i++) {
                line = reader.readLine();
                String[] parts = line.split("\\s+");

                if(i<=9){
                    sb.append(line);
                    sb.append(System.lineSeparator());
                } else {
                   rightTextLabel.setText(sb.toString());
                }

                if(i>9 && i<=30){
                   sb1.append(line);
                   sb1.append(System.lineSeparator());
                } else {
                    bottomTextLabels[0].setText(sb1.toString());
                }

                if(i>30 && i<=49){
                    sb2.append(line);
                    sb2.append(System.lineSeparator());
                } else {
                    bottomTextLabels[1].setText(sb2.toString());
                }

                if(i>49 && i<=68){
                    sb3.append(line);
                    sb3.append(System.lineSeparator());
                } else {
                    bottomTextLabels[2].setText(sb3.toString());
                }

                if(i>68 && i<=89){
                    sb4.append(line);
                    sb4.append(System.lineSeparator());
                } else {
                    bottomTextLabels[3].setText(sb4.toString());
                }

                if(i>89 && i<=100){
                    sb5.append(line);
                    sb5.append(System.lineSeparator());
                } else {
                    bottomTextLabels[4].setText(sb5.toString());
                }

                if(i>100 && i<=111){
                    sb6.append(line);
                    sb6.append(System.lineSeparator());
                } else {
                    bottomTextLabels[5].setText(sb6.toString());
                }

                if(i>111 && i<=118){
                    sb7.append(line);
                    sb7.append(System.lineSeparator());
                } else {
                    bottomTextLabels[6].setText(sb7.toString());
                }

                if(i>118 && i<=125){
                    sb8.append(line);
                    sb8.append(System.lineSeparator());
                } else {
                    bottomTextLabels[7].setText(sb8.toString());
                }

                if (line.contains("Bytes")) {
                    // Дані для графіку використання мережі для всіх інтерфейсів кожну секунду
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
     * Координати X відповідають розміру відповідних серій даних, а координати Y представляють кількість байт.
     */
    private void updateChart() {
        rxSeries.getData().add(new XYChart.Data<>(rxSeries.getData().size(), rxBytes));
        txSeries.getData().add(new XYChart.Data<>(txSeries.getData().size(), txBytes));
    }

}
