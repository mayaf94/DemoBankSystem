package BankSystem;

import BankActions.Loan;
import Costumers.Customer;
import SystemExceptions.InccorectInputType;
import mypackage.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class FromXmlToClasses implements Serializable {

    public static boolean fromXmlToObjects(List<Loan> loanInSystem, String FileName,
                                        Set<String> allCategoriesInSystem, String customerName, List<String> allLoansNames) {
        boolean greatSuccess = true;
        try {
            File file = new File(FileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AbsDescriptor descriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);
            allCategoriesInSystem.addAll(descriptor.getAbsCategories().getAbsCategory());
            fromObjectToLoans(loanInSystem, descriptor.getAbsLoans().getAbsLoan(), descriptor.getAbsCategories().getAbsCategory(), customerName);
            loansIdCheckWithLoansInSystem(allLoansNames, loanInSystem);

        } catch (JAXBException | InccorectInputType e) {
            System.out.println(e);
            greatSuccess = false;
        }
        return greatSuccess;
    }

//    private static void fromObjectToCustomersList(List<Customer> customerInSystem, List<AbsCustomer> absCustomers, List<Loan> loansInSystems) throws InccorectInputType {
//        List<Loan> loansAfterNameFilter;
//        List<String> loanNamesAsBorrower;
//        if(fromListToMapCheck(absCustomers.stream().map(AbsCustomer::getName).collect(Collectors.toList()))){
//        for (AbsCustomer curCustomer: absCustomers) {
//            loansAfterNameFilter = loansInSystems.stream().filter(L -> L.getNameOfLoaner().equals(curCustomer.getName())).collect(Collectors.toList());
//            loanNamesAsBorrower = loansNames(loansAfterNameFilter);
//            customerInSystem.add(new Customer(curCustomer.getName(), curCustomer.getAbsBalance(), loanNamesAsBorrower));
//        }
//        }
//        else
//            throw new InccorectInputType(InccorectInputType.getCustomerDuplication());
//    }

    private static void fromObjectToLoans(List<Loan> loansInSystem, List<AbsLoan> absLoans, List<String> categories, String customerName) throws InccorectInputType {
        for (AbsLoan curLoan: absLoans) {
            if (fromListToMapCheck(absLoans.stream().map(AbsLoan::getId).collect(Collectors.toList()))) {
                if (categories.contains(curLoan.getAbsCategory())) {
                        if ((curLoan.getAbsTotalYazTime()) % (curLoan.getAbsPaysEveryYaz()) == 0) {
                            loansInSystem.add(new Loan(curLoan.getId(), customerName, curLoan.getAbsCategory(),
                                    curLoan.getAbsCapital(), curLoan.getAbsTotalYazTime(),
                                    curLoan.getAbsPaysEveryYaz(), curLoan.getAbsIntristPerPayment()));
                        } else
                            throw new InccorectInputType(InccorectInputType.getNotDivided());
                } else
                    throw new InccorectInputType("The category for loan: " + curLoan.getId() + "does not exist in the system");
            }
            else
                throw new InccorectInputType(InccorectInputType.getLoanNameDuplication());
        }
    }

//    private static List<String> loansNames(List<Loan> loansAfterFilter){
//        List<String> listOfLoansNames = new ArrayList<>();
//        for (Loan curLoan: loansAfterFilter) {
//            listOfLoansNames.add(curLoan.getNameOfLoan());
//        }
//        return listOfLoansNames;
//    }

    private static Boolean fromListToMapCheck(List<String> allNames){
        List<String> listAfterChangToUpperCase;
        listAfterChangToUpperCase = allNames.stream().map(String::toUpperCase).collect(Collectors.toList());
        Set<String> customerMap = new HashSet<>(listAfterChangToUpperCase);
        if(customerMap.size() == allNames.size())
            return true;
        return false;
    }

    private static void loansIdCheckWithLoansInSystem(List<String> loansNamesInSystem, List<Loan> loansFromFile) throws InccorectInputType {
        for (Loan curLoan: loansFromFile) {
            if(loansNamesInSystem.contains(curLoan.getNameOfLoan()))
                throw new InccorectInputType(InccorectInputType.getLoanNameDuplication());//TODO: print error message to client
        }
    }
}
