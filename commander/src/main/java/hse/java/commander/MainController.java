package hse.java.commander;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

<<<<<<< HEAD
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
=======
import java.nio.file.Path;

public class MainController {
>>>>>>> 1c195628027c24a0d2a677be0bd75b5381ae77e5

public class MainController {
    @FXML
<<<<<<< HEAD
    public Button move;
    @FXML
=======
>>>>>>> 1c195628027c24a0d2a677be0bd75b5381ae77e5
    public ListView<String> left;
    @FXML
    public ListView<String> right;
    @FXML
    public Label leftPathLabel;
    @FXML
    public Label rightPathLabel;

<<<<<<< HEAD
    private File leftDirectory = new File(System.getProperty("user.home"));
    private File rightDirectory = new File(System.getProperty("user.home"));

    public void initialize() {
        refresh(left, leftDirectory);
        refresh(right, rightDirectory);

        left.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleNavigation(left, true);
            }
        });

        right.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleNavigation(right, false);
            }
        });

        move.setOnAction(event -> moveFile());
    }

    private void refresh(ListView<String> list, File directory) {
        list.getItems().clear();
        list.getItems().add("..");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                list.getItems().add(file.isDirectory() ? "[" + file.getName() + "]" : file.getName());
            }
        }
        if (list == left) {
            leftPathLabel.setText(directory.getAbsolutePath());
        } else {
            rightPathLabel.setText(directory.getAbsolutePath());
        }
    }

    private void handleNavigation(ListView<String> list, boolean isLeft) {
        String selected = list.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        File currentDir = isLeft ? leftDirectory : rightDirectory;
        File nextDirectory;

        if (selected.equals("..")) {
            nextDirectory = currentDir.getParentFile();
        } else {
            String name = selected.replace("[", "").replace("]", "");
            nextDirectory = new File(currentDir, name);
        }

        if (nextDirectory != null && nextDirectory.isDirectory()) {
            if (isLeft) {
                leftDirectory = nextDirectory;
            } else {
                rightDirectory = nextDirectory;
            }
            refresh(list, nextDirectory);
        }
    }

    private void moveFile() {
        boolean moveFromLeft = left.getSelectionModel().getSelectedItem() != null;
        ListView<String> fromList = moveFromLeft ? left : right;
        File fromDirectory = moveFromLeft ? leftDirectory : rightDirectory;
        File toDirectory = moveFromLeft ? rightDirectory : leftDirectory;

        String selected = fromList.getSelectionModel().getSelectedItem();

        if (selected == null || selected.equals("..")) {
            return;
        }

        String name = selected.replace("[", "").replace("]", "");
        Path source = new File(fromDirectory, name).toPath();
        Path destination = new File(toDirectory, name).toPath();

        try {
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
            refresh(left, leftDirectory);
            refresh(right, rightDirectory);
        } catch (IOException e) {
            System.err.println("Анлак, не удалось перенести: " + e.getMessage());
        }
    }
}
=======
    @FXML
    public Button move;

    private Path leftDir;
    private Path rightDir;

    // for testing
    public void setInitialDirs(Path leftStart, Path rightStart) {
        this.leftDir = leftStart;
        this.rightDir = rightStart;
    }

    public void initialize() {
        move.setOnMouseClicked(event -> {

        });
        System.out.println(System.getProperty("user.home"));
        left.getItems().add("Kek");

        left.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int index = left.getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    left.getItems().set(index, "clicked");
                }
            }
        });
    }
}
>>>>>>> 1c195628027c24a0d2a677be0bd75b5381ae77e5
