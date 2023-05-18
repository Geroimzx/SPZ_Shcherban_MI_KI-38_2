package com.example.network_monitoring;

import com.example.network_monitoring.network_device.NetworkDeviceTab;
import com.example.network_monitoring.quality.NetworkQualityTab;
import com.example.network_monitoring.statistic.NetworkStatisticTab;
import com.example.network_monitoring.traffic_limit.TrafficLimitTab;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class NetworkMonitoringApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.LEFT);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setRotateGraphic(true);
        tabPane.setTabMinWidth(150);
        tabPane.setTabMinWidth(150);

        Tab networkDevicesTab = new NetworkDeviceTab();
        ImageView networkDevicesIcon = new ImageView(new Image(getClass().getResourceAsStream("switch.png")));
        networkDevicesIcon.setFitWidth(20);
        networkDevicesIcon.setFitHeight(20);
        networkDevicesTab.setGraphic(networkDevicesIcon);

        Tab trafficTab = new NetworkStatisticTab();
        ImageView trafficIcon = new ImageView(new Image(getClass().getResourceAsStream("data-analytics.png")));
        trafficIcon.setFitWidth(20);
        trafficIcon.setFitHeight(20);
        trafficTab.setGraphic(trafficIcon);


        Tab connectionQualityTab = new NetworkQualityTab();
        ImageView connectionQualityIcon = new ImageView(new Image(getClass().getResourceAsStream("quality.png")));
        connectionQualityIcon.setFitWidth(20);
        connectionQualityIcon.setFitHeight(20);
        connectionQualityTab.setGraphic(connectionQualityIcon);

        Tab trafficLimitTab = new TrafficLimitTab();
        ImageView networkLimitIcon = new ImageView(new Image(getClass().getResourceAsStream("fiat-money.png")));
        networkLimitIcon.setFitWidth(20);
        networkLimitIcon.setFitHeight(20);
        trafficLimitTab.setGraphic(networkLimitIcon);


        tabPane.getTabs().addAll(networkDevicesTab, trafficTab, connectionQualityTab, trafficLimitTab);

        // Додаємо панель вкладок до кореневої панелі
        root.getChildren().add(tabPane);

        // Створюємо сцену
        Scene scene = new Scene(root, 1400, 1000);

        // Встановлюємо сцену на підмостках
        primaryStage.setScene(scene);

        // Встановлюємо заголовок вікна
        primaryStage.setTitle("Моніторинг мережі");

        // Встановлення іконки програми
        Image icon = new Image(getClass().getResourceAsStream("monitoring.png"));
        primaryStage.getIcons().add(icon);

        // Показуємо вікно
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}