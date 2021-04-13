module Algo.Data.Structure.IA.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;
    requires java.base;

    opens mg.jaona to javafx.fxml;
    exports mg.jaona;
}