package BankSystem;

import BankActions.*;
import Costumers.Customer;
import DTOs.*;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static BankSystem.FromXmlToClasses.fromXmlToObjects;
import static java.util.stream.Collectors.groupingBy;

public class SystemImplement implements BankSystem , Serializable {
    private Map<String, Customer> Customers = new HashMap<>();
    private Map<String, Loan> LoansInBank = new HashMap<>();
    private Set<String> allCategories = new HashSet<>();
    private int Yaz = 1;
    transient private SimpleStringProperty yazProperty = new SimpleStringProperty();
    private Boolean isRewind = false;
    private int RewindYaz = 1;
    private List<LoanForSale> loanForSale = new ArrayList<>();
    public Integer getRewindYaz(){
        return RewindYaz;
    }

    public Boolean isRewind() {
        return isRewind;
    }

    public BankSystemDTO getBankSystemDTO(){
        return new BankSystemDTO(RewindYaz, " ",
                getListOfDTOsCustomer(), getListOfLoansDTO(), getAllCategories(), isRewind);
    }

    public SimpleStringProperty getYazProperty() {
        return yazProperty;
    }

    public List<LoanDTOs> ReadingTheSystemInformationFile(String FileName, String customerName){
        boolean flag;
        List<Loan> loansInSystem = new ArrayList<>();
        flag = fromXmlToObjects(loansInSystem, FileName, allCategories, customerName, LoansInBank.keySet().stream().collect(Collectors.toList()));
        if(flag) {
            LoansInBank.putAll(loansInSystem.stream().collect(Collectors.toMap(Loan::getNameOfLoan, loan -> loan)));
            Customers.get(customerName).addingNewLoansAsBorrowerToExistingLoansAsBorrower(loansInSystem);
            List<String> loansName =  loansInSystem.stream()
                    .map(L -> L.getNameOfLoan())
                    .collect(Collectors.toList());

            return getListOfLoansDtoByListOfNamesOFLoans(loansName);
        }
        return null;
    }

    public int getCurrentYaz(){
        int tmp = Yaz;
        return tmp;
    }

    @Override
    public AccountTransactionDTO DepositToAccount(int amount,String nameOfCostumer){
        return Customers.get(nameOfCostumer).DepositMoney(amount,Yaz);
    }

    @Override
    public AccountTransactionDTO WithdrawFromTheAccount(int amount,String nameOfCostumer){
        if(checkIfMoneyCanBeWithdraw(amount,nameOfCostumer)){
            return Customers.get(nameOfCostumer).WithdrawMoney(amount, Yaz);
        }
        return null;
    }

    public void addCustomerToBank(String nameOfCustomer){
        if(!Customers.containsKey(nameOfCustomer))
            Customers.put(nameOfCustomer,new Customer(nameOfCustomer,0,null));
    }

    public List<CustomerDTOs> getListOfDTOsCustomer(){
        List<CustomerDTOs> customerDtosList = new ArrayList<>(Customers.size());
        List<AccountTransactionDTO> listOFTransactinsDTOs;

        for (Customer curCustomer: Customers.values()) {
            listOFTransactinsDTOs = getListOfTransactionsDTO(curCustomer.getTransactions());
            customerDtosList.add(new CustomerDTOs(curCustomer,listOFTransactinsDTOs));
        }
        return Collections.unmodifiableList(customerDtosList);
    }

    public List<LoanDTOs> getListOfLoansDTO(){
        List<Loan> listOfLoans = LoansInBank.values().stream().collect(Collectors.toList());
        List<LoanDTOs> listOfLoansDTO = new ArrayList<>(listOfLoans.size());
        for (Loan curLoan : listOfLoans) {
            listOfLoansDTO.add(new LoanDTOs(curLoan));
        }
        return listOfLoansDTO;
    }

