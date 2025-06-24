package Scenes.CollectionPage;

import Database.ConnectorDAO;
import Model.Book;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static Scenes.AddBookPage.AddBookPageController.addHoverAnimation;
import static Scenes.MainPage.MainPageController.getLayoutX;
import static Scenes.MainPage.MainPageController.displayImage;

public class CollectionPageController extends SceneChanger implements Initializable {
    @FXML
    public AnchorPane mainAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadBooks();
    }

    public void loadBooks() {
        List<Book> books = new ConnectorDAO().getBooksInCollection(Main.collectionId);
        for(int i = 0; i < books.size(); i++) {
            createNodes(books, i);
        }
    }

    private void createNodes(List<Book> books, int i) {
        int x = getLayoutX(i);
        int y = getLayoutY(i);
        Book book = books.get(i);
        ImageView imageView = new ImageView();
        displayImage(book.getCover(), imageView);
        imageView.setFitWidth(164);
        imageView.setFitHeight(208);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setVisible(true);
        addHoverAnimation(imageView, 1.05, 1.05, 1, 1);
        imageView.setOnMouseClicked(mouseEvent -> {
            Main.bookId = book.getId();
            changeSceneToBookPageME(mouseEvent);
        });

        Tooltip tooltip = new Tooltip(book.getTitle() + "\n" + book.getAuthor());
        Tooltip.install(imageView, tooltip);
        tooltip.setStyle("-fx-font-size: 14px;");
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.setHideDelay(Duration.millis(200));

        Label label = new Label(book.getTitle());
        label.setVisible(true);
        label.setLayoutX(x);
        label.setLayoutY(y + 210);
        label.setPrefWidth(164);
        label.setPrefHeight(70);
        label.getStyleClass().add("title-label");
        label.setWrapText(true);

        mainAnchorPane.getChildren().addAll(imageView, label);
    }

    private int getLayoutY(int num) {
        num /= 4;
        return 48 + 322 * (num);
    }
}
