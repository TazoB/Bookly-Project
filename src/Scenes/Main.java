package Scenes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    public static int userId;
    public static int collectionId;
    public static int bookId;
    public static int characterId;

    public static void main(String[] ignoredArgs) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Scenes/FirstPage/firstPage.fxml")));

        stage.setScene(new Scene(root));
        stage.setTitle("BOOKLY");
        stage.getIcons().add(
                new Image("Style/Images/books-icon.png")
        );
        stage.setResizable(false);
        stage.show();
    }
}