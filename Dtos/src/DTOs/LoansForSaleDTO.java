package DTOs;

import BankActions.LoanForSale;

public class LoansForSaleDTO {
    private String seller;
    private Integer price;
    private String loanName;

    public LoansForSaleDTO(LoanForSale loan) {
        this.seller = loan.getSeller();
        this.price = loan.getPrice();
        this.loanName = loan.getLoanName();
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
}
