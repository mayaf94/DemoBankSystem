package customerMainApp;

import DTOs.AccountTransactionDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import clientController.ClientController;
import component.loansComponent.ViewLoansInfo.ViewLoansInfoController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class customerDataTables implements Serializable {

    private TableView<LoanDTOs> LoansAsLoanerData;
    private TableView<LoanDTOs> LoansAsLenderData;
    private TableView<LoanDTOs> MatchinLoansForScramble;
    private TableView<LoanDTOs> LoansForSell;
    private TableView<LoanDTOs> LoansToBuy;
    private TableView<LoanDTOs> LoansAsLoanerDataForPaymentTab = new TableView<>();
    private TableView<AccountTransactionDTO> TransactionTable = new TableView<>();
    ViewLoansInfoController loansInfoController = new ViewLoansInfoController();
    ClientController clientController;

    public customerDataTables(ClientController i_clientController,TableView<LoanDTOs> i_LoansAsLoanerData,TableView<LoanDTOs> i_LoansAsLenderData,TableView<LoanDTOs> i_MatchinLoansForScramble,TableView<LoanDTOs> i_LoansForSell,TableView<LoanDTOs> i_LoansForBuy) {
        clientController = i_clientController;
        List<LoanDTOs> lst = new ArrayList<>();
        loansInfoController.setMainController(clientController);
        LoansAsLoanerData = i_LoansAsLoanerData;
        LoansAsLenderData = i_LoansAsLenderData;
        LoansForSell = i_LoansForSell;
        LoansToBuy = i_LoansForBuy;
        MatchinLoansForScramble = i_MatchinLoansForScramble;
        buildLoansForSellOrBuyTable(LoansToBuy);
        buildLoansForSellOrBuyTable(LoansForSell);
        loansInfoController.buildLoansTableView(MatchinLoansForScramble);
        loansInfoController.buildLoansTableView(LoansAsLoanerData);
        loansInfoController.buildLoansTableView(LoansAsLenderData);
        //buildLoansTableForPaymentTab();
        buildTransactionsTable();
    }

    private void buildLoansForSellOrBuyTable(TableView<LoanDTOs> LoansTradeTable){
        loansInfoController.buildLoansTableView(LoansTradeTable);
        final TableColumn<LoanDTOs, Integer> price = new TableColumn<>( "Price" );
        price.setCellValueFactory( new PropertyValueFactory<>("price"));

        LoansTradeTable.getColumns().add(price);
        price.setPrefWidth(150);

        final TableColumn<LoanDTOs, Boolean> selectedColumn = new TableColumn<>( "Select" );
        selectedColumn.setCellValueFactory( new PropertyValueFactory<>("select"));
        selectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectedColumn));
        selectedColumn.setPrefWidth(125);
        LoansTradeTable.getColumns().add( selectedColumn );
        selectedColumn.getTableView().setEditable(true);

    }

