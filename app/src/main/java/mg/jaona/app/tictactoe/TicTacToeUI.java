package mg.jaona.app.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import mg.jaona.app.graph.controllers.GraphController;
import mg.jaona.app.tictactoe.controllers.TicTacToeController;

import java.io.IOException;

public class TicTacToeUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tictactoe.fxml"));
        Parent root = (Parent) loader.load();
        TicTacToeController ctrl = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(400);
        stage.setWidth(600);
        stage.setResizable(false);
        stage.setTitle("Tic Tac Toe");
        stage.show();
    }
}
