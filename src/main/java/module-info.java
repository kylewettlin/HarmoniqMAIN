module com.harmoniqscrum {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires json.simple;
    requires java.desktop;
    
    // JFugue is not a module, it will be loaded as automatic module
    
    exports com.harmoniqscrum;
    exports com.harmoniqscrum.model;

    opens com.harmoniqscrum to javafx.fxml;
} 