package sample;

/**
 * Created by ДРЮС on 27.06.2016.
 */
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import javafx.scene.media.*;
import java.io.*;



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
    @FXML
    private Slider PIMPSlide;
    @FXML
    private ImageView pahom;
    @FXML
    private ImageView tarelka;
    @FXML
    private Label sugar;

    private ArrayList<Circle> CircArr;// = new ArrayList<>();
    private ArrayList<Label> LabArr;// = new ArrayList<>();
    private ArrayList<Line> LinArr;// = new ArrayList<>();
    private ArrayList<Label> LabDFS;
    /**
     * Граф
     */
    private graph G;
    private Algorithm alg;
    private Media mainSong;     // звуковая дорожка0
    private Media song1;        // звуковая дорожка1
    private MediaPlayer mediaPlayer1;    // плеер для главной песни
    private MediaPlayer mediaPlayer2;    // плеер

    private int counter = -1;
    private static int INDEX = 0;

   public void Change_fon ()
    {
        if(!pahom.isVisible())
        {
            pahom.setVisible(true);
            mediaPlayer1.pause();

            mediaPlayer2 = new MediaPlayer(song1);
            mediaPlayer2.setVolume(0.3);
            mediaPlayer2.setCycleCount(3);
            mediaPlayer2.play();

        }
        else
        {
            pahom.setVisible(false);
            mediaPlayer1.play();
            mediaPlayer2.stop();
        }
    }

    public void Generation_kek() {
            try {
                int x = Integer.parseInt(Amount.getText());
                if (x == 228)
                {
                    EGG("Один четыре восемь восемь");
                }
                else if (x == 1488)
                {
                    EGG("Два два восемь");
                }
                else if (x == 2016)
                {
                    EGG("Графа нет! Но вы держитесь тут, всего вам доброго и хорошего настроения!");
                }
               else if (x > 100 || x < 2) {
                    error("Выход за пределы дозволенного.");
                } else {
                    G = new graph(x);
                                  // Инициализация графа
                    if(PIMPSlide.getValue() == 0) G.E = G.initialisation(x, graph.density.rare);
                    else G.E = G.initialisation(x, graph.density.normal);

                    paint_GRAPH(G,Circle1);

                    G.show(G.gr_);
                    Generation.setDisable(true);

                 // Отработка алгоритма
                    alg = new Algorithm();
                    alg.DFS_bridge(G);

                    // Выдача плана прохождения алгоритма нахождения мостов
                            Messeges.clear();
                    Messeges.appendText("V = " + G.V + ", E = " + G.E+ "\n" );
                    //   Messeges.appendText("********************\n");
                            Messeges.appendText("Порядок отметок прямых/обратных ребер и мостов:\n");
                    for (int i = 0; i < alg.Order.size(); i++)
                    {Messeges.appendText((i+1) + ")  Ребро (" + alg.Order.get(i).get(0) + ", " + alg.Order.get(i).get(1) + ") - ");
                    switch (alg.Order.get(i).get(2))
                    {
                    case (1):
                        Messeges.appendText("<Прямое>\n");
                        break;
                    case (2):
                        Messeges.appendText("<Обратное>\n");
                        break;
                    case (3):
                        Messeges.appendText("<Мост>\n");
                        break;
                    }
                    }
                    if (alg.Order.get(alg.Order.size()-1).get(2) != 3)
                        Messeges.appendText(" В данном графе нет ни одного моста!");
                            // Активируем кнопку "Следующий шаг"
                    Next_step.setDisable(false);

                    LabDFS = new ArrayList<>(); //ДОБАВЛЕНО
                    for (int i = 0; i < G.V; i++)
                    {
                        LabDFS.add(new Label("(" + i + ")"));
                        LabDFS.get(i).setVisible(false);

                        LabDFS.get(i).setLayoutX(CircArr.get(alg.revNum.get(i)).getCenterX() -15);
                        LabDFS.get(i).setLayoutY(CircArr.get(alg.revNum.get(i)).getCenterY() + 3);
                        LabDFS.get(i).setTextFill(Color.RED);
                        Pane1.getChildren().add(LabDFS.get(i));
                    }

                    Prev_step.setDisable(true);

                    // Песни
                    mainSong = new Media(new File("src/sample/mainsong.wav").toURI().toString());
                    song1 = new Media(new File("src/sample/song1.wav").toURI().toString());
                    mediaPlayer1 = new MediaPlayer(mainSong);
                    mediaPlayer1.setMute(false);
                    mediaPlayer1.setVolume(0.3);
                    //mediaPlayer1.setCycleCount(3);
                    mediaPlayer1.play();
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
            CircArr.add(new Circle(x1,y1,7,Color.DARKVIOLET));
            LabArr.add(new Label(" " + i));
            LabArr.get(i).setLayoutX(CircArr.get(i).getCenterX());
            LabArr.get(i).setLayoutY(CircArr.get(i).getCenterY() + 3);
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

                    counter++;
                }
                FLAGHOK = true;
            }
        }
        for (int i = 0; i < G.V; i++)
        {
            Pane1.getChildren().add(LabArr.get(i));
            Pane1.getChildren().add(CircArr.get(i));
        }
    }

    /**
     *
     * @param counter
     * @param alg
     */
    public void showRibs(int counter , Algorithm alg) // должен измениться каунтер
    {

            for(int j = 0; j < LinArr.size(); j++)
            {
                if ((( LinArr.get(j).getStartX() == CircArr.get(alg.Order.get(counter).get(0)).getCenterX() ) &&
                        (LinArr.get(j).getStartY() == CircArr.get(alg.Order.get(counter).get(0)).getCenterY()) &&
                (LinArr.get(j).getEndX() == CircArr.get(alg.Order.get(counter).get(1)).getCenterX()) &&
                        (LinArr.get(j).getEndY() == CircArr.get(alg.Order.get(counter).get(1)).getCenterY())) ||

                ((( LinArr.get(j).getEndX() == CircArr.get(alg.Order.get(counter).get(0)).getCenterX() ) &&
                        (LinArr.get(j).getEndY() == CircArr.get(alg.Order.get(counter).get(0)).getCenterY()) &&
                        (LinArr.get(j).getStartX() == CircArr.get(alg.Order.get(counter).get(1)).getCenterX()) &&
                        (LinArr.get(j).getStartY() == CircArr.get(alg.Order.get(counter).get(1)).getCenterY()))))

                {
                    if (alg.Order.get(counter).get(2) == 1)
                    {


                        if (INDEX == 0)
                        {
                            LabDFS.get(INDEX).setVisible(true);
                        }
                        LabDFS.get(++INDEX).setVisible(true);
                        LinArr.get(j).setStrokeWidth(4);
                        LinArr.get(j).setStroke(Color.GREEN);
                    }
                    else if (alg.Order.get(counter).get(2) == 2)
                    {
                        LabDFS.get(INDEX).setVisible(true);
                        LinArr.get(j).setStrokeWidth(4);
                        LinArr.get(j).setStroke(Color.YELLOW);
                        //INDEX--;
                    }
                    else
                    {
                        LinArr.get(j).setStrokeWidth(4);
                        LinArr.get(j).setStroke(Color.RED);
                    }
                }

            }

    }

    /**
     *
     * @param counter
     * @param alg
     */
    public void hideRibs (int counter, Algorithm alg) {
        //counter--;
        for(int j = 0; j < LinArr.size(); j++)
        {
            if ((( LinArr.get(j).getStartX() == CircArr.get(alg.Order.get(counter).get(0)).getCenterX() ) &&
                    (LinArr.get(j).getStartY() == CircArr.get(alg.Order.get(counter).get(0)).getCenterY()) &&
                    (LinArr.get(j).getEndX() == CircArr.get(alg.Order.get(counter).get(1)).getCenterX()) &&
                    (LinArr.get(j).getEndY() == CircArr.get(alg.Order.get(counter).get(1)).getCenterY())) ||

            ((( LinArr.get(j).getEndX() == CircArr.get(alg.Order.get(counter).get(0)).getCenterX() ) &&
                    (LinArr.get(j).getEndY() == CircArr.get(alg.Order.get(counter).get(0)).getCenterY()) &&
                    (LinArr.get(j).getStartX() == CircArr.get(alg.Order.get(counter).get(1)).getCenterX()) &&
                    (LinArr.get(j).getStartY() == CircArr.get(alg.Order.get(counter).get(1)).getCenterY()))))
            {
                if (LinArr.get(j).getStroke() == Color.RED)
                {
                    //INDEX--;
                    LinArr.get(j).setStrokeWidth(4);
                    LinArr.get(j).setStroke(Color.GREEN);
                }
                else
                {
                    if (LinArr.get(j).getStroke() == Color.GREEN) INDEX--; //проверить
                    LinArr.get(j).setStrokeWidth(1);
                    LinArr.get(j).setStroke(Color.BLACK);
                }

            }
        }
    }

    /**
     *
     */
    public void incCounter() {
        if (alg.Numeration > counter + 1 ) {

                if (Prev_step.isDisable()) {
                    Prev_step.setDisable(false);
                }
                    counter++;
                    showRibs(counter, alg);
                    if (alg.Numeration <= counter + 1)
                    {
                        Next_step.setDisable(true);
                       // LabDFS.get(counter).setVisible(true);
                    }
                }
        }

    public void decCounter() {
        if (counter >= 0) {
            if (Next_step.isDisable()) {
                Next_step.setDisable(false);
            }

            hideRibs(counter, alg);
            counter--;

            if (counter == -1) {
                Prev_step.setDisable(true);
            }

        }
    }


    /**
     *
     * @param s
     */

   public void error(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error Message");
        alert.setHeaderText(s);
        alert.showAndWait();
    }

    public void EGG(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("КЕК");
        alert.setHeaderText(s);
        alert.showAndWait();
    }

    public void bread ()
    {
        //tarelka.setVisible(true);
        sugar.setOpacity(1);
        tarelka.setOpacity(1);
    }

    public void unbread ()
    {
        //tarelka.setVisible(true);
        sugar.setOpacity(0);
        tarelka.setOpacity(0);
    }


}





