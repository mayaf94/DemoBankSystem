package BankSystem;

import DTOs.*;
import SystemExceptions.InccorectInputType;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BankSystem extends Serializable {

    List<LoanDTOs>          ReadingTheSystemInformationFile(String FileName, String customerName);
    AccountTransactionDTO   DepositToAccount(int amount, String nameOfCostumerToDepositTo);
    AccountTransactionDTO   WithdrawFromTheAccount(int amount, String nameOfCostumerToDepositTo);
    List<LoanDTOs>          ActivationOfAnInlay(List<String> chosenCategories, int minimumDuration, int minimumInterestForSingleYaz,int maxOpenLoansForLoanOwner, String name);
    CustomerDTOs            LoansInlay(List<String> namesOfLoans,int amountOfMoneyUserWantToInvest,String nameOfLender,int maxOwnerShipOfTheLoan);
    BankSystemDTO IncreaseYaz();
    List<CustomerDTOs>      getListOfDTOsCustomer();
    boolean                 checkIfCustomerHasEnoughMoneyToInvestByGivenAmount(String i_nameOfCustomer,int amountToInvest);
    List<LoanDTOs>          getListOfLoansDTO();
    List<LoanDTOs>          getListOfLoansDtoByListOfNamesOFLoans(List<String> i_loansName);
    SimpleStringProperty    getYazProperty();
    void                    fullPaymentOnLoans(List<String> loanNames, String customerName);
    CategoriesDTO           getAllCategories();
    CustomerDTOs            getCustomerByName(String name);
    int                     getCurrentYaz();
    List<String>            checkWhatLoansCanBeFullyPaidSystem(List<String> loanNames, String customerName);
    List<String>            checkIfCanPayAllLoans(Map<String,Integer> loansToPay, String customerName);
    Boolean                 checkIfMoneyCanBeWithdraw(int amount, String customerName);
    void                    setYazProperty();
    void                    YazlyPaymentForGivenLoans(Map<String,Integer> loansToPay);
    void                    addCustomerToBank(String nameOfCustomer);
    Map<String, SimpleStringProperty> getLoanDataByStatusPropertyFromSystemMap(String loanName);
    List<LoanDTOs> getAllLoansThatAreForSale();

    }
