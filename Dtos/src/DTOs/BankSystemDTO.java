package DTOs;

import BankActions.Loan;
import Costumers.Customer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BankSystemDTO {
    private Integer curYaz;
    private String msg;
    private Map<String, CustomerDTOs> Customers = new HashMap<>();
    private Map<String, LoanDTOs> LoansInBank = new HashMap<>();
    private CategoriesDTO categories;

    public BankSystemDTO(Integer curYaz, String msg, Map<String, CustomerDTOs> customers, Map<String, LoanDTOs> loansInBank) {
        this.curYaz = curYaz;
        this.msg = msg;
        Customers = customers;
        LoansInBank = loansInBank;
    }

    public Integer getCurYaz() {
        return curYaz;
    }

    public String getMsg() {
        return msg;
    }
}
