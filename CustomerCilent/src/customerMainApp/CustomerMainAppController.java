package customerMainApp;


import DTOs.AccountTransactionDTO;
import DTOs.CategoriesDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import login.LoginController;
import okhttp3.*;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.StatusBar;
import org.jetbrains.annotations.NotNull;
import refreshers.CustomerInfoRefresher;
import util.Constants;
import util.http.HttpClientUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static util.Constants.GSON_INSTANCE;

public class CustomerMainAppController extends ClientController {

    @FXML private AnchorPane AccountTransInfo;
    @FXML private Button ChargeBT;
    @FXML private Button WithdrawBT;
    @FXML private Label welcomeCustomer;
    @FXML private Label yazLB;
    @FXML private Integer curYaz;
    @FXML private MenuItem loadFile;
    @FXML private Label balanceOfCustomer;
    @FXML private TableView<LoanDTOs> LoansAsLoaner;
    @FXML private TableView<LoanDTOs> LoansAsLender;
    @FXML private TableView<LoanDTOs> LoansAsLoanerTableForPaymentTab;
    @FXML private TextField amountToInvest;
    @FXML private Label errorAmountToInvest;
    @FXML private CheckComboBox<String> categories;
    @FXML private TextField minimumYaz;
    @FXML private TextField AmountTB;
    @FXML private TextField maxOpenLoans;
    @FXML private TextField maxLoanOwner;
    @FXML private TextField minInterest;
    @FXML private Label errorMaxLoanOwner;
    @FXML private StatusBar FindLoansProgress;
    @FXML private CheckListView<String> checkLoansToInvest;
    @FXML private Button invest;
    @FXML private Label howManyLoansFound;
    @FXML private Button findLoans;
    @FXML private TableView<LoanDTOs> relevantLoans;
    @FXML private Button resetSearch;
    @FXML private CheckBox selectAllLoansToInvest;
    @FXML private ListView<String> notificationsView;
    @FXML private Button fullPayment;
    @FXML private Button yazlyPayment;
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
    private  String curCustomerName;
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

