package pacman;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private AnchorPane kontejner;

    @FXML
    public void gotoGame() throws IOException {
        Stage stage = (Stage) kontejner.getScene().getWindow();

        // these two of them return the same stage
        // Swap screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Resources/FXMLDocument.fxml"));

        Parent root = loader.load();

        stage.setScene(new Scene(root,650,650));

        FXMLDocumentController dc = loader.getController();
        dc.play();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
