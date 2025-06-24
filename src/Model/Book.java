package Model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Book {
    private int id;
    private String title;
    private String author;
    private byte[] cover;
    private String status;
    private int numberOfPages;
    private String genre;
    private int rating;
    private String description;
    private int currentPage;
    private LocalDate startDate;
    private LocalDate endDate;
    private Timestamp dateCreated;

    public Book(int id, String title, String author, byte[] cover, String status, int numberOfPages, String genre, int rating, String description, int currentPage, LocalDate startDate, LocalDate endDate, Timestamp dateCreated) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.status = status;
        this.numberOfPages = numberOfPages;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
        this.currentPage = currentPage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateCreated = dateCreated;
    }

    public Book(String title, String author, byte[] cover, String status, int numberOfPages, String genre, int rating, String description, int currentPage, LocalDate startDate, LocalDate endDate, Timestamp dateCreated) {
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.status = status;
        this.numberOfPages = numberOfPages;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
        this.currentPage = currentPage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateCreated = dateCreated;
    }

    public Book(String title, String author, byte[] cover, String status, int numberOfPages, String genre, int rating, String description, int currentPage, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.status = status;
        this.numberOfPages = numberOfPages;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
        this.currentPage = currentPage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
