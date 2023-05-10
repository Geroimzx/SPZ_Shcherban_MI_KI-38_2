package com.example.network_monitoring;

import com.example.network_monitoring.network_device.NetworkDeviceTable;
import com.example.network_monitoring.network_statistic.NetworkStatistic;
import com.example.network_monitoring.network_traffic.NetworkQuality;
import com.example.network_monitoring.network_usage.NetworkUsageGraph;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class NetworkMonitoringApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.LEFT);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setRotateGraphic(true);
        tabPane.setTabMinWidth(150);
        tabPane.setTabMinWidth(150);


        Tab networkDevicesTab = new NetworkDeviceTable();
        ImageView networkDevicesIcon = new ImageView(new Image(getClass().getResourceAsStream("switch.png")));
        networkDevicesIcon.setFitWidth(20);
        networkDevicesIcon.setFitHeight(20);
        networkDevicesTab.setGraphic(networkDevicesIcon);

        Tab trafficTab = new NetworkUsageGraph();
        ImageView trafficIcon = new ImageView(new Image(getClass().getResourceAsStream("tunnel.png")));
        trafficIcon.setFitWidth(20);
        trafficIcon.setFitHeight(20);
        trafficTab.setGraphic(trafficIcon);

        Tab networkStatsTab = new NetworkStatistic();
        ImageView networkStatsIcon = new ImageView(new Image(getClass().getResourceAsStream("data-analytics.png")));
        networkStatsIcon.setFitWidth(20);
        networkStatsIcon.setFitHeight(20);
        networkStatsTab.setGraphic(networkStatsIcon);

        Tab connectionQualityTab = new NetworkQuality();
        ImageView connectionQualityIcon = new ImageView(new Image(getClass().getResourceAsStream("quality.png")));
        connectionQualityIcon.setFitWidth(20);
        connectionQualityIcon.setFitHeight(20);
        connectionQualityTab.setGraphic(connectionQualityIcon);



        Tab networkLimitTab = new Tab("Обмеження трафіку");
        ImageView networkLimitIcon = new ImageView(new Image(getClass().getResourceAsStream("quality.png")));
        networkLimitIcon.setFitWidth(20);
        networkLimitIcon.setFitHeight(20);
        networkLimitTab.setGraphic(networkLimitIcon);

        Tab speedTestTab = new Tab("Тест швидкості з'єднання");
        ImageView speedTestIcon = new ImageView(new Image(getClass().getResourceAsStream("quality.png")));
        speedTestIcon.setFitWidth(20);
        speedTestIcon.setFitHeight(20);
        speedTestTab.setGraphic(speedTestIcon);



        tabPane.getTabs().addAll(networkDevicesTab, trafficTab, networkStatsTab, connectionQualityTab, networkLimitTab, speedTestTab);

        // Додаємо панель вкладок до кореневої панелі
        root.getChildren().add(tabPane);

        // Створюємо сцену
        Scene scene = new Scene(root, 1200, 1000);
        // Завантаження CSS файлу зі стилями
        scene.getStylesheets().add(getClass().getResource("DarkTheme.css").toExternalForm());

        // Встановлюємо сцену на підмостках
        primaryStage.setScene(scene);

        // Встановлюємо заголовок вікна
        primaryStage.setTitle("Моніторинг мережі");

        // Показуємо вікно
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}