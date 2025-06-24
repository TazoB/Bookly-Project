package Database;

import Model.Character;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterDAO {
    private static final DatabaseConnectionManager dbcm = new DatabaseConnectionManager();

    public void insert(Character character) {
        String INSERT = "INSERT INTO Characters (name, alias, character_type, gender, age, description, importance, death, physical_appearance, relate_to_mainChar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(INSERT)) {
            ps.setString(1, character.getName());
            ps.setString(2, character.getAlias());
            ps.setString(3, character.getCharacterType());
            ps.setString(4, character.getGender());
            ps.setInt(5, character.getAge());
            ps.setString(6, character.getDescription());
            ps.setString(7, character.getImportance());
            ps.setString(8, character.getDeath());
            ps.setString(9, character.getPhysicalAppearance());
            ps.setString(10, character.getRelateToMainChar());

            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Character findById(int id) {
        String FIND_BY_ID = "SELECT * FROM Characters WHERE character_id = ?";
        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Character(
                        id,
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
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Character> findAll() {
        List<Character> characters = new ArrayList<>();
        String FIND_ALL = "SELECT * FROM Characters";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(FIND_ALL)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                characters.add(new Character(
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
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return characters;
    }

    public void delete(int id) {
        String DELETE = "DELETE FROM Characters WHERE character_id = ?";

        try (PreparedStatement ps = dbcm.getConnection().prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(Character character) {
        String UPDATE = """
                UPDATE Characters
                SET name = ?,
                alias = ?,
                character_type = ?,
                gender = ?,
                age = ?,
                description = ?,
                importance = ?,
                death = ?,
                physical_appearance = ?,
                relate_to_mainChar = ?
                WHERE id = ?;
                """;

        try(PreparedStatement ps = dbcm.getConnection().prepareStatement(UPDATE)) {
            ps.setString(1, character.getName());
            ps.setString(2, character.getAlias());
            ps.setString(3, character.getCharacterType());
            ps.setString(4, character.getGender());
            ps.setInt(5, character.getAge());
            ps.setString(6, character.getDescription());
            ps.setString(7, character.getImportance());
            ps.setString(8, character.getDeath());
            ps.setString(9, character.getPhysicalAppearance());
            ps.setString(10, character.getRelateToMainChar());
            ps.setInt(11, character.getId());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