    @FXML private TextField LoanNameTakeLoanTA;
    @FXML private TextField categoryTakeLoanTA;
    @FXML private TextField principalAmountTA;
    @FXML private TextField totalDurationTakeLoanTA;
    @FXML private TextField paymentFreqTakeLoanTA;
    @FXML private TextField InterestTakeLoanTA;
    @FXML private Button takeOutLoanBT;



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
    }

    void updateCategoriesInScrambleView(){
        String finalUrl = HttpUrl
                .parse(Constants.CATEGORISATIONS)
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
    void takeOutLoanBTClicked(ActionEvent event) {
        String nameOfLoan = LoanNameTakeLoanTA.getText();
        String principalAmount = principalAmountTA.getText();
        String durationOfLoan = totalDurationTakeLoanTA.getText();
        String paymentFreq = paymentFreqTakeLoanTA.getText();
        String category = categoryTakeLoanTA.getText();
        String interest = InterestTakeLoanTA.getText();

        String finalUrl = HttpUrl
                .parse(Constants.TAKE_OUT_A_LOAN)
                .newBuilder()
                .addQueryParameter("LoanName",nameOfLoan)
                .addQueryParameter("originalAmount",principalAmount)
                .addQueryParameter("duration",durationOfLoan)
                .addQueryParameter("frequency",paymentFreq)
                .addQueryParameter("category",category)
                .addQueryParameter("interest",interest)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl,new Callback(){

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
                    if (response.code() != 200) {
                        Platform.runLater(() -> {
                            String responseBody = null;
                            try {
                                responseBody = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            errorAlert.setContentText(responseBody);
                            errorAlert.showAndWait();
                        });
                    }
                    else {
                        Platform.runLater(() -> {
                            String rawBody = null;
                            try {
                                rawBody = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            LoanDTOs newLoanAsLoaner = GSON_INSTANCE.fromJson(rawBody, LoanDTOs.class);
                            //LoansAsLoaner.getItems().add(newLoanAsLoaner);
                        });
                    }
                });
            }
        });
        resetTakeOutLoan();
    }

    @FXML
    void findLoansBtClicked(ActionEvent event) {
        String balance = balanceOfCustomer.getText().substring(9);
        if(balance.isEmpty())
            balance = "0";
        if(Integer.parseInt(balance) > Integer.parseInt(amountToInvest.getText())) {
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
        StringJoiner joiner = new StringJoiner(",");
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

    void resetTakeOutLoan(){
        Platform.runLater(() ->{
            LoanNameTakeLoanTA.clear();
            principalAmountTA.clear();
            totalDurationTakeLoanTA.clear();
            paymentFreqTakeLoanTA.clear();
            categoryTakeLoanTA.clear();
            InterestTakeLoanTA.clear();
        });
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

    //void makeSelectedChecked

    @FXML
    void fullPaymentClicked(ActionEvent event) {

        List<String> LoansToClose = customerInfoTables.getLoansAsLoanerDataForPaymentTab().getItems().stream()
                .filter(L -> L.isSelected()).collect(Collectors.toMap(LoanDTOs::getNameOfLoan,loan -> loan))
                .keySet().stream().collect(Collectors.toList());
        if (LoansToClose.size() != 0) {
            Type type = new TypeToken<List<String>>(){}.getType();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.Companion.create(GSON_INSTANCE.toJson(LoansToClose,type),mediaType);


            String finalUrl = HttpUrl
                    .parse(Constants.LOANSPAYMENT)
                    .newBuilder()
                    .addQueryParameter("typeOfPayment","full")
                    .addQueryParameter("AutoPayment","manual")
                    .build()
                    .toString();

            HttpClientUtil.runAsyncWithBodyForPost(finalUrl,requestBody, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        errorAlert.setContentText(String.valueOf(e));
                        errorAlert.showAndWait();
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();//Name of loans that the user has enough money to pay for.
                        Platform.runLater(()->{
                                confirmationAlert.setContentText("You can not pay fully on the loans that you choose but you can pay the loans: " + responseBody + " Press OK to continue the process");
                                confirmationAlert.showAndWait();
                                if(confirmationAlert.getResult().getText().equals("OK")){
                                    fullPayTheMinimumLoansThatUserCanPay(requestBody);
                                }
                        });
                    }
                }
            });

        }
    }

    @FXML
    void yazlyPaymentClicked(ActionEvent event) {
        List<String> nameOfLoansThatCanBePaid = new ArrayList<>();
        List<LoanDTOs> loansToPay = customerInfoTables.getLoansAsLoanerDataForPaymentTab().getItems().stream().collect(Collectors.toList());
        Map<String,Integer> loansToPayAndAmountOfPayment = new HashMap<>();
        for(LoanDTOs curLoan : loansToPay){
            if(curLoan.getAmountToPay().isEmpty())
                loansToPay.remove(curLoan);//TODO debug and make sure if user dosnt insert amount to pay its empty
        }
        if(loansToPay.size() != 0) {
            Type type = new TypeToken<List<LoanDTOs>>(){}.getType();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.Companion.create(GSON_INSTANCE.toJson(loansToPay,type),mediaType);

            String finalUrl = HttpUrl
                    .parse(Constants.LOANSPAYMENT)
                    .newBuilder()
                    .addQueryParameter("typeOfPayment","yazly")
                    .addQueryParameter("AutoPayment","manual")
                    .build()
                    .toString();
            HttpClientUtil.runAsyncWithBodyForPost(finalUrl,requestBody, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        errorAlert.setContentText(String.valueOf(e));
                        errorAlert.showAndWait();
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();//Name of loans that the user has enough money to pay for.
                        Platform.runLater(() -> {
                            if(responseBody.isEmpty()){
                                errorAlert.setContentText("you dont have enough money to pay for any of the loans");
                                errorAlert.showAndWait();
                            }
                            else {
                                confirmationAlert.setContentText("You can not pay all the loans that you choose but you can pay the loans: " + responseBody + " Press OK to continue the process");
                                confirmationAlert.showAndWait();
                                if (confirmationAlert.getResult().getText().equals("OK")) {
                                    yazliPayTheMinimumLoansThatUserCanPay(loansToPay);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void yazliPayTheMinimumLoansThatUserCanPay(List<LoanDTOs> i_loansToPay) {
        Type type = new TypeToken<List<LoanDTOs>>(){}.getType();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.Companion.create(GSON_INSTANCE.toJson(i_loansToPay,type),mediaType);

        String finalUrl = HttpUrl
                .parse(Constants.LOANSPAYMENT)
                .newBuilder()
                .addQueryParameter("typeOfPayment","yazly")
                .addQueryParameter("AutoPayment","auto")//auto mean tell system to pay the loans the loans the user able to pay and manual is ask the user if he wants to pay what he can becaus he dosnet have enoug money to all the loans
                .build()
                .toString();
        HttpClientUtil.runAsyncWithBodyForPost(finalUrl,requestBody,new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    errorAlert.setContentText(String.valueOf(e));
                    errorAlert.showAndWait();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //nothing to do if successful
            }
        });


    }

    private void fullPayTheMinimumLoansThatUserCanPay(RequestBody requestBody ) {
        String finalUrl = HttpUrl
                .parse(Constants.LOANSPAYMENT)
                .newBuilder()
                .addQueryParameter("typeOfPayment","full")
                .addQueryParameter("AutoPayment","auto")//auto mean tell system to pay the loans the loans the user able to pay and manual is ask the user if he wants to pay what he can becaus he dosnet have enoug money to all the loans
                .build()
                .toString();
        HttpClientUtil.runAsyncWithBodyForPost(finalUrl,requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    errorAlert.setContentText(String.valueOf(e));
                    errorAlert.showAndWait();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
             //nothing to do if successful
            }
        });


    }

    @FXML
    void investBtClicked(ActionEvent event) {

        String maxOwnerShipOfTheLoan = "100";
        if(!maxLoanOwner.getText().isEmpty()){
            maxOwnerShipOfTheLoan = maxLoanOwner.getText();
        }
        StringJoiner joiner = new StringJoiner(", ");
        checkLoansToInvest.getCheckModel().getCheckedItems().stream().forEach(joiner::add);
        String LoansToInvest = joiner.toString();
        String finalUrl = HttpUrl
                .parse(Constants.INVESMENTS)
                .newBuilder()
                .addQueryParameter("maxLoanOwnership",maxOwnerShipOfTheLoan)
                .addQueryParameter("amountToInvest",amountToInvest.getText())
                .addQueryParameter("LoansNames", LoansToInvest)
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
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        errorAlert.setContentText("Something went wrong: " + responseBody);
                    });
                }
            }
        });

        checkLoansToInvest.getCheckModel().clearChecks();
        resetScrambleTab();
    }

    @FXML
    void selectAllLoansToInvestBtClicked(ActionEvent event) {
        if(selectAllLoansToInvest.isSelected())
            checkLoansToInvest.getCheckModel().checkAll();
        else{
            checkLoansToInvest.getCheckModel().clearChecks();
        }
    }

    @FXML
    void openReadMeClicked(ActionEvent event) throws IOException {
        File readMeFile = new File("/Users/dan/Downloads/ABS part 3 2/ClientCommons/readme.rtf");
        Desktop.getDesktop().edit(readMeFile);
    }

    @FXML
    void resetSearchBtClicked(ActionEvent event) {
        resetScrambleTab();
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
        customerInfoTables = new customerDataTables(this,LoansAsLoaner,LoansAsLender,LoansAsLoanerTableForPaymentTab,relevantLoans,LoansToSellTable,LoansToBuyTable);
        customerInfoTables.getTransactionTable().prefWidthProperty().bind(AccountTransInfo.widthProperty());
        customerInfoTables.getTransactionTable().prefHeightProperty().bind(AccountTransInfo.heightProperty());
        AccountTransInfo.getChildren().setAll(customerInfoTables.getTransactionTable());
        welcomeCustomer.setText("Hello " + curCustomerName);
        startRefresher();
    }

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
        List<String> LoansToSell = LoansToSellTable.getItems().stream()
                .filter(L -> L.isSelected())
                .collect(Collectors.toMap(LoanDTOs::getNameOfLoan,loan -> loan))
                .keySet().stream().collect(Collectors.toList());

        Type type = new TypeToken<List<String>>(){}.getType();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(
                mediaType, GSON_INSTANCE.toJson(LoansToSell,type));

        String finalUrl = HttpUrl
                .parse(Constants.SELL_LOANS)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncWithBodyForPost(finalUrl,requestBody, new Callback(){

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
                    //TODO maya im not sure if we need to do something after the customer put to sale one or more loans.
                });
            }
        });
    }

    @FXML
    void BuyLoansClicked(ActionEvent event) {
        List<String> LoansToBuy = LoansToBuyTable.getItems().stream()
                .filter(L -> L.isSelected())
                .collect(Collectors.toMap(LoanDTOs::getNameOfLoan,loan -> loan))
                .keySet().stream().collect(Collectors.toList());

        Type type = new TypeToken<List<String>>(){}.getType();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.Companion.create(GSON_INSTANCE.toJson(LoansToBuy,type),mediaType);//the

        String finalUrl = HttpUrl
                .parse(Constants.BUY_LOANS)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncWithBodyForPost(finalUrl,requestBody, new Callback(){

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    errorAlert.setContentText(String.valueOf(e));
                    errorAlert.showAndWait();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String rawBody = response.body().string();
                Boolean FromJson = GSON_INSTANCE.fromJson(rawBody, Boolean.class);
                if(!FromJson) {
                    errorAlert.setContentText("You dont have money to buy all the loans you want.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    public void startRefresher() {
        clientRefresher = new CustomerInfoRefresher(this::updateTableLoansAsLoaner,
                this::updateTableLoansAsLenderAndLoanTableForPaymentTab
                ,this::updateTableLoansToSellTable
                ,this::updateTableLoansToBuyTable
                ,this::updateTableNotificationsView
                ,this::updateTransactionTable
                ,this::updateYazLB
                ,this::disableBT
                ,this::updateCategories
        ,this::balanceLBUpdate
        ,curCustomerName, this::clearAllTables);
        timer = new Timer();
        timer.schedule(clientRefresher, 2000, 2000);
    }

    public  void updateTableLoansAsLoaner(List<LoanDTOs> allLoans){
        List<LoanDTOs> tmp = allLoans.stream().filter(l -> l.getNameOfLoaner().equals(curCustomerName)).collect(Collectors.toList());

        Platform.runLater(() ->{
            if(LoansAsLoaner.getItems().size() != tmp.size()){
                customerInfoTables.addLoanToLoansAsLoanerTable(tmp);
            }
            else {
                Map<String, Set<Integer>> versionsOfLoans = new HashMap<>();
                for (LoanDTOs curLoan : tmp) {
                    Set<Integer> mySet = new HashSet<Integer>();
                    mySet.add(curLoan.getVersion());
                    versionsOfLoans.put(curLoan.getNameOfLoan(), mySet);
                }
                for (LoanDTOs curLoan : LoansAsLoaner.getItems()) {
                    if(!versionsOfLoans.containsKey(curLoan.getNameOfLoan())){
                        customerInfoTables.addLoanToLoansAsLoanerTable(tmp);
                    }
                    else{
                        versionsOfLoans.get(curLoan.getNameOfLoan()).add(curLoan.getVersion());
                    }
                }
                if(!versionsOfLoans.values().stream().filter(L -> (L.size() > 1)).collect(Collectors.toList()).isEmpty()){
                    customerInfoTables.addLoanToLoansAsLoanerTable(tmp);
                }
            }
        });
    }

    public void updateTableLoansAsLenderAndLoanTableForPaymentTab(List<LoanDTOs> allLoans){
        List<LoanDTOs> tmp = allLoans.stream().filter(L -> L.getListOfLenders().containsKey(curCustomerName)).collect(Collectors.toList());
        List<LoanDTOs> loansForPaymentTab = allLoans.stream().filter(L -> L.getNameOfLoaner().equals(curCustomerName)).collect(Collectors.toList());
        Platform.runLater(() ->{
            if(LoansAsLender.getItems().size() != tmp.size()){
                customerInfoTables.addLoanToLoansAsLenderTable(tmp);
            }
            else {
                Map<String, Set<Integer>> versionsOfLoans = new HashMap<>();
                for (LoanDTOs curLoan : tmp) {
                    Set<Integer> mySet = new HashSet<Integer>();
                    mySet.add(curLoan.getVersion());
                    versionsOfLoans.put(curLoan.getNameOfLoan(), mySet);
                }
                for (LoanDTOs curLoan : LoansAsLender.getItems()) {
                    if(!versionsOfLoans.containsKey(curLoan.getNameOfLoan())){
                        customerInfoTables.addLoanToLoansAsLenderTable(tmp);
                    }
                    else{
                        versionsOfLoans.get(curLoan.getNameOfLoan()).add(curLoan.getVersion());
                    }
                }
                if(!versionsOfLoans.values().stream().filter(L -> (L.size() > 1)).collect(Collectors.toList()).isEmpty()){
                    customerInfoTables.addLoanToLoansAsLenderTable(tmp);
                }
            }
            if(LoansAsLoanerTableForPaymentTab.getItems().size() != loansForPaymentTab.size()){
                customerInfoTables.updateLoanAsLoanerForPaymentTab(loansForPaymentTab);
            }
            else {
                Map<String, Set<Integer>> versionsOfLoans2 = new HashMap<>();
                for (LoanDTOs curLoan : loansForPaymentTab) {
                    Set<Integer> mySet = new HashSet<Integer>();
                    mySet.add(curLoan.getVersion());
                    versionsOfLoans2.put(curLoan.getNameOfLoan(), mySet);
                }
                for (LoanDTOs curLoan : LoansAsLoanerTableForPaymentTab.getItems()) {
                    if(!versionsOfLoans2.containsKey(curLoan.getNameOfLoan())){
                        customerInfoTables.updateLoanAsLoanerForPaymentTab(loansForPaymentTab);
                    }
                    else{
                        versionsOfLoans2.get(curLoan.getNameOfLoan()).add(curLoan.getVersion());
                    }
                }
                if(!versionsOfLoans2.values().stream().filter(L -> (L.size() > 1)).collect(Collectors.toList()).isEmpty()){
                    customerInfoTables.updateLoanAsLoanerForPaymentTab(loansForPaymentTab);
                }
            }
        });
    }

    public void updateCategories(List<String> i_allCategories){
        Platform.runLater(() -> {
            if (categories.getItems().size() < i_allCategories.size()) {
                categories.getItems().clear();
                categories.getItems().addAll(i_allCategories);
            }
        });
    }

    public void updateTableLoansToSellTable(List<LoanDTOs> allLoans){
        List<LoanDTOs> tmp = allLoans.stream().filter(L -> L.getListOfLenders().containsKey(curCustomerName))
                .collect(Collectors.toList()).stream().filter(L -> L.getStatusName().equals("ACTIVE")).collect(Collectors.toList());
        Platform.runLater(() ->{
           if(LoansToSellTable.getItems().size() != tmp.size()){
               customerInfoTables.addLoanToLoansForSellTable(tmp);
           }
           else {
               Map<String, Set<Integer>> versionsOfLoans = new HashMap<>();
               for (LoanDTOs curLoan : tmp) {
                   Set<Integer> mySet = new HashSet<Integer>();
                   mySet.add(curLoan.getVersion());
                   versionsOfLoans.put(curLoan.getNameOfLoan(), mySet);
               }
               for (LoanDTOs curLoan : LoansToSellTable.getItems()) {
                   if(!versionsOfLoans.containsKey(curLoan.getNameOfLoan())){
                       customerInfoTables.addLoanToLoansForSellTable(tmp);
                   }
                   else{
                       versionsOfLoans.get(curLoan.getNameOfLoan()).add(curLoan.getVersion());
                   }
               }
               if(!versionsOfLoans.values().stream().filter(L -> (L.size() > 1)).collect(Collectors.toList()).isEmpty()){
                   customerInfoTables.addLoanToLoansForSellTable(tmp);
               }
           }
        });
    }

    public void updateTableLoansToBuyTable(List<LoanDTOs> loansToBuy){
        Platform.runLater(() ->{
            if(LoansToBuyTable.getItems().size() != loansToBuy.size()){
                customerInfoTables.addLoanToLoansForBuyTable(loansToBuy);
            }
            else {
                Map<String, Set<Integer>> versionsOfLoans = new HashMap<>();
                for (LoanDTOs curLoan : loansToBuy) {
                    Set<Integer> mySet = new HashSet<Integer>();
                    mySet.add(curLoan.getVersion());
                    versionsOfLoans.put(curLoan.getNameOfLoan(), mySet);
                }
                for (LoanDTOs curLoan : LoansToBuyTable.getItems()) {
                    if(!versionsOfLoans.containsKey(curLoan.getNameOfLoan())){
                        customerInfoTables.addLoanToLoansForBuyTable(loansToBuy);
                    }
                    else{
                        versionsOfLoans.get(curLoan.getNameOfLoan()).add(curLoan.getVersion());
                    }
                }
                if(!versionsOfLoans.values().stream().filter(L -> (L.size() > 1)).collect(Collectors.toList()).isEmpty()){
                    customerInfoTables.addLoanToLoansForBuyTable(loansToBuy);
                }
            }
        });
    }//TODO

    public void updateTableNotificationsView(List<CustomerDTOs> allNotifications){
        Platform.runLater(() ->{
            allNotifications.stream().filter(L -> L.getName().equals(curCustomerName)).collect(Collectors.toList()).get(0).getNotifications();
            if(allNotifications.stream().filter(L -> L.getName().equals(curCustomerName)).collect(Collectors.toList()).get(0).getNotifications().size() !=  notificationsView.getItems().size()) {
                notificationsView.getItems().clear();
                notificationsView.getItems().addAll(allNotifications.stream().filter(L -> L.getName().equals(curCustomerName)).collect(Collectors.toList()).get(0).getNotifications());
            }
        });
    }

    public void updateTransactionTable(List<CustomerDTOs> allCustomers){
        Platform.runLater(() ->{
            customerInfoTables.addTransactionsToTable(allCustomers.stream().filter(L -> L.getName().equals(curCustomerName)).collect(Collectors.toList()).get(0).getTransactions());
        });
    }

    public void updateYazLB(Integer yaz){
        Platform.runLater(()-> yazLB.setText("Yaz: " + yaz));
    }

    public void balanceLBUpdate(List<CustomerDTOs> allCustomers){
        Platform.runLater(() -> balanceOfCustomer.setText("Balance: " + allCustomers.stream().filter(L -> L.getName().equals(curCustomerName)).collect(Collectors.toList()).get(0).getBalance()));
    }

    public void disableBT(Boolean isRewind){
        if(isRewind){
            Platform.runLater(()->{
                String yazTmp = yazLB.getText();
                yazLB.setText(yazTmp + " (Rewind)");
            });
        }

        Platform.runLater(() -> {
            amountToInvest.setDisable(isRewind);
            ChargeBT.setDisable(isRewind);
            WithdrawBT.setDisable(isRewind);
            findLoans.setDisable(isRewind);
            resetSearch.setDisable(isRewind);
            invest.setDisable(isRewind);
            selectAllLoansToInvest.setDisable(isRewind);
            fullPayment.setDisable(isRewind);
            yazlyPayment.setDisable(isRewind);
            takeOutLoanBT.setDisable(isRewind);
            SellLoanBT.setDisable(isRewind);
            BuyLoansBT.setDisable(isRewind);
            LoanNameTakeLoanTA.setDisable(isRewind);
            categoryTakeLoanTA.setDisable(isRewind);
            principalAmountTA.setDisable(isRewind);
            totalDurationTakeLoanTA.setDisable(isRewind);
            paymentFreqTakeLoanTA.setDisable(isRewind);
            InterestTakeLoanTA.setDisable(isRewind);
            categories.setDisable(isRewind);
            minimumYaz.setDisable(isRewind);
            AmountTB.setDisable(isRewind);
            maxOpenLoans.setDisable(isRewind);
            maxLoanOwner.setDisable(isRewind);
            minInterest.setDisable(isRewind);
            loadFile.setDisable(isRewind);
        });

    }

    public void clearAllTables(Integer yaz){
        Platform.runLater(() -> {
        LoansAsLoaner.getItems().clear();
        LoansAsLender.getItems().clear();
        LoansToSellTable.getItems().clear();
        notificationsView.getItems().clear();
        customerInfoTables.clearTransactionsToTable();
        balanceOfCustomer.setText("balance: ");
        });
    }

}
