package DTOs;

import Costumers.Customer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomerDTOs implements Serializable {
    private String name;
    private Integer numOfLoansAsLender;
    private Integer numOfLoansAsBorrower;
    private Integer balance;
    private String LoansAsALender;
    private String LoansAsABorrower;
    private List<AccountTransactionDTO> DtosTransactions;
    private int amountInvested = 0;
    private String notifications;

    public CustomerDTOs() {
    }

    public CustomerDTOs(Customer curCustomer, List<AccountTransactionDTO> ListOfTransactions) {
        //this.name.set(curCustomer.getName());
        name = curCustomer.getName();
        //balance.set(curCustomer.getMoneyInAccount());
        balance = curCustomer.getMoneyInAccount();
        //notifications = curCustomer.getNotifications();
        StringBuilder tmpStringNot = new StringBuilder();
        for (String curNot: curCustomer.getNotifications()) {
            tmpStringNot.append(" , " + curNot);
        }
        notifications = tmpStringNot.toString();
        if(curCustomer.getLoansAsALender() == null)
            LoansAsALender = null;
        else {
            StringBuilder tmpString = new StringBuilder();
            for (String curLoan: curCustomer.getLoansAsALender()) {
                tmpString.append(" , " + curLoan);
            }
            LoansAsALender = tmpString.toString();
            //numOfLoansAsLender.set((LoansAsALender.split(" , ")).length);
            numOfLoansAsLender = LoansAsALender.split(" , ").length;
        }
        if(curCustomer.getLoansAsABorrower() == null)
            LoansAsABorrower = null;
        else{
            StringBuilder tmpString = new StringBuilder();
            for (String curLoan: curCustomer.getLoansAsABorrower()) {
                tmpString.append(" , " + curLoan);
            }
            LoansAsABorrower = tmpString.toString();
            //numOfLoansAsLender.set((LoansAsABorrower.split(" , ")).length);
            numOfLoansAsLender = LoansAsABorrower.split(" , ").length;
        }
        if(ListOfTransactions == null)
            DtosTransactions = null;
        else
            DtosTransactions = Collections.unmodifiableList(ListOfTransactions);
    }

    public void setName(String name) {
        //this.name.set(name);
        this.name = name;
    }

    public void setNumOfLoansAsLender(int numOfLoansAsLender) {
        this.numOfLoansAsLender = numOfLoansAsLender;
    }

    public void setNumOfLoansAsBorrower(int numOfLoansAsBorrower) {
        //this.numOfLoansAsBorrower.set(numOfLoansAsBorrower);
        this.numOfLoansAsBorrower = numOfLoansAsBorrower;
    }

    public void setBalance(int balance) {
        //this.balance.set(balance);
        this.balance = balance;
    }

//    public void setLoansAsALender(List<String> loansAsALender) {
//        LoansAsALender = loansAsALender;
//    }

//    public void setLoansAsABorrower(List<String> loansAsABorrower) {
//        LoansAsABorrower = loansAsABorrower;
//    }

    public void setDtosTransactions(List<AccountTransactionDTO> dtosTransactions) {
        DtosTransactions = dtosTransactions;
    }

    public void setAmountInvested(int amountInvested) {
        this.amountInvested = amountInvested;
    }

//    public void setNotifications(List<String> notifications) {
//        this.notifications = notifications;
//    }

    public CustomerDTOs(int amount){
        amountInvested = amount;
    }

//    public SimpleStringProperty nameProperty() {
//        return name;
//    }

//    public SimpleIntegerProperty numOfLoansAsLenderProperty() {
//        return numOfLoansAsLender;
//    }

//    public SimpleIntegerProperty numOfLoansAsBorrowerProperty() {
//        return numOfLoansAsBorrower;
//    }

//    public SimpleIntegerProperty balanceProperty() {
//        return balance;
//    }



    public int getAmountInvested() {
        return amountInvested;
    }

    public String getName() {
        return name;
    }

    public List<String> getLoansAsALender() {
        if (LoansAsALender == null)
            return null;
        else
            return Collections.unmodifiableList(new ArrayList<String>(Arrays.asList(LoansAsALender.split(" , "))));
    }

    public int getNumOfLoansAsLender() {
        return numOfLoansAsLender;
    }

    public int getNumOfLoansAsBorrower() {
        return numOfLoansAsBorrower;
    }

    public int getBalance() {
        return balance;
    }

    public List<AccountTransactionDTO> getDtosTransactions() {
        return DtosTransactions;
    }

    public List<String> getLoansAsABorrower() {
        if (LoansAsABorrower == null)
            return null;
        else
            return Collections.unmodifiableList(new ArrayList<String>(Arrays.asList(LoansAsABorrower.split(" , "))));
    }

    public List<AccountTransactionDTO> getTransactions() {
        return DtosTransactions;
    }

    public List<String> getNotifications() {
        return (new ArrayList<String>(Arrays.asList(notifications.split(" , "))));
    }

    public String getLoansAsLenderString(){
        return LoansAsALender;
    }

    public String getLoansAsBorrowerString(){
        return LoansAsABorrower;
    }

    public String getNotificationsAsString(){
        return notifications;
    }

    public static class CustomerDTOAdapter implements JsonSerializer<CustomerDTOs> {
        @Override
        public JsonElement serialize(CustomerDTOs customerDTOs, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonElement = new JsonObject();
            jsonElement.addProperty("name", customerDTOs.getName());
            jsonElement.addProperty("numOfLoansAsLender", customerDTOs.getNumOfLoansAsLender());
            jsonElement.addProperty("numOfLoansAsBorrower", customerDTOs.getNumOfLoansAsBorrower());
            jsonElement.addProperty("balance", customerDTOs.getBalance());
            jsonElement.addProperty("LoansAsALender", customerDTOs.getLoansAsLenderString());
            jsonElement.addProperty("LoansAsABorrower", customerDTOs.getLoansAsBorrowerString());
            jsonElement.addProperty("amountInvested", customerDTOs.getAmountInvested());
            jsonElement.addProperty("notifications", customerDTOs.getNotificationsAsString());
            jsonElement.getAsJsonArray("DtosTransactions");

            return jsonElement;
        }
    }
}
