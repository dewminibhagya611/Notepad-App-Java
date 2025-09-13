import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import java.io.*;
import java.util.Optional;

public class SimpleNotepad extends Application {
    private TextArea textArea;
    private Stage stage;
    private ToolBar toolBar;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setTitle("Simple Notepad");

        textArea = new TextArea();
        textArea.setStyle("-fx-control-inner-background: #2f3640; -fx-text-fill: #f1c40f; -fx-font-family: 'Roboto'; -fx-font-size: 16px; -fx-border-color: #dcdde1; -fx-border-radius: 5px;");
        textArea.setPrefHeight(500);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2f3640, #353b48);");


        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #0984e3; -fx-text-fill: #ffffff;");

        Menu fileMenu = new Menu("File");
        fileMenu.setStyle("-fx-text-fill: #e84393; -fx-font-size: 14px;");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(openItem, saveItem, exitItem);

        Menu editMenu = new Menu("Edit");
        editMenu.setStyle("-fx-text-fill: #e84393; -fx-font-size: 14px;");
        MenuItem cutItem = new MenuItem("Cut");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");
        editMenu.getItems().addAll(cutItem, copyItem, pasteItem);

        Menu formatMenu = new Menu("Format");
        formatMenu.setStyle("-fx-text-fill: #e84393; -fx-font-size: 14px;");
        MenuItem fontItem = new MenuItem("Choose Font");
        MenuItem colorItem = new MenuItem("Choose Text Color");
        formatMenu.getItems().addAll(fontItem, colorItem);

        Menu helpMenu = new Menu("Help");
        helpMenu.setStyle("-fx-text-fill: #e84393; -fx-font-size: 14px;");
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, formatMenu, helpMenu);


        toolBar = new ToolBar();
        toolBar.setStyle("-fx-background-color: #0984e3;");
        Button openButton = new Button("Open");
        Button saveButton = new Button("Save");
        toolBar.getItems().addAll(openButton, saveButton);

        root.setTop(menuBar);
        root.setCenter(textArea);
        root.setBottom(toolBar);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


        openItem.setOnAction(this::handleOpen);
        openButton.setOnAction(this::handleOpen);
        saveItem.setOnAction(this::handleSave);
        saveButton.setOnAction(this::handleSave);
        exitItem.setOnAction(e -> System.exit(0));
        cutItem.setOnAction(e -> textArea.cut());
        copyItem.setOnAction(e -> textArea.copy());
        pasteItem.setOnAction(e -> textArea.paste());
        fontItem.setOnAction(this::handleChooseFont);
        colorItem.setOnAction(this::handleChooseColor);
        aboutItem.setOnAction(e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About Me");
            alert.setHeaderText("Meet Your Notepad Buddy!");
            alert.setContentText("Hey there! This is your friendly Modern Notepad\nCrafted with love by Your Name\nID: Your ID\nEnjoy your writing journey!");
            alert.getDialogPane().setStyle("-fx-background-color: #2f3640; -fx-text-fill: #f1c40f;");
            alert.show();
        });
    }

    private void handleOpen(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.setText(reader.lines().reduce("", (a, b) -> a + b + "\n"));
                showInfoAlert("Yay! File opened successfully. Let’s get creative!");
            } catch (IOException ex) {
                showErrorAlert("Oops! Something went wrong while opening: " + ex.getMessage());
            }
        }
    }

    private void handleSave(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText());
                showInfoAlert("Great job! Your file is saved and ready to shine!");
            } catch (IOException ex) {
                showErrorAlert("Hmm, looks like we hit a snag saving: " + ex.getMessage());
            }
        }
    }

    private void handleChooseColor(ActionEvent e) {
        ColorPicker colorPicker = new ColorPicker();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Pick a Color");
        alert.setHeaderText("Make it Your Own!");
        alert.getDialogPane().setContent(colorPicker);
        alert.getDialogPane().setStyle("-fx-background-color: #2f3640; -fx-text-fill: #f1c40f;");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Color color = colorPicker.getValue();
            if (color != null) {
                String hex = String.format("#%02X%02X%02X", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));
                textArea.setStyle(textArea.getStyle() + "; -fx-text-fill: " + hex + ";");
            }
        }
    }

    private void handleChooseFont(ActionEvent e) {
        ChoiceDialog<String> familyDialog = new ChoiceDialog<>("Roboto", Font.getFamilies());
        familyDialog.setTitle("Choose Your Font");
        familyDialog.setHeaderText("Style Your Text!");
        familyDialog.getDialogPane().setStyle("-fx-background-color: #2f3640; -fx-text-fill: #f1c40f;");
        Optional<String> familyResult = familyDialog.showAndWait();
        if (familyResult.isPresent()) {
            String family = familyResult.get();

            TextInputDialog sizeDialog = new TextInputDialog("16");
            sizeDialog.setTitle("Choose Font Size");
            sizeDialog.setHeaderText("How Big Should It Be?");
            sizeDialog.getDialogPane().setStyle("-fx-background-color: #2f3640; -fx-text-fill: #f1c40f;");
            Optional<String> sizeResult = sizeDialog.showAndWait();
            if (sizeResult.isPresent()) {
                double size = Double.parseDouble(sizeResult.get());
                textArea.setStyle(textArea.getStyle() + "; -fx-font-family: '" + family + "'; -fx-font-size: " + size + "px;");
            }
        }
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.getDialogPane().setStyle("-fx-background-color: #2f3640; -fx-text-fill: #f1c40f;");
        alert.show();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR, message);
        alert.getDialogPane().setStyle("-fx-background-color: #2f3640; -fx-text-fill: #f1c40f;");
        alert.show();
    }
}