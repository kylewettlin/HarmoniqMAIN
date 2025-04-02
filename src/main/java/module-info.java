module com.harmoniqscrum {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires json.simple;
    requires junit;
    // JFugue modules
    requires jfugue;
    
    exports com.harmoniqscrum.model;
    exports com.harmoniqscrum.model.view;
    
    opens com.harmoniqscrum.model to javafx.fxml;
    opens com.harmoniqscrum.model.view to javafx.fxml;
} 