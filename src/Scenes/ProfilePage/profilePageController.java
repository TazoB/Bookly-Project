package Scenes.ProfilePage;

import Database.UserDAO;
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
import static Scenes.MainPage.MainPageController.displayImage;

public class profilePageController extends SceneChanger implements Initializable {
    @FXML
    public ImageView profilePicImageView;
    @FXML
    public AnchorPane usernameAnchorPane;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public Label ageLabel;
    @FXML
    public TextField usernameField;
    @FXML
    public Label usernameErrorLabel;
    @FXML
    public AnchorPane nameAnchorPane;
    @FXML
    public TextField firstNameField;
    @FXML
    public Label nameErrorLabel;
    @FXML
    public TextField lastNameField;
    @FXML
    public AnchorPane ageAnchorPane;
    @FXML
    public TextField ageField;
    @FXML
    public Label ageErrorLabel;
    @FXML
    public AnchorPane passwordAnchorPane;
    @FXML
    public PasswordField oldPasswordField;
    @FXML
    public Label passwordErrorLabel;
    @FXML
    public PasswordField newPasswordField;
    @FXML
    public AnchorPane profilePictureAnchorPane;
    @FXML
    public ImageView newProfilePicImageView;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public CheckBox rememberCheckBox;
    @FXML
    public Button usernameEditor;
    @FXML
    public Button nameEditor;
    @FXML
    public Button ageEditor;
    @FXML
    public Button visibilityButton;
    @FXML
    public Button visibilityButton1;
    @FXML
    public TextField visiblePasswordField;
    @FXML
    public TextField visiblePasswordField1;
    private User user;
    private UserDAO userDAO;
    RememberUser rememberUser;

    private boolean isPasswordVisible = false;
    private boolean isPasswordVisible1 = false;
    private Image eyeImage;
    private Image eyeSlashImage;
    private ImageView eyeImageView;
    private ImageView eyeImageView1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = new UserDAO();
        user = userDAO.findById(Main.userId);
        rememberUser = new RememberUser();
        displayImage(user.getProfilePic(), profilePicImageView);
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        ageLabel.setText("Age: " + user.getAge());
        setTextFormatter(ageField);

        List<Integer> ids = rememberUser.getRememberedUserIds();
        if(ids.contains(user.getId())) {
            rememberCheckBox.setSelected(true);
        }


        ImageView imageView1 = new ImageView(
                new Image("/Style/Images/edit-icon.png")
        );
        imageView1.setFitWidth(35);
        imageView1.setFitHeight(35);

        ImageView imageView2 = new ImageView(
                new Image("/Style/Images/edit-icon.png")
        );
        imageView2.setFitWidth(35);
        imageView2.setFitHeight(35);

        ImageView imageView3 = new ImageView(
                new Image("/Style/Images/edit-icon.png")
        );
        imageView3.setFitWidth(35);
        imageView3.setFitHeight(35);

        usernameEditor.setGraphic(imageView1);
        nameEditor.setGraphic(imageView2);
        ageEditor.setGraphic(imageView3);

        eyeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Style/Images/open-eye-icon.png")));
        eyeSlashImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Style/Images/closed-eye-icon.png")));

        eyeImageView = new ImageView(eyeImage);
        eyeImageView.setFitWidth(20);
        eyeImageView.setFitHeight(20);

        eyeImageView1 = new ImageView(eyeImage);
        eyeImageView1.setFitWidth(20);
        eyeImageView1.setFitHeight(20);

        visibilityButton.setGraphic(eyeImageView);
        visibilityButton1.setGraphic(eyeImageView1);

        visiblePasswordField.textProperty().bindBidirectional(oldPasswordField.textProperty());
        visiblePasswordField1.textProperty().bindBidirectional(newPasswordField.textProperty());

