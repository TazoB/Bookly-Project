package Scenes.CharacterPage;

import Database.BookDAO;
import Database.ConnectorDAO;
import Model.Book;
import Model.Character;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CharacterPageController extends SceneChanger implements Initializable {
    @FXML
    public Label bookTitle;
    @FXML
    public AnchorPane mainAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Book book = new BookDAO().findById(Main.bookId);
        bookTitle.setText("Characters of '" + book.getTitle() + "'");

        List<Character> characters = new ConnectorDAO().getCharactersInBook(book.getId());

        for(int i = 0; i < characters.size(); i++) {
            try {
                createNodes(i, characters);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createNodes(int index, List<Character> characters) {
        Character character = characters.get(index);
        String name = character.getName();
        Label label = new Label((index+1) + ". " + name);
        label.setLayoutX(42);
        label.setLayoutY(getLabelY(index));
        label.setVisible(true);
        label.getStyleClass().add("character-name");

        Button button = new Button();
        button.setLayoutX(757);
        button.setLayoutY(getButtonY(index));
        button.setVisible(true);
        button.getStyleClass().add("flat-button");
        button.setText(">");
        button.setOnAction(_ -> {
            try {
                super.changeSceneToCharacterInfoPage(mainAnchorPane, character.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Line line = new Line();
        line.setStartX(-110);
        line.setStartY(1);
        line.setEndX(726);
        line.setEndY(1);
        line.setLayoutX(109);
        line.setLayoutY(label.getLayoutY() + 55.5);
        line.setStrokeWidth(2);
        line.setVisible(true);

        mainAnchorPane.getChildren().addAll(label, button, line);
    }

    private int getLabelY(int index) {
        return 83 + 70 * index;
    }
    private int getButtonY(int index) {
        return 58 + 70 * index;
    }
}
