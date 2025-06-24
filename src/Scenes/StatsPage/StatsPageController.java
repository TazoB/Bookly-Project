package Scenes.StatsPage;

import Database.ConnectorDAO;
import Database.UserDAO;
import Model.Book;
import Model.User;
import Scenes.Main;
import Scenes.SceneChanger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static Scenes.MainPage.MainPageController.displayImage;

public class StatsPageController extends SceneChanger implements Initializable {
    @FXML
    public Label totalBooks;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public ImageView cover1;
    @FXML
    public ImageView cover2;
    @FXML
    public ImageView cover3;
    @FXML
    public ImageView cover4;
    @FXML
    public Label title1;
    @FXML
    public Label title2;
    @FXML
    public Label title3;
    @FXML
    public Label title4;
    @FXML
    public ImageView imageView1;
    @FXML
    public ImageView imageView2;
    @FXML
    public ImageView imageView3;
    @FXML
    public ImageView imageView4;
    @FXML
    public Label label1;
    @FXML
    public Label label2;
    @FXML
    public Label label3;
    @FXML
    public Label label4;
    @FXML
    public TextField genreField;
    private int initialized = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<User> users = new UserDAO().findAll();
        List<Integer> totalBooks = new ArrayList<>();

        for (User user : users) {
            totalBooks.add(new ConnectorDAO().getBooksInUser(user.getId()).size());
        }

        for (int i = 0; i < totalBooks.size() - 1; i++) {
            for (int j = 0; j < totalBooks.size() - i - 1; j++) {
                if (totalBooks.get(j) < totalBooks.get(j+1)) {
                    int t = totalBooks.get(j);
                    totalBooks.set(j, totalBooks.get(j+1));
                    totalBooks.set(j+1, t);

                    User temp = users.get(j);
                    users.set(j, users.get(j+1));
                    users.set(j+1, temp);
                }
            }
        }
        createNodes(users, totalBooks);
        setFavouriteGenre();
    }

    private void createNodes(List<User> users, List<Integer> totalBooks) {
        User user = new UserDAO().findById(Main.userId);
        boolean userIsTop10 = false;
        for(int i = 0; i < users.size(); i++) {
            if(i == 10) break;
            int y = getY(i);
            Label numeral = new Label((i+1) + ".");
            numeral.setLayoutX(110);
            numeral.setLayoutY(y);
            numeral.getStyleClass().add("leaderboard-label");

            Label username = new Label(users.get(i).getUsername());
            username.setLayoutX(183);
            username.setLayoutY(y);
            username.getStyleClass().add("leaderboard-label");

            Label allBooks = new Label(totalBooks.get(i) + "");
            allBooks.setLayoutX(620);
            allBooks.setLayoutY(y);
            allBooks.getStyleClass().add("leaderboard-label");

            if(user.getId() == users.get(i).getId()) {
                userIsTop10 = true;
                this.totalBooks.setText("Total Books: " + allBooks.getText());
                numeral.setTextFill(Color.DARKRED);
                username.setTextFill(Color.DARKRED);
                allBooks.setTextFill(Color.DARKRED);
            }
            anchorPane.getChildren().addAll(numeral, username, allBooks);
        }
        if(! userIsTop10) {
            int y = getY(10);
            int num = 0;

            for(int i = 0; i < users.size(); i++) {
                if(users.get(i).getId() == user.getId()) {
                    num = i+1;
                }
            }

            Label numeral = new Label((num) + ".");
            numeral.setLayoutX(110);
            numeral.setLayoutY(y);
            numeral.getStyleClass().add("leaderboard-label");

            Label username = new Label(users.get(num-1).getUsername());
            username.setLayoutX(183);
            username.setLayoutY(y);
            username.getStyleClass().add("leaderboard-label");

            Label allBooks = new Label(totalBooks.get(num-1) + "");
            allBooks.setLayoutX(620);
            allBooks.setLayoutY(y);
            allBooks.getStyleClass().add("leaderboard-label");
            this.totalBooks.setText("Total Books: " + allBooks.getText());
            numeral.setTextFill(Color.DARKRED);
            username.setTextFill(Color.DARKRED);
            allBooks.setTextFill(Color.DARKRED);
            anchorPane.getChildren().addAll(numeral, username, allBooks);
        }
    }

    private int getY(int index) {
        return 1000 + 50 * index;
    }

    private void setFavouriteGenre() {
        Map<String, Integer> genres = new HashMap<>();
        List<Book> books = new ConnectorDAO().getBooksInUser(Main.userId);
        for (Book book : books) {
            String genre = book.getGenre();
            if(genre == null) continue;
            genre = genre.toLowerCase();
            if (genres.containsKey(genre))
                genres.put(genre, genres.get(genre) + 1);
            else
                genres.put(genre, 1);
        }
        genres.remove(null);
        String favouriteGenre = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : genres.entrySet()) {
            if (entry.getValue() > maxCount) {
                favouriteGenre = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        fetchBookData("subject:" + favouriteGenre);
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
            int bookCount = Math.min(4, items.length());

            for (int i = 0; i < bookCount; i++) {
                JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");

                String title = volumeInfo.optString("title", "Unknown Title");
                String author = volumeInfo.optJSONArray("authors") != null ?
                        volumeInfo.getJSONArray("authors").optString(0, "Unknown Author") : "Unknown Author";
                String imageUrl = volumeInfo.optJSONObject("imageLinks") != null ?
                        volumeInfo.getJSONObject("imageLinks").optString("thumbnail", "") : "";

                int finalI = i;
                byte[] coverImageData = downloadImage(imageUrl);

                Platform.runLater(() -> {
                    if(initialized < 4) {
                        switch (finalI) {
                            case 0 -> updateBook(title, author, coverImageData, 0, true);
                            case 1 -> updateBook(title, author, coverImageData, 1, true);
                            case 2 -> updateBook(title, author, coverImageData, 2, true);
                            case 3 -> updateBook(title, author, coverImageData, 3, true);
                        }
                        initialized += 1;
                    } else {
                        switch (finalI) {
                            case 0 -> updateBook(title, author, coverImageData, 0, false);
                            case 1 -> updateBook(title, author, coverImageData, 1, false);
                            case 2 -> updateBook(title, author, coverImageData, 2, false);
                            case 3 -> updateBook(title, author, coverImageData, 3, false);
                        }
                    }
                });
            }
        } else {
            System.out.println("No books found.");
        }
    }

    private void updateBook(String title, String author, byte[] coverImageData, int index, boolean status) {
        List<Label> titles;
        List<ImageView> covers;
        if(status) {
            titles = List.of(title1, title2, title3, title4);
            covers = List.of(cover1, cover2, cover3, cover4);
        } else {
            titles = List.of(label1, label2, label3, label4);
            covers = List.of(imageView1, imageView2, imageView3, imageView4);
        }
        titles.get(index).setText(title);
        displayImage(coverImageData, covers.get(index));
        createTooltip(title, author, covers.get(index));
    }

    private void createTooltip(String title, String author, ImageView cover) {
        Tooltip tooltip = new Tooltip(title + "\n" + author);
        Tooltip.install(cover, tooltip);
        tooltip.setStyle("-fx-font-size: 14px;");
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.setHideDelay(Duration.millis(200));
    }

    public byte[] downloadImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            String contentType = connection.getContentType();
            if (responseCode != 200 || (contentType != null && !contentType.startsWith("image"))) {
                return null;
            }

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

    public void searchBook(ActionEvent ignoredactionEvent) {
        String genre = genreField.getText();
        if(! genre.isBlank()) {
            fetchBookData("subject:" + genre.toLowerCase());
        }
    }
}