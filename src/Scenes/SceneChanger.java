package Scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneChanger {
    public void goToMyBooks(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "MainPage/mainPage.fxml");
    }

    public void goToGoals(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "GoalsPage/goalsPage.fxml");
    }

    public void goToStats(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "StatsPage/statsPage.fxml");
    }

    public void goToProfile(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "ProfilePage/profilePage.fxml");
    }

    public void changeSceneToAddBookPage(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "AddBookPage/addBookPage.fxml");
    }

    public void changeSceneToAddCollectionPage(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "AddCollectionPage/addCollectionPage.fxml");
    }

    public void changeSceneToLogInPage(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "Registration/LogIn/logIn.fxml");
    }

    public void changeSceneToSignUpPage(MouseEvent mouseEvent) throws IOException {
        changeSceneUsingMouseEvent(mouseEvent, "/Scenes/Registration/SignUp/signUP.fxml");
    }

    public void changeSceneToSignUpPageAE(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "Registration/SignUp/signUP.fxml");
    }

    public void changeSceneToLogInPageME(MouseEvent mouseEvent) throws IOException {
        changeSceneUsingMouseEvent(mouseEvent, "/Scenes/Registration/LogIn/logIn.fxml");
    }

    public void changeSceneToResetUserME(MouseEvent mouseEvent) throws IOException {
        changeSceneUsingMouseEvent(mouseEvent, "/Scenes/Registration/ResetUser/resetUser.fxml");
    }

    public void changeSceneToUpdateBookPage(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "UpdateBookPage/updateBookPage.fxml");
    }

    public void changeSceneToAddCharacterPage(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "AddCharacterPage/addCharacterPage.fxml");
    }

    public void changeSceneToCharacterPage(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "CharacterPage/characterPage.fxml");
    }

    public void changeSceneToUsersPage(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "UsersPage/usersPage.fxml");
    }

    public void changeSceneToCharacterPageME(MouseEvent mouseEvent) throws Exception {
        changeSceneUsingMouseEvent(mouseEvent, "/Scenes/CharacterPage/characterPage.fxml");
    }

    public void changeSceneToCharacterInfoPage(AnchorPane anchorPane, int id) throws Exception {
        Main.characterId = id;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("CharacterInfoPage/characterInfoPage.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void changeSceneToBookPageME (MouseEvent mouseEvent) {
        try {
            changeSceneUsingMouseEvent(mouseEvent, "/Scenes/BookPage/bookPage.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeSceneToBookPage(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "BookPage/bookPage.fxml");
    }

    public void changeSceneToBookPageAE(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Switch to book page");
        alert.setHeaderText("If you click OK you will lose the progress.");
        alert.setContentText("Do you want to leave?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    changeScene(actionEvent, "BookPage/bookPage.fxml");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void changeSceneToCollectionPageAE(ActionEvent actionEvent) throws Exception {
        changeScene(actionEvent, "CollectionPage/collectionPage.fxml");
    }

    public void changeSceneToCollectionPageME(MouseEvent mouseEvent) {
        try {
            changeSceneUsingMouseEvent(mouseEvent, "/Scenes/CollectionPage/collectionPage.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeSceneToMainPage(ActionEvent actionEvent) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Switch to main page");
        alert.setHeaderText("If you click OK you will lose the progress.");
        alert.setContentText("Do you want to leave?");


        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    goToMyBooks(actionEvent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void changeScene(ActionEvent actionEvent, String fileName) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fileName));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void changeSceneUsingMouseEvent(MouseEvent event, String fileName) throws IOException {
        URL fxmlUrl = Main.class.getResource(fileName);

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
