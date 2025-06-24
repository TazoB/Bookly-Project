package Scenes.FirstPage;

import Scenes.*;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class firstPageController extends SceneChanger implements Initializable {
    @FXML
    private Label mainLabel;

    @FXML
    private Button enterButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setProperties();
    }

    private void setProperties() {
        FadeTransition fade = new FadeTransition(Duration.seconds(2), mainLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        ScaleTransition scale = new ScaleTransition(Duration.seconds(1.5), enterButton);
        scale.setFromX(0.9);
        scale.setFromY(0.9);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.play();
    }

    @Override
    public void changeSceneToUsersPage(ActionEvent actionEvent) throws Exception {
        List<Integer> ids = new RememberUser().getRememberedUserIds();
        if(! ids.isEmpty()) {
            super.changeSceneToUsersPage(actionEvent);
        } else {
            super.changeSceneToLogInPage(actionEvent);
        }
    }
}
