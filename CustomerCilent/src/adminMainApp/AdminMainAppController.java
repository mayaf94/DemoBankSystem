package adminMainApp;

import CustomerInfoView.ViewCustomersInfoController;
import DTOs.BankSystemDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import clientController.ClientController;
import common.BankResourcesConstants;
import component.loansComponent.ViewLoansInfo.ViewLoansInfoController;
import customerMainApp.customerDataTables;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import login.LoginController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.controlsfx.control.table.TableRowExpanderColumn;
import org.jetbrains.annotations.NotNull;
import refreshers.AdminTablesRefresher;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static util.Constants.GSON_INSTANCE;

public class AdminMainAppController extends ClientController {

    @FXML
    private AnchorPane viewByAdminContainer2;
    @FXML
    private BorderPane viewByAdminContainer;
    @FXML
    private GridPane MainButtonsBox;
    @FXML
    private Button IncreaseYazBtn;
    @FXML
    private Button RewindYazBT;
    @FXML private TableView<LoanDTOs> LoansData;
    @FXML private TableView<CustomerDTOs> CustomerData;
    @FXML
    private Label loansInAbsLb;
    @FXML
    private Label customersInAbsLb;
    @FXML
    private Label currentYazLB;
    private LoginController loginController;
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private String adminName;
    private Stage primaryStage;
    private Scene adminAppScene;
    @FXML private Label msgLB;
    private Boolean isRewind = false;
    customerDataTables adminInfoTables;

    private Timer timer;
    private TimerTask loanTableRefresher;




    @FXML
    private void initialize() {
        currentYazLB.setText("Current Yaz: 1");
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
                            if(BankSystemFromJson.getRewind())
                                msgLB.setText(BankSystemFromJson.getMsg() + ", you are still in rewind status.");
                            else
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
        ViewLoansInfoController loansInfoController = new ViewLoansInfoController();
        loansInfoController.setMainController(this);
        Platform.runLater(()-> loansInfoController.buildLoansTableView(LoansData));

        String finalUrl = HttpUrl
                .parse(Constants.getAllLoansAndCustomersInBank)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        try {
                            String rawBody = response.body().string();
                            BankSystemDTO BankSystemFromJson = GSON_INSTANCE.fromJson(rawBody, BankSystemDTO.class);
                            buildCustomersTableView(BankSystemFromJson.getCustomers(), BankSystemFromJson.getLoansInBank());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
        startListRefresher();
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
        String finalUrl = HttpUrl
                .parse(Constants.REWIND_YAZ)
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
                            msgLB.setText("You are in rewind status");
                            msgLB.setStyle("-fx-text-fill: #e70d0d; -fx-font-size: 16px;");
                        } catch (IOException e) {
                           // e.printStackTrace();
                        }
        });
        }
         }
          });
    }

    public void startListRefresher() {
        loanTableRefresher = new AdminTablesRefresher(this::updateLoansTable
                ,this::updateCustomerTable);
        timer = new Timer();
        timer.schedule(loanTableRefresher, 2000, 2000);
    }

    private void buildCustomersTableView(List<CustomerDTOs> i_allCustomers, List<LoanDTOs> i_allLoans){
        CustomerData.getItems().clear();
        List<CustomerDTOs> allCustomers = i_allCustomers;
        TableRowExpanderColumn<CustomerDTOs> expanderColumn = new TableRowExpanderColumn<>(param -> {
            try {
                return expandCustomerInfo(param, i_allLoans);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });

        expanderColumn.setPrefWidth(45);
        TableColumn<CustomerDTOs, String> nameOfCustomer = new TableColumn<>("Name of customer");
        nameOfCustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameOfCustomer.setPrefWidth(125);

        TableColumn<CustomerDTOs, String> balance = new TableColumn<>("Balance");
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        balance.setPrefWidth(125);

        TableColumn<CustomerDTOs, String> loansAsLoaner = new TableColumn<>("Loans as Borrower");
        loansAsLoaner.setCellValueFactory(new PropertyValueFactory<>("numOfLoansAsBorrower"));
        loansAsLoaner.setPrefWidth(125);

        TableColumn<CustomerDTOs, String> loansAsLender = new TableColumn<>("Loans as lender");
        loansAsLender.setCellValueFactory(new PropertyValueFactory<>("numOfLoansAsLender"));
        loansAsLender.setPrefWidth(125);


        if(CustomerData.getColumns().isEmpty())
            CustomerData.getColumns().addAll(expanderColumn, nameOfCustomer, balance, loansAsLoaner, loansAsLender);
        CustomerData.getItems().addAll(FXCollections.observableArrayList(allCustomers));

    }

    private GridPane expandCustomerInfo(TableRowExpanderColumn.TableRowDataFeatures<CustomerDTOs> param, List<LoanDTOs> allLoans) throws IOException {
        GridPane workSpace = new GridPane();
        workSpace.setHgap(10);
        workSpace.setVgap(5);

        CustomerDTOs customer = param.getValue();

        FXMLLoader loader = new FXMLLoader();
        URL CustomerViewFXML = getClass().getResource(BankResourcesConstants.VIEWCUSTOMERDATAEXPANDED_RESOURCE_IDENTIFIRE);
        loader.setLocation(CustomerViewFXML);
        GridPane CustomerExpandedDetails = loader.load();
        ViewCustomersInfoController customersInfoController = loader.getController();
        CustomerExpandedDetails.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        List<String> LoansAsALender = customer.getLoansAsALender();
        List<String> LoansAsBorrower = customer.getLoansAsABorrower();

        customersInfoController.SetLoansAsLenderByStatusLabels(allLoans.stream().filter(L -> LoansAsALender.contains(L.getNameOfLoan())).collect(Collectors.toList()));
        customersInfoController.SetLoansAsLoanerByStatusLabels(allLoans.stream().filter(L -> LoansAsBorrower.contains(L.getNameOfLoan())).collect(Collectors.toList()));//TODO: change the map

        return CustomerExpandedDetails;
    }

    private void updateCustomerTable(List<CustomerDTOs> allCustomersInSystem){
        Platform.runLater(() -> {
            CustomerData.getItems().clear();
            CustomerData.getItems().addAll(allCustomersInSystem);
            CustomerData.refresh();
            msgLB.setText(" ");
        });
    }

    private void updateLoansTable(List<LoanDTOs> allLoans){

        Platform.runLater(() ->{
            if(LoansData.getItems().size() != allLoans.size()){
                LoansData.getItems().clear();
                LoansData.getItems().addAll(allLoans);
                LoansData.refresh();            }
            else {
                Map<String, Set<Integer>> versionsOfLoans = new HashMap<>();
                for (LoanDTOs curLoan : allLoans) {
                    Set<Integer> mySet = new HashSet<Integer>();
                    mySet.add(curLoan.getVersion());
                    versionsOfLoans.put(curLoan.getNameOfLoan(), mySet);
                }
                for (LoanDTOs curLoan : LoansData.getItems()) {
                    if(!versionsOfLoans.containsKey(curLoan.getNameOfLoan())){
                        LoansData.getItems().clear();
                        LoansData.getItems().addAll(allLoans);
                        LoansData.refresh();
                    }
                    else{
                        versionsOfLoans.get(curLoan.getNameOfLoan()).add(curLoan.getVersion());
                    }
                }
                if(!versionsOfLoans.values().stream().filter(L -> (L.size() > 1)).collect(Collectors.toList()).isEmpty()){
                    LoansData.getItems().clear();
                    LoansData.getItems().addAll(allLoans);
                    LoansData.refresh();
                }
            }
        });
    }
}



