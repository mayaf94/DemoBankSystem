package Component.PopUpWindows;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;

public class ErrorWindow {



/*    errorPopUpWindow = loader.load();
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.initOwner(primaryStage);
    Scene dialogScene = new Scene(errorPopUpWindow, 550, 200);
      dialog.setTitle("Error");
      dialog.setScene(dialogScene);*/

    @FXML
    private ImageView errorIcon;

    @FXML
    private Label errorLabel;

    @FXML
    private Button OkBt;

}