/*    private void buildLoansTableForPaymentTab(){
        List<LoanDTOs> filteredLoansActiveAndRisk = clientController.getSystemCustomerLoansByListOfLoansName(customer.getLoansAsABorrower()).stream().filter(L -> (L.getStatusName().equals("ACTIVE") || L.getStatusName().equals("RISK"))).collect(Collectors.toList());
        loansInfoController.buildLoansTableView(LoansAsLoanerDataForPaymentTab,filteredLoansActiveAndRisk);

        final TableColumn<LoanDTOs, Integer> nextYazPayment = new TableColumn<>( "Next yaz payment" );
        nextYazPayment.setCellValueFactory( new PropertyValueFactory<>("nextYazPayment"));
        LoansAsLoanerDataForPaymentTab.getColumns().add(nextYazPayment);
        nextYazPayment.setPrefWidth(150);

        final TableColumn<LoanDTOs, Integer> amountToPayThisYaz = new TableColumn<>( "Amount To Pay This Yaz" );
        amountToPayThisYaz.setCellValueFactory( new PropertyValueFactory<>("AmountToPayThisYaz"));
        LoansAsLoanerDataForPaymentTab.getColumns().add(amountToPayThisYaz);
        amountToPayThisYaz.setPrefWidth(125);


        final TableColumn<LoanDTOs, String> amountToPayCol = new TableColumn<>("Amount to pay");
        amountToPayCol.setCellValueFactory( new PropertyValueFactory<>("AmountToPay"));
        amountToPayCol.setPrefWidth(120);
        amountToPayCol.setCellFactory(TextFieldTableCell.forTableColumn());
        amountToPayCol.setOnEditCommit(
                event -> {
                    if((event.getTableView().getItems().get(event.getTablePosition().getRow()).getNextYazPayment()) != clientController.getCurrentYaz()){
                        event.getTableView().getItems().get(event.getTablePosition().getRow()).setAmountToPay("0");
                    }
                    else{
                        event.getTableView().getItems().get(event.getTablePosition().getRow()).setAmountToPay(event.getNewValue());
                    }
                }
        );


        LoansAsLoanerDataForPaymentTab.getColumns().add( amountToPayCol );
        amountToPayCol.getTableView().setEditable(true);

        final TableColumn<LoanDTOs, Boolean> selectedColumn = new TableColumn<>( "Selected" );
        selectedColumn.setCellValueFactory( new PropertyValueFactory<>("selected"));
        selectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectedColumn));
        selectedColumn.setPrefWidth(125);
        LoansAsLoanerDataForPaymentTab.getColumns().add( selectedColumn );
        selectedColumn.getTableView().setEditable(true);

    }
*///TODO buildLoansTableForPaymentTab

    public TableView<LoanDTOs> getLoansAsLoanerDataForPaymentTab() {
        return LoansAsLoanerDataForPaymentTab;
    }

    public TableView<LoanDTOs> getLoansAsLoanerData() {
        return LoansAsLoanerData;
    }

    public TableView<LoanDTOs> getLoansAsLenderData() {
        return LoansAsLenderData;
    }

    public TableView<AccountTransactionDTO> getTransactionTable() {
        return TransactionTable;
    }

    private void buildTransactionsTable(){

        TableColumn<AccountTransactionDTO, String> TransactionType = new TableColumn<>("Type Of Transaction");
        TransactionType.setCellValueFactory(new PropertyValueFactory<>("TransactionType"));
        TransactionType.setPrefWidth(150);

        TableColumn<AccountTransactionDTO, String> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //amount.setPrefWidth(50);

        TableColumn<AccountTransactionDTO, String> yazOfAction = new TableColumn<>("Yaz of action");
        yazOfAction.setCellValueFactory(new PropertyValueFactory<>("yazOfAction"));
        yazOfAction.setPrefWidth(120);

        TableColumn<AccountTransactionDTO, String> previousBalance = new TableColumn<>("Previous balance");
        previousBalance.setCellValueFactory(new PropertyValueFactory<>("previousBalance"));
        previousBalance.setPrefWidth(120);

        TableColumn<AccountTransactionDTO, String> curBalance = new TableColumn<>("Current balance");
        curBalance.setCellValueFactory(new PropertyValueFactory<>("curBalance"));
        curBalance.setPrefWidth(110);

        TransactionTable.getColumns().addAll(TransactionType, amount, yazOfAction, previousBalance,curBalance);
    }

    public void addTransactionsToTable(List<AccountTransactionDTO> transaction){
        TransactionTable.getItems().addAll(transaction);
    }


    public void addLoanToLoansAsLoanerTable(List<LoanDTOs> loans){
        LoansAsLoanerData.getItems().addAll(loans);
    }

    public void addLoanToLoansAsLenderTable(List<LoanDTOs> loans){
        LoansAsLenderData.getItems().addAll(loans);
    }

   /* public void updateLoansTables(CustomerDTOs curCustomer){
        List<LoanDTOs> loansAsLoaner = clientController.getSystemCustomerLoansByListOfLoansName(curCustomer.getLoansAsABorrower());
        updateLoansAsLender(clientController.getSystemCustomerLoansByListOfLoansName(curCustomer.getLoansAsALender()));
        updateLoansAsLoaner(loansAsLoaner);
        updateLoanAsLoanerForPaymentTab(loansAsLoaner);
        updateTransactionTable(curCustomer);
    }*/

   public void updateMatchingLoansForScramble(List<LoanDTOs> i_loansAsLender){
       MatchinLoansForScramble.getItems().clear();
       MatchinLoansForScramble.getItems().addAll(i_loansAsLender);
       MatchinLoansForScramble.refresh();
   }

    public void updateLoansAsLender(List<LoanDTOs> i_loansAsLender){
        LoansAsLenderData.getItems().clear();
        LoansAsLenderData.getItems().addAll(i_loansAsLender);
        LoansAsLenderData.refresh();
    }

    private void updateLoansAsLoaner(List<LoanDTOs> i_loansAsLoaner){
        LoansAsLoanerData.getItems().clear();
        LoansAsLoanerData.getItems().addAll(i_loansAsLoaner);
        LoansAsLoanerData.refresh();
        LoansAsLoanerData.refresh();
    }

    private void updateTransactionTable(CustomerDTOs curCustomer){
        List<AccountTransactionDTO> Transactions = curCustomer.getTransactions();
        TransactionTable.getItems().clear();
        TransactionTable.getItems().addAll(Transactions);
        TransactionTable.refresh();

    }

    private void updateLoanAsLoanerForPaymentTab(List<LoanDTOs> i_loansAsLoaner){
        LoansAsLoanerDataForPaymentTab.getItems().clear();
        List<LoanDTOs> filteredLoansActiveAndRisk = i_loansAsLoaner.stream().filter(L -> (L.getStatusName().equals("ACTIVE") || L.getStatusName().equals("RISK"))).collect(Collectors.toList());
        LoansAsLoanerDataForPaymentTab.getItems().addAll(filteredLoansActiveAndRisk);
        LoansAsLoanerDataForPaymentTab.refresh();
    }


}
