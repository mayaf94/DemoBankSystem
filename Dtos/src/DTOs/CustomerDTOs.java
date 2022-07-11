package DTOs;

import Costumers.Customer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerDTOs implements Serializable {
    private SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private SimpleIntegerProperty numOfLoansAsLender = new SimpleIntegerProperty(this, "numOfLoansAsLender");
    private SimpleIntegerProperty numOfLoansAsBorrower = new SimpleIntegerProperty(this, "numOfLoansAsBorrower");
    private SimpleIntegerProperty balance = new SimpleIntegerProperty(this, "balance");
    private List<String> LoansAsALender;
    private List<String> LoansAsABorrower;
    private List<AccountTransactionDTO> DtosTransactions;
    private int amountInvested = 0;
    private List<String> notifications = new ArrayList<>();

    public CustomerDTOs( Customer curCustomer, List<AccountTransactionDTO> ListOfTransactions) {
        this.name.set(curCustomer.getName());
        balance.set(curCustomer.getMoneyInAccount());
        notifications = curCustomer.getNotifications();
        if(curCustomer.getLoansAsALender() == null)
            LoansAsALender = null;
        else {
            LoansAsALender = Collections.unmodifiableList(curCustomer.getLoansAsALender());
            numOfLoansAsLender.set(LoansAsALender.size());
        }
        if(curCustomer.getLoansAsABorrower() == null)
            LoansAsABorrower = null;
        else{
            LoansAsABorrower = Collections.unmodifiableList(curCustomer.getLoansAsABorrower());
            numOfLoansAsBorrower.set(LoansAsABorrower.size());
        }
        if(ListOfTransactions == null)
            DtosTransactions = null;
        else
            DtosTransactions = Collections.unmodifiableList(ListOfTransactions);
    }

    public CustomerDTOs(int amount){
        amountInvested = amount;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleIntegerProperty numOfLoansAsLenderProperty() {
        return numOfLoansAsLender;
    }

    public SimpleIntegerProperty numOfLoansAsBorrowerProperty() {
        return numOfLoansAsBorrower;
    }

    public SimpleIntegerProperty balanceProperty() {
        return balance;
    }

    public int getAmountInvested() {
        return amountInvested;
    }

    public String getName() {
        return name.get();
    }

    public List<String> getLoansAsALender() {
        if (LoansAsALender == null)
            return null;
        else
            return Collections.unmodifiableList(LoansAsALender);
    }

    public int getNumOfLoansAsLender() {
        return numOfLoansAsLender.get();
    }

    public int getNumOfLoansAsBorrower() {
        return numOfLoansAsBorrower.get();
    }

    public int getBalance() {
        return balance.get();
    }

    public List<AccountTransactionDTO> getDtosTransactions() {
        return DtosTransactions;
    }

    public List<String> getLoansAsABorrower() {
        if (LoansAsABorrower == null)
            return null;
        else
            return Collections.unmodifiableList(LoansAsABorrower);
    }

    public List<AccountTransactionDTO> getTransactions() {
        return DtosTransactions;
    }

    public List<String> getNotifications() {
        return notifications;
    }
}

