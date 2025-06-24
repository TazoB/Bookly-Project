package Scenes.MainPage;

import Database.ConnectorDAO;
import Model.Book;
import Model.Collection;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static Scenes.AddBookPage.AddBookPageController.addHoverAnimation;

public class MainPageController extends SceneChanger implements Initializable {
    @FXML
    public AnchorPane mainAnchorPane;
    @FXML
    public ImageView allBooksImageView;
    @FXML
    public ImageView finishedImageView;
    @FXML
    public ImageView readingImageView;
    @FXML
    public ImageView toReadImageView;
    @FXML
    public Label numOfBooks1;
    @FXML
    public Label numOfBooks2;
    @FXML
    public Label numOfBooks3;
    @FXML
    public Label numOfBooks4;
    @FXML
    private Label booklyLabel;
    public int userId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userId = Main.userId;
        fadeTransition();
        loadCollections();
    }

    private void loadCollections() {
        List<Collection> collections = new ConnectorDAO().getCollectionsInUser(Main.userId);
        int number = collections.size() - 4;


        List<ImageView> imageViews = new ArrayList<>(List.of(allBooksImageView, finishedImageView, readingImageView, toReadImageView));
        List<Label> labels = new ArrayList<>(List.of(numOfBooks1, numOfBooks2, numOfBooks3, numOfBooks4));

        for (int i = 0; i < number; i++) {
            createNodes(i, imageViews);
        }

        for (int i = 0; i < collections.size(); i++) {
            int id = collections.get(i).getId();
            List<Book> books = new ConnectorDAO().getBooksInCollection(id);

            if(books.isEmpty()) {
                imageViews.get(i).setImage(new Image(Objects.requireNonNull(getClass().getResource("/Style/Images/BAS901293212312BAS.png")).toExternalForm()));
            }
            else {
                byte[] cover = books.getLast().getCover();
                displayImage(cover, imageViews.get(i));
                if(i < 4) {
                    int size = books.size();
                    labels.get(i).setText(size + " Books");
                }
            }
            addHoverAnimation(imageViews.get(i), 1.05, 1.05, 1, 1);

            int finalI = i;
            imageViews.get(i).setOnMouseClicked(mouseEvent -> {
                Main.collectionId = collections.get(finalI).getId();
                changeSceneToCollectionPageME(mouseEvent);
            });
        }
    }

    private void fadeTransition() {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), booklyLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public static int getLayoutX(int num) {
        num %= 4;
        return switch (num) {
            case 0 -> 31;
            case 1 -> 237;
            case 2 -> 443;
            case 3 -> 649;
            default -> num;
        };
    }

    public static int getLayoutY(int num) {
        num /= 4;
        return 48 + 322 * (num + 1);
    }

    public static void displayImage(byte[] imageData, ImageView imageView) {
        if (imageData != null && imageData.length > 0) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            Image image = new Image(inputStream);

            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            double originalWidth = image.getWidth();
            double originalHeight = image.getHeight();

            double scaleX = 164 / originalWidth;
            double scaleY = 208 / originalHeight;

            double scaledWidth, scaledHeight;
            if (scaleX < scaleY) {
                scaledWidth = 164;
                scaledHeight = originalHeight * scaleX;
            } else {
                scaledWidth = originalWidth * scaleY;
                scaledHeight = 208;
            }

            double offsetX = (164 - scaledWidth) / 2;
            double offsetY = (208 - scaledHeight) / 2;

            imageView.setTranslateX(offsetX);
            imageView.setTranslateY(offsetY);
        }
    }

    private void createNodes(int i, List<ImageView> imageViews) {
        int x = getLayoutX(i);
        int y = getLayoutY(i);

        ConnectorDAO cd = new ConnectorDAO();
        Collection c = cd.getCollectionsInUser(Main.userId).get(4 + i);

        ImageView imageView = new ImageView("/Style/Images/BAS901293212312BAS.png");
        imageView.setFitWidth(164);
        imageView.setFitHeight(208);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setX(x);
        imageView.setY(y);
        imageView.setVisible(true);
        addHoverAnimation(imageView, 1.05, 1.05, 1, 1);

        if(! c.getDescription().isEmpty()) {
            Tooltip tooltip = new Tooltip(c.getDescription());
            Tooltip.install(imageView, tooltip);
            tooltip.setStyle("-fx-font-size: 14px;");
            tooltip.setShowDelay(Duration.millis(100));
            tooltip.setHideDelay(Duration.millis(200));
        }

        Label label = new Label(c.getName());
        label.setVisible(true);
        label.setLayoutX(x);
        label.setLayoutY(y + 210);
        label.setPrefWidth(164);
        label.setPrefHeight(70);
        label.getStyleClass().add("title-label");
        label.setWrapText(true);

        int numberOfBooks = new ConnectorDAO().getBooksInCollection(c.getId()).size();
        Label countLabel = new Label(numberOfBooks + " Books");
        countLabel.setVisible(true);
        countLabel.setPrefWidth(164);
        countLabel.setAlignment(Pos.CENTER);
        countLabel.setLayoutX(x);
        countLabel.setLayoutY(label.getLayoutY() + 63);
        countLabel.getStyleClass().add("count-label");

        mainAnchorPane.getChildren().addAll(imageView, label, countLabel);
        imageViews.add(imageView);
    }
}