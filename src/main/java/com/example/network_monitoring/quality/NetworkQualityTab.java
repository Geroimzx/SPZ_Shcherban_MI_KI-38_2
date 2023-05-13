package com.example.network_monitoring.quality;

import com.example.network_monitoring.notification.FloatingNotification;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Клас NetworkQualityTab представляє собою моніторинг якості з'єднання з серверами
 */
public class NetworkQualityTab extends Tab {
    /**
     * Таблиця з даними про якість з'єднання з серверами
     */
    private TableView<Data> table;

    /**
     * Кількість серверів
     */
    private final int serverCount = 5;

    /**
     * Масив серверів
     */
    private ServerListElement[] serverListElement;

    /**
     * Конструктор класу NetworkQualityTab
     * Налаштовує TableView
     * Запускає оновлення якості з'єднання з заданим списком сервером
     */
    public NetworkQualityTab() {
        setText("Якість з'єднання");

        serverListElement = new ServerListElement[serverCount];
        serverListElement[0] = new ServerListElement("google.com");
        serverListElement[1] = new ServerListElement("youtube.com");
        serverListElement[2] = new ServerListElement("bing.com");
        serverListElement[3] = new ServerListElement("duckduckgo.com");
        serverListElement[4] = new ServerListElement("zoom.us");

        // Створюємо колонки таблиці
        TableColumn<Data, String> serverAddressColumn = new TableColumn<>("Адреса сервера");
        serverAddressColumn.setMinWidth(500);
        serverAddressColumn.setSortable(false);
        serverAddressColumn.setCellValueFactory(new PropertyValueFactory<>("serverAddress"));

        TableColumn<Data, String> responseTimeColumn = new TableColumn<>("Час відповіді");
        responseTimeColumn.setMinWidth(100);
        responseTimeColumn.setSortable(false);
        responseTimeColumn.setCellValueFactory(new PropertyValueFactory<>("responseTime"));

        TableColumn<Data, String> connectionQualityColumn = new TableColumn<>("Якість з'єднання");
        connectionQualityColumn.setMinWidth(200);
        connectionQualityColumn.setSortable(false);
        connectionQualityColumn.setCellValueFactory(new PropertyValueFactory<>("connectionQuality"));

        // Створюємо таблицю та додаємо до неї колонки
        table = new TableView<>();
        table.getColumns().addAll(serverAddressColumn, responseTimeColumn, connectionQualityColumn);

        startUpdating();

        setContent(table);
    }

    /**
     * Повертає дані для таблиці table
     */
    private ObservableList<Data> getData() {
        ObservableList<Data> data = FXCollections.observableArrayList();
        for (ServerListElement element:
                serverListElement) {
            data.add(new Data(element.getServerAddress(), element.getResponseTime(), element.getQuality()));
        }
        return data;
    }

    /**
     * Оновлює дані про якість зєднання з серверами
     * Вносить оновлені дані в таблицю table
     */
    private void startUpdating() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    for (int i = 0; i < serverCount; i++) {
                        // Виконання запиту до сервера та вимірювання часу відповіді
                        try {
                            long startTime = System.currentTimeMillis();
                            Socket socket = new Socket(serverListElement[i].getServerAddress(), 80);
                            socket.close();
                            serverListElement[i].setResponseTime(System.currentTimeMillis() - startTime);

                            if (serverListElement[i].getResponseTime() < 50) {
                                serverListElement[i].setQuality(ServerListElement.qualityEnum.excellent.toString());
                            } else if (serverListElement[i].getResponseTime() < 400) {
                                serverListElement[i].setQuality(ServerListElement.qualityEnum.good.toString());
                            }else {
                                serverListElement[i].setQuality(ServerListElement.qualityEnum.bad.toString());
                            }
                        } catch (IOException e) {
                            // Обробка помилки підключення до сервера
                            serverListElement[i].setResponseTime(999999);
                            serverListElement[i].setQuality(ServerListElement.qualityEnum.unavailable.toString());
                            // Запуск виводу на екран спливаючого вікна з сповіщенням про помилку доступу до сервера
                            new FloatingNotification().showNotification("Помилка доступу до серверу:\n" + serverListElement[i].getServerAddress() + "\nПеревірте з'єднання з мережею!");
                        }
                    }
                    table.setItems(getData());
                });
            }
        }, 1000, 10000);
    }

    /**
     * Клас, який описує дані, що будуть відображатися в таблиці table
     */
    public static class Data {

        private String serverAddress;
        private long responseTime;
        private String connectionQuality;

        public Data(String serverAddress, long responseTime, String connectionQuality) {
            this.serverAddress = serverAddress;
            this.responseTime = responseTime;
            this.connectionQuality = connectionQuality;
        }

        public String getServerAddress() {
            return serverAddress;
        }

        public long getResponseTime() {
            return responseTime;
        }

        public String getConnectionQuality() {
            return connectionQuality;
        }
    }
}
