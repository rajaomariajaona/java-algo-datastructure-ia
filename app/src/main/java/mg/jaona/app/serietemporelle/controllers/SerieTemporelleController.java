package mg.jaona.app.serietemporelle.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.util.StringConverter;
import mg.jaona.app.serietemporelle.SerieTemporelleUI;
import mg.jaona.ia.PerceptronMultilayer;
import mg.jaona.ia.SerieTemporelle;
import mg.jaona.ia.Takens;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
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

    @FXML
    private LineChart<String, Float> lineChartNMSE;

    @FXML
    private LineChart<String, Float> lineChartPredict;

    @FXML
    private Text stepText;

    @FXML
    private Slider sliderNbToPredict;

    @FXML
    private ChoiceBox<PredictMode> choiceStepMode;

    private enum PredictMode {MULTIPLE, ONE}

    private Map<PredictMode, String> predictModeStringMap = Map.of(PredictMode.MULTIPLE, "Plusieurs pas", PredictMode.ONE, "Un pas");

    private Map<PredictMode, String> sliderTexts = Map.of(PredictMode.MULTIPLE, "Nombre de pas", PredictMode.ONE, "Nombre de predictions à faire");
    private Map<PredictMode, Integer> minValues = Map.of(PredictMode.MULTIPLE, 2, PredictMode.ONE, 10);


    private XYChart.Series<String, Float> fitError = new XYChart.Series();
    private XYChart.Series<String, Float> testError = new XYChart.Series();

    private XYChart.Series<String, Float> networkValues = new XYChart.Series();
    private XYChart.Series<String, Float> serieValues = new XYChart.Series();

    private PerceptronMultilayer pmc;

    private List<SerieTemporelle.DataSet> values;
    private boolean isLearning = false;
    private PerceptronMultilayer.Structure structure = new PerceptronMultilayer.Structure(0, 0);

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
            btnComputeStructure.setDisable(inputNbHiddenLayer.getText().trim().equals("") || Integer.parseInt(inputNbHiddenLayer.getText()) == 0);
        });

        inputAlpha.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]{0,3}([\\.]\\d{0,10})?")) {
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

        fitError.setName("Erreur d'apprentissage");
        testError.setName("Erreur de test");
        lineChartNMSE.setCreateSymbols(false);
        lineChartNMSE.getData().add(fitError);
        lineChartNMSE.getData().add(testError);

        networkValues.setName("Valeur generé par le réseau");
        serieValues.setName("Valeur attendue");
        lineChartPredict.getData().add(networkValues);
        lineChartPredict.getData().add(serieValues);

        choiceStepMode.getItems().addAll(PredictMode.MULTIPLE, PredictMode.ONE);
        choiceStepMode.setConverter(new StringConverter<PredictMode>() {
            @Override
            public String toString(PredictMode object) {
                return predictModeStringMap.get(object);
            }

            @Override
            public PredictMode fromString(String string) {
                return predictModeStringMap.entrySet()
                        .stream()
                        .filter(predictModeStringEntry -> predictModeStringEntry.getValue().equals(string))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList())
                        .get(0);
            }
        });
        choiceStepMode.setValue(PredictMode.ONE);
        choiceStepMode.setOnAction(event ->
                handlePredictMode(choiceStepMode.getValue())
        );
        tableValues.setPlaceholder(new Label(""));
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
        btnInitWeight.setDisable(true);
        hideGraph();
        this.inputNbHiddenLayer.setText("");
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
        try {
            int inputLayer = takens.firstLayerLength();
            txtNbLayerInput.setText(String.valueOf(inputLayer));
            if (structure.getNbInputLayer() != inputLayer || structure.getNbHiddenLayer() != Integer.parseInt(inputNbHiddenLayer.getText())) {
                this.structure.setNbHiddenLayer(Integer.parseInt(inputNbHiddenLayer.getText()));
                this.structure.setNbInputLayer(inputLayer);
                this.pmc = new PerceptronMultilayer(this.structure);
                redraw();
                titledPredict.setDisable(true);
                titledLearn.setDisable(true);
                fitError.getData().clear();
                testError.getData().clear();
            }
            btnInitWeight.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redraw() {
        String s = pmc.generateNetworkVariableScript();
        webviewStructure.getEngine().executeScript(s);
        webviewStructure.getEngine().executeScript("draw(network)");
        if (checkBoxWeight.isSelected()) {
            webviewStructure.getEngine().executeScript("showWeight()");
        }
        showGraph();
    }

    private void hideGraph() {
        this.webviewStructure.getEngine().executeScript("hideGraph()");
    }

    private void showGraph() {
        this.webviewStructure.getEngine().executeScript("showGraph()");
    }

    @FXML
    private void handleInitWeight(ActionEvent e) {
        boolean isInited = pmc.isWeightInitialized();
        if (pmc != null && !isInited) {
            pmc.initializeWeight();
            redraw();
            titledPredict.setDisable(true);
            titledLearn.setDisable(false);
        } else if (isInited) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("Les poids ont été déjà initialiser.\nVoulez vous vraiment le re-initialiser?");
            a.show();
            a.setOnCloseRequest(event -> {
                if (a.getResult().equals(ButtonType.OK)) {
                    pmc.initializeWeight();
                    redraw();
                    titledPredict.setDisable(true);
                    titledLearn.setDisable(false);
                }
            });
        }

    }

    private static int c = 0;

    @FXML
    private void handleLearn(ActionEvent e) {
        btnLearn.setDisable(true);
        fitError.getData().clear();
        testError.getData().clear();
        SerieTemporelleController.c = 0;
        pmc.setAlpha(Float.parseFloat(inputAlpha.getText()));
        CompletableFuture.runAsync(() -> pmc.fit(this.values.stream().map(SerieTemporelle.DataSet::getY).collect(Collectors.toList()),
                Integer.parseInt(inputMaxEpoch.getText()), 0.2f, value -> Platform.runLater(() -> {
                    fitError
                            .getData()
                            .add(new XYChart.Data<>(Integer.valueOf(SerieTemporelleController.c).toString(), value.getFit()));
                    testError
                            .getData()
                            .add(new XYChart.Data<>(Integer.valueOf(SerieTemporelleController.c).toString(), value.getTest()));
                    SerieTemporelleController.c++;
                }))).thenRun(() -> {
            btnLearn.setDisable(false);
            titledPredict.setDisable(false);
            redraw();
        });
    }

    @FXML
    private void handleCheckBoxWeight(ActionEvent e) {
        if (checkBoxWeight.isSelected()) {
            webviewStructure.getEngine().executeScript("showWeight()");
        } else {
            webviewStructure.getEngine().executeScript("hideWeight()");
        }
    }

    private void handlePredictMode(PredictMode value) {
        stepText.setText(sliderTexts.get(value));
        sliderNbToPredict.setMin(minValues.get(value));
        sliderNbToPredict.setMax(minValues.get(value) + 20);
    }

    @FXML
    private void handlePredict(ActionEvent ae) {
        networkValues.getData().clear();
        serieValues.getData().clear();
        int sliderValue = Double.valueOf(sliderNbToPredict.getValue()).intValue();
        long startIndex = Math.round(this.values.size() * (1 - 0.2));
        List<Float> values = this.values.stream()
                .skip(startIndex)
                .map(SerieTemporelle.DataSet::getY)
                .collect(Collectors.toList());
        List<Float> output = new ArrayList<>();
        List<Float> series = values.stream().skip(this.structure.getNbInputLayer()).limit(sliderValue).collect(Collectors.toList());
        if (choiceStepMode.getValue() == PredictMode.ONE) {
            output = pmc.predictOneStep(values.stream().limit(sliderValue + this.structure.getNbInputLayer() - 1).collect(Collectors.toList()), sliderValue);
        } else if (choiceStepMode.getValue() == PredictMode.MULTIPLE) {
            output = pmc.predictMultipleStep(values.stream().limit(this.structure.getNbInputLayer()).collect(Collectors.toList()), sliderValue);
        }
        for (int i = 0; i < output.size(); i++) {
            networkValues.getData().add(new XYChart.Data<>(Long.valueOf(startIndex + this.structure.getNbInputLayer() + i).toString(), output.get(i)));
            serieValues.getData().add(new XYChart.Data<>(Long.valueOf(startIndex + this.structure.getNbInputLayer() + i).toString(), series.get(i)));
        }
    }
}
