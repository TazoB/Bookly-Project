package Scenes.Registration.LogIn;

import Database.UserDAO;
import Model.User;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LogInController extends SceneChanger implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button logInButton;
    @FXML
    public Label errorLabel;
    @FXML
    public ImageView errorIcon;
    @FXML
    public TextField visiblePasswordField;
    @FXML
    public Button visibilityButton;
    @FXML
    public AnchorPane mainAnchorPane;

    private boolean isPasswordVisible = false;
    private Image eyeImage;
    private Image eyeSlashImage;
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        String username = usernameField.getText();
        String password = passwordField.getText();

        List<User> users = new UserDAO().findAll();
        boolean user_exists = false;

        for (User user : users) {
            if(user.getUsername().equals(username)) {
                user_exists = true;
                if(user.getPassword().equals(password)) {
                    Main.userId = user.getId();
                    goToMyBooks(actionEvent);
                } else {
                    errorIcon.setVisible(true);
                    errorIcon.setLayoutX(204);
                    errorLabel.setVisible(true);
                    errorLabel.setText("Wrong Password. Try again or click 'Reset Your Password'.");
                }
                break;
            }
        }
        if(! user_exists) {
            errorIcon.setVisible(true);
            errorIcon.setLayoutX(279);
            errorLabel.setVisible(true);
            errorLabel.setText("We can't find your Bookly account.");
        }
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
