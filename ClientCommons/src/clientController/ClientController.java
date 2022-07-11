package clientController;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//customer and admin controller will inherits from this class
public abstract class ClientController {

    public abstract void setPrimaryStage(Stage i_primaryStage);
    public abstract void switchToClientApp();
    public abstract void setCurrentClientUserName(String userName);
    public abstract void setRootPane(Scene i_rootPane);
    public abstract Boolean isAdmin();

}
