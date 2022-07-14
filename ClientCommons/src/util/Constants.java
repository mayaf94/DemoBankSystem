package util;

import com.google.gson.Gson;

public class Constants {
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/ABSApp";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;


    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String ChargeAccount = FULL_SERVER_PATH + "/Transaction";
    public final static String LoadLoansFromXML = FULL_SERVER_PATH + "/LoansFromFile";
    public final static String CATEGORISATIONS = FULL_SERVER_PATH + "/GetCategories";
    public final static String SELL_LOANS = FULL_SERVER_PATH + "/SellLoans";
    public final static String SCRAMBLE = FULL_SERVER_PATH + "/Scramble";
    public final static String INCREASE_YAZ = FULL_SERVER_PATH + "/IncreaseYaz";
    public final static String REWIND_YAZ = FULL_SERVER_PATH + "/RewindYaz";
    public static final String LOANSPAYMENT = FULL_SERVER_PATH + "/LoansPayment";
    public static final String INVESMENTS = FULL_SERVER_PATH + "/investInLoans";
    public final static String TAKE_OUT_A_LOAN = FULL_SERVER_PATH + "/takeOutLoan";





    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();

}
