package sample;

/**
 * Created by ДРЮС on 27.06.2016.
 */
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Vector;


//import java.
public class Controller {

    @FXML
    private TextField Amount;
    @FXML
    private TextArea Messeges;
    @FXML
    private Button Generation;
    @FXML
    private Circle Circle1;
    @FXML
    private GridPane Main_stage;
    @FXML
    private Pane Pane1;
    /**
     * Граф
     */
    private graph G;
    private Algorithm alg;

    //....
    /*Controller(){
        //LOL = new graph();
    }*/



    public void Generation_kek() {
            try {
                int x = Integer.parseInt(Amount.getText());
                if (x > 10 || x < 2) {
                    error("Выход за пределы дозволенного.");
                } else {
                    G = new graph(x);
                    G.E = G.initialisation(x, graph.density.rare);
                    Messeges.appendText("V = " + G.V + ", E = " + G.E+ "\n" );

                    paint_GRAPH(G,Circle1);

                    G.show(G.gr_);
                    Generation.setDisable(true);

                 // Отработка алгоритма
                    alg = new Algorithm();
                    alg.DFS_bridge(G);
//                    Messeges.appendText("\n Порядок добавления ребер:");
//                    for (int i = 0; i < 2*G.E; i++)
//                    {
//                        Messeges.appendText("\n(" + alg.Order.get(i).get(0) + ", " +
//                                alg.Order.get(i).get(1) + ", " + alg.Order.get(i).get(2) + ") ");
//                    }
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                error("Ошибка ввода.");
            }

       /* Algorithm T = new Algorithm();
        T.DFS_bridge(LOL);*/



    }

    /**
     * Функция отрисовки графа
     * @param G - граф
     * @param R - круг, на котором будут располагаться вершины графа
     */
    public void paint_GRAPH ( graph G, Circle R){
        ArrayList<Circle> CircArr = new ArrayList<>();
        ArrayList<Label> LabArr = new ArrayList<>();

        ArrayList<Line> LinArr = new ArrayList<>();

    // Отрисовка вершин графа
        double x1,y1;
        for (int i =0; i < G.V; i++){
            x1 = R.getRadius()*Math.cos(2*Math.PI/G.V*i) + R.getCenterX();
            y1 = R.getRadius()*Math.sin(2*Math.PI/G.V*i) + R.getCenterY();
            CircArr.add(new Circle(x1,y1,4,Color.DARKVIOLET));
            LabArr.add(new Label(" " + i));

            Pane1.getChildren().add(CircArr.get(i));

            LabArr.get(i).setLayoutX(CircArr.get(i).getCenterX());
            LabArr.get(i).setLayoutY(CircArr.get(i).getCenterY() + 3);

            Pane1.getChildren().add(LabArr.get(i));
        }

     // Отрисовка ребер графа
        for (int end = 0, counter = 0; end < G.V; end ++){
            for (int KUK = 0; KUK < G.gr_.get(end).size(); KUK++)
            {
                LinArr.add(new Line(CircArr.get(end).getCenterX(),CircArr.get(end).getCenterY(),CircArr.get(G.gr_.get(end).get(KUK)).getCenterX(),CircArr.get(G.gr_.get(end).get(KUK)).getCenterY()));
                Pane1.getChildren().add(LinArr.get(counter));
                counter++;
            }
        }
    }




   public void error(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error Message");
        alert.setHeaderText(s);
        alert.showAndWait();
    }


}


