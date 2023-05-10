package com.example.network_monitoring.network_traffic;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ServerCheckController {
    @FXML
    private ListView<String> serverList;

    private ServerCheckTask serverCheckTask;

    public ServerCheckController() {
        serverList = new ListView<>();
        serverList.getItems().add("example.com");

        serverCheckTask = new ServerCheckTask("google.com");
        serverCheckTask.start();
    }

    public ListView<String> getServerList() {
        return serverList;
    }

    public void setServerList(ListView<String> serverList) {
        this.serverList = serverList;
    }

    public static void checkServer() {

    }

}
