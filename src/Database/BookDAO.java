package Database;

import Model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BookDAO {
    private static final DatabaseConnectionManager dbcm = new DatabaseConnectionManager();

    public void insert(Book book) {
        String INSERT = "INSERT INTO Books (title, author, cover, status, number_of_pages, genre, rating, description, current_page, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(INSERT)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setBytes(3, book.getCover());
            ps.setString(4, book.getStatus());
            ps.setInt(5, book.getNumberOfPages());
            ps.setString(6, book.getGenre());
            ps.setInt(7, book.getRating());
            ps.setString(8, book.getDescription());
            ps.setInt(9, book.getCurrentPage());
            ps.setDate(10, (book.getStartDate() == null) ? null : Date.valueOf(book.getStartDate()));
            ps.setDate(11, (book.getEndDate() == null) ? null : Date.valueOf(book.getEndDate()));
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Book findById(int id) {
        String FIND_BY_ID = "SELECT * FROM Books WHERE book_id = ?";
        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Book(
                        id,
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBytes(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getInt(8),
                        rs.getString(9),
                        rs.getShort(10),
                        (rs.getDate(11) == null) ? null : rs.getDate(11).toLocalDate(),
                        (rs.getDate(12) == null) ? null : rs.getDate(12).toLocalDate(),
                        rs.getTimestamp(13)
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String FIND_ALL = "SELECT * FROM Books";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_ALL)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getBytes(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getInt(8),
                        rs.getString(9),
                        rs.getInt(10),
                        (rs.getDate(11) == null) ? null : rs.getDate(11).toLocalDate(),
                        (rs.getDate(12) == null) ? null : rs.getDate(12).toLocalDate(),
                        rs.getTimestamp(13)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    public void delete(int id) {
        String DELETE = "DELETE FROM Books WHERE book_id = ?";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(Book book) {
        String UPDATE = """
                UPDATE books
                SET title = ?,
                author = ?,
                cover = ?,
                status = ?,
                number_of_pages = ?,
                genre = ?,
                rating = ?,
                description = ?,
                current_page = ?,
                start_date = ?,
                end_date = ?
                WHERE book_id = ?;
                """;

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setBytes(3, book.getCover());
            ps.setString(4, book.getStatus());
            ps.setInt(5, book.getNumberOfPages());
            ps.setString(6, book.getGenre());
            ps.setInt(7, book.getRating());
            ps.setString(8, book.getDescription());
            ps.setInt(9, book.getCurrentPage());
            ps.setDate(10, (book.getStartDate() == null) ? null : Date.valueOf(book.getStartDate()));
            ps.setDate(11, (book.getEndDate() == null) ? null : Date.valueOf(book.getEndDate()));
            ps.setInt(12, book.getId());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
