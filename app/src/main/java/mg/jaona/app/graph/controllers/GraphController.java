package mg.jaona.app.graph.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import mg.jaona.datastructure.graph.AdjacencyListGraph;
import mg.jaona.datastructure.graph.Vertex;
import netscape.javascript.JSObject;
import java.net.URL;
import java.util.*;

public class GraphController implements Initializable {
    @FXML
    private WebView webview;

    private AdjacencyListGraph<Integer> g;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Vertex[] vertexes = new Vertex[]{new Vertex("a"),
                new Vertex("b"),
                new Vertex("c")};
        Map<Vertex, List<Map.Entry<Vertex, Integer>>> list = new HashMap<>();
        for (Vertex e : vertexes) {
            List<Map.Entry<Vertex, Integer>> edge = new ArrayList<>();
            for (Vertex f : vertexes) {
                edge.add(new AbstractMap.SimpleEntry(f, randInt(0, 40)));
            }
            list.put(e, edge);
        }
        g = new AdjacencyListGraph();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Vertex e : vertexes) {
            g.addVertex(e);
            sb.append(e.toString());
            sb.append(",");
        }
        sb.append("]");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[");
        for (Vertex key : list.keySet()) {
            for (Map.Entry<Vertex, Integer> target : list.get(key)) {
                sb2.append("{ source:\"");
                sb2.append(key.getLabel());
                sb2.append("\", target:\"");
                sb2.append(target.getKey().getLabel());
                sb2.append("\", weight:");
                sb2.append(target.getValue());
                sb2.append("},");
                g.addEdge(key, target.getKey(), target.getValue());
            }
        }
        sb2.append("]");
        webview.getEngine().executeScript("const nn = " + sb.toString());
        webview.getEngine().executeScript("const ll = " + sb2.toString());
    }

    private int randInt(int min, int max) {
        return Double.valueOf(Math.floor(Math.random() * (max - min) + min)).intValue();
    }

    @FXML
    public void handleButton(ActionEvent ae){
        webview.getEngine().executeScript("run(nn,ll)");
    }

    public WebView getWebview() {
        return webview;
    }
}
