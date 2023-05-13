package com.example.network_monitoring.traffic_limit;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Вкладка для встановлення обмежень трафіку.
 */
public class TrafficLimitTab extends Tab {
    private TrafficLimit trafficLimit;
    private GridPane gridPane;
    private TextField limitField;

    /**
     * Конструктор класу TrafficLimitTab.
     */
    public TrafficLimitTab() {
        setText("Обмеження трафіку");

        trafficLimit = TrafficLimit.loadTrafficLimits();
        if(trafficLimit == null) {
            trafficLimit = new TrafficLimit(1000);
        }
        trafficLimit.startUpdating();

        // GridPane для розташування елементів форми
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Label та TextField для обмеження трафіку
        Label limitLabel = new Label("Встановити ліміт:");
        limitField = new TextField();
        limitField.setText(String.valueOf(trafficLimit.getTrafficLimit()));
        gridPane.add(limitLabel, 0, 0);
        gridPane.add(limitField, 1, 0);

        // Кнопка для збереження обмежень трафіку
        Button saveButton = new Button("Зберегти");

        saveButton.setOnAction(e -> {
            trafficLimit.setTrafficLimit(Long.parseLong(limitField.getText()));
            trafficLimit.saveTrafficLimits();
        });

        gridPane.add(saveButton, 0, 2);

        setContent(gridPane);
    }
}
