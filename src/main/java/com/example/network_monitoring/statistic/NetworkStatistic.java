package com.example.network_monitoring.statistic;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Клас NetworkStatistic представляє собою моніторинг статистики отриманих і надісланих байтів
 */
public class NetworkStatistic {
    /**
     * Кількість надісланих байт.
     */
    private long sentBytes;

    /**
     * Кількість отриманих байт.
     */
    private long receivedBytes;

    /**
     * Загальна кількість байт.
     */
    private long totalBytes;

    /**
     * Початкова вибрана одиниця вимірювання трафіку.
     */
    private MemoryUnit selectedUnit = MemoryUnit.MEGABYTES;

    /**
     * Мітка для відображення кількості надісланих байт.
     */
    private Label sentLabel;

    /**
     * Мітка для відображення кількості отриманих байт.
     */
    private Label receivedLabel;

    /**
     * Мітка для відображення загальної кількості байт.
     */
    private Label totalLabel;

    private ComboBox<MemoryUnit> unitComboBox;

    /**
     * Створює новий екземпляр класу NetworkStatistic і налаштовує користувацький інтерфейс.
     * Також розпочинає процес оновлення міток з інформацією про трафік.
     */
    public NetworkStatistic() {
        setupUI();
        startUpdating();
    }

    /**
     * Налаштовує користувацький інтерфейс для відображення інформації про трафік.
     * Створює мітки для відображення інформації про надіслані, отримані та загальні байти,
     * а також комбінований список для вибору одиниць вимірювання трафіку.
     * Встановлює обробники подій для комбінованого списку для оновлення вибраної одиниці вимірювання
     * та оновлює значення міток.
     */
    private void setupUI() {
        // Створюємо мітки для відображення інформації про трафік
        sentLabel = new Label("Надіслано: 0 " + selectedUnit.getDisplayName());
        receivedLabel = new Label("Отримано: 0 " + selectedUnit.getDisplayName());
        totalLabel = new Label("Загально: 0 " + selectedUnit.getDisplayName());

        // Створюємо комбінований список для вибору одиниць вимірювання трафіку
        unitComboBox = new ComboBox<>();
        unitComboBox.getItems().addAll(MemoryUnit.values());
        unitComboBox.setValue(selectedUnit);
        unitComboBox.setOnAction(e -> {
            selectedUnit = unitComboBox.getValue();
            updateTrafficLabels();
        });


    }


    public Label getSentLabel() {
        return sentLabel;
    }

    public Label getReceivedLabel() {
        return receivedLabel;
    }

    public Label getTotalLabel() {
        return totalLabel;
    }

    public ComboBox<MemoryUnit> getUnitComboBox() {
        return unitComboBox;
    }

    /**
     * Оновлює значень міток з інформацією про трафік
     * Метод виконує запит до командного рядка для отримання даних про статистику використання мережі.
     * Процес будується за допомогою ProcessBuilder з командою "cmd.exe /c netstat -e".
     * Читає вивід процесу через буферизований читач BufferedReader.
     * Обробляє кожен рядок виводу і перевіряє, чи містить він слово "Bytes".
     * Якщо знаходиться рядок з відповідною інформацією, розбиває його на частини і оновлює значення отриманих і надісланих байтів.
     * Закриває читач і знищує процес.
     * У разі виникнення виключення IOException, виводить стек виключення.
      */
    private void updateTrafficLabels() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "netstat", "-e");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Bytes")) {
                    String[] parts = line.split("\\s+");

                    sentBytes = parseBytes(parts[1]);
                    receivedBytes = parseBytes(parts[2]);
                    totalBytes = sentBytes + receivedBytes;
                    break;
                }
            }

            reader.close();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sentLabel.setText("Отримано трафіку в поточному з'єднані: " + convertBytes(sentBytes) + " " + selectedUnit.getDisplayName());
        receivedLabel.setText("Надіслано трафіку в поточному з'єднані: " + convertBytes(receivedBytes) + " " + selectedUnit.getDisplayName());
        totalLabel.setText("Загальна кількість надісланого і отриманого трафіку в поточному з'єднані: " + convertBytes(totalBytes) + " " + selectedUnit.getDisplayName());
    }

    /**
     * Починає оновлення даних статистики мережевого використання.
     * Використовує Timer для запуску оновлення в фоновому потоці та Platform.runLater() для оновлення в головному потоці JavaFX.
     * Оновлює дані статистики мережевого використання кожні 3 секунди.
     */
    private void startUpdating() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    updateTrafficLabels();
                });
            }
        }, 0, 3000); // Оновлюємо кожні 3 секунди
    }

    /**
     * Конвертує байти в обрану одиницю вимірювання
     * @param bytes кількість байт
     * @return число з плаваючою комою, яке представляє значення байтів у вибраних одиницях вимірювання
     */
    private float convertBytes(long bytes) {
        return (float) (bytes / Math.pow(1024, selectedUnit.ordinal()));
    }

    /**
     * Enum одиниць вимірювання пам'яті
      */
    public enum MemoryUnit {
        BYTES("байт"),
        KILOBYTES("кБ"),
        MEGABYTES("МБ"),
        GIGABYTES("ГБ");

        private final String displayName;

        MemoryUnit(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
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
}
