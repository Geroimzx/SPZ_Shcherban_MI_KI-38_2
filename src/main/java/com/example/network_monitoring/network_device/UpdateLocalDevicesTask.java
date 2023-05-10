package com.example.network_monitoring.network_device;

import com.example.network_monitoring.network_device.NetworkDevice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class UpdateLocalDevicesTask extends Task<Void> {

    public ObservableList<NetworkDevice> deviceList;

    public UpdateLocalDevicesTask(ObservableList<NetworkDevice> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    protected Void call() throws Exception {
        while (true) {
            // Оновлення deviceList за допомогою методу getLocalDevices()
            ObservableList<NetworkDevice> newDevices = FXCollections.observableArrayList(NetworkDevice.getLocalDevices());

            // Очищення старих даних в deviceList та додавання нових
            deviceList.clear();
            deviceList.addAll(newDevices);

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
