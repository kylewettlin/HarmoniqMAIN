module com.harmoniqscrum {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires junit;
    requires json.simple;
    requires java.desktop;
    
    // JFugue modules
    requires jfugue;
    
    exports com.harmoniqscrum.model;
    exports com.harmoniqscrum.model.view;
    
    opens com.harmoniqscrum.model to javafx.fxml;
    opens com.harmoniqscrum.model.view to javafx.fxml;
} 