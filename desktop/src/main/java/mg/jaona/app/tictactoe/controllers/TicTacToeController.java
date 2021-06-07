package mg.jaona.app.tictactoe.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import mg.jaona.algo.Minimax;
import mg.jaona.algo.TicTacToeAI;
import mg.jaona.algo.TicTacToeGame;
import mg.jaona.datastructure.matrix.Matrix;
import mg.jaona.datastructure.matrix.MatrixSize;

import java.net.URL;
import java.util.*;

public class TicTacToeController implements Initializable {
    @FXML
    Button btnStart;
    @FXML
    GridPane grid;

    private boolean isFinished = false;
    private Matrix matrix;

    private int currentTurn = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.matrix = new Matrix(new MatrixSize(3, 3), 0);
        this.synchronise();
    }

    private void synchronise() {
        this.clearValue();
        for (int i = 0; i < this.matrix.getSize().getRow(); i++) {
            for (int j = 0; j < this.matrix.getSize().getCol(); j++) {
                switch (Math.round(matrix.get(i, j))) {
                    case 1:
                        this.drawValue(i, j, 'X');
                        break;
                    case -1:
                        this.drawValue(i, j, 'O');
                        break;
                    default:
                        this.drawValue(i, j, null);
                }
            }
        }
    }

    private void clearValue() {
        this.grid.getChildren().clear();
    }

    private void drawValue(int i, int j, Character mark) {
        Label label = new Label(mark != null ? mark.toString() : "");
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-background-color: #c8d6e5;-fx-font-size: 65px;");
        label.setMinSize(90, 90);
        label.setMaxSize(90, 90);

        if (mark == null) {
            label.setOnMouseClicked(this::handleClick);
        }
        this.grid.add(label, j, i);
    }

    @FXML
    private void handleStart(ActionEvent ae) {

    }

    @FXML
    private void handleReset(ActionEvent ae) {
        this.isFinished = false;
        this.matrix.clear();
        this.synchronise();
    }

    @FXML
    private void handleClick(MouseEvent me) {
        if (!this.isFinished) {
            Node source = (Node) me.getSource();
            Integer colIndex = GridPane.getColumnIndex(source);
            Integer rowIndex = GridPane.getRowIndex(source);
            this.matrix.set(rowIndex, colIndex, this.currentTurn);
            this.currentTurn = this.currentTurn == -1 ? 1 : -1;
            this.synchronise();
            checkFinished();
            if (!this.isFinished) {
                getAIResponse();
                checkFinished(true);
            }
            System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
        }
    }

    private void getAIResponse() {
        this.matrix = TicTacToeAI.bestMoveMinimax(this.matrix, this.currentTurn == -1 ? Minimax.Player.MINIMIZER : Minimax.Player.MAXIMIZER);
        this.currentTurn = this.currentTurn == -1 ? 1 : -1;
        this.synchronise();
    }
    private void checkFinished(){
        checkFinished(false);
    }
    private void checkFinished(boolean isIa) {
        if (!TicTacToeGame.hasMove(this.matrix)) {
            this.isFinished = true;
            Minimax.Player winner = TicTacToeGame.someoneWon(this.matrix);
            if (winner != null) {
//                Dialog dialog = new Dialog();
//                dialog.setTitle(new Label("Fin de la partie"));
//                String msg = "";
//                char id = winner.equals(Minimax.Player.MINIMIZER) ? 'O' : 'X';
//                if(isIa){
//                    msg += "L' IA (" + id +  ") a ";
//                }else{
//                    msg += "Vous (" + id +  ") avez ";
//                }
//                msg += "gagné";
//                dialog.setContent(new Label(msg));
//                Button okButton = new Button("OK");
//                okButton.setOnAction(e -> {
//                    dialog.hide();
//                });
//                dialog.getButtons().add(okButton);
//                dialog.showAndWait();
                System.out.println(winner.toString());
            }else {
//                Dialog dialog = new Dialog();
//                dialog.setTitle(new Label("Fin de la partie"));
//                dialog.setContent(new Label("Match Null"));
//                Button okButton = new Button("OK");
//                okButton.setOnAction(e -> {
//                    dialog.hide();
//                });
//                dialog.getButtons().add(okButton);
//                dialog.showAndWait();
                System.out.println(winner.toString());
            }
        }
    }
}