    private List<AccountTransactionDTO> getListOfTransactionsDTO(List<AccountTransaction> accountTransactions){
        List<AccountTransactionDTO> ListOfTransactionDTO = new ArrayList<>();
        if(accountTransactions == null)
            return ListOfTransactionDTO = null;
        else {
            for (AccountTransaction curTransaction : accountTransactions) {
                ListOfTransactionDTO.add(new AccountTransactionDTO(curTransaction));
            }
        }
        return ListOfTransactionDTO;
    }

    public List<LoanDTOs> ActivationOfAnInlay(List<String> chosenCategories, int minimumDuration, int minimumInterestForSingleYaz,int maxOpenLoansForLoanOwner,String name){
        List<LoanDTOs> filteredListOfLoansDTO = new ArrayList<>();
        List<Loan> filteredListOfLoans = LoansInBank.values().stream()
                .filter(L-> chosenCategories.contains(L.getCategory()))
                .filter(L-> Customers.get(L.getNameOfLoaner()).getLoansAsABorrower().size() <= maxOpenLoansForLoanOwner)
                .filter(L->(L.getDurationOfTheLoan() >= minimumDuration))
                .filter(L->(L.getInterest() >= minimumInterestForSingleYaz)).filter(L->!L.getNameOfLoaner().equals(name))
                .filter(L->((L.getStatus().equals(LoanStatus.NEW)) || (L.getStatus().equals(LoanStatus.PENDING))))
                .collect(Collectors.toList());
        for (Loan curLoan: filteredListOfLoans) {
            filteredListOfLoansDTO.add(new LoanDTOs(curLoan));
        }
        return filteredListOfLoansDTO;
    }

    public List<LoanDTOs> getListOfLoansDtoByListOfNamesOFLoans(List<String> i_loansName){
        List<LoanDTOs> listOfLoansDTO = new ArrayList<>(i_loansName.size());
        for (String loanName : i_loansName){
            if(LoansInBank.containsKey(loanName));
            listOfLoansDTO.add(new LoanDTOs(LoansInBank.get(loanName)));
        }
        return listOfLoansDTO;

    }

    public boolean checkIfCustomerHasEnoughMoneyToInvestByGivenAmount(String i_nameOfCustomer,int amountToInvest){
        return Customers.get(i_nameOfCustomer).getMoneyInAccount() >= amountToInvest;
    }

    public LoanDTOs takeOutLoan(String i_LoanerName,String i_loanName,String i_category,int i_principalAmount,int i_totalDuration,int i_paymentFreq,int i_interest){
        if(!allCategories.contains(i_category)){
            allCategories.add(i_category);
        }
        Loan newLoan = new Loan(i_loanName,i_LoanerName,i_category,i_principalAmount,i_totalDuration,i_paymentFreq,i_interest);
        LoansInBank.put(i_loanName,newLoan);
        Customers.get(i_LoanerName).addLoanAsLoaner(i_loanName);
        return new LoanDTOs(newLoan);
    }

    public CustomerDTOs LoansInlay(List<String> namesOfLoans,int amountOfMoneyUserWantToInvest,String nameOfLender,int maxOwnerShipOfTheLoan) {//TODO check if maxOwnerShip Does work
        List<Loan> loansForInvestment = FindLoansInSystemByNames(namesOfLoans);
        int numOfLoans = loansForInvestment.size();
        int tmpMoneyInvested,moneyToInvest;
        int dividedAmount;
        int totalAmountInvested = 0;
        for (Loan loan : loansForInvestment) {
            dividedAmount = amountOfMoneyUserWantToInvest / numOfLoans;
            if(calcPercentValueForLoan(loan.getOriginalAmount(),maxOwnerShipOfTheLoan) < dividedAmount) {
                moneyToInvest = calcPercentValueForLoan(loan.getOriginalAmount(),maxOwnerShipOfTheLoan);
            }
            else{
                moneyToInvest = dividedAmount;
            }
                if (loan.getTheAmountLeftToMakeTheLoanActive() < moneyToInvest) {
                    tmpMoneyInvested = loan.getTheAmountLeftToMakeTheLoanActive();
                    loan.setAnInvestment(loan.getTheAmountLeftToMakeTheLoanActive(), nameOfLender, Yaz);
                    this.depositTheMoneyOfTheLoanInBorrowerAccount(loan.getOriginalAmount(), loan.getNameOfLoaner(), loan);
                } else {
                    tmpMoneyInvested = moneyToInvest;
                    loan.setAnInvestment(moneyToInvest, nameOfLender, Yaz);
                    this.depositTheMoneyOfTheLoanInBorrowerAccount(loan.getOriginalAmount(), loan.getNameOfLoaner(), loan);
                }


            amountOfMoneyUserWantToInvest -= tmpMoneyInvested;
            Customers.get(nameOfLender).makeAnInvestment(loan.getNameOfLoan(),tmpMoneyInvested, Yaz);
            totalAmountInvested += tmpMoneyInvested;
            numOfLoans--;

        }
        CustomerDTOs lender = new CustomerDTOs(totalAmountInvested);
        return lender;
    }

