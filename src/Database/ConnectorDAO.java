package Database;

import Model.Book;
import Model.Character;
import Model.Collection;
import Scenes.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectorDAO {
    private static final DatabaseConnectionManager dbcm = new DatabaseConnectionManager();

    public void addCollectionToUser(int collectionId, int userId) {
        String INSERT = "INSERT INTO Connector (user_id, collection_id) VALUES (?, ?)";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(INSERT)) {
            ps.setInt(1, userId);
            ps.setInt(2, collectionId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBookToCollection(int bookId, int collectionId) {
        String INSERT = "INSERT INTO Connector (user_id, collection_id, book_id) VALUES (?, ?, ?)";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(INSERT)) {
            ps.setInt(1, Main.userId);
            ps.setInt(2, collectionId);
            ps.setInt(3, bookId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCharacterToBook(int userId, int collectionId, int bookId, int characterId) {
        String INSERT = "INSERT INTO Connector (user_id, collection_id, book_id, character_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(INSERT)) {
            ps.setInt(1, userId);
            ps.setInt(2, collectionId);
            ps.setInt(3, bookId);
            ps.setInt(4, characterId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeBookCollection(int bookId, int oldCollectionId, int newCollectionId) {
        String UPDATE = """
                UPDATE Connector
                SET collection_id = ?
                WHERE book_id = ?
                AND collection_id = ?;
                """;

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setInt(1, newCollectionId);
            ps.setInt(2, bookId);
            ps.setInt(3, oldCollectionId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Collection> getCollectionsInUser(int userId) {
        List<Collection> collections = new ArrayList<>();
        String GET_COLLECTIONS_IN_USERS = """
                SELECT c.collection_id, c.name, c.description, c.date_created
                FROM Collections c
                JOIN Connector cnn ON c.collection_id = cnn.collection_id
                WHERE cnn.user_id = ? and book_id is null;
                """;

        try (PreparedStatement pstmt = dbcm.getConnection().prepareStatement(GET_COLLECTIONS_IN_USERS)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                collections.add(
                        new Collection(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getTimestamp(4)
                        )
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return collections;
    }

    public List<Book> getBooksInCollection(int collectionId) {
        List<Book> books = new ArrayList<>();
        String GET_BOOKS_IN_COLLECTION = """
                SELECT b.book_id, b.title, b.author, b.cover, b.status, b.number_of_pages, b.genre, b.rating, b.description, b.current_page, b.start_date, b.end_date, b.date_created
                FROM Books b
                JOIN Connector cnn ON b.book_id = cnn.book_id
                WHERE cnn.collection_id = ? AND cnn.user_id = ? AND cnn.character_id IS NULL;
                """;

        try (PreparedStatement pstmt = dbcm.getConnection().prepareStatement(GET_BOOKS_IN_COLLECTION)) {
            pstmt.setInt(1, collectionId);
            pstmt.setInt(2, Main.userId);
            ResultSet rs = pstmt.executeQuery();

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

    public List<Book> getBooksInCollection(String collectionName) {
        List<Book> books = new ArrayList<>();
        String GET_BOOKS_IN_COLLECTION = """
                SELECT b.book_id, b.title, b.author, b.cover, b.status, b.number_of_pages, b.genre, b.rating, b.description, b.current_page, b.start_date, b.end_date, b.date_created
                FROM Books b
                JOIN Connector cnn ON b.book_id = cnn.book_id
                JOIN Collections c ON c.collection_id = cnn.collection_id
                WHERE c.name = ? AND cnn.user_id = ? AND cnn.character_id IS NULL;
                """;

        try (PreparedStatement pstmt = dbcm.getConnection().prepareStatement(GET_BOOKS_IN_COLLECTION)) {
            pstmt.setString(1, collectionName);
            pstmt.setInt(2, Main.userId);
            ResultSet rs = pstmt.executeQuery();

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

    public List<Book> getBooksInUser(int userId) {
        List<Book> books = new ArrayList<>();
        String GET_BOOKS_IN_USERS = """
                SELECT b.book_id, b.title, b.author, b.cover, b.status, b.number_of_pages, b.genre, b.rating, b.description, b.current_page, b.start_date, b.end_date, b.date_created
                FROM Books b
                JOIN Connector cnn ON b.book_id = cnn.book_id
                JOIN Collections c ON c.collection_id = cnn.collection_id
                WHERE cnn.user_id = ? AND character_id is null AND c.name = 'All Books';
                """;

        try (PreparedStatement pstmt = dbcm.getConnection().prepareStatement(GET_BOOKS_IN_USERS)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

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

    public List<Character> getCharactersInBook(int bookId) {
        List<Character> characters = new ArrayList<>();
        String GET_CHARACTER_IN_BOOK = """
                SELECT c.character_id, c.name, c.alias, c.character_type, c.gender, c.age, c.description, c.importance, c.death, c.physical_appearance, c.relate_to_mainChar, c.date_created
                FROM Characters c
                JOIN Connector cnn ON c.character_id = cnn.character_id
                WHERE cnn.book_id = ? AND cnn.user_id = ? AND cnn.collection_id = ?
                """;

        try (PreparedStatement pstmt = dbcm.getConnection().prepareStatement(GET_CHARACTER_IN_BOOK)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, Main.userId);
            pstmt.setInt(3, Main.collectionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                characters.add(
                        new Character(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getInt(6),
                                rs.getString(7),
                                rs.getString(8),
                                rs.getString(9),
                                rs.getString(10),
                                rs.getString(11),
                                rs.getTimestamp(12)
                        )
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return characters;
    }

    public Collection findCollectionByName(String name) {
        String FIND_BY_NAME = """
                        SELECT c.collection_id, c.name, c.description, c.date_created
                        FROM Collections c
                        JOIN Connector cnn ON cnn.collection_id = c.collection_id
                        WHERE c.name = ? and cnn.user_id = ?;
                """;
        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_BY_NAME)) {
            ps.setString(1, name);
            ps.setInt(2, Main.userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Collection(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getTimestamp(4)
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void deleteBook(int book_id) {
        String DELETE = """
                    DELETE
                    From Connector
                    WHERE book_id = ?;
                """;

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(DELETE)) {
            ps.setInt(1, book_id);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCharacter(int character_id) {
        String DELETE = """
                    DELETE
                    From Connector
                    WHERE character_id = ?;
                """;

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(DELETE)) {
            ps.setInt(1, character_id);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}