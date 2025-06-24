package Scenes.Registration.SignUp;

import Database.CollectionDAO;
import Database.ConnectorDAO;
import Database.UserDAO;
import Model.Collection;
import Model.User;
import Scenes.Main;
import Scenes.RememberUser;
import Scenes.SceneChanger;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static Scenes.AddBookPage.AddBookPageController.setTextFormatter;

public class SignUpController extends SceneChanger implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label errorLabel;
    @FXML
    public ImageView errorIcon;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public AnchorPane recoverAnchorPane;
    @FXML
    public TextField ageField;
    @FXML
    public ChoiceBox<String> questionField;
    @FXML
    public TextField answerField;
    @FXML
    public Button visibilityButton;
    @FXML
    public TextField visiblePasswordField;
    @FXML
    public Label errorLabel1;
    @FXML
    public ImageView errorIcon1;
    @FXML
    public AnchorPane profilePicAnchorPane;
    @FXML
    public ImageView profilePicImageView;
    @FXML
    public AnchorPane rememberAnchorPane;
    @FXML
    public AnchorPane mainAnchorPane;

    private boolean isPasswordVisible = false;
    private Image eyeImage;
    private Image eyeSlashImage;
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextFormatter(ageField);
        questionField.getItems().addAll(
                "What was the name of your first pet?",
                "What was your childhood nickname?",
                "What is the name of your favorite teacher?",
                "What is your favourite color?",
                "What is the name of your favorite book from childhood?",
                "What is the name of your favorite sports team?",
                "What was the name of your first school?"
        );

        eyeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Style/Images/open-eye-icon.png")));
        eyeSlashImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Style/Images/closed-eye-icon.png")));

        imageView = new ImageView(eyeImage);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        visibilityButton.setGraphic(imageView);

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        visibilityButton.setOnAction(_ -> togglePasswordVisibility());
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

    public void signUp(ActionEvent actionEvent) throws Exception {
            ConnectorDAO cnn = new ConnectorDAO();
            CollectionDAO cd = new CollectionDAO();
            UserDAO ud = new UserDAO();
            ud.insert(
                    new User(
                            usernameField.getText(),
                            passwordField.getText(),
                            firstNameField.getText(),
                            lastNameField.getText(),
                            Integer.parseInt(ageField.getText()),
                            getCoverBytes(),
                            questionField.getValue().toLowerCase(),
                            answerField.getText().toLowerCase()
                    )
            );

            User user = ud.findAll().getLast();

            cd.insert(new Collection("All Books", null));
            cd.insert(new Collection("Finished", null));
            cd.insert(new Collection("Reading", null));
            cd.insert(new Collection("To Read", null));

            int collectionId = cd.findAll().getLast().getId();
            int userId = user.getId();

            cnn.addCollectionToUser(collectionId - 3, userId);
            cnn.addCollectionToUser(collectionId - 2, userId);
            cnn.addCollectionToUser(collectionId - 1, userId);
            cnn.addCollectionToUser(collectionId, userId);

            Main.userId = userId;
            goToMyBooks(actionEvent);
    }

    private byte[] getCoverBytes() {
        Image image = profilePicImageView.getImage();

        if (image.getUrl() != null) {
            File imageFile = new File(image.getUrl().replaceAll("%20", " ").substring(6));
            try (FileInputStream fis = new FileInputStream(imageFile)) {
                return fis.readAllBytes();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        } else {
            try {
                File tempFile = File.createTempFile("book_cover", ".png");
                tempFile.deleteOnExit();

                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", tempFile);

                try (FileInputStream fis = new FileInputStream(tempFile)) {
                    return fis.readAllBytes();
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    public void returnToSignUp() {
        recoverAnchorPane.setVisible(false);
    }

    public void showRecoverAnchorPane() {
        checkFields();
    }

    private void checkFields() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if(username.length() < 5 || username.length() > 20) {
            if(username.isBlank()) {
                setErrorProperties("Please enter username", 324);
            } else if(username.length() < 5){
                setErrorProperties("Your username must be at least 5 characters", 254);
            } else {
                setErrorProperties("Your username can be maximum of 20 characters", 237);
            }
        } else if(new UserDAO().findByUsername(username) != null) {
            setErrorProperties("User with this username already exists", 272);
        } else if(password.length() < 8 || password.length() > 20) {
            if(password.isBlank()) {
                setErrorProperties("Please enter password", 324);
            } else if(password.length() < 8) {
                setErrorProperties("Your password must be at least 8 characters", 254);
            } else {
                setErrorProperties("Your username can be maximum of 20 characters", 237);
            }
        } else if(firstName.isBlank()) {
            setErrorProperties("Please enter your first name", 305);
        } else if(lastName.isBlank()) {
            setErrorProperties("Please enter your last name", 305);
        } else {
            setErrorVisibility();
            recoverAnchorPane.setVisible(true);
        }
    }

    public void handleImageViewClick() {
        Stage stage = (Stage) profilePicImageView.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Book Cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString(),
                    profilePicImageView.getFitWidth(),
                    profilePicImageView.getFitHeight(),
                    true, true);

            profilePicImageView.setImage(image);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), profilePicImageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
    }

    private void setErrorProperties(String errorText, int layoutX) {
        setErrorVisibility();
        errorLabel.setText(errorText);
        errorIcon.setLayoutX(layoutX);
    }

    private void setErrorVisibility() {
        errorIcon.setVisible(true);
        errorLabel.setVisible(true);
    }

    public void showProfilePicAnchorPane() {
        if(ageField.getText().isBlank()) {
            errorIcon1.setLayoutX(314);
            errorIcon1.setVisible(true);
            errorLabel1.setVisible(true);
            errorLabel1.setText("Age option must be filled ");
        } else if(questionField.getValue() == null) {
            errorIcon1.setLayoutX(303);
            errorIcon1.setVisible(true);
            errorLabel1.setVisible(true);
            errorLabel1.setText("Question field can't be blank");
        } else if(answerField.getText().isBlank()) {
            errorIcon1.setLayoutX(307);
            errorIcon1.setVisible(true);
            errorLabel1.setVisible(true);
            errorLabel1.setText("Answer field can't be blank");
        } else {
            profilePicAnchorPane.setVisible(true);
        }
    }

    public void returnToRecover() {
        profilePicAnchorPane.setVisible(false);
        recoverAnchorPane.setVisible(true);
    }

    public void returnToProfilePic() {
        profilePicAnchorPane.setVisible(true);
        rememberAnchorPane.setVisible(false);
    }

    public void rememberAccount(ActionEvent actionEvent) throws Exception {
        signUp(actionEvent);
        new RememberUser().rememberUserId(Main.userId);
    }

    public void showRememberUser(ActionEvent actionEvent) throws Exception {
        RememberUser rememberUser = new RememberUser();
        List<Integer> ids = rememberUser.getRememberedUserIds();
        if(ids.size() == 3) {
            signUp(actionEvent);
        } else {
            rememberAnchorPane.setVisible(true);
            profilePicAnchorPane.setVisible(false);
            recoverAnchorPane.setVisible(false);
            mainAnchorPane.setVisible(false);
        }
    }
}