    private int calcPercentValueForLoan(int amount,int percent){
        return (amount * percent) / 100;
    }

    private void depositTheMoneyOfTheLoanInBorrowerAccount(int amount, String nameOfLoaner, Loan loan){
        if(loan.getStatus().equals(LoanStatus.ACTIVE))
        {
            this.DepositToAccount(amount, nameOfLoaner);
        }
    }

    private List<Loan> FindLoansInSystemByNames(List<String> namesOfLoans){
        List<Loan> loans = new ArrayList<>();
        for (String loanName : namesOfLoans){
            if(!loans.contains(LoansInBank.get(loanName)))
                loans.add(LoansInBank.get(loanName));
        }
        loans.sort(Comparator.comparing(Loan::getTheAmountLeftToMakeTheLoanActive));
        return loans;
    }

    public BankSystemDTO IncreaseYaz(){
        int debt = 0;
        Map<Loan,Integer> newRiskLoans = new HashMap<>();
        for(Loan curLoan : LoansInBank.values().stream().filter(L -> (!L.getStatus().equals(LoanStatus.FINISHED)) && (L.getNextYazForPayment() == Yaz)).collect(Collectors.toList())){
            for(Map.Entry<String,LeftToPay> curLender :curLoan.getMapOfLenders().entrySet()){
                debt += curLender.getValue().getAmountToPayByGivenYaz(Yaz);
            }
            if(debt != 0){
                curLoan.makeRisk(Yaz,debt);
                newRiskLoans.put(curLoan,debt);
                curLoan.setDebt(debt);
            }
            else{
                curLoan.setNextYazForPayment();
                curLoan.setDebt(0);
            }
        }
        sendMassagesToNewRiskLoans(newRiskLoans);
        if(!isRewind) {
            Yaz++;
            RewindYaz++;
            yazProperty.set("Current Yaz: " + Yaz);
            for (Customer curCustomer : Customers.values()) {
                List<Loan> curCustomerLoans = LoansInBank.values().stream().filter(L -> L.getNameOfLoaner().equals(curCustomer.getName())).filter(L -> (L.getStatus().equals(LoanStatus.ACTIVE) || L.getStatus().equals(LoanStatus.RISK))).collect(Collectors.toList());
                for (Loan curLoan : curCustomerLoans) {
                    curLoan.setHowManyYazAreLeft();
                    if (curLoan.getNextYazForPayment() == Yaz) {
                        messageMaker(curLoan.getNameOfLoaner(), curLoan.getYazlyPaymentWithDebtsCalculation(Yaz), curLoan.getNameOfLoan(), "The payment date has arrived on the loan: ");
                    }
                }
            }
        }
        else {
            RewindYaz++;
            if(RewindYaz == Yaz)
                isRewind = false;
        }
            return new BankSystemDTO(RewindYaz, "Increase Yaz was successful",
                    getListOfDTOsCustomer(), getListOfLoansDTO(), getAllCategories(), isRewind);

    }

    public int rewindYaz(){
        isRewind = true;
        if(RewindYaz != 1)
            RewindYaz--;
        return RewindYaz;
    }

