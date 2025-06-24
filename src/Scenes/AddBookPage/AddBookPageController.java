package Scenes.AddBookPage;
import Database.*;
import Model.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import Scenes.Main;
import Scenes.SceneChanger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddBookPageController extends SceneChanger implements Initializable {
    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public SVGPath star1;
    @FXML
    public SVGPath star2;
    @FXML
    public SVGPath star3;
    @FXML
    public SVGPath star4;
    @FXML
    public SVGPath star5;
    @FXML
    public RadioButton finishedRadioButton;
    @FXML
    public RadioButton readingRadioButton;
    @FXML
    public RadioButton toReadRadioButton;
    @FXML
    public TextField bookTitleField;
    @FXML
    public TextField bookAuthorField;
    @FXML
    public TextField pagesField;
    @FXML
    public Label ratingLabel;
    @FXML
    public TextField isbnField;
    @FXML
    public TextField currentPageField;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public Label optionalLabel;
    @FXML
    public Label optionalLabel1;
    @FXML
    public DatePicker endDatePicker;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField genreField;
    @FXML
    public Label coverErrorLabel;
    @FXML
    private ImageView bookCoverImageView;
    private int rating;
    private String genre;

    @Override
    public void changeSceneToMainPage(ActionEvent actionEvent) throws Exception {
        if(isAllEmpty())
            super.goToMyBooks(actionEvent);
        else
            super.changeSceneToMainPage(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setImageViewProperties();
        setChoiceBoxItems();
        setStarProperties();
        setPagesFieldProperties();
    }

    public void appearRating() {
        setReadingVisibility(false);
        setFinishedVisibility(true);
    }

    public void disappearRating() {
        setFinishedVisibility(false);
        setReadingVisibility(true);
    }

    public void toReadRadioOnAction() {
        setFinishedVisibility(false);
        setReadingVisibility(false);
    }

    @FXML
    public void onSearchClicked() {
        String query = isbnField.getText().trim();
        if (!query.isEmpty()) {
            fetchBookData(query);
        }
    }

    public void fetchBookData(String searchQuery) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String encoded = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.googleapis.com/books/v1/volumes?q=" + encoded))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(this::parseBookData)
                    .exceptionally(e -> {
                        System.out.println("Error fetching book data: " + e.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void parseBookData(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);
        JSONArray items = json.optJSONArray("items");

        if (items != null && !items.isEmpty()) {
            JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

            String title = volumeInfo.optString("title", "");
            String author = volumeInfo.optJSONArray("authors") != null ?
                    volumeInfo.getJSONArray("authors").optString(0, "") : "";
            String genre = volumeInfo.optJSONArray("categories") != null ?
                    volumeInfo.getJSONArray("categories").optString(0, "") : "";
            String imageUrl = volumeInfo.optJSONObject("imageLinks") != null ?
                    volumeInfo.getJSONObject("imageLinks").optString("thumbnail", "") : "";

            byte[] coverImageData = downloadImage(imageUrl);

            Platform.runLater(() -> {
                bookTitleField.setText(title);
                bookAuthorField.setText(author);
                this.genre = genre;

                if (coverImageData != null && coverImageData.length > 0) {
                    Image image = new Image(new ByteArrayInputStream(coverImageData));
                    bookCoverImageView.setImage(image);
                } else {
                    bookCoverImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/Style/Images/BAS901293212312BAS.png")).toExternalForm()));
                }
            });
        }
    }


    public byte[] downloadImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.out.println("Image URL is null or empty.");
            return null;
        }

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                return outputStream.toByteArray();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void handleImageViewClick() {
        Stage stage = (Stage) bookCoverImageView.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Book Cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString(),
                    bookCoverImageView.getFitWidth(),
                    bookCoverImageView.getFitHeight(),
                    true, true);

            bookCoverImageView.setImage(image);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), bookCoverImageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
    }

    public void submitBookData(ActionEvent actionEvent) throws Exception {
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        String numberOfPages = pagesField.getText();
        String currentPage = currentPageField.getText();
        String coverURL = bookCoverImageView.getImage().getUrl();
        if(coverURL == null) coverURL = "blank";

        if(title.isBlank() || author.isBlank() || numberOfPages.isBlank() || (readingRadioButton.isSelected() && currentPage.isBlank()) || coverURL.endsWith("BAS901293212312BAS.png")) {
            if(title.isBlank())
                bookTitleField.setStyle("-fx-border-color: red;");
            else
                bookTitleField.setStyle("-fx-border-color: black;");
            if(author.isBlank())
                bookAuthorField.setStyle("-fx-border-color: red;");
            else
                bookAuthorField.setStyle("-fx-border-color: black;");
            if(numberOfPages.isBlank())
                pagesField.setStyle("-fx-border-color: red;");
            else
                pagesField.setStyle("-fx-border-color: black;");
            if(readingRadioButton.isSelected()) {
                if(currentPage.isBlank())
                    currentPageField.setStyle("-fx-border-color: red;");
                else
                    currentPageField.setStyle("-fx-border-color: black;");
            }
            coverErrorLabel.setVisible(coverURL.endsWith("BAS901293212312BAS.png"));
        } else {
            BookDAO bd = new BookDAO();
            ConnectorDAO cnd = new ConnectorDAO();

            byte[] cover;
            cover = getCoverBytes();

            String status;
            int page = 0;
            if(finishedRadioButton.isSelected()) {
                status = "Finished";
                page = Integer.parseInt(pagesField.getText());
            }
            else if(toReadRadioButton.isSelected()) {
                status = "To Read";
            }
            else {
                status = "Reading";
                page = Integer.parseInt(currentPageField.getText());
            }
            genre = (genreField.getText().isBlank()) ? null : genreField.getText();

            bd.insert(
                    new Book(
                            bookTitleField.getText(),
                            bookAuthorField.getText(),
                            cover,
                            status,
                            Integer.parseInt(pagesField.getText()),
                            genre,
                            rating,
                            descriptionField.getText(),
                            page,
                            startDatePicker.getValue(),
                            endDatePicker.getValue()
                    )
            );

            int allBooksId = cnd.findCollectionByName("All Books").getId();
            int finishedBooksId = cnd.findCollectionByName("Finished").getId();
            int readingBooksId = cnd.findCollectionByName("Reading").getId();
            int toReadId = cnd.findCollectionByName("To Read").getId();

            int bookId = bd.findAll().getLast().getId();
            cnd.addBookToCollection(bookId, allBooksId);

            if(finishedRadioButton.isSelected()) cnd.addBookToCollection(bookId, finishedBooksId);
            else if(toReadRadioButton.isSelected()) cnd.addBookToCollection(bookId, toReadId);
            else cnd.addBookToCollection(bookId, readingBooksId);


            String collection = choiceBox.getValue();
            if(! collection.equals("No Collections")) {
                CollectionDAO cd = new CollectionDAO();
                List<Collection> collections = cd.findAll()
                        .stream()
                        .filter(collection1 -> collection1.getName().equals(collection))
                        .toList();
                cnd.addBookToCollection(bookId, collections.getLast().getId());
            }

            goToMyBooks(actionEvent);
        }
    }

    private byte[] getCoverBytes() {
        Image image = bookCoverImageView.getImage();

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

    private void changeColorToYellow(SVGPath star) {
        star.setFill(Color.YELLOW);
    }
    private void changeColorToWhite(SVGPath star) {
        star.setFill(Color.WHITE);
    }

    private boolean isAllEmpty() {
        if(! bookTitleField.getText().isEmpty()) return false;
        if(! bookAuthorField.getText().isEmpty()) return false;
        if(! pagesField.getText().isEmpty()) return false;
        if(choiceBox.getValue() != null) return false;
        return !star1.getFill().equals(Color.YELLOW);
    }

    private void setFinishedVisibility(boolean status) {
        ratingLabel.setVisible(status);
        star1.setVisible(status);
        star2.setVisible(status);
        star3.setVisible(status);
        star4.setVisible(status);
        star5.setVisible(status);
        endDatePicker.setVisible(status);
        optionalLabel1.setVisible(status);
    }

    private void setReadingVisibility(boolean status) {
        currentPageField.setVisible(status);
        startDatePicker.setVisible(status);
        optionalLabel.setVisible(status);
    }

    private void setImageViewProperties() {
        Rectangle clip = new Rectangle();
        clip.setWidth(bookCoverImageView.getFitWidth());
        clip.setHeight(bookCoverImageView.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        bookCoverImageView.setClip(clip);
        addHoverAnimation(bookCoverImageView, 1.05, 1.05, 1, 1);
    }

    private void setChoiceBoxItems() {
        List<String> items = new ArrayList<>();
        List<Collection> collections = new ConnectorDAO().getCollectionsInUser(Main.userId);

        for(int i = 4; i < collections.size(); i++) {
            items.add(collections.get(i).getName());
        }

        choiceBox.getItems().add("No Collections");
        choiceBox.getItems().addAll(items);
    }

    private void setStarProperties() {
        List<Node> stars = List.of(star1, star2, star3, star4, star5);
        for (int i = 0; i < stars.size(); i++) {
            addHoverAnimation(stars.get(i), 0.7, 0.8, 0.6, 0.7);

            final int index = i;
            stars.get(i).setOnMouseClicked(_ -> {
                for (int j = 0; j < stars.size(); j++) {
                    if (j <= index) {
                        changeColorToYellow((SVGPath) stars.get(j));
                    } else {
                        changeColorToWhite((SVGPath) stars.get(j));
                    }
                }
                rating = index + 1;
            });
        }
    }

    private void setPagesFieldProperties() {
        setTextFormatter(pagesField);
        setTextFormatter(currentPageField);
    }

    public static void setTextFormatter(TextField field) {
        field.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        }));
    }

    public static void addHoverAnimation(Node node, double x1, double y1, double x2, double y2) {
        node.setOnMouseEntered(_ -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
            st.setToX(x1);
            st.setToY(y1);
            st.play();
        });
        node.setOnMouseExited(_ -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
            st.setToX(x2);
            st.setToY(y2);
            st.play();
        });
    }
}
