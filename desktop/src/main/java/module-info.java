module Algo.Data.Structure.IA.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires java.logging;
    requires java.base;
    requires jdk.jsobject;
    requires Algo.Data.Structure.IA.library;

    opens mg.jaona to javafx.fxml;
    opens mg.jaona.app.graph to javafx.graphics, javafx.fxml;
    opens mg.jaona.app.graph.controllers to javafx.fxml;
    opens mg.jaona.app.serietemporelle to javafx.graphics, javafx.fxml;
    opens mg.jaona.app.serietemporelle.controllers to javafx.fxml;
    opens mg.jaona.app.tictactoe to javafx.graphics, javafx.fxml;
    opens mg.jaona.app.tictactoe.controllers to javafx.fxml;
    exports mg.jaona;
}