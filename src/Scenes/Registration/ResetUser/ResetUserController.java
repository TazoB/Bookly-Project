package Scenes.Registration.ResetUser;

import Database.UserDAO;
import Model.User;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class ResetUserController extends SceneChanger {
    @FXML
    public TextField usernameField;
    @FXML
    public Button nextButton;
    @FXML
    public Label errorLabel;
    @FXML
    public ImageView errorIcon;
    @FXML
    public AnchorPane QaAnchorPane;
    @FXML
    public Label errorLabel1;
    @FXML
    public ImageView errorIcon1;
    @FXML
    public Label questionLabel;
    @FXML
    public TextField answerField;
    @FXML
    public AnchorPane passwordAnchor;
    @FXML
    public Label errorLabel2;
    @FXML
    public ImageView errorIcon2;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField reenterPasswordField;
    @FXML
    public Button visibilityButton;
    @FXML
    public Button visibilityButton1;
    @FXML
    public TextField visiblePasswordField;
    @FXML
    public TextField visibleReenterPasswordField;
    private User user;

    private boolean isPasswordVisible = false;
    private boolean isPasswordVisible1 = false;
    private Image eyeImage;
    private Image eyeSlashImage;
    private ImageView imageView;
    private ImageView imageView1;

    public void changeToUsername(ActionEvent ignoredActionEvent) {
        QaAnchorPane.setVisible(false);
        errorLabel.setVisible(false);
        errorIcon.setVisible(false);
    }

    public void changeToQA(ActionEvent ignoredactionEvent) {
        String username = usernameField.getText();
        UserDAO ud = new UserDAO();

        if(ud.findByUsername(username) == null) {
            errorIcon.setVisible(true);
            errorLabel.setVisible(true);
            errorLabel.setText("We can't find user with this username");
            errorIcon.setLayoutX(271);
        } else {
            QaAnchorPane.setVisible(true);
            user = ud.findByUsername(username);
            String question = user.getQuestion();

            questionLabel.setText("Question: " + question);
        }
    }

    public void changeToNewPassword(ActionEvent ignoredactionEvent) {
        String correctAnswer = user.getAnswer();
        String answer = answerField.getText().toLowerCase();

        if(! correctAnswer.equals(answer)) {
            errorIcon1.setVisible(true);
            errorLabel1.setVisible(true);
        } else {
            passwordAnchor.setVisible(true);

            eyeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Style/Images/open-eye-icon.png")));
            eyeSlashImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Style/Images/closed-eye-icon.png")));

            imageView = new ImageView(eyeImage);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);

            imageView1 = new ImageView(eyeImage);
            imageView1.setFitWidth(20);
            imageView1.setFitHeight(20);

            visibilityButton.setGraphic(imageView);
            visibilityButton1.setGraphic(imageView1);

            visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());
            visibleReenterPasswordField.textProperty().bindBidirectional(reenterPasswordField.textProperty());

            visibilityButton.setOnAction(_ -> {
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
            });
            visibilityButton1.setOnAction(_ -> {
                isPasswordVisible1 = !isPasswordVisible1;

                if (isPasswordVisible1) {
                    visibleReenterPasswordField.setVisible(true);
                    visibleReenterPasswordField.setManaged(true);
                    reenterPasswordField.setVisible(false);
                    reenterPasswordField.setManaged(false);
                    imageView1.setImage(eyeSlashImage);
                } else {
                    visibleReenterPasswordField.setVisible(false);
                    visibleReenterPasswordField.setManaged(false);
                    reenterPasswordField.setVisible(true);
                    reenterPasswordField.setManaged(true);
                    imageView1.setImage(eyeImage);
                }
            });
        }
    }

    public void resetPassword(ActionEvent actionEvent) throws Exception {
        String password = passwordField.getText();
        String reenterPassword = reenterPasswordField.getText();

        if(password.length() < 8 || password.length() > 20) {
            if(password.isBlank()) {
                errorIcon2.setVisible(true);
                errorLabel2.setVisible(true);
                errorLabel2.setText("Please enter password");
                errorIcon2.setLayoutX(324);
            } else if(password.length() < 8) {
                errorIcon2.setVisible(true);
                errorLabel2.setVisible(true);
                errorLabel2.setText("Your password must be at least 8 characters");
                errorIcon2.setLayoutX(254);
            } else {
                errorIcon2.setVisible(true);
                errorLabel2.setVisible(true);
                errorLabel2.setText("Your username can be maximum of 20 characters");
                errorIcon2.setLayoutX(237);
            }
        } else if(! password.equals(reenterPassword)) {
            errorIcon2.setVisible(true);
            errorLabel2.setVisible(true);
            errorIcon2.setLayoutX(320);
            errorLabel2.setText("Passwords don't match");
        } else {
            UserDAO ud = new UserDAO();
            ud.updatePassword(user.getId(), password);
            Main.userId = user.getId();
            goToMyBooks(actionEvent);
        }
    }

    public void changeToQAFromPassword(ActionEvent ignoredactionEvent) {
        passwordAnchor.setVisible(false);
        errorLabel1.setVisible(false);
        errorIcon1.setVisible(false);
    }
}
