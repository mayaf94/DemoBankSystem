package BankActions;

public class LoanForSale {
    private String seller;
    private Integer price;
    private String loanName;

    public LoanForSale(String seller, Integer price, String loanName) {
        this.seller = seller;
        this.price = price;
        this.loanName = loanName;
    }

    public String getSeller() {
        return seller;
    }

    public Integer getPrice() {
        return price;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }
}
