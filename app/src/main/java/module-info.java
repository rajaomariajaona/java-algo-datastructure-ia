module Algo.Data.Structure.IA.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires java.logging;
    requires java.base;
    requires jdk.jsobject;

    opens mg.jaona to javafx.fxml;
    opens mg.jaona.app.graph to javafx.graphics, javafx.fxml;
    opens mg.jaona.app.graph.controllers to javafx.fxml;
    exports mg.jaona;
}