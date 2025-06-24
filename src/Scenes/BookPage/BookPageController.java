package Scenes.BookPage;

import Database.BookDAO;
import Database.CollectionDAO;
import Database.ConnectorDAO;
import Model.Book;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import static Scenes.AddBookPage.AddBookPageController.addHoverAnimation;
import static Scenes.AddBookPage.AddBookPageController.setTextFormatter;
import static Scenes.MainPage.MainPageController.displayImage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookPageController extends SceneChanger implements Initializable {
    @FXML
    public ImageView bookCoverImage;
    @FXML
    public Label bookTitleLabel;
    @FXML
    public Label bookAuthorLabel;
    @FXML
    public Label bookStatusLabel;
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
    public Label pagesReadLabel;
    @FXML
    public Button startReadingButton;
    @FXML
    public Button finishBookButton;
    @FXML
    public Button changePageButton;
    @FXML
    public Label genreLabel;
    @FXML
    public Button charactersButton;
    @FXML
    public Button descriptionButton;
    private static Book book;
    @FXML
    public AnchorPane ratingAnchorPane;
    @FXML
    public SVGPath rateStar1;
    @FXML
    public SVGPath rateStar2;
    @FXML
    public SVGPath rateStar3;
    @FXML
    public SVGPath rateStar4;
    @FXML
    public SVGPath rateStar5;
    private static int rating;
    @FXML
    public AnchorPane startReadingAnchorPane;
    @FXML
    public DatePicker startDate;
    @FXML
    public TextField currentPageField;
    @FXML
    public TextField changePageField;
    @FXML
    public AnchorPane changePageAnchorPane;
    @FXML
    public AnchorPane descriptionAnchorPane;
    @FXML
    public Label descriptionLabel;
    @FXML
    public AnchorPane mainAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        book = new BookDAO().findById(Main.bookId);
        displayImage(book.getCover(), bookCoverImage);
        bookTitleLabel.setText(book.getTitle());
        bookAuthorLabel.setText("by " + book.getAuthor());
        bookStatusLabel.setText(book.getStatus());
        if(! (book.getGenre() == null)) {
            genreLabel.setText("Genre: " + book.getGenre());
        }

        List<SVGPath> stars = new ArrayList<>(List.of(star1, star2, star3, star4, star5));
        if(book.getStatus().equals("Finished")) {
            startReadingButton.setVisible(false);
            finishBookButton.setVisible(false);
            changePageButton.setVisible(false);
            int rating = book.getRating();
            for (int i = 0; i < rating; i++) {
                stars.get(i).setFill(Color.YELLOW);
            }
            pagesReadLabel.setText(book.getNumberOfPages() + "/" + book.getNumberOfPages());
        } else if(book.getStatus().equals("Reading")) {
            stars.forEach(star -> star.setVisible(false));
            startReadingButton.setVisible(false);
            pagesReadLabel.setText(book.getCurrentPage() + "/" + book.getNumberOfPages());
        } else {
            stars.forEach(star -> star.setVisible(false));
            changePageButton.setVisible(false);
            pagesReadLabel.setText("0/" + book.getNumberOfPages());
        }
        setTextFormatter(currentPageField);
    }

    public void finishBook(ActionEvent ignoredactionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Finish Book");
        alert.setContentText("By this action you will change the status of the book from '" + book.getStatus() + "' to 'Finished'.");
        alert.setHeaderText("Click OK to make the change");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                setRateVisibility();
            }
        });
    }

    private void setRateVisibility() {
        ratingAnchorPane.setVisible(true);
        mainAnchorPane.setDisable(true);
        List<SVGPath> stars = new ArrayList<>(List.of(rateStar1, rateStar2, rateStar3, rateStar4, rateStar5));
        for (int i = 0; i < stars.size(); i++) {
            addHoverAnimation(stars.get(i), 1.1, 1, 1, 0.9);

            final int index = i;
            stars.get(i).setOnMouseClicked(_ -> {
                for (int j = 0; j < stars.size(); j++) {
                    if (j <= index) {
                        stars.get(j).setFill(Color.YELLOW);
                    } else {
                        stars.get(j).setFill(Color.WHITE);
                    }
                }
                rating = index + 1;
            });
        }
    }

    public void startReading(ActionEvent ignoredactionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Start Reading");
        alert.setContentText("By this action you will change the status of the book from 'To Read' to 'Reading'.");
        alert.setHeaderText("Click OK to make the change");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                startReadingAnchorPane.setVisible(true);
                mainAnchorPane.setDisable(true);
            }
        });
    }

    public void cancelRating(ActionEvent ignoredactionEvent) {
        ratingAnchorPane.setVisible(false);
        mainAnchorPane.setDisable(false);
    }

    public void cancelStartReading(ActionEvent ignoredactionEvent) {
        startReadingAnchorPane.setVisible(false);
        mainAnchorPane.setDisable(false);
    }

    public void submitRating(ActionEvent actionEvent) {
        book.setRating(rating);
        cancelRating(actionEvent);
        changeStatus("Finished", actionEvent);
    }

    public void submitStartReading(ActionEvent actionEvent) {
        book.setStartDate((startDate.getValue() == null) ? LocalDate.now() : startDate.getValue());
        book.setCurrentPage((currentPageField.getText() == null) ? 0 : Integer.parseInt(currentPageField.getText()));
        cancelStartReading(actionEvent);
        changeStatus("Reading", actionEvent);
    }

    private void changeStatus(String status, ActionEvent actionEvent) {
        int oldCollectionId = new CollectionDAO().findByName(book.getStatus()).getId();
        int newCollectionId = new CollectionDAO().findByName(status).getId();
        new ConnectorDAO().changeBookCollection(book.getId(), oldCollectionId, newCollectionId);
        book.setStatus(status);
        new BookDAO().update(book);
        try {
            goToMyBooks(actionEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeCurrentPage(ActionEvent ignoredactionEvent) {
        changePageAnchorPane.setVisible(true);
        changePageField.setText("");
        mainAnchorPane.setDisable(true);
    }

    public void cancelChangePage(ActionEvent ignoredactionEvent) {
        changePageAnchorPane.setVisible(false);
        mainAnchorPane.setDisable(false);
    }

    public void submitChangePage(ActionEvent actionEvent) {
        book.setCurrentPage(Integer.parseInt(changePageField.getText()));
        new BookDAO().update(book);
        cancelChangePage(actionEvent);
        pagesReadLabel.setText(book.getCurrentPage() + "/" + book.getNumberOfPages());
    }

    public void cancelDescription(ActionEvent ignoredactionEvent) {
        descriptionAnchorPane.setVisible(false);
        mainAnchorPane.setDisable(false);
    }

    public void descriptionPopUp(ActionEvent ignoredactionEvent) {
        String description = book.getDescription();
        descriptionAnchorPane.setVisible(true);
        mainAnchorPane.setDisable(true);
        if(description.isBlank())
            descriptionLabel.setText("No Description");
        else
            descriptionLabel.setText(book.getDescription());
    }

    public void deleteBook(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Book");
        alert.setContentText("By this action you will delete this book and lose the information of this book");
        alert.setHeaderText("Click OK to make the change");
        ImageView icon = new ImageView(
                new Image("/Style/Images/delete-icon.png")
        );
        icon.setFitHeight(30);
        icon.setFitWidth(30);
        alert.setGraphic(icon);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                BookDAO bd = new BookDAO();
                ConnectorDAO cd = new ConnectorDAO();
                bd.delete(book.getId());
                cd.deleteBook(book.getId());
                try {
                    goToMyBooks(actionEvent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void updateBook(ActionEvent actionEvent) throws Exception {
        Main.bookId = book.getId();
        super.changeSceneToUpdateBookPage(actionEvent);
    }
}
