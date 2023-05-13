module com.example.network_monitoring {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.example.network_monitoring to javafx.fxml;
    exports com.example.network_monitoring;
    exports com.example.network_monitoring.network_device;
    opens com.example.network_monitoring.network_device to javafx.fxml;
    exports com.example.network_monitoring.quality;
    opens com.example.network_monitoring.quality to javafx.fxml;
}