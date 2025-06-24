package Database;

import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final DatabaseConnectionManager dbcm = new DatabaseConnectionManager();

    public void insert(User user) {
        String INSERT = "INSERT INTO Users (username, password, first_name, last_name, age, profile_pic, question, answer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(INSERT)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setInt(5, user.getAge());
            ps.setBytes(6, user.getProfilePic());
            ps.setString(7, user.getQuestion());
            ps.setString(8, user.getAnswer());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public User findById(int id) {
        String FIND_BY_ID = "SELECT * FROM Users WHERE user_id = ?";
        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        id,
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getBytes(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getTimestamp(10)
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User findByUsername(String username) {
        String FIND_BY_USERNAME = "SELECT * FROM Users WHERE username = ?";
        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_BY_USERNAME)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getBytes(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getTimestamp(10)
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String FIND_ALL = "SELECT * FROM Users";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_ALL)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getBytes(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getTimestamp(10)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public void delete(int id) {
        String DELETE = "DELETE FROM Users WHERE user_id = ?";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(User user) {
        String UPDATE = """
                UPDATE Users
                SET username = ?,
                password = ?,
                first_name = ?,
                last_name = ?,
                age = ?,
                profile_pic = ?,
                question = ?,
                answer = ?
                WHERE user_id = ?;
                """;

        try(PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setInt(5, user.getAge());
            ps.setBytes(6, user.getProfilePic());
            ps.setString(7, user.getQuestion());
            ps.setString(8, user.getAnswer());
            ps.setInt(9, user.getId());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePassword(int userId, String password) {
        String UPDATE = """
                UPDATE Users
                SET password = ?
                WHERE user_id = ?;
                """;

        try(PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setString(1, password);
            ps.setInt(2, userId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUsername(int userId, String username) {
        String UPDATE = """
                UPDATE Users
                SET username = ?
                WHERE user_id = ?;
                """;

        try(PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setString(1, username);
            ps.setInt(2, userId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateName(int userId, String firstName, String lastName) {
        String UPDATE = """
                UPDATE Users
                SET first_name = ?,
                last_name = ?
                WHERE user_id = ?;
                """;

        try(PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setInt(3, userId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateAge(int userId, int age) {
        String UPDATE = """
                UPDATE Users
                SET age = ?
                WHERE user_id = ?;
                """;

        try(PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setInt(1, age);
            ps.setInt(2, userId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateProfilePic(int userId, byte[] profilePic) {
        String UPDATE = """
                UPDATE Users
                SET profile_pic = ?
                WHERE user_id = ?;
                """;

        try(PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setBytes(1, profilePic);
            ps.setInt(2, userId);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
