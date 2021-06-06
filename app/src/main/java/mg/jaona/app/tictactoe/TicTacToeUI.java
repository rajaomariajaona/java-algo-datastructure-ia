package mg.jaona.app.tictactoe;

import com.gluonhq.charm.glisten.application.GlassPane;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import mg.jaona.app.graph.controllers.GraphController;
import mg.jaona.app.tictactoe.controllers.TicTacToeController;

import java.io.IOException;

public class TicTacToeUI extends MobileApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        addViewFactory(HOME_VIEW, () -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tictactoe.fxml"));
            Parent parent = null;
            try {
               parent = (Parent) loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            VBox root = new VBox(20,parent);
            root.setAlignment(Pos.CENTER);
            View view = new View(root);
            return view;
        });
    }

    @Override
    public void postInit(Scene scene) {
        MobileApplication.getInstance().setSwatch(Swatch.DEEP_ORANGE);
        MobileApplication.getInstance().getAppBar().setTitleText("Tic Tac Toe");
    }
}
