package Scenes.CharacterInfoPage;

import Database.CharacterDAO;
import Database.ConnectorDAO;
import Model.Character;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

public class CharacterInfoPageController extends SceneChanger implements Initializable {
    @FXML
    public AnchorPane mainAnchorPane;
    @FXML
    public Label nameLabel;
    @FXML
    public Label aliasLabel;
    @FXML
    public Label typeLabel;
    @FXML
    public Label genderLabel;
    @FXML
    public Label ageLabel;
    @FXML
    public AnchorPane descriptionAnchorPane;
    @FXML
    public Label descriptionLabel;
    @FXML
    public AnchorPane importanceAnchorPane;
    @FXML
    public Label importanceLabel;
    @FXML
    public AnchorPane appearanceAnchorPane;
    @FXML
    public Label appearanceLabel;
    @FXML
    public AnchorPane relateToAnchorPane;
    @FXML
    public Label relateToLabel;
    @FXML
    public AnchorPane deathAnchorPane;
    @FXML
    public Label deathLabel;
    @FXML
    public ImageView deleteButton;
    private static Character character;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CharacterDAO cd = new CharacterDAO();
        character = cd.findById(Main.characterId);
        String name = character.getName();
        String alias = character.getAlias();
        String type = character.getCharacterType();
        String gender = character.getGender();
        String age = character.getAge() + "";

        nameLabel.setText(name);
        aliasLabel.setText((alias.isBlank()) ? "No Information" : alias);
        typeLabel.setText(type);
        genderLabel.setText(gender);
        ageLabel.setText((age.isBlank() || age.equals("987654321")) ? "No Information" : age);
        deleteButton.setOnMouseClicked(mouseEvent -> {
            Alert alert = getAlert();
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    ConnectorDAO cnn = new ConnectorDAO();
                    cd.delete(character.getId());
                    cnn.deleteCharacter(character.getId());
                    try {
                        changeSceneToCharacterPageME(mouseEvent);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
    }

    private static @NotNull Alert getAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Book");
        alert.setContentText("By this action you will delete this book and lose the information of this book");
        alert.setHeaderText("Click OK to make the change");
        ImageView icon = new ImageView(
                new Image("/Style/Images/delete-icon.png")
        );
        icon.setFitHeight(30);
        icon.setFitWidth(30);
        alert.setGraphic(icon);
        return alert;
    }

    public void descriptionPopUp(ActionEvent ignoredactionEvent) {
        setVisibility(descriptionAnchorPane, true);
        if (character.getDescription() == null || character.getDescription().isBlank())
            descriptionLabel.setText("No Description");
        else
            descriptionLabel.setText(character.getDescription());
    }

    public void importancePopUp(ActionEvent ignoredactionEvent) {
        setVisibility(importanceAnchorPane, true);
        if (character.getImportance() == null || character.getImportance().isBlank())
            importanceLabel.setText("No Importance");
        else
            importanceLabel.setText(character.getImportance());
    }

    public void deathStoryPopUp(ActionEvent ignoredactionEvent) {
        setVisibility(deathAnchorPane, true);
        if (character.getDeath() == null || character.getDeath().isBlank())
            deathLabel.setText("No Death Story");
        else
            deathLabel.setText(character.getDeath());
    }

    public void appearancePopUp(ActionEvent ignoredactionEvent) {
        setVisibility(appearanceAnchorPane, true);
        if (character.getPhysicalAppearance() == null || character.getPhysicalAppearance().isBlank())
            appearanceLabel.setText("No Physical Appearance");
        else
            appearanceLabel.setText(character.getPhysicalAppearance());
    }

    public void relateToPopUp(ActionEvent ignoredactionEvent) {
        setVisibility(relateToAnchorPane, true);
        if (character.getRelateToMainChar() == null || character.getRelateToMainChar().isBlank())
            relateToLabel.setText("No Relationship To Main Character");
        else
            relateToLabel.setText(character.getRelateToMainChar());
    }

    public void cancelDescription(ActionEvent ignoredactionEvent) {
        setVisibility(descriptionAnchorPane, false);
    }

    public void cancelImportance(ActionEvent ignoredactionEvent) {
        setVisibility(importanceAnchorPane, false);
    }

    public void cancelAppearance(ActionEvent ignoredactionEvent) {
        setVisibility(appearanceAnchorPane, false);
    }

    public void cancelRelateTo(ActionEvent ignoredactionEvent) {
        setVisibility(relateToAnchorPane, false);
    }

    public void cancelDeath(ActionEvent ignoredactionEvent) {
        setVisibility(deathAnchorPane, false);
    }

    private void setVisibility(AnchorPane anchorPane, boolean status) {
        anchorPane.setVisible(status);
        mainAnchorPane.setDisable(status);
    }
}
