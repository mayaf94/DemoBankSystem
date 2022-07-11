package SystemExceptions;

import java.io.Serializable;

public class InccorectInputType extends Exception {
    private static String  notInteger = "The answer you typed does not match the subtype the system needs. Please type an answer in type of: ";
    private static String notDivided = "The duration of the loan is not divided without a remainder in the frequency of payments";
    private static String LoanNameDuplication = "This Loan name is already exist in the system.";
    private static String CustomerDuplication = "This customer names is already exist in the system";
    private static String wrongFileType = "You should provide file name that ends with .xml";
    private static String ChoiceIsOutOfRange = "one or more of your choice is out of the valid range";
    private static String greaterThenZero = "Please insert a positive number";

    public static String getChoiceIsOutOfRange() {
        return ChoiceIsOutOfRange;
    }

    public static String getGreaterThenZero() {
        return greaterThenZero;
    }

    public static String getWrongFileType() {
        return wrongFileType;
    }

    public static String getCustomerDuplication() {
        return CustomerDuplication;
    }

    public static String getNotInteger() {
        return notInteger;
    }

    public static String getNotDivided() {
        return notDivided;
    }

    public InccorectInputType(String message) {
        super(message);
    }

    public static String getLoanNameDuplication() {
        return LoanNameDuplication;
    }
}
