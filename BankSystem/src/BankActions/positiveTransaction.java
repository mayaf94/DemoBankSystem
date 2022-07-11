package BankActions;

public class positiveTransaction extends AccountTransaction {

    public positiveTransaction(int amount, int yaz, int moneyInAccount) {
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
        this.amountAfter = amountBefore + amount;
    }

    @Override
    public String getSignOfTransaction() {
        return "+";
    }
}