        visibilityButton.setOnAction(_ -> {
            isPasswordVisible = !isPasswordVisible;

            if (isPasswordVisible) {
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);
                oldPasswordField.setVisible(false);
                oldPasswordField.setManaged(false);
                eyeImageView.setImage(eyeSlashImage);
            } else {
                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);
                oldPasswordField.setVisible(true);
                oldPasswordField.setManaged(true);
                eyeImageView.setImage(eyeImage);
            }
        });
        visibilityButton1.setOnAction(_ -> {
            isPasswordVisible1 = !isPasswordVisible1;

            if (isPasswordVisible1) {
                visiblePasswordField1.setVisible(true);
                visiblePasswordField1.setManaged(true);
                newPasswordField.setVisible(false);
                newPasswordField.setManaged(false);
                eyeImageView1.setImage(eyeSlashImage);
            } else {
                visiblePasswordField1.setVisible(false);
                visiblePasswordField1.setManaged(false);
                newPasswordField.setVisible(true);
                newPasswordField.setManaged(true);
                eyeImageView1.setImage(eyeImage);
            }
        });
    }

    public void changeRememberStatus(ActionEvent ignoredActionEvent) {
        List<Integer> ids = rememberUser.getRememberedUserIds();
        if(rememberCheckBox.isSelected()) {
            if(! ids.contains(user.getId())) {
                if(ids.size() == 3) {
                    rememberCheckBox.setSelected(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Can't Add Account");
                    alert.setContentText("You can only add 3 accounts to remember");
                    alert.showAndWait();
                } else {
                    rememberUser.rememberUserId(user.getId());
                }
            }
        } else {
            if(ids.contains(user.getId())) {
                rememberUser.removeUserId(user.getId());
            }
        }
    }

    public void changePassword(ActionEvent ignoredactionEvent) {
        passwordAnchorPane.setVisible(true);
    }

    public void editUsername(ActionEvent ignoredactionEvent) {
        usernameAnchorPane.setVisible(true);
        usernameField.setText(user.getUsername());
    }

    public void editName(ActionEvent ignoredactionEvent) {
        nameAnchorPane.setVisible(true);
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
    }

    public void editAge(ActionEvent ignoredactionEvent) {
        ageAnchorPane.setVisible(true);
        ageField.setText(user.getAge() + "");
    }

    public void logOut(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText("By clicking 'OK' you will log out from your account");
        alert.setContentText("Do you want to log out?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    List<Integer> ids = rememberUser.getRememberedUserIds();
                    if(ids.isEmpty()) {
                        changeSceneToLogInPage(actionEvent);
                    } else {
                        changeSceneToUsersPage(actionEvent);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void changeProfilePicture(ActionEvent ignoredactionEvent) {
        profilePictureAnchorPane.setVisible(true);
        displayImage(user.getProfilePic(), newProfilePicImageView);
    }

    public void cancelUsernameAnchorPane(ActionEvent ignoredactionEvent) {
        usernameAnchorPane.setVisible(false);
    }

    public void submitUsername(ActionEvent ignoredactionEvent) {
        String username = usernameField.getText();
        if(username.length() < 5 || username.length() > 20) {
            if(username.isBlank())
                usernameErrorLabel.setText("Please enter username");
            else if(username.length() < 5)
                usernameErrorLabel.setText("Your username must be at least 5 characters");
            else
                usernameErrorLabel.setText("Your username can be maximum of 20 characters");
        } else {
            userDAO.updateUsername(user.getId(), username);
            usernameAnchorPane.setVisible(false);
            user = userDAO.findById(user.getId());
            usernameLabel.setText(user.getUsername());
        }
    }

    public void cancelNameAnchorPane(ActionEvent ignoredactionEvent) {
        nameAnchorPane.setVisible(false);
    }

    public void submitName(ActionEvent ignoredactionEvent) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        if(firstName.isBlank()|| lastName.isBlank()) {
            nameErrorLabel.setText("You have to fill both of the fields");
        } else {
            userDAO.updateName(user.getId(), firstName, lastName);
            nameAnchorPane.setVisible(false);
            user = userDAO.findById(user.getId());
            nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        }
    }

    public void cancelAgeAnchorPane(ActionEvent ignoredactionEvent) {
        ageAnchorPane.setVisible(false);
    }

    public void submitAge(ActionEvent ignoredactionEvent) {
        if(ageField.getText().isBlank()) {
            ageErrorLabel.setText("Please enter age");
        } else {
            userDAO.updateAge(user.getId(), Integer.parseInt(ageField.getText()));
            ageAnchorPane.setVisible(false);
            user = userDAO.findById(user.getId());
            ageLabel.setText("Age: " + user.getAge());
        }
    }

    public void cancelPasswordAnchorPane(ActionEvent ignoredactionEvent) {
        passwordAnchorPane.setVisible(false);
    }

    public void submitPassword(ActionEvent ignoredactionEvent) {
        String oldPassword = oldPasswordField.getText();
        String password = user.getPassword();
        if(! oldPassword.equals(password))
            passwordErrorLabel.setText("Wrong Password");
        else {
            String newPassword = newPasswordField.getText();
            if(newPassword.length() < 8 || newPassword.length() > 20) {
                if(newPassword.isBlank())
                    passwordErrorLabel.setText("Please enter new password");
                else if(password.length() < 8)
                    passwordErrorLabel.setText("Your password must be at least 8 characters");
                else
                    passwordErrorLabel.setText("Your username can be maximum of 20 characters");
            } else {
                userDAO.updatePassword(user.getId(), newPassword);
                passwordAnchorPane.setVisible(false);
                user = userDAO.findById(user.getId());
            }
        }
    }

    public void cancelProfilePicAnchorPane(ActionEvent ignoredactionEvent) {
        profilePictureAnchorPane.setVisible(false);
    }

    public void submitProfilePicture(ActionEvent ignoredactionEvent) {
        userDAO.updateProfilePic(user.getId(), getProfilePicBytes());
        profilePictureAnchorPane.setVisible(false);
        user = userDAO.findById(user.getId());
        displayImage(user.getProfilePic(), profilePicImageView);
    }

    public void handleImageViewClick() {
        Stage stage = (Stage) newProfilePicImageView.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Book Cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString(),
                    newProfilePicImageView.getFitWidth(),
                    newProfilePicImageView.getFitHeight(),
                    true, true);
            newProfilePicImageView.setImage(image);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), newProfilePicImageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
    }

    private byte[] getProfilePicBytes() {
        Image image = newProfilePicImageView.getImage();

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
                File tempFile = File.createTempFile("profile_pic", ".png");
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
}
