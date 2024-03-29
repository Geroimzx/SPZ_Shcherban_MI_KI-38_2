package com.example.network_monitoring.network_device;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Представляє собою вкладку для відображення таблиці мережевих пристроїв.
 */
public class NetworkDeviceTab extends Tab {
    /**
     * Таблиця, що відображає мережеві пристрої.
     */
    private TableView<NetworkDevice> networkDevicesTable = new TableView<>();

    /**
     * Стовпець для відображення імені мережевого пристрою.
     */
    private TableColumn<NetworkDevice, String> nameCol;

    /**
     * Стовпець для відображення IP-адреси мережевого пристрою.
     */
    private TableColumn<NetworkDevice, String> ipAddressCol;

    /**
     * Стовпець для відображення MAC-адреси мережевого пристрою.
     */
    private TableColumn<NetworkDevice, String> macAddressCol;

    /**
     * Стовпець для відображення типу мережевого пристрою.
     */
    private TableColumn<NetworkDevice, String> deviceTypeCol;

    /**
     * Стовпець для відображення статусу "Up" мережевого пристрою.
     */
    private TableColumn<NetworkDevice, Boolean> isUpCol;

    /**
     * Стовпець для відображення статусу "Loopback" мережевого пристрою.
     */
    private TableColumn<NetworkDevice, Boolean> isLoopbackCol;

    /**
     * Список мережевих пристроїв, що використовується як джерело даних для таблиці.
     */
    private ObservableList<NetworkDevice> networkDevices;

    /**
     * Конструктор класу NetworkDeviceTab.
     */
    public NetworkDeviceTab() {
        setText("Мережеві пристрої");

        nameCol = new TableColumn<>("Ім'я");
        ipAddressCol = new TableColumn<>("IP-адреса");
        macAddressCol = new TableColumn<>("MAC-адреса");
        deviceTypeCol = new TableColumn<>("Тип");
        isUpCol = new TableColumn<>("Up");
        isLoopbackCol = new TableColumn<>("Loopback");

        nameCol.setSortable(false);
        ipAddressCol.setSortable(false);
        macAddressCol.setSortable(false);
        deviceTypeCol.setSortable(false);
        isUpCol.setSortable(false);
        isLoopbackCol.setSortable(false);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ipAddressCol.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        macAddressCol.setCellValueFactory(new PropertyValueFactory<>("macAddress"));
        deviceTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        isUpCol.setCellValueFactory(new PropertyValueFactory<>("isUp"));
        isLoopbackCol.setCellValueFactory(new PropertyValueFactory<>("isLoopback"));

        nameCol.setMinWidth(480);
        ipAddressCol.setMinWidth(220);
        macAddressCol.setMinWidth(200);

        networkDevicesTable.getColumns().addAll(nameCol, ipAddressCol, macAddressCol, deviceTypeCol, isUpCol, isLoopbackCol);

        // Створення ObservableList та встановлення його як джерела даних для TableView
        networkDevices = FXCollections.observableArrayList();
        networkDevicesTable.setItems(networkDevices);

        startUpdating();

        setContent(networkDevicesTable);
    }

    /**
     * Запускає процес оновлення таблиці мережевих пристроїв.
     */
    private void startUpdating() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Оновлення deviceList за допомогою методу getLocalDevices()
                    ObservableList<NetworkDevice> newDevices = null;
                    try {
                        newDevices = FXCollections.observableArrayList(NetworkDevice.getLocalDevices());
                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    }
                    // Очищення старих даних в deviceList та додавання нових
                    networkDevices.clear();
                    networkDevices.addAll(newDevices);
                });
            }
        }, 0, 3000); // Оновлюємо кожні 3 секунди
    }
}
