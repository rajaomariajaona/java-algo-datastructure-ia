module Algo.Data.Structure.IA.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires java.logging;
    requires java.base;
    requires jdk.jsobject;

    opens mg.jaona to javafx.fxml;
    opens mg.jaona.app.serietemporelle to javafx.graphics, javafx.fxml;
    opens mg.jaona.app.serietemporelle.controllers to javafx.fxml;
    opens mg.jaona.ia to javafx.base;
    exports mg.jaona;
}