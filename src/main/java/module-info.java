module com.example.network_monitoring {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.network_monitoring to javafx.fxml;
    exports com.example.network_monitoring;
    exports com.example.network_monitoring.network_device;
    opens com.example.network_monitoring.network_device to javafx.fxml;
    exports com.example.network_monitoring.network_traffic;
    opens com.example.network_monitoring.network_traffic to javafx.fxml;
}