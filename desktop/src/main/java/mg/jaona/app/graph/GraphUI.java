package mg.jaona.app.graph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import mg.jaona.app.graph.controllers.GraphController;

import java.io.IOException;

public class GraphUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("graph.fxml"));
        Parent root = (Parent) loader.load();
        GraphController ctrl = (GraphController) loader.getController();
        WebView webView = ctrl.getWebview();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Graph");
        stage.show();
        webView.getEngine().load(getClass().getResource("graph.html").toString());
    }
}
