package Scenes.GoalsPage;

import Database.ConnectorDAO;
import Database.UserDAO;
import Model.User;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class GoalsPageController extends SceneChanger implements Initializable {
    @FXML
    public Rectangle rectangle1;
    @FXML
    public Rectangle rectangle2;
    @FXML
    public Rectangle rectangle3;
    @FXML
    public Rectangle rectangle4;
    @FXML
    public Label label1;
    @FXML
    public Label label2;
    @FXML
    public Label label3;
    @FXML
    public Label label4;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = new UserDAO().findById(Main.userId);
        ConnectorDAO connectorDAO = new ConnectorDAO();
        int totalBooks = connectorDAO.getBooksInUser(user.getId()).size();
        int finishedBooks = connectorDAO.getBooksInCollection("Finished").size();
        int readingBooks = connectorDAO.getBooksInCollection("Reading").size();

        if(finishedBooks < 10)
            label1.setText(finishedBooks + "/10");
        else {
            label1.setText("10/10");
            setFill(rectangle1);
        }

        if(finishedBooks < 50)
            label2.setText(finishedBooks + "/50");
        else {
            label2.setText("50/50");
            setFill(rectangle2);
        }

        if(readingBooks < 5)
            label3.setText(readingBooks + "/5");
        else {
            label3.setText("5/5");
            setFill(rectangle3);
        }

        if(totalBooks < 100)
            label4.setText(totalBooks + "/100");
        else {
            label4.setText("100/100");
            setFill(rectangle4);
        }
    }

    private void setFill(Rectangle rectangle) {
        rectangle.setFill(Color.valueOf("5de145"));
    }
}