    private void sendMassagesToNewRiskLoans(Map<Loan,Integer> i_newRiskLoans) {
        for (Map.Entry<Loan,Integer> curLoan : i_newRiskLoans.entrySet()) {
            messageMaker(curLoan.getKey().getNameOfLoaner(), curLoan.getValue(), curLoan.getKey().getNameOfLoan(), "You did not pay on the due date for loan: ");
            }
    }

    private Boolean checkIfTheLoanIsFinished(Loan curLoan){
        Boolean flag = false;
        if(curLoan.getHowManyYazAreLeft() == 0) {
            flag = (curLoan.getTheInterestYetToBePaidOnTheLoan() == 0 && curLoan.getTheAmountOfPrincipalPaymentYetToBePaid() == 0);
            if (flag)
                curLoan.makeFinished(Yaz);
        }
        return flag;
    }

    public static boolean checkFileName(String FileName){
        if(FileName.endsWith("xml"))
            return true;
        return false;
    }

    public CategoriesDTO getAllCategories(){
        return new CategoriesDTO(allCategories);
    }

    public void YazlyPaymentForGivenLoans(Map<String,Integer> loansToPay){
        int moneyPaidTmp;
        int totalAmountPaidThisYaz = 0;
        for(Map.Entry<String,Integer> entry : loansToPay.entrySet()){
            int amountUserWantToPay = entry.getValue();
            if(amountUserWantToPay != 0) {
                int amountOfLenders = LoansInBank.get(entry.getKey()).getMapOfLenders().size();
                Map<String,LeftToPay> sortedLendersByAmountToPay = LoansInBank.get(entry.getKey()).getMapOfLenders().entrySet().stream()
                        .sorted(Comparator.comparingInt(e -> e.getValue().getAmountToPayByGivenYaz(Yaz)))
                        .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2) -> e1,LinkedHashMap::new));

