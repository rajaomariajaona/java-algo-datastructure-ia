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
    private TextField inputValues, inputNbHiddenLayer;

    @FXML
    private Button btnComputeValues, btnComputeStructure;

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

    private PerceptronMultilayer pmc;

    private List<SerieTemporelle.DataSet> values;

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
            System.out.println(pmc.generateNetworkVariableScript());
            webviewStructure.getEngine().executeScript(pmc.generateNetworkVariableScript());
            webviewStructure.getEngine().executeScript("draw(network)");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
