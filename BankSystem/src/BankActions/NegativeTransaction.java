package BankActions;

public class NegativeTransaction extends AccountTransaction {

    public NegativeTransaction(int amount,int yaz,int moneyInAccount) {
        amountOfTransaction = amount;
        amountBefore = moneyInAccount;
        Transaction();
        timeOfAction = yaz;
    }

    @Override
    public void Transaction() {
        setAmountAfter(amountOfTransaction);
    }

    @Override
    public void setAmountAfter(int amount) {
        this.amountAfter = amountBefore - amount;
    }

    @Override
    public String getSignOfTransaction() {
        return "-";
    }
}
