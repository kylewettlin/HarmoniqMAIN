module com.harmoniqscrum {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires json.simple;
    // JFugue modules
    requires jfugue;
    requires org.junit.jupiter.api;
    
    exports com.harmoniqscrum.model;
    exports com.harmoniqscrum.model.view;
    
    opens com.harmoniqscrum.model;
    opens com.harmoniqscrum.model.view;
} 