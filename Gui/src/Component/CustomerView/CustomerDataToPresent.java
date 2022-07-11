package Component.CustomerView;

import Component.MainComponent.BankController;
import Component.ViewLoansInfo.ViewLoansInfoController;
import DTOs.AccountTransactionDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDataToPresent implements Serializable {

    CustomerDTOs customer;
    private TableView<LoanDTOs> LoansAsLoanerData = new TableView<>();
    private TableView<LoanDTOs> LoansAsLenderData = new TableView<>();
    private TableView<LoanDTOs> LoansAsLoanerDataForPaymentTab = new TableView<>();
    private TableView<AccountTransactionDTO> TransactionTable = new TableView<>();
    ViewLoansInfoController loansInfoController = new ViewLoansInfoController();
    BankController mainController;

    public CustomerDataToPresent(CustomerDTOs customer, BankController i_mainController) {
        mainController = i_mainController;
        this.customer = customer;
        List<LoanDTOs> lst = new ArrayList<>();
        loansInfoController.setMainController(mainController);
        loansInfoController.buildLoansTableView(LoansAsLoanerData,mainController.getSystemCustomerLoansByListOfLoansName(customer.getLoansAsABorrower()));
        loansInfoController.buildLoansTableView(LoansAsLenderData,mainController.getSystemCustomerLoansByListOfLoansName(customer.getLoansAsALender()));
        buildLoansTableForPaymentTab();
        buildTransactionsTable(customer.getTransactions());
    }

    private void buildLoansTableForPaymentTab(){
        List<LoanDTOs> filteredLoansActiveAndRisk = mainController.getSystemCustomerLoansByListOfLoansName(customer.getLoansAsABorrower()).stream().filter(L -> (L.getStatusName().equals("ACTIVE") || L.getStatusName().equals("RISK"))).collect(Collectors.toList());
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
                    if((event.getTableView().getItems().get(event.getTablePosition().getRow()).getNextYazPayment()) != mainController.getCurrentYaz()){
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

    private void buildTransactionsTable(List<AccountTransactionDTO> customerTransactions ){

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
        TransactionTable.getItems().addAll(FXCollections.observableArrayList(customerTransactions));
    }

    public void updateLoansTables(CustomerDTOs curCustomer){
        List<LoanDTOs> loansAsLoaner = mainController.getSystemCustomerLoansByListOfLoansName(curCustomer.getLoansAsABorrower());
        updateLoansAsLender(mainController.getSystemCustomerLoansByListOfLoansName(curCustomer.getLoansAsALender()));
        updateLoansAsLoaner(loansAsLoaner);
        updateLoanAsLoanerForPaymentTab(loansAsLoaner);
        updateTransactionTable(curCustomer);
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
