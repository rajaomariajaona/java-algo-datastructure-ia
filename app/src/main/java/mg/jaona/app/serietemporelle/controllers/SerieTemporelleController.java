package mg.jaona.app.serietemporelle.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import mg.jaona.app.serietemporelle.SerieTemporelleUI;
import mg.jaona.ia.PerceptronMultilayer;
import mg.jaona.ia.SerieTemporelle;
import mg.jaona.ia.Takens;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SerieTemporelleController implements Initializable {

    @FXML
    private TextField inputValues, inputNbHiddenLayer, inputMaxEpoch, inputAlpha;

    @FXML
    private Button btnComputeValues, btnComputeStructure, btnLearn, btnInitWeight, btnStopLearn;

    @FXML
    private Accordion menuAccordion;

    @FXML
    private AnchorPane paneStructure, paneValues, paneLearn, panePredict;

    @FXML
    private TitledPane titledValues, titledLearn, titledPredict, titledStructure;

    @FXML
    private TableView<SerieTemporelle.DataSet> tableValues;

    @FXML
    private Text txtNbLayerHidden, txtNbLayerInput;

    @FXML
    private WebView webviewStructure;

    @FXML
    private CheckBox checkBoxWeight;

    private PerceptronMultilayer pmc;

    private List<SerieTemporelle.DataSet> values;
    private boolean isLearning = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<TitledPane, AnchorPane> mapPaneTitled = Map.of(titledValues, paneValues, titledLearn, paneLearn, titledPredict, panePredict, titledStructure, paneStructure);
        inputValues.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-4]{0,1}([\\.]\\d{0,4})?")) {
                inputValues.setText(oldValue);
            }
            btnComputeValues.setDisable(inputValues.getText().trim().equals(""));
        });
        inputNbHiddenLayer.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]{0,3}")) {
                inputNbHiddenLayer.setText(oldValue);
            }
            txtNbLayerHidden.setText(inputNbHiddenLayer.getText().equals("") ? "0" : inputNbHiddenLayer.getText());
            btnComputeStructure.setDisable(inputNbHiddenLayer.getText().trim().equals(""));
        });

        inputAlpha.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-2]{0,1}([\\.]\\d{0,10})?")) {
                inputAlpha.setText(oldValue);
            }
            btnLearn.setDisable(inputMaxEpoch.getText().trim().equals("") || inputAlpha.getText().trim().equals(""));
        });
        inputMaxEpoch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]{0,10}")) {
                inputMaxEpoch.setText(oldValue);
            }
            btnLearn.setDisable(inputMaxEpoch.getText().trim().equals("") || inputAlpha.getText().trim().equals(""));
        });
        menuAccordion.expandedPaneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                mapPaneTitled.get(newValue).toFront();
        });
        webviewStructure.setContextMenuEnabled(false);
        webviewStructure.getEngine().load(SerieTemporelleUI.class.getResource("visualize-network.html").toString());
        this.values = new ArrayList<>();
    }

    @FXML
    private void handleComputeValues(ActionEvent ae) {
        this.values = SerieTemporelle.compute500Values(Float.parseFloat(inputValues.getText()), SerieTemporelle.X0);
        menuAccordion.getPanes().forEach(titledPane -> titledPane.setDisable(!(titledPane.equals(titledStructure) || titledPane.equals(titledValues))));
        ObservableList<SerieTemporelle.DataSet> observableList = FXCollections.observableList(this.values);
        TableColumn xCol = (TableColumn) tableValues.getColumns().get(0);
        xCol.setCellValueFactory(new PropertyValueFactory<SerieTemporelle.DataSet, String>("x"));
        TableColumn yCol = (TableColumn) tableValues.getColumns().get(1);
        yCol.setCellValueFactory(new PropertyValueFactory<SerieTemporelle.DataSet, String>("y"));
        tableValues.setItems(observableList);
    }

    @FXML
    private void handleComputeStructure(ActionEvent ae) {
        menuAccordion.getPanes().forEach(titledPane -> titledPane.setDisable(!(titledPane.equals(titledStructure) || titledPane.equals(titledValues) || titledPane.equals(titledLearn))));
        List<Float> listVector = this.values.stream().map(SerieTemporelle.DataSet::getY).collect(Collectors.toList());
        float[] vector = new float[listVector.size()];
        for (int i = 0; i < listVector.size(); i++) {
            vector[i] = listVector.get(i);
        }
        Takens takens = new Takens(vector);
        try{
            int inputLayer = takens.firstLayerLength();
            txtNbLayerInput.setText(String.valueOf(inputLayer));
            this.pmc = new PerceptronMultilayer(new PerceptronMultilayer.Structure(inputLayer,Integer.parseInt(inputNbHiddenLayer.getText())));
            redraw();
            btnInitWeight.setDisable(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void redraw(){
        String s= pmc.generateNetworkVariableScript();
        System.out.println(s);
        webviewStructure.getEngine().executeScript(s);
        webviewStructure.getEngine().executeScript("draw(network)");
        if(checkBoxWeight.isSelected()){
            webviewStructure.getEngine().executeScript("showWeight()");
        }
    }

    @FXML
    private void handleInitWeight(ActionEvent e){
        boolean isInited = pmc.isWeightInitialized();
        if(pmc != null && !isInited){
            pmc.initializeWeight();
            redraw();
        }else if(isInited){
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("Les poids ont été déjà initialiser.\nVoulez vous vraiment le re-initialiser?");
            a.show();
            a.setOnCloseRequest(event -> {
                if(a.getResult().equals(ButtonType.OK)){
                    pmc.initializeWeight();
                    redraw();
                }
            });
        }
    }
    @FXML
    private void handleLearn(ActionEvent e){

    }
    @FXML
    private void handleStopLearn(ActionEvent e){

    }

    @FXML
    private void handleCheckBoxWeight(ActionEvent e){
        if(checkBoxWeight.isSelected()){
            webviewStructure.getEngine().executeScript("showWeight()");
        }else{
            webviewStructure.getEngine().executeScript("hideWeight()");
        }
    }

}
