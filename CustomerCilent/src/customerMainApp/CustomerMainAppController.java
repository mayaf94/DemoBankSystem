package customerMainApp;


import DTOs.AccountTransactionDTO;
import DTOs.CategoriesDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import adminMainApp.AdminMainAppController;
import clientController.ClientController;
import com.google.gson.reflect.TypeToken;
//import com.sun.deploy.util.StringUtils;
//import org.apache.commons.lang.StringUtils;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import login.LoginController;
import okhttp3.*;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.StatusBar;
import org.jetbrains.annotations.NotNull;
import refreshers.AdminTablesRefresher;
import refreshers.CustomerInfoRefresher;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static util.Constants.GSON_INSTANCE;

public class CustomerMainAppController extends ClientController {

    @FXML private AnchorPane customerViewWindow;
    @FXML private VBox ChargeOrWithdraw;
    @FXML private AnchorPane AccountTransInfo;
    @FXML private TextField AmountTB;
    @FXML private Button ChargeBT;
    @FXML private Button WithdrawBT;
    @FXML private Label welcomeCustomer;
    @FXML private Label balanceOfCustomer;
    @FXML private Label LoansAsLoanerLabel;
    @FXML private Label LoansAsLenderLabel;
    @FXML private TableView<LoanDTOs> LoansAsLoaner;
    @FXML private TableView<LoanDTOs> LoansAsLender;
    @FXML private TextField amountToInvest;
    @FXML private Label errorAmountToInvest;
    @FXML private CheckComboBox<String> categories;
    @FXML private TextField minimumYaz;
    @FXML private TextField maxOpenLoans;
    @FXML private TextField maxLoanOwner;
    @FXML private Label errorMaxLoanOwner;
    @FXML private TextField minInterest;
    @FXML private StatusBar FindLoansProgress;
    @FXML private CheckListView<String> checkLoansToInvest;
    @FXML private Button invest;
    @FXML private Label howManyLoansFound;
    @FXML private Button findLoans;
    @FXML private TableView<LoanDTOs> relevantLoans;
    @FXML private Button resetSearch;
    @FXML private CheckBox selectAllLoansToInvest;
    @FXML private ListView<?> notificationsView;
    @FXML private Button fullPayment;
    @FXML private Button yazlyPayment;
    @FXML private AnchorPane LoansAsLoanerTableForPaymentTab;
    @FXML private MenuBar customerMenuBar;
    @FXML private Menu fileOption;
    @FXML private Menu viewOption;
    @FXML private Menu skinOptions;
    @FXML private MenuItem blueSkin;
    @FXML private MenuItem redSkin;
    @FXML private Menu HelpOption;
    @FXML private MenuItem openReadMeFile;
    @FXML private RadioMenuItem animationsBtn;
    private Stage primaryStage;
    private Scene customerAppScene;
    private Scene LoginScene;
    private SimpleStringProperty howManyMatchingLoansFoundProp = new SimpleStringProperty("");
    private Map<String, List<String>> messages;
    private String curCustomerName;
    private Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    private Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    LoginController loginController;
    customerDataTables customerInfoTables;
    @FXML private Label LoansToSellLB;
    @FXML private Label LoansToBuyLB;
    @FXML private TableView<LoanDTOs> LoansToSellTable;
    @FXML private TableView<LoanDTOs> LoansToBuyTable;
    @FXML private Button SellLoanBT;
    @FXML private Button BuyLoansBT;

    private Timer timer;
    private TimerTask clientRefresher;

    @FXML
    private void initialize() {
        AmountTB.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    AmountTB.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        amountToInvest.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    amountToInvest.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        minimumYaz.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    minimumYaz.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        maxOpenLoans.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    maxOpenLoans.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        maxLoanOwner.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    maxLoanOwner.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        minInterest.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    minInterest.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        howManyLoansFound.textProperty().bind(howManyMatchingLoansFoundProp);
    }

