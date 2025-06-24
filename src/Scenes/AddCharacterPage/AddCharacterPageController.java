package Scenes.AddCharacterPage;

import Database.CharacterDAO;
import Database.ConnectorDAO;
import Model.Character;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static Scenes.AddBookPage.AddBookPageController.setTextFormatter;

public class AddCharacterPageController extends SceneChanger implements Initializable {
    @FXML
    public TextField nameField;
    @FXML
    public TextField aliasFiled;
    @FXML
    public RadioButton mainCharacterType;
    @FXML
    public RadioButton secondaryCharacterType;
    @FXML
    public RadioButton maleRadioButton;
    @FXML
    public RadioButton femaleRadioButton;
    @FXML
    public TextField ageField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public TextField relateToMainChar;
    @FXML
    public TextField physicalAppField;
    @FXML
    public TextArea importanceField;
    @FXML
    public TextField deathField;
    @FXML
    public Label relateToLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextFormatter(ageField);
    }

    public void submitCharacterData(ActionEvent actionEvent) throws Exception {
        if(nameField.getText().isBlank()) {
            nameField.setStyle("-fx-border-color: red;");
        } else {
            CharacterDAO cd = new CharacterDAO();
            ConnectorDAO cnn = new ConnectorDAO();
            cd.insert(
                    new Character(
                            nameField.getText(),
                            aliasFiled.getText(),
                            (mainCharacterType.isSelected()) ? "Main" : "Secondary",
                            (maleRadioButton.isSelected()) ? "Male" : "Female",
                            (ageField.getText() == null || ageField.getText().isBlank()) ? 987654321 : Integer.parseInt(ageField.getText()),
                            descriptionField.getText(),
                            importanceField.getText(),
                            deathField.getText(),
                            physicalAppField.getText(),
                            (secondaryCharacterType.isSelected()) ? relateToMainChar.getText() : null
                    )
            );
            Main.characterId = cd.findAll().getLast().getId();
            cnn.addCharacterToBook(Main.userId, Main.collectionId, Main.bookId, Main.characterId);
            changeSceneToBookPage(actionEvent);
        }
    }

    public void setRelateVisibilityFalse(ActionEvent ignoredActionEvent) {
        relateToLabel.setVisible(false);
        relateToMainChar.setVisible(false);
        ageField.setLayoutY(304);
        deathField.setLayoutY(378);
    }

    public void setRelateVisibilityTrue(ActionEvent ignoredActionEvent) {
        relateToLabel.setVisible(true);
        relateToMainChar.setVisible(true);
        ageField.setLayoutY(274);
        deathField.setLayoutY(325);
    }
}