                for (Map.Entry<String, LeftToPay> curLender : sortedLendersByAmountToPay.entrySet()) {
                    int dividedAmount = amountUserWantToPay / amountOfLenders;
                    moneyPaidTmp = makePayment(dividedAmount,curLender.getKey(),curLender.getValue(), entry.getKey());
                    amountUserWantToPay -= moneyPaidTmp;
                    amountOfLenders--;
                    totalAmountPaidThisYaz += moneyPaidTmp;
                }
                LoansInBank.get(entry.getKey()).makeLoanPayment(Yaz,totalAmountPaidThisYaz,true);
                Customers.get(LoansInBank.get(entry.getKey()).getNameOfLoaner()).WithdrawMoney(totalAmountPaidThisYaz,Yaz);
            }
        }
    }

    public int makePayment(int amountToPay,String nameOfCustomer,LeftToPay customerLeftToPay,String nameOfLoan){
        int amountInvested = customerLeftToPay.setAmountPaidInGivenYaz(Yaz,amountToPay);
        Customers.get(nameOfCustomer).DepositMoney(amountInvested,Yaz);

        Map<String, Integer> mapToChang = LoansInBank.get(nameOfLoan).getMapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment();
        for (Map.Entry<String,Integer> entry: mapToChang.entrySet()) {
            entry.setValue(amountOfPrincipalPaymentThatIsLeft(nameOfLoan, entry.getKey()));
            setNewAmount(entry.getKey(),nameOfLoan);
        }
        return amountInvested;
    }

    public void fullPaymentOnLoans(List<String> loanNames, String customerName){
        Loan curLoanToPay;
        Customer curCustomer = Customers.get(customerName);
        for (String curLoan: loanNames) {
            curLoanToPay = LoansInBank.get(curLoan);
            payForLoanFully(curLoanToPay, curCustomer);

            Map<String, Integer> mapToChang = LoansInBank.get(curLoan).getMapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment();
            for (Map.Entry<String,Integer> entry: mapToChang.entrySet()) {
                entry.setValue(amountOfPrincipalPaymentThatIsLeft(curLoan, entry.getKey()));
            }
            for (LoanForSale curLoanForSale: loanForSale) {
                if(curLoanForSale.getLoanName().equals(curLoan) && curLoanForSale.getSeller().equals(customerName))
                    loanForSale.remove(curLoan);
            }
        }
    }

    private void payForLoanFully(Loan loan, Customer customer){
        Boolean flag = false;
        int sumOfPayments = 0,curPayment = 0;
        for(Map.Entry<String,LeftToPay> curLender : loan.getMapOfLenders().entrySet()){
            curPayment = curLender.getValue().getAmountLeftToPayToCloseTheLoan(loan.getInterest());
            Customers.get(curLender.getKey()).DepositMoney(curPayment,Yaz);
            curLender.getValue().resetLeftToPayAfterClosingTheLoan();
            sumOfPayments += curPayment;
        }
        customer.WithdrawMoney(sumOfPayments,Yaz);
        loan.makeFullyPaymentToCloseLoan(Yaz,sumOfPayments - (sumOfPayments * loan.getInterest()) / 100,(sumOfPayments * loan.getInterest()) / 100);
        checkIfTheLoanIsFinished(loan);
       /* while(!flag){
            loan.setHowManyYazAreLeft();
            loan.getYazlyPaymentWithDebtsCalculation();
            customer.WithdrawMoney(loan.getYazlyPaymentWithDebts(), Yaz);
            this.makePayment(loan);
            loan.makeLoanPayment(Yaz);
            flag = checkIfTheLoanIsFinished(loan);
        }*/
    }


    public Map<String, SimpleStringProperty> getLoanDataByStatusPropertyFromSystemMap(String loanName){
        return LoansInBank.get(loanName).getLoanDataByStatusPropertyAndStatusProperty();
    }

    public List<LoanDTOs> getAllLoansThatAreForSale() {
        return getListOfLoansDtoByListOfNamesOFLoans(LoansInBank.keySet().stream().collect(Collectors.toList()))
                .stream().filter(L -> L.getForSale()).collect(Collectors.toList());//TODO  not sure we need it we need TheSame that returns loansForSale
    }

    public CustomerDTOs getCustomerByName(String name){
        return new CustomerDTOs(Customers.get(name),getListOfTransactionsDTO(Customers.get(name).getTransactions()));
    }

    public List<String> checkWhatLoansCanBeFullyPaidSystem(List<String> loanNames, String customerName){
        List<Loan> wantedLoans = LoansInBank.values().stream().filter(L -> loanNames.contains(L.getNameOfLoan())).sorted(Comparator.comparingInt(e -> (e.getTheAmountOfPrincipalPaymentYetToBePaid() + e.getTheInterestYetToBePaidOnTheLoan()))).collect(Collectors.toList());
        List<String> loansThatCanBeFullyPaid = new ArrayList<>();
        int moneyInThatCanBeAfterPayment = Customers.get(customerName).getMoneyInAccount();
        for (Loan curLoan: wantedLoans) {
            if(moneyInThatCanBeAfterPayment >=  curLoan.getTheAmountOfPrincipalPaymentYetToBePaid() + curLoan.getTheInterestYetToBePaidOnTheLoan()){
                moneyInThatCanBeAfterPayment -= curLoan.getTheAmountOfPrincipalPaymentYetToBePaid() + curLoan.getTheInterestYetToBePaidOnTheLoan();
                loansThatCanBeFullyPaid.add(curLoan.getNameOfLoan());
            }
        }
        return loansThatCanBeFullyPaid;
    }

    public List<String> checkIfCanPayAllLoans(Map<String,Integer> loansToPay, String customerName){
        int amountInBank = Customers.get(customerName).getMoneyInAccount();
        List<String> loansThatCanBePaid = new ArrayList<>();
        Map<String,Integer> sortedLendersByAmountToPay = loansToPay.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (Map.Entry<String, Integer> curLoan: sortedLendersByAmountToPay.entrySet()) {
            if(amountInBank != 0){
                amountInBank -= curLoan.getValue();
                loansThatCanBePaid.add(curLoan.getKey());
            }
        }
        return loansThatCanBePaid;
    }

    public Boolean checkIfMoneyCanBeWithdraw(int amount, String customerName){
        return amount <= Customers.get(customerName).getMoneyInAccount();
    }

    private void messageMaker(String customerName, int amount, String loanName, String msg) {
        StringBuilder message = new StringBuilder();
        message.append("Hello " + customerName + "\n" + msg + loanName);
        message.append("\n" + "The amount for payment is: " + amount);
        Customers.get(customerName).getNotifications().add(message.toString());
    }

    public void setYazProperty(){
        yazProperty = new SimpleStringProperty();
        yazProperty.set("Current Yaz: " + Yaz);
    }

    public void checkIfUserHasTheTotalPriceOfLoansUserWantsToBuy(Map<String,String> mapOfKeySellerAndValueLoan, String buyer){
        int sum = 0;
        for (Map.Entry<String,String> entry: mapOfKeySellerAndValueLoan.entrySet()) {
            for (LoanForSale curLoan: loanForSale) {
                //if(curLoan.getSeller().equals(entry.getKey()) && curLoan.getLoanName().equals(entry.get()))
            }
        }
    }

    //TODO  together with maya i think it should be map<string,List<string>>  <seller,LoansForSaleThatTheSellerSells>
    public void buyLoans(Map<String,String> mapOfKeySellerAndValueLoan, String buyer){
        for (Map.Entry<String,String> entry: mapOfKeySellerAndValueLoan.entrySet()) {
            //TODO  check if buyer has enough money to buy loan from seller (the logic need to be the same as in the invest method)
            Customers.get(entry.getKey()).getLoansAsALender().remove(LoansInBank.get(entry.getValue()).getNameOfLoan());
            LeftToPay leftToPayOfTheSeller = LoansInBank.get(entry.getValue()).getMapOfLenders().get(entry.getKey());
            LoansInBank.get(entry.getValue()).getMapOfLenders().remove(entry.getKey());
            LoansInBank.get(entry.getValue()).getMapOfLenders().put(buyer, leftToPayOfTheSeller);
            Customers.get(buyer).getLoansAsALender().add(LoansInBank.get(entry.getValue()).getNameOfLoan());
        }
        }

        private Integer amountOfPrincipalPaymentThatIsLeft(String nameOfLoan, String nameOfSeller) {
            Integer sum = 0;
            Integer tempIntrest = LoansInBank.get(nameOfLoan).getInterest();
            for (Integer curLeftToPay : LoansInBank.get(nameOfLoan).getMapOfLenders().get(nameOfSeller).getAmountLeftToPay()) {
                Integer tmp = (curLeftToPay * tempIntrest) / 100;
                sum += (curLeftToPay - tmp);
            }
            return sum;
        }

        public void putLoanOnSale(List<String> loansForSale, String seller){
            for (String curLoan: loansForSale) {
                loanForSale.add(new LoanForSale(seller, LoansInBank.get(curLoan).getMapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment().get(seller),curLoan));
            }
        }

        private void setNewAmount(String seller, String loanName){
            for (LoanForSale curLoan: loanForSale) {
                if(curLoan.getSeller().equals(seller) && curLoan.getLoanName().equals(loanName)){
                    curLoan.setPrice(LoansInBank.get(loanName).getMapOfAllBorrowersAndWhatIsLeftToPayFromThePrincipalPayment().get(seller));
                }
            }
        }

        public List<LoansForSaleDTO> getAllLoansForSaleForTheCustomer(String Buyer){
        List<LoansForSaleDTO> loansForSaleDTOList = new ArrayList<>();
            for (LoanForSale curLoan: loanForSale) {
                if(!curLoan.getSeller().equals(Buyer))
                    loansForSaleDTOList.add(new LoansForSaleDTO(curLoan));
            }
            return loansForSaleDTOList;
        }
}