    @FXML
    void LoadFxmlClicked(ActionEvent event) {
        List<LoanDTOs> allLoans = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Bank Data File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile == null){
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();
        String finalUrl = HttpUrl
                .parse(Constants.LoadLoansFromXML)
                .newBuilder()
                .addQueryParameter("filePath", absolutePath)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    errorAlert.setContentText(String.valueOf(e));
                    errorAlert.showAndWait();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        try {
                            String rawBody = response.body().string();
                            List<LoanDTOs> loanAsLoanerToAddToTable = GSON_INSTANCE.fromJson(rawBody, new TypeToken<List<LoanDTOs>>() {
                            }.getType());
                            customerInfoTables.addLoanToLoansAsLoanerTable(loanAsLoanerToAddToTable);
                            if (response.headers().get("NEW_CATEGORIES").equals("true")) {
                                updateCategoriesInScrambleView();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        errorAlert.setContentText(responseBody);
                        errorAlert.showAndWait();
                    });
                }
            }
        });
        //TODO update bank customers table in admin view;
    }

    void updateCategoriesInScrambleView(){
        String finalUrl = HttpUrl
                .parse(Constants.GETALLCATEGORIES)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    errorAlert.setContentText(String.valueOf(e));
                    errorAlert.showAndWait();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Platform.runLater(() -> {
                        String rawBody = null;
                        try {
                            rawBody = response.body().string();
                            CategoriesDTO categoriesInSystem = GSON_INSTANCE.fromJson(rawBody,CategoriesDTO.class);
                            categories.getItems().addAll(categoriesInSystem.getCategories());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            }
        });
    }

    @FXML
    void blueSkinClicked(ActionEvent event) {

    }

    @FXML
    void redSkinClicked(ActionEvent event) {

    }

    @FXML
    void chargeClicked(ActionEvent event) {
        if (!AmountTB.getText().trim().isEmpty()) {
            String AmountToCharge = AmountTB.getText();
            String finalUrl = HttpUrl
                    .parse(Constants.ChargeAccount)
                    .newBuilder()
                    .addQueryParameter("transactionAmount", AmountToCharge)
                    .addQueryParameter("typeOfTransaction","DEPOSIT")
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
                    if(response.isSuccessful()){
                        Platform.runLater(() -> {
                            try {
                                String rawBody = response.body().string();
                                AccountTransactionDTO transactionFromJson = GSON_INSTANCE.fromJson(rawBody, AccountTransactionDTO.class);
                                List<AccountTransactionDTO> tmpTransactions = new ArrayList<>();
                                tmpTransactions.add(transactionFromJson);
                                customerInfoTables.addTransactionsToTable(tmpTransactions);
                                balanceOfCustomer.setText("Balance: " + transactionFromJson.getCurBalance());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            });

        }
        AmountTB.clear();
        AmountTB.setText(AmountTB.getText());
    }

    @FXML
    void withdrawClicked(ActionEvent event) {
        if (!AmountTB.getText().trim().isEmpty()) {
            String AmountToCharge = AmountTB.getText();
            String finalUrl = HttpUrl
                    .parse(Constants.ChargeAccount)
                    .newBuilder()
                    .addQueryParameter("transactionAmount", AmountToCharge)
                    .addQueryParameter("typeOfTransaction","WITHDRAW")
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        errorAlert.setContentText(String.valueOf(e));
                        errorAlert.showAndWait();
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        Platform.runLater(() -> {
                            try {
                                String rawBody = response.body().string();
                                AccountTransactionDTO transactionFromJson = GSON_INSTANCE.fromJson(rawBody, AccountTransactionDTO.class);
                                List<AccountTransactionDTO> tmpTransactions = new ArrayList<>();
                                tmpTransactions.add(transactionFromJson);
                                customerInfoTables.addTransactionsToTable(tmpTransactions);
                                balanceOfCustomer.setText(String.valueOf("Balance: " + transactionFromJson.getCurBalance()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else{
                        String responseBody = response.body().string();
                            Platform.runLater(() -> {
                                errorAlert.setContentText(responseBody);
                                errorAlert.showAndWait();
                            });
                    }
                }
            });

        }
        AmountTB.clear();
        AmountTB.setText(AmountTB.getText());
    }

    @FXML
    void findLoansBtClicked(ActionEvent event) {
        if(Integer.parseInt(balanceOfCustomer.getText()) > Integer.parseInt(amountToInvest.getText())) {
            disableFilterFields(true);
            startTask();
        }
        else{
            Platform.runLater(() ->{
                errorAlert.setContentText("You can't invest more than you have!");
                errorAlert.show();
                errorAmountToInvest.setText("You can't invest more than you have!");
                errorAmountToInvest.setStyle("-fx-text-fill: #e70d0d; -fx-font-size: 16px;");//TODO not visible after invesment reset
            });
        }
    }

    private void startTask() {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                updateMessage("Looking for relevant Loans...");
                Thread.sleep(2500);
               // Platform.runLater(() -> {
                            int max = 100000;
                            for (int i = 0; i < max; i++) {
                                if (i == 300)
                                    updateMessage("Scanning Loans in ABS");
                                if (i % 1000 == 0)
                                    updateMessage("finding Loans according your requirements");
                                updateProgress(i, max);
                            }
              //  });
                getRelevantLoansByUserParameters();
                Platform.runLater(() -> {
                    updateProgress(0, 0);
                    done();
                });
                return null;
            }
        };

        FindLoansProgress.textProperty().bind(task.messageProperty());
        FindLoansProgress.progressProperty().bind(task.progressProperty());

        // remove bindings again
        task.setOnSucceeded(event -> {
            FindLoansProgress.textProperty().unbind();
            FindLoansProgress.progressProperty().unbind();
        });
        new Thread(task).start();
    }

    private void getRelevantLoansByUserParameters() {
        StringBuilder minYaz = new StringBuilder("0");
        StringBuilder i_minInterest = new StringBuilder("0");
        StringBuilder i_maxOpenLoansForLoanOwner = new StringBuilder();
        List<String> i_categories;
        boolean maxOpenLoansSelected = false;
        i_categories = categories.getCheckModel().getCheckedItems();
        if(i_categories.isEmpty()){
            i_categories = categories.getItems();
        }
        //String i_categoriesAsOneString = StringUtils.join(i_categories, ", ");
        StringJoiner joiner = new StringJoiner(", ");
        i_categories.stream().forEach(C -> joiner.add(C));
        String i_categoriesAsOneString = joiner.toString();
        if (getUserParametersForScramble(minYaz, i_minInterest, i_maxOpenLoansForLoanOwner, maxOpenLoansSelected)) {
            if(!i_maxOpenLoansForLoanOwner.toString().equals(""))
                maxOpenLoansSelected = true;
            String finalUrl = HttpUrl
                    .parse(Constants.SCRAMBLE)
                    .newBuilder()
                    .addQueryParameter("minimumDuration",String.valueOf(minYaz))
                    .addQueryParameter("minimumInterestForSingleYaz",String.valueOf(i_minInterest))
                    .addQueryParameter("maxOpenLoansForLoanOwner",String.valueOf(i_maxOpenLoansForLoanOwner))
                    .addQueryParameter("Categories", i_categoriesAsOneString)
                    .addQueryParameter("maxOpenLoansForLoanOwnerSelected",String.valueOf(maxOpenLoansSelected))
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        errorAlert.setContentText(String.valueOf(e));
                        errorAlert.showAndWait();
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        Platform.runLater(() -> {
                            try {
                                String rawBody = response.body().string();
                                List<LoanDTOs> MatchingLoans = GSON_INSTANCE.fromJson(rawBody, new TypeToken<List<LoanDTOs>>(){}.getType());
                                customerInfoTables.updateMatchingLoansForScramble(MatchingLoans);
                                howManyMatchingLoansFoundProp.set("Found " + MatchingLoans.size() + " matching loans!");
                                howManyLoansFound.setStyle("-fx-text-fill: #e70d0d; -fx-font-size: 16px;");
                                checkLoansToInvest.getItems().addAll(MatchingLoans.stream().collect(Collectors.toMap(LoanDTOs::getNameOfLoan, loan -> loan)).
                                        keySet().stream().collect(Collectors.toList()));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else{
                        String responseBody = response.body().string();
                        Platform.runLater(() -> {
                            errorAlert.setContentText(responseBody);
                            errorAlert.show();
                            resetScrambleTab();
                        });
                    }
                }
            });
        }
        else{
            Platform.runLater(() -> {
                errorAlert.setContentText("You cant invest more then 100 present!");
                errorAlert.show();
                resetScrambleTab();
            });
        }

    }

    private boolean getUserParametersForScramble(StringBuilder  minYaz ,StringBuilder i_minInterest ,StringBuilder i_maxOpenLoansForLoanOwner,boolean maxOpenLoansSelected){
        if(!minimumYaz.getText().isEmpty())
            minYaz.replace(0,minYaz.length(),minimumYaz.getText());


        if(!minInterest.getText().isEmpty())
            i_minInterest.replace(0,i_minInterest.length(),minInterest.getText());

        if(!maxOpenLoans.getText().isEmpty()) {
            i_maxOpenLoansForLoanOwner.append(maxOpenLoans.getText());
            maxOpenLoansSelected = true;
        }
        if(!(maxLoanOwner.getText().isEmpty())){
            int i_temp = Integer.parseInt((maxLoanOwner.getText()));
            if(i_temp > 100){
                return false;
            }
        }
        return true;
    }

    void resetScrambleTab(){
        Platform.runLater(() ->{
            disableFilterFields(false);
            amountToInvest.clear();
            categories.getCheckModel().clearChecks();
            minimumYaz.clear();
            maxOpenLoans.clear();
            maxLoanOwner.clear();
            minInterest.clear();
            selectAllLoansToInvest.setSelected(false);
            checkLoansToInvest.getItems().clear();
            howManyMatchingLoansFoundProp.set("");
            errorAmountToInvest.setText("");
            relevantLoans.getItems().clear();
        });
    }

    private void disableFilterFields(boolean disable){
        amountToInvest.setDisable(disable);
        categories.setDisable(disable);
        minimumYaz.setDisable(disable);
        maxOpenLoans.setDisable(disable);
        maxLoanOwner.setDisable(disable);
        minInterest.setDisable(disable);
    }

    @FXML
    void fullPaymentClicked(ActionEvent event) {

    }

    @FXML
    void investBtClicked(ActionEvent event) {

    }

    @FXML
    void logoutClicked(ActionEvent event) {

    }

    @FXML
    void openReadMeClicked(ActionEvent event) {

    }

    @FXML
    void resetSearchBtClicked(ActionEvent event) {

    }

    @FXML
    void selectAllLoansToInvestBtClicked(ActionEvent event) {

    }

    @FXML
    void yazlyPaymentClicked(ActionEvent event) {

    }

    @FXML
    void animationClicked(ActionEvent event){}

    @Override
    public void setPrimaryStage(Stage i_primaryStage) {
        this.primaryStage = i_primaryStage;
    }

    @Override
    public void switchToClientApp() {
        primaryStage.setScene(customerAppScene);
        primaryStage.show();
        customerInfoTables = new customerDataTables(this,LoansAsLoaner,LoansAsLender,relevantLoans,LoansToSellTable,LoansToBuyTable);
        customerInfoTables.getTransactionTable().prefWidthProperty().bind(AccountTransInfo.widthProperty());
        customerInfoTables.getTransactionTable().prefHeightProperty().bind(AccountTransInfo.heightProperty());
        AccountTransInfo.getChildren().setAll(customerInfoTables.getTransactionTable());
        welcomeCustomer.setText("Hello " + curCustomerName);
        startRefresher();
    }
//
    public void switchToLoginScene() {
        primaryStage.setScene(LoginScene);
        primaryStage.show();
    }//TODO

    @Override
    public void setCurrentClientUserName(String userName) {
        curCustomerName = userName;
    }

    @Override
    public void setRootPane(Scene i_customerAppScene) {
        customerAppScene = i_customerAppScene;
    }

    @Override
    public Boolean isAdmin() {
        return false;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @FXML
    void SellLoansClicked(ActionEvent event) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("somParam", "someValue")
                .build();

//        Request request = new Request.Builder()
//                .url(BASE_URL + route)
//                .post(requestBody)
//                .build();

    }

    @FXML
    void BuyLoansClicked(ActionEvent event) {

    }

    public void startRefresher() {
        clientRefresher = new CustomerInfoRefresher(CustomerMainAppController::updateTableLoansAsLoaner,
                CustomerMainAppController::updateTableLoansAsLender
                ,CustomerMainAppController::updateTableLoansToSellTable
                ,CustomerMainAppController::updateTableLoansToBuyTable
                ,CustomerMainAppController::updateTableNotificationsView
                ,CustomerMainAppController::updateTableCustomerInfoTables1
                ,CustomerMainAppController::updateTableCustomerInfoTables2
                ,CustomerMainAppController::updateYazLB
                ,CustomerMainAppController::disableBT
                ,CustomerMainAppController::updateCategories
        ,CustomerMainAppController::balanceLBUpdate);
        timer = new Timer();
        timer.schedule(clientRefresher, 4000, 4000);
    }

    public static void updateTableLoansAsLoaner(List<LoanDTOs> allLoans){}

    public static void updateTableLoansAsLender(List<LoanDTOs> allLoans){}

    public static void updateCategories(List<String> allCategories){}

    public static void updateTableLoansToSellTable(List<LoanDTOs> allLoans){}

    public static void updateTableLoansToBuyTable(List<LoanDTOs> allLoans){}

    public static void updateTableNotificationsView(List<CustomerDTOs> allNotifications){}

    public static void updateTableCustomerInfoTables1(List<LoanDTOs> allLoans){}

    public static void updateTableCustomerInfoTables2(List<CustomerDTOs> allCustomers){}

    public static void updateYazLB(Integer yaz){

    }

    public static void balanceLBUpdate(Integer balance){}

    public static void disableBT(Boolean isRewind){}


}
