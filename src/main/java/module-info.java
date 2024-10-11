module minicinemanaz.com.nazpedawi709378endassignment {
    requires javafx.controls;
    requires javafx.fxml;


    opens minicinemanaz.com.nazpedawi709378endassignment to javafx.fxml;
    exports minicinemanaz.com.nazpedawi709378endassignment;
    exports minicinemanaz.com.nazpedawi709378endassignment.controllers;
    opens minicinemanaz.com.nazpedawi709378endassignment.controllers to javafx.fxml;
    exports minicinemanaz.com.nazpedawi709378endassignment.models;
    opens minicinemanaz.com.nazpedawi709378endassignment.models to javafx.fxml;
    exports minicinemanaz.com.nazpedawi709378endassignment.data;
    opens minicinemanaz.com.nazpedawi709378endassignment.data to javafx.fxml;
}