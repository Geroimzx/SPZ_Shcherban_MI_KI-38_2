module com.example.netwrok_monitoring {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.network_monitoring to javafx.fxml;
    exports com.example.network_monitoring;
    exports com.example.network_monitoring.network_device;
    exports com.example.network_monitoring.quality;
    opens com.example.network_monitoring.network_device to javafx.fxml;
    opens com.example.network_monitoring.traffic_limit to javafx.fxml;
}