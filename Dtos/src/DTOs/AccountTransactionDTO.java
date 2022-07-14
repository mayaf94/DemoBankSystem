package DTOs;

import BankActions.AccountTransaction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.Serializable;
import java.lang.reflect.Type;

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

    public static class TransactionsDTOAdapter implements JsonSerializer<AccountTransactionDTO> {
        @Override
        public JsonElement serialize(AccountTransactionDTO accountTransactionDTO, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonElement = new JsonObject();
            jsonElement.addProperty("TransactionType", accountTransactionDTO.getTransactionType());
            jsonElement.addProperty("amount", accountTransactionDTO.getAmount());
            jsonElement.addProperty("yazOfAction", accountTransactionDTO.getYazOfAction());
            jsonElement.addProperty("previousBalance", accountTransactionDTO.getPreviousBalance());
            jsonElement.addProperty("curBalance", accountTransactionDTO.getCurBalance());

            return jsonElement;
        }
    }
}
//    private String TransactionType;
//    private Integer amount;
//    private Integer yazOfAction;
//    private Integer previousBalance;
//    private Integer curBalance;