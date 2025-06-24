package Scenes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RememberUser {
    public void rememberUserId(int userId) {
        try {
            Path path = Paths.get(System.getProperty("user.home"), ".bookly_remembered_users.txt");
            Files.writeString(path, userId + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Integer> getRememberedUserIds() {
        Path path = Paths.get(System.getProperty("user.home"), ".bookly_remembered_users.txt");
        if (!Files.exists(path)) return new ArrayList<>();

        try {
            return Files.readAllLines(path).stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public void removeUserId(int userId) {
        List<Integer> ids = getRememberedUserIds();
        ids.removeIf(id -> id == userId);

        try {
            Path path = Paths.get(System.getProperty("user.home"), ".bookly_remembered_users.txt");
            List<String> lines = ids.stream().map(String::valueOf).collect(Collectors.toList());
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
