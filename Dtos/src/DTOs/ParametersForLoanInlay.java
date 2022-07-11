package DTOs;

import java.util.List;

public class ParametersForLoanInlay {
    List<String> chosenCategories;
    int minimumDuration;
    int minimumInterestForSingleYaz;
    int maxOpenLoansForLoanOwner;
    String name;

    public ParametersForLoanInlay(List<String> chosenCategories, int minimumDuration, int minimumInterestForSingleYaz, int maxOpenLoansForLoanOwner, String name) {
        this.chosenCategories = chosenCategories;
        this.minimumDuration = minimumDuration;
        this.minimumInterestForSingleYaz = minimumInterestForSingleYaz;
        this.maxOpenLoansForLoanOwner = maxOpenLoansForLoanOwner;
        this.name = name;
    }

    public List<String> getChosenCategories() {
        return chosenCategories;
    }

    public int getMinimumDuration() {
        return minimumDuration;
    }

    public int getMinimumInterestForSingleYaz() {
        return minimumInterestForSingleYaz;
    }

    public int getMaxOpenLoansForLoanOwner() {
        return maxOpenLoansForLoanOwner;
    }

    public String getName() {
        return name;
    }
}
