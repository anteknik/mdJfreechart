module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.jsoup;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires jfreechart;
    requires YahooFinanceAPI;
    requires slf4j.api;
    requires java.desktop;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}