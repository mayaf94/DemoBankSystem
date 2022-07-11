package BankActions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeftToPay implements Serializable {
    private int originalAmount;
    private int paid;
    private int amountLeftToPay;
    private int debt;
    private int amountOfPayments;
    private int debtInterest;
    private int interestRate;
    private int lastYazOfLoan;
    private Map<Integer,Integer> paymentForEachYaz = new HashMap<>();

    public LeftToPay(int amountLeft, int amountOfPayment,int i_interestRate){
        originalAmount = amountLeft;
        this.amountLeftToPay = amountLeft;
        this.amountOfPayments = amountOfPayment;
        debt = 0;
        debtInterest = 0;
        interestRate = i_interestRate;
    }

    public void calculatePaymentForEachYazAfterLoanBecomeActive(int yazOfActivation,int paymentFrequancy){
        int amountLeftToPayTmp = amountLeftToPay;
        int amountOfPaymentsTmp = amountOfPayments;
        for(int i = 1; i <= amountOfPayments; i++){
            paymentForEachYaz.put(i + paymentFrequancy,amountLeftToPayTmp / amountOfPaymentsTmp + ((amountLeftToPayTmp/amountOfPaymentsTmp) * interestRate) /100 );
            if (amountLeftToPayTmp != 0) {
                amountLeftToPayTmp -= amountLeftToPayTmp / amountOfPaymentsTmp;
            }
            if(amountOfPaymentsTmp != 1)
                amountOfPaymentsTmp--;
            lastYazOfLoan = i + paymentFrequancy;
        }

    }

    public int getLastYazOfLoan() {
        return lastYazOfLoan;
    }

    public Map<Integer, Integer> getPaymentForEachYaz() {
        return paymentForEachYaz;
    }

    public void resetLeftToPayAfterClosingTheLoan(){
        amountLeftToPay = 0;
        resetDebt();
        amountOfPayments = 0;
    }

    public void setAmountLeftToPay() {
        if (amountLeftToPay != 0)
            amountLeftToPay -= amountLeftToPay / amountOfPayments;
    }

    public void setAmountOfPayment(){
        if(amountOfPayments != 1)
            amountOfPayments--;
    }

    public void resetDebt(){
        debt = 0;
        debtInterest = 0;
    }

    public  int getAmountToPayByGivenYaz(int yaz){
        if(yaz > lastYazOfLoan){
            yaz = lastYazOfLoan;
        }
        if(paymentForEachYaz.containsKey(yaz))
            return paymentForEachYaz.get(yaz);

        return 0;
    }

    public int setAmountPaidInGivenYaz(int yaz,int amountToPay){
        if(yaz > lastYazOfLoan){
            yaz = lastYazOfLoan;
        }
        int tmp = paymentForEachYaz.get(yaz);
        if(amountToPay >= tmp){
            paymentForEachYaz.put(yaz,0);
            return tmp;
        }
        paymentForEachYaz.put(yaz,tmp - amountToPay);
        return amountToPay;
    }

    public int amountToPayThisYaz(){
        return (amountLeftToPay/amountOfPayments) + debt;
    }

    public int interestToPayThisYaz(int interest){
        return (((amountLeftToPay/amountOfPayments) * interest) /100 ) + debtInterest;
    }

    public void setDebt(int interest){
        this.debt = (amountLeftToPay/amountOfPayments) + debt;
        this.debtInterest = (((amountLeftToPay/amountOfPayments) * interest) /100 ) + debtInterest;
    }

    public void setOriginalAmount(int amount) {
        originalAmount += amount;
        amountLeftToPay += amount;
    }

    public int getAmountLeftToPayToCloseTheLoan(int interest) {
        return ((amountLeftToPay * interest) / 100) + amountLeftToPay  + debt + debtInterest;
    }

    public int getOriginalAmount() {
        return originalAmount;
    }

    public List<Integer> getAmountLeftToPay(){
        return paymentForEachYaz.values().stream().collect(Collectors.toList());
    }
}
