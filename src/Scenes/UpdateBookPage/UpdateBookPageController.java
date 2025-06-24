package Scenes.UpdateBookPage;

import Database.BookDAO;
import Database.CollectionDAO;
import Database.ConnectorDAO;
import Model.Book;
import Model.Collection;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.embed.swing.SwingFXUtils;
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
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static Scenes.MainPage.MainPageController.displayImage;

public class UpdateBookPageController extends SceneChanger implements Initializable {
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
    public ImageView bookCoverImageView;
    private static Book book;
    private int rating;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        book = new BookDAO().findById(Main.bookId);
        rating = book.getRating();
        setImageViewProperties();
        setChoiceBoxItems();
        setStarProperties();
        setPagesFieldProperties();
        setFieldsProperties();
    }

    private void setFieldsProperties() {
        bookTitleField.setText(book.getTitle());
        bookAuthorField.setText(book.getAuthor());
        pagesField.setText(book.getNumberOfPages() + "");
        genreField.setText(book.getGenre());

        String status = book.getStatus();
        if(status.equals("Finished")) {
            finishedRadioButton.setSelected(true);
            appearRating();
        }
        else if(status.equals("Reading")){
            readingRadioButton.setSelected(true);
            disappearRating();
        }
        else {
            toReadRadioButton.setSelected(true);
            toReadRadioOnAction();
        }

        descriptionField.setText(book.getDescription());

        rating = book.getRating();
        List<SVGPath> stars = List.of(star1, star2, star3, star4, star5);
        for(int i = 0; i < rating; i++) {
            stars.get(i).setFill(Color.YELLOW);
        }
        endDatePicker.setValue(book.getEndDate());
        startDatePicker.setValue(book.getStartDate());
        currentPageField.setText(book.getCurrentPage() + "");
        displayImage(book.getCover(), bookCoverImageView);
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

    public void editBookData(ActionEvent actionEvent) throws Exception {
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        String numberOfPages = pagesField.getText();
        String currentPage = currentPageField.getText();

        if(title.isBlank() || author.isBlank() || numberOfPages.isBlank() || (readingRadioButton.isSelected() && currentPage.isBlank())) {
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
        } else {
            String status;
            if(finishedRadioButton.isSelected()) status = "Finished";
            else if(readingRadioButton.isSelected()) status = "Reading";
            else status = "To Read";

            int oldCollectionId = new CollectionDAO().findByName(book.getStatus()).getId();
            int newCollectionId = new CollectionDAO().findByName(status).getId();
            new ConnectorDAO().changeBookCollection(book.getId(), oldCollectionId, newCollectionId);

            Book newBook = new Book(
                    book.getId(),
                    bookTitleField.getText(),
                    bookAuthorField.getText(),
                    getCoverBytes(),
                    status,
                    Integer.parseInt(pagesField.getText()),
                    genreField.getText(),
                    rating,
                    descriptionField.getText(),
                    Integer.parseInt(currentPageField.getText()),
                    startDatePicker.getValue(),
                    endDatePicker.getValue(),
                    book.getDateCreated()
            );
            new BookDAO().update(newBook);
            changeSceneToBookPage(actionEvent);
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
            System.out.println("Image is from memory, not a file path.");
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
