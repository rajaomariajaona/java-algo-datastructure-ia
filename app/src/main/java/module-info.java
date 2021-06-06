module Algo.Data.Structure.IA.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires java.logging;
    requires java.base;
    requires jdk.jsobject;
    requires com.gluonhq.charm.glisten;
    requires com.gluonhq.attach.statusbar;
    requires com.gluonhq.attach.display;
    requires com.gluonhq.attach.util;
    requires com.gluonhq.attach.lifecycle;
    requires com.gluonhq.attach.storage;

    opens mg.jaona to javafx.fxml;
    opens mg.jaona.app.graph to javafx.graphics, javafx.fxml;
    opens mg.jaona.app.graph.controllers to javafx.fxml;
    opens mg.jaona.app.serietemporelle to javafx.graphics, javafx.fxml;
    opens mg.jaona.app.serietemporelle.controllers to javafx.fxml;
    opens mg.jaona.app.tictactoe to javafx.graphics, javafx.fxml;
    opens mg.jaona.app.tictactoe.controllers to javafx.fxml;
    opens mg.jaona.ia to javafx.base;
    exports mg.jaona;
}