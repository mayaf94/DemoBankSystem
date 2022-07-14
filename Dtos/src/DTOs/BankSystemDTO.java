package DTOs;

import BankActions.Loan;
import Costumers.Customer;

import java.util.*;

public class BankSystemDTO {
    private Integer curYaz;
    private String msg;
    private List<CustomerDTOs> Customers = new ArrayList<>();
    private List<LoanDTOs> LoansInBank = new ArrayList<>();
    private CategoriesDTO categories;
    private Boolean isRewind;

    public BankSystemDTO() {
    }

    public BankSystemDTO(Integer curYaz, String msg, List<CustomerDTOs> customers,List<LoanDTOs> loansInBank, CategoriesDTO categories, Boolean rewind) {
        this.curYaz = curYaz;
        this.msg = msg;
        Customers = customers;
        LoansInBank = loansInBank;
        this.categories = categories;
        isRewind = rewind;
    }

    public Integer getCurYaz() {
        return curYaz;
    }

    public String getMsg() {
        return msg;
    }

    public List<CustomerDTOs> getCustomers() {
        return Customers;
    }

    public List<LoanDTOs> getLoansInBank() {
        return LoansInBank;
    }

    public CategoriesDTO getCategories() {
        return categories;
    }

    public Boolean getRewind() {
        return isRewind;
    }

    public void setCurYaz(Integer curYaz) {
        this.curYaz = curYaz;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCustomers(List<CustomerDTOs> customers) {
        Customers = customers;
    }

    public void setLoansInBank(List<LoanDTOs> loansInBank) {
        LoansInBank = loansInBank;
    }

    public void setCategories(CategoriesDTO categories) {
        this.categories = categories;
    }

    public void setRewind(Boolean rewind) {
        isRewind = rewind;
    }
}
