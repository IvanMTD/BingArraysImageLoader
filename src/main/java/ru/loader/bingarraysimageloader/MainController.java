package ru.loader.bingarraysimageloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ru.loader.bingarraysimageloader.internet.BingSearch;

import javax.swing.text.html.ImageView;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorId;

    @FXML
    private Button directoryChooser;

    @FXML
    private Button enterButton;

    @FXML
    private ListView<String> information;

    @FXML
    private TextField pathArea;

    @FXML
    private TextField searchField;

    private BingSearch bingSearch;
    private String currentDirectory = "C:\\";

    @FXML
    void initialize() {
        assert directoryChooser != null : "fx:id=\"directoryChooser\" was not injected: check your FXML file 'main-scene.fxml'.";
        assert enterButton != null : "fx:id=\"enterButton\" was not injected: check your FXML file 'main-scene.fxml'.";
        assert information != null : "fx:id=\"information\" was not injected: check your FXML file 'main-scene.fxml'.";
        assert pathArea != null : "fx:id=\"pathArea\" was not injected: check your FXML file 'main-scene.fxml'.";
        assert searchField != null : "fx:id=\"searchField\" was not injected: check your FXML file 'main-scene.fxml'.";

        pathArea.setText(currentDirectory);
        searchField.setText("some information");

        enterButton.setOnAction(event -> {
            bingSearch = new BingSearch();
            String[] requestArray = searchField.getText().split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            for(String r : requestArray){
                if(stringBuilder.length() == 0){
                    stringBuilder.append(r);
                }else{
                    stringBuilder.append("+").append(r);
                }
            }
            String request = stringBuilder.toString();
            String url = "https://www.bing.com/images/search?q=" + request + "&first=";
            try {
                List<String> links = bingSearch.getImageLinks(url,10);
                information.getItems().addAll(links);
            } catch (IOException e) {
                information.getItems().add(e.getMessage());
                //throw new RuntimeException(e);
            }
        });

        directoryChooser.setOnAction(event -> {
            if(!pathArea.getText().equals(currentDirectory)){
                File file = new File(pathArea.getText());
                if(file.exists()){
                    currentDirectory = pathArea.getText();
                }else{
                    information.getItems().add("Не верно указан путь к папке. Возвращено предыдущее значение: " + currentDirectory);
                    pathArea.setText(currentDirectory);
                }
            }
            final DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setInitialDirectory(new File(currentDirectory));
            Stage stage = (Stage) anchorId.getScene().getWindow();
            File file = dirChooser.showDialog(stage);
            if(file != null){
                currentDirectory = file.getAbsolutePath();
                pathArea.setText(currentDirectory);
            }
        });
    }
}
