package com.example.network_monitoring.notification;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Клас FloatingNotification представляє спливаючі повідомлення на вікні JavaFX.
 */
public class FloatingNotification {
    /**
     * Вікно сповіщення
     */
    private Stage primaryStage;
    /**
     * Унікальний ідентифікатор повідомлення для кожного спливаючого повідомлення.
     */
    private static int notification_id = 0;

    /**
     * Ширина спливаючого повідомлення.
     */
    private static final int WIDTH = 300;

    /**
     * Висота спливаючого повідомлення.
     */
    private static final int HEIGHT = 100;

    /**
     * Час показу спливаючого повідомлення в мілісекундах.
     */
    private static final int SHOW_TIME = 5000;

    /**
     * Конструктор класу FloatingNotification.
     */
    public FloatingNotification() {
    }

    /**
     * Відображає спливаюче повідомлення з заданим текстом.
     *
     * @param message текст повідомлення
     */
    public void showNotification(String message) {
        Platform.runLater(() -> {
            primaryStage = new Stage();
            // Створюємо елементи інтерфейсу
            Label notificationLabel = new Label(message);
            notificationLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
            StackPane root = new StackPane(notificationLabel);
            root.setStyle("-fx-background-color: #333333; -fx-padding: 10px;");
            root.setAlignment(Pos.CENTER);

            // Налаштування сцени та вікна
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setAlwaysOnTop(true);
            primaryStage.setScene(scene);

            // Отримуємо розмір екрану
            double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
            double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

            // Встановлюємо положення вікна справа знизу
            double x = screenWidth - WIDTH - 10;  // Відступ 10 пікселів від правого краю
            double y = screenHeight - HEIGHT - 10 - notification_id*(HEIGHT + 10);  // Відступ 10 пікселів від нижнього краю
            notification_id++;
            primaryStage.setX(x);
            primaryStage.setY(y);

            // Анімація зникнення повідомлення через SHOW_TIME секунд
            new Thread(() -> {
                try {
                    Thread.sleep(SHOW_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                closeNotification();
            }).start();

            // Відображення вікна
            primaryStage.show();
        });
    }

    /**
     * Закриває вікно спливаючого сповіщення
     */
    private void closeNotification() {
        Platform.runLater(() -> primaryStage.close());
        notification_id--;
    }
}