package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Collection;
import Scenes.Main;

public class CollectionDAO {
    private static final DatabaseConnectionManager dbcm = new DatabaseConnectionManager();

    public void insert(Collection collection) {
        String INSERT = "INSERT INTO Collections (name, description) VALUES (?, ?)";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(INSERT)) {
            ps.setString(1, collection.getName());
            ps.setString(2, collection.getDescription());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Collection findById(int id) {
        String FIND_BY_ID = "SELECT * FROM Collections WHERE collection_id = ?";
        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
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

    public Collection findByName(String name) {
        String FIND_BY_NAME = """
                    SELECT c.collection_id, c.name, c.description, c.date_created
                    FROM Collections c
                    JOIN Connector cnn
                    ON c.collection_id = cnn.collection_id
                    WHERE c.name = ?
                    AND cnn.user_id = ?
                    AND cnn.book_id is null;
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

    public List<Collection> findAll() {

        List<Collection> collections = new ArrayList<>();
        String FIND_ALL = "SELECT * FROM Collections";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                collections.add(new Collection(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getTimestamp(4)
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return collections;
    }

    public void delete(int id) {
        String DELETE = "DELETE FROM Collections WHERE collection_id = ?";

        try (PreparedStatement pstmt = dbcm.getConnection().prepareStatement(DELETE)) {
            pstmt.setInt(1, id);
            pstmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(Collection collection) {
        String UPDATE = """
                UPDATE Collections
                SET name = ?,
                description = ?
                WHERE id = ?;
                """;

        try(PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setString(1, collection.getName());
            ps.setString(2, collection.getDescription());
            ps.setInt(3, collection.getId());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
