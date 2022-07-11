package BankActions;

import java.io.Serializable;

public abstract class AccountTransaction  implements Serializable {
   protected int timeOfAction;
   protected int amountOfTransaction;
   protected int amountBefore;
   protected int amountAfter;

    protected AccountTransaction() {
    }

    abstract public void Transaction();

    public int getTimeOfAction() {
        return timeOfAction;
    }

    public int amountOfTransaction() {
        return amountOfTransaction;
    }

    public int getAmountBefore() {
        return amountBefore;
    }

    public int getAmountAfter() {
        return amountAfter;
    }

    public int getAmountOfTransaction() {
        return amountOfTransaction;
    }

    abstract public String getSignOfTransaction();

    abstract public void setAmountAfter(int amountAfter);



}

