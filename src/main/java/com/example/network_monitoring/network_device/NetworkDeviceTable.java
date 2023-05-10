package com.example.network_monitoring.network_device;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.SocketException;

public class NetworkDeviceTable {
    private TableView<NetworkDevice> networkDevicesTable = new TableView<>();

    private TableColumn<NetworkDevice, String> nameCol;
    private TableColumn<NetworkDevice, String> ipAddressCol;
    private TableColumn<NetworkDevice, String> macAddressCol;
    private TableColumn<NetworkDevice, String> deviceTypeCol;
    private TableColumn<NetworkDevice, Boolean> isUpCol;
    private TableColumn<NetworkDevice, Boolean> isLoopbackCol;

    private ObservableList<NetworkDevice> networkDevices;

    public NetworkDeviceTable() throws SocketException {
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

        networkDevicesTable.getColumns().addAll(nameCol, ipAddressCol, macAddressCol, deviceTypeCol, isUpCol, isLoopbackCol);

        // Створення ObservableList та встановлення його як джерела даних для TableView
        networkDevices = FXCollections.observableArrayList();
        networkDevicesTable.setItems(networkDevices);


        UpdateLocalDevicesTask updateTask = new UpdateLocalDevicesTask(networkDevices);
        updateTask.start();
    }

    public TableView<NetworkDevice> getNetworkDevicesTable() {
        return networkDevicesTable;
    }

    public void setNetworkDevicesTable(TableView<NetworkDevice> networkDevicesTable) {
        this.networkDevicesTable = networkDevicesTable;
    }
}
