package Scenes.AddCollectionPage;
import Scenes.Main;
import Scenes.SceneChanger;
import Database.*;
import Model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class AddCollectionPageController extends SceneChanger {
    @FXML
    public TextField collectionName;
    @FXML
    public TextArea description;
    @FXML
    public Label errorMessage;

    @Override
    public void changeSceneToMainPage(ActionEvent actionEvent) throws Exception {
        if(collectionName.getText().isEmpty()) {
            super.goToMyBooks(actionEvent);
        } else {
            super.changeSceneToMainPage(actionEvent);
        }
    }

    public void submitCollectionData(ActionEvent actionEvent) throws Exception {
        CollectionDAO cd = new CollectionDAO();
        String name = collectionName.getText();

        ConnectorDAO c = new ConnectorDAO();
        List<Collection> collections = c.getCollectionsInUser(Main.userId);

        boolean containsCollection = false;
        for (Collection collection : collections) {
            if (collection.getName().equals(name)) {
                containsCollection = true;
                break;
            }
        }

        if(! containsCollection) {
            cd.insert(
                    new Collection(
                            collectionName.getText(),
                            description.getText()
                    )
            );
            int id = cd.findAll().getLast().getId();
            new ConnectorDAO().addCollectionToUser(id, Main.userId);
            goToMyBooks(actionEvent);
        } else {
            errorMessage.setVisible(true);
        }
    }
}