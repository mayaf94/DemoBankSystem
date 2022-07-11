package adminMainApp;

import DTOs.AccountTransactionDTO;
import DTOs.BankSystemDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import clientController.ClientController;
import customerMainApp.customerDataTables;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import login.LoginController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;

import static util.Constants.GSON_INSTANCE;

public class AdminMainAppController extends ClientController {

    @FXML private AnchorPane viewByAdminContainer2;
    @FXML private BorderPane viewByAdminContainer;
    @FXML private GridPane MainButtonsBox;
    @FXML private Button IncreaseYazBtn;
    @FXML private Button RewindYazBT;
    @FXML private TableView<LoanDTOs> LoansData;
    @FXML private TableView<CustomerDTOs> CustomerData;
    @FXML private Label loansInAbsLb;
    @FXML private Label customersInAbsLb;
    @FXML private Label currentYazLB;
    private LoginController loginController;
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private String adminName;
    private Stage primaryStage;
    private Scene adminAppScene;
    @FXML private Label msgLB;

    @FXML
    private void initialize() {
    }

    @FXML
    void clickOnIncreaseYaz(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(Constants.INCREASE_YAZ)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorAlert.setContentText("Operation failed");
                errorAlert.show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        try {
                            String rawBody = response.body().string();
                            BankSystemDTO BankSystemFromJson = GSON_INSTANCE.fromJson(rawBody, BankSystemDTO.class);
                            currentYazLB.setText("Current Yaz: " + BankSystemFromJson.getCurYaz());
                            msgLB.setText(BankSystemFromJson.getMsg());
                            msgLB.setStyle("-fx-text-fill: #e70d0d; -fx-font-size: 16px;");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }


    @Override
    public void setPrimaryStage(Stage i_primaryStage) {
        this.primaryStage = i_primaryStage;
    }

    @Override
    public void switchToClientApp() {
        primaryStage.setScene(adminAppScene);
        primaryStage.show();
    }

    @Override
    public void setCurrentClientUserName(String userName) {
        adminName = userName;
    }

    @Override
    public void setRootPane(Scene i_rootPane) {
        adminAppScene = i_rootPane;
    }

    @Override
    public Boolean isAdmin() {
        return true;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @FXML
    void clickOnRewindYazBT(ActionEvent event) {

    }
}

