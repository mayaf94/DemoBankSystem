package DTOs;

import BankActions.AccountTransaction;

import java.io.Serializable;

public class AccountTransactionDTO implements Serializable {


    private String TransactionType;
    private Integer amount;
    private Integer yazOfAction;
    private Integer previousBalance;
    private Integer curBalance;


    public AccountTransactionDTO(AccountTransaction Transaction){
        //amount.set(Transaction.getAmountOfTransaction());
        amount = Transaction.getAmountOfTransaction();
        //yazOfAction.set(Transaction.getTimeOfAction());
        yazOfAction = Transaction.getTimeOfAction();
        //previousBalance.set(Transaction.getAmountBefore());
        previousBalance = Transaction.getAmountBefore();
        //curBalance.set(Transaction.getAmountAfter());
        curBalance = Transaction.getAmountAfter();
        if(previousBalance < curBalance){
            TransactionType = "+";

        }
        else {
            TransactionType = "-";
        }
    }
/*
    public String isTransactionType() {
        return TransactionType.get();
    }*/

    public String getTransactionType() {
        return TransactionType;
    }

/*    public int getAmount() {
        return amount.get();
    }*/

    public Integer getAmount() {
        return amount;
    }

 /*   public int getYazOfAction() {
        return yazOfAction.get();
    }*/

    public Integer getYazOfAction() {
        return yazOfAction;
    }

/*    public int getPreviousBalance() {
        return previousBalance.get();
    }*/

    public Integer getPreviousBalance() {
        return previousBalance;
    }

   /* public int getCurBalance() {
        return curBalance.get();
    }*/

    public Integer getCurBalance() {
        return curBalance;
    }
}
