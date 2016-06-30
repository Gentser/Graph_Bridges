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
import java.util.Collections;
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
    @FXML
    private Button Next_step;
    @FXML
    private Button Prev_step;

    private ArrayList<Circle> CircArr;// = new ArrayList<>();
    private ArrayList<Label> LabArr;// = new ArrayList<>();
    private ArrayList<Line> LinArr;// = new ArrayList<>();
    /**
     * Граф
     */
    private graph G;
    private Algorithm alg;

    private int counter = -1;



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

                    Prev_step.setDisable(true);

                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                error("Ошибка ввода.");
            }

    }

    /**
     * Функция отрисовки графа
     * @param G - граф
     * @param R - круг, на котором будут располагаться вершины графа
     */
    public void paint_GRAPH ( graph G, Circle R){
        CircArr = new ArrayList<>(); //точки
        LabArr = new ArrayList<>(); //лейблы
        LinArr = new ArrayList<>(); // линии

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
        boolean FLAGHOK = true;
        for (int end = 0, counter = 0; end < G.V; end ++){
            for (int KUK = 0; KUK < G.gr_.get(end).size(); KUK++)
            {

                for (int TRALALA = 0; TRALALA < LinArr.size(); TRALALA++ )
                {
                    if ((LinArr.get(TRALALA).getStartX() == CircArr.get(G.gr_.get(end).get(KUK)).getCenterX()) &&
                    (LinArr.get(TRALALA).getStartY() == CircArr.get(G.gr_.get(end).get(KUK)).getCenterY()) &&
                            (LinArr.get(TRALALA).getEndX() == CircArr.get(end).getCenterX()) &&
                            (LinArr.get(TRALALA).getEndY() == CircArr.get(end).getCenterY()))
                    {
                        FLAGHOK = false;
                        break;

                    }
                }

                if (FLAGHOK)
                {
                    LinArr.add(new Line(CircArr.get(end).getCenterX(),CircArr.get(end).getCenterY(),CircArr.get(G.gr_.get(end).get(KUK)).getCenterX(),CircArr.get(G.gr_.get(end).get(KUK)).getCenterY()));
                    Pane1.getChildren().add(LinArr.get(counter));
                    // LinArr.add(LinArr.get(counter));
                    counter++;
                }
                FLAGHOK = true;
            }
        }
    }

    public void showRibs(int counter , Algorithm alg) // должен измениться каунтер
    {
/*        for(int i = 0; i < counter; i++)
        {*/
        //counter++;
            for(int j = 0; j < LinArr.size(); j++)
            {
                if ((( LinArr.get(j).getStartX() == CircArr.get(alg.Order.get(counter).get(0)).getCenterX() ) &&
                        (LinArr.get(j).getStartY() == CircArr.get(alg.Order.get(counter).get(0)).getCenterY()) &&
                (LinArr.get(j).getEndX() == CircArr.get(alg.Order.get(counter).get(1)).getCenterX()) &&
                        (LinArr.get(j).getEndY() == CircArr.get(alg.Order.get(counter).get(1)).getCenterY())) ||

                        //или
                ((( LinArr.get(j).getEndX() == CircArr.get(alg.Order.get(counter).get(0)).getCenterX() ) &&
                        (LinArr.get(j).getEndY() == CircArr.get(alg.Order.get(counter).get(0)).getCenterY()) &&
                        (LinArr.get(j).getStartX() == CircArr.get(alg.Order.get(counter).get(1)).getCenterX()) &&
                        (LinArr.get(j).getStartY() == CircArr.get(alg.Order.get(counter).get(1)).getCenterY()))))

                {
                    if (alg.Order.get(counter).get(2) == 1)
                    {
                       // Pane1.getChildren().remove(LinArr.get(j));
                        //LinArr.get(j).setFill(Color.GREEN); //прямое
                        LinArr.get(j).setStrokeWidth(2);

                        LinArr.get(j).setStroke(Color.GREEN);
                       // LinArr.get(j).

                       // Pane1.getChildren().add(LinArr.get(j));
                    }
                    else if (alg.Order.get(counter).get(2) == 2)
                    {
                       // Pane1.getChildren().remove(LinArr.get(j));
                       // LinArr.get(j).setFill(Color.INDIGO); //обратное
                        LinArr.get(j).setStrokeWidth(2);

                        LinArr.get(j).setStroke(Color.YELLOW);
                       // Pane1.getChildren().add(LinArr.get(j));
                    }
                    else
                    {
                       // Pane1.getChildren().remove(LinArr.get(j));

                       // LinArr.get(j).setFill(Color.PEACHPUFF); //мостик
                        LinArr.get(j).setStrokeWidth(2);


                        LinArr.get(j).setStroke(Color.RED);
                       // Pane1.getChildren().add(LinArr.get(j));
                    }
                }

            }

        //прячем предыдущее ребро
    }

    public void hideRibs (int counter, Algorithm alg) {
        //counter--;
        for(int j = 0; j < LinArr.size(); j++)
        {
            if ((( LinArr.get(j).getStartX() == CircArr.get(alg.Order.get(counter).get(0)).getCenterX() ) &&
                    (LinArr.get(j).getStartY() == CircArr.get(alg.Order.get(counter).get(0)).getCenterY()) &&
                    (LinArr.get(j).getEndX() == CircArr.get(alg.Order.get(counter).get(1)).getCenterX()) &&
                    (LinArr.get(j).getEndY() == CircArr.get(alg.Order.get(counter).get(1)).getCenterY())) ||

            //или
            ((( LinArr.get(j).getEndX() == CircArr.get(alg.Order.get(counter).get(0)).getCenterX() ) &&
                    (LinArr.get(j).getEndY() == CircArr.get(alg.Order.get(counter).get(0)).getCenterY()) &&
                    (LinArr.get(j).getStartX() == CircArr.get(alg.Order.get(counter).get(1)).getCenterX()) &&
                    (LinArr.get(j).getStartY() == CircArr.get(alg.Order.get(counter).get(1)).getCenterY()))))
            {
               // Pane1.getChildren().remove(LinArr.get(j));
              //  LinArr.get(j).setFill(Color.BLACK); //тип закарсили
                if (LinArr.get(j).getStroke() == Color.RED)
                {
                    LinArr.get(j).setStrokeWidth(1);
                    LinArr.get(j).setStroke(Color.GREEN);
                }
                else
                {
                    LinArr.get(j).setStrokeWidth(1);
                    LinArr.get(j).setStroke(Color.BLACK);
                }

            }
        }
    }

    public void incCounter() {
        if (alg.Numeration > counter + 1 )
        {
            if (Prev_step.isDisable())
            {
                Prev_step.setDisable(false);
            }
            counter ++;
            showRibs(counter, alg);
        }
        else
        {
            Next_step.setDisable(true);
        }
    }

    public void decCounter() {
        if (counter >= 0){
            if(Next_step.isDisable())
            {
                Next_step.setDisable(false);
            }

            hideRibs(counter, alg);
            counter --;
        }
        else
        {
            Prev_step.setDisable(true);
        }
    }




   public void error(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error Message");
        alert.setHeaderText(s);
        alert.showAndWait();
    }


}




