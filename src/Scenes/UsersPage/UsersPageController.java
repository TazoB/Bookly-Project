package Scenes.UsersPage;

import Database.UserDAO;
import Model.User;
import Scenes.Main;
import Scenes.RememberUser;
import Scenes.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static Scenes.MainPage.MainPageController.displayImage;

public class UsersPageController extends SceneChanger implements Initializable {
    @FXML
    public AnchorPane mainAnchorPane;
    @FXML
    public AnchorPane logInAnchorPane;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label usernameLabel;
    @FXML
    public ImageView profilePicImageView;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField visiblePasswordField;
    @FXML
    public Button visibilityButton;
    private UserDAO userDAO;

    private boolean isPasswordVisible = false;
    private Image eyeImage;
    private Image eyeSlashImage;
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RememberUser rememberUser = new RememberUser();
        List<Integer> ids = rememberUser.getRememberedUserIds();
        userDAO = new UserDAO();
        if(ids.size() == 1) {
            setProperties(ids.getLast(), 366);
        } else if(ids.size() == 2) {
            setProperties(ids.getFirst(), 222);
            setProperties(ids.getLast(), 503);
        } else if(ids.size() == 3) {
            setProperties(ids.getFirst(), 127);
            setProperties(ids.get(1), 366);
            setProperties(ids.getLast(), 589);
        }

        eyeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Style/Images/open-eye-icon.png")));
        eyeSlashImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Style/Images/closed-eye-icon.png")));

        imageView = new ImageView(eyeImage);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        visibilityButton.setGraphic(imageView);

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        visibilityButton.setOnAction(_ -> togglePasswordVisibility());
    }

    public void logIn(ActionEvent actionEvent) throws Exception {
        User user = userDAO.findByUsername(usernameLabel.getText());
        String password = passwordField.getText();
        String realPassword = user.getPassword();
        if(! password.equals(realPassword)) {
            errorLabel.setVisible(true);
        } else {
            Main.userId = user.getId();
            goToMyBooks(actionEvent);
        }
    }

    private void setProperties(int userId, int x) {
        setImageViewProperties(userId, x);
        setLabelProperties(userId, x);
    }

    private void setImageViewProperties(int userId, int x) {
        User user = userDAO.findById(userId);
        ImageView imageView = new ImageView();
        displayImage(user.getProfilePic(), imageView);
        imageView.setFitWidth(146);
        imageView.setFitHeight(197);
        imageView.setLayoutX(x);
        imageView.setLayoutY(233);
        imageView.setOnMouseClicked(_ -> {
            Main.userId = user.getId();
            logInAnchorPane.setVisible(true);
            displayImage(user.getProfilePic(), profilePicImageView);
            usernameLabel.setText(user.getUsername());
            logInAnchorPane.toFront();
        });
        mainAnchorPane.getChildren().add(imageView);
    }

    private void setLabelProperties(int userId, int x) {
        User user = userDAO.findById(userId);
        Label label = new Label(user.getUsername());
        label.setLayoutX(x);
        label.setLayoutY(430);
        label.setPrefWidth(146);
        label.setAlignment(Pos.CENTER);
        label.setFont(
                new Font(24)
        );
        mainAnchorPane.getChildren().add(label);
    }

    public void cancelLogInAnchorPane() {
        logInAnchorPane.setVisible(false);
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            imageView.setImage(eyeSlashImage);
        } else {
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            imageView.setImage(eyeImage);
        }
    }
}
