package DTOs;

import BankActions.Loan;
import BankActions.LoanStatus;
import BankActions.Payment;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import java.io.Serializable;
import java.util.List;

public class LoanDTOs implements Serializable {

    private String nameOfLoan;
    private String category;
    private String statusName;
    private String nameOfLoaner;
    private Boolean selected;
    private String amountToPay;
    private Integer nextYazPayment;
    private Integer amountToPayThisYaz;

    private final int originalAmount;
    private int amountRaisedSoFar;
    private final int durationOfTheLoan;
    private final int paymentFrequency;
    private final int interest;
    private final int theDateTheLoanBecameActive;
    private Map<String, Integer> listOfLenders;
    private final int howManyUnitsOfTimeAreLeft; //TODO: wtf does it mean
    private final float interestPayedSoFar;
    private final float theInterestYetToBePaidOnTheLoan;
    private final int theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar;
    private final int theAmountOfTheFundYetToBePaid;
    private final LoanStatus status;
    private final List<Payment> loansPayments;
    private int howMuchLeftToMakeLoanActive;
    private int nextPaymentYaz;
    private int endDate;
    private int principalYetToBePaid;
    private int interestYetToPaid;
    private int totalMissedYazNeedToPayBack;
    private int debt;
    private int version;
    private Boolean isForSale = false;
    private Map<String, Integer> mapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment = new HashMap<>();
    private String seller = " ";
    private Integer price = 0;


    public LoanDTOs(Loan loan){
        nameOfLoan = loan.getNameOfLoan();
        version = loan.getVersion();
        category = loan.getCategory();
        nextYazPayment = loan.getNextYazForPayment();
        originalAmount = loan.getOriginalAmount();
        nameOfLoaner = loan.getNameOfLoaner();
        durationOfTheLoan = loan.getDurationOfTheLoan();
        paymentFrequency = loan.getPaymentFrequency();
        howManyUnitsOfTimeAreLeft = loan.getHowManyYazAreLeft();
        interest = loan.getInterest();
        listOfLenders = loan.getListOfLenders();
        interestPayedSoFar = loan.getInterestPayedSoFar();
        theInterestYetToBePaidOnTheLoan = loan.getTheInterestYetToBePaidOnTheLoan();
        theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar = loan.getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar();
        theAmountOfTheFundYetToBePaid = loan.getTheAmountOfPrincipalPaymentYetToBePaid();
        status = loan.getStatus();
        statusName = status.name();
        nextPaymentYaz = loan.getNextYazForPayment();
        amountRaisedSoFar = loan.getOriginalAmount() - loan.getTheAmountLeftToMakeTheLoanActive();
        howMuchLeftToMakeLoanActive = loan.getTheAmountLeftToMakeTheLoanActive();
        theDateTheLoanBecameActive = loan.getStartingDate();
        endDate = loan.getFinishDate();
        loansPayments = loan.getPayments();
        amountToPayThisYaz = loan.getYazlyPaymentWithDebtsCalculation(nextPaymentYaz);
        totalMissedYazNeedToPayBack = loan.getTotalMissedYazNeedToPayBack();
        debt = loan.getDebt();
        isForSale = loan.getForSale();
        mapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment = loan.getMapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment();
    }

    public LoanDTOs(String seller, Integer price, String loanName){
        this.seller = seller;
        this.price = price;
        this.nameOfLoan = loanName;
        originalAmount = 0;
        amountRaisedSoFar = 0;
        durationOfTheLoan = 0;
        paymentFrequency = 0;
        interest = 0;
        theDateTheLoanBecameActive = 0;
        listOfLenders = null;
        howManyUnitsOfTimeAreLeft = 0;
        interestPayedSoFar = 0;
        theInterestYetToBePaidOnTheLoan = 0;
        theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar = 0;
        theAmountOfTheFundYetToBePaid = 0;
        status = null;
        loansPayments = null;
    }

    public Map<String, Integer> getMapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment() {
        return mapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public Integer getPrice() {
        return price;
    }

    public int getVersion() {
        return version;
    }

    public Boolean getForSale() {
        return isForSale;
    }

    public int getEndDate() {
        return endDate;
    }

    public int getTheDateTheLoanBecameActive() {
        return theDateTheLoanBecameActive;
    }

    public int getHowMuchLeftToMakeLoanActive() {
        return howMuchLeftToMakeLoanActive;
    }

    public String getNameOfLoan() {
        return nameOfLoan;
    }

    public int getAmountToPayThisYaz() {
        return amountToPayThisYaz;
    }


    public int getAmountRaisedSoFar() {
        return amountRaisedSoFar;
    }

    public String getCategory() {
        return category;
    }

    public int getOriginalAmount() {
        return originalAmount;
    }

    public String getNameOfLoaner() {
        return nameOfLoaner;
    }

    public int getDurationOfTheLoan() {
        return durationOfTheLoan;
    }

    public int getPaymentFrequency() {
        return paymentFrequency;
    }

    public int getInterest() {
        return interest;
    }

    public Map<String, Integer> getListOfLenders() {
        return listOfLenders;
    }

    public float getInterestPayedSoFar() {
        return interestPayedSoFar;
    }

    public float getTheInterestYetToBePaidOnTheLoan() {
        return theInterestYetToBePaidOnTheLoan;
    }

    public int getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar() {
        return theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar;
    }

    public int getTheAmountOfTheFundYetToBePaid() {
        return theAmountOfTheFundYetToBePaid;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName;
    }

    public List<Payment> getLoansPayments() {
        return loansPayments;
    }

    public boolean isSelected() {return selected;}

    public String getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(String amountToPay) {this.amountToPay = amountToPay;}

    public int getNextYazPayment() {
        return nextYazPayment;
    }

    public int getTotalMissedYazNeedToPayBack() {
        return totalMissedYazNeedToPayBack;
    }

    public int getDebt() {
        return debt;
    }

}
