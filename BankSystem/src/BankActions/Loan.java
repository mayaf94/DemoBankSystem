package BankActions;

import DTOs.Payment;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Loan implements Serializable {
    private String nameOfLoan;
    private String category;
    private int originalAmount;
    private String nameOfLoaner;
    private int durationOfTheLoan;//the time for the loan
    private int paymentFrequency;
    private int  interest;
    private Map<String, LeftToPay> listOfLenders;
    private int howManyYazAreLeft;
    private int version = 0;
    private int interestPayedSoFar;
    private int theInterestYetToBePaidOnTheLoan;
    private int theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar;
    private int theAmountOfPrincipalPaymentYetToBePaid;
    private  int theAmountLeftToMakeTheLoanActive;
    private Boolean active = false;
    private LoanStatus status = LoanStatus.NEW;
    private int nextYazForPayment;//the next time unit for the payment if the loan is active
    private int startingDate;
    private int finishDate;//will be updated when the loan is finished
    private List<Payment> Payments;
    private int yazlyPayment;
    private int yazlyInterest;
    private int totalMissedYazNeedToPayBack = 0;
    transient private Map<String, SimpleStringProperty> loanDataByStatusPropertyAndStatusProperty;
    private int debt;
    private Boolean isForSale = false;
    private Map<String, Integer> mapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment = new HashMap<>();

    public Map<String, Integer> getMapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment() {
        return mapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment;
    }

    public Loan(String nameOfLoan, String nameOfLoaner, String category, int originalAmount, int durationOfTheLoan,
                int paymentFrequency, int interest) {
        this.nameOfLoan = nameOfLoan;
        this.category = category;
        this.originalAmount = originalAmount;
        this.nameOfLoaner = nameOfLoaner;
        this.durationOfTheLoan = durationOfTheLoan;
        this.paymentFrequency = paymentFrequency;
        this.interest = interest;
        this.howManyYazAreLeft = durationOfTheLoan;
        theAmountLeftToMakeTheLoanActive = originalAmount;
        theAmountOfPrincipalPaymentYetToBePaid = originalAmount;
        listOfLenders = new HashMap<>();
        Payments = new ArrayList<>();
        yazlyPayment = 0;
        yazlyInterest =0;
        theInterestYetToBePaidOnTheLoan = (originalAmount * interest) / 100;
        loanDataByStatusPropertyAndStatusProperty = new HashMap<>();
        loanDataByStatusPropertyAndStatusProperty.put("LoanDataByStatusProperty", new SimpleStringProperty());
        loanDataByStatusPropertyAndStatusProperty.get("LoanDataByStatusProperty").set("0$ Raised so far ");
        loanDataByStatusPropertyAndStatusProperty.put("statusProperty", new SimpleStringProperty(LoanStatus.NEW.name()));
    }

    public Map<String, SimpleStringProperty> getLoanDataByStatusPropertyAndStatusProperty() {
        return loanDataByStatusPropertyAndStatusProperty;
    }

    public String getNameOfLoan() {
        return nameOfLoan;
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

    public Boolean getForSale() {
        return isForSale;
    }

    public void setForSale(Boolean forSale) {
        isForSale = forSale;
    }

    public Map<String,Integer> getListOfLenders() {
        Map<String,Integer> listOfLenderAndTheirShareInTheInvesment = new HashMap<>();
        for (Map.Entry<String,LeftToPay> entry : listOfLenders.entrySet())
            listOfLenderAndTheirShareInTheInvesment.put(entry.getKey(),entry.getValue().getOriginalAmount());
        return listOfLenderAndTheirShareInTheInvesment;
    }

    public Map<String, LeftToPay> getMapOfLenders() {
        return listOfLenders;
    }

    public int getHowManyYazAreLeft() {
        return howManyYazAreLeft;
    }

    public float getInterestPayedSoFar() {
        return interestPayedSoFar;
    }

    public int getTheInterestYetToBePaidOnTheLoan() {
        return theInterestYetToBePaidOnTheLoan;
    }

    public int getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar() {
        return theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar;
    }

    public int getTheAmountOfPrincipalPaymentYetToBePaid() {
        return theAmountOfPrincipalPaymentYetToBePaid;
    }

    public Boolean getActive() {
        return active;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setHowManyYazAreLeft(){
        if(howManyYazAreLeft != 0){
            howManyYazAreLeft--;
        }
        version++;
    }

    public int getTheAmountLeftToMakeTheLoanActive() {
        return theAmountLeftToMakeTheLoanActive;
    }

    public int getNextYazForPayment() {
        return nextYazForPayment;
    }

    public int getStartingDate() {
        return startingDate;
    }

    public int getFinishDate() {
        return finishDate;
    }

    public List<Payment> getPayments() {
        return Payments;
    }


    public boolean ifTheNameIsInTheNameList(String name){
        for (String curName: listOfLenders.keySet().stream().collect(Collectors.toList())) {
            if(curName.equals(name))
                return true;
        }
        return false;
    }

    public void setAnInvestment(int amount, String nameOfLender, int yaz){
        if(!listOfLenders.containsKey(nameOfLender)) {
            listOfLenders.put(nameOfLender, new LeftToPay(amount, (durationOfTheLoan / paymentFrequency), interest));
            mapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment.put(nameOfLender,amount);
        }
        else {
            listOfLenders.get(nameOfLender).setOriginalAmount(amount);
            int newAmount = listOfLenders.get(nameOfLender).getOriginalAmount();
            mapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment.put(nameOfLender, newAmount);
        }
        theAmountLeftToMakeTheLoanActive -= amount;
        if(theAmountLeftToMakeTheLoanActive == 0){
            status = LoanStatus.ACTIVE;
            totalMissedYazNeedToPayBack = 0;
            active = true;
            startingDate = yaz;
            nextYazForPayment = yaz + paymentFrequency;
            for(LeftToPay curLenderLeftToPay : listOfLenders.values()){
                curLenderLeftToPay.calculatePaymentForEachYazAfterLoanBecomeActive(yaz,paymentFrequency);
            }
        }
        else{
            if(status.equals(LoanStatus.NEW)){
                status = LoanStatus.PENDING;
            }
        }
        version++;
    }

    public void makeRisk(int yaz,int amountNotPaid){
        totalMissedYazNeedToPayBack++;
        setNextYazForPayment();
        for (LeftToPay curLender: listOfLenders.values()) {
            if(nextYazForPayment <= curLender.getLastYazOfLoan()) {
                int tmp = curLender.getAmountToPayByGivenYaz(nextYazForPayment);
                curLender.getPaymentForEachYaz().put(nextYazForPayment, amountNotPaid + tmp);
                curLender.getPaymentForEachYaz().put(nextYazForPayment - paymentFrequency, 0);
            }
        }
        status = LoanStatus.RISK;
        this.makeLoanPayment(yaz,amountNotPaid,false);
        version++;
    }

    public void setNextYazForPayment(){
        nextYazForPayment += paymentFrequency;
        version++;
    }

    public void makeFinished(int yaz){
        status = LoanStatus.FINISHED;
        finishDate = yaz;
        active = false;
        version++;
    }

    public int getYazlyPaymentWithDebtsCalculation(int curYaz){
        int sum = 0;
        for (LeftToPay curLender: listOfLenders.values()) {
            sum += curLender.getAmountToPayByGivenYaz(curYaz);
        }
        return sum;
    }

    public int getYazlyPaymentWithDebts(){
        return yazlyPayment + yazlyInterest;
    }

    public void makeLoanPayment(int yaz,int amountPaid,boolean paid){
        int principalAmount,interestAmount;
        interestAmount = ((amountPaid * interest) / 100);
        principalAmount = amountPaid - interestAmount;
        Payments.add(new Payment(yaz,principalAmount,interestAmount,amountPaid,paid));
        if(paid) {
            theAmountOfPrincipalPaymentYetToBePaid -= principalAmount;
            theInterestYetToBePaidOnTheLoan -= interestAmount;
            theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar += principalAmount;
            interestPayedSoFar += interestAmount;

            int debt = 0;
            for (LeftToPay curLoner : listOfLenders.values()) {
                debt += curLoner.getAmountToPayByGivenYaz(yaz);
            }
            if (debt == 0) {
                status = LoanStatus.ACTIVE;
                active = true;
            }
        }
        version++;
    }

    public int getVersion() {
        return version;
    }

    public void makeFullyPaymentToCloseLoan(int yaz, int principalAmount, int interestAmount){
        howManyYazAreLeft = 0;
        Payments.add(new Payment(yaz, principalAmount, interestAmount, principalAmount + interestAmount, true));
        theAmountOfPrincipalPaymentYetToBePaid = 0;
        theInterestYetToBePaidOnTheLoan = 0;
        theAmountOfThePrincipalPaymentPaidOnTheLoanSoFar += principalAmount;
        interestPayedSoFar += interestAmount;
        totalMissedYazNeedToPayBack = 0;
        version++;
    }

    public int getTotalMissedYazNeedToPayBack() {
        return totalMissedYazNeedToPayBack;
    }

    public void setDebt(int debt) {
        this.debt = debt;
        version++;
    }

    public int getDebt() {
        return debt;
    }
}

