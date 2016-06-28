package sample;

/**
 * Created by ДРЮС on 27.06.2016.
 */
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;



//import java.
public class Controller {

    @FXML
    private TextField Amount;
    @FXML
    private TextArea Messeges;
    @FXML
    private Button Generation;

    //создание графа


    //....



    public void Generation_kek() {
            try {
                int x = Integer.parseInt(Amount.getText());
                if (x > 10 || x < 0) {
                    error("Выход за пределы дозволенного.");
                } else {
                    Messeges.appendText("Число вершин: " + x +"\n" );
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                error("Ошибка ввода.");
            }

    }




   public void error(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error Message");
        alert.setHeaderText(s);
        alert.showAndWait();
    }


}


