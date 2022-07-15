package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.LoanDTOs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Constants.servletConstants;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "PaymentForLoan",urlPatterns = {"/LoansPayment"})
public class PaymentForLoans extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        List<String> LoansCanBePaid;
        String usernameFromSession = SessionUtils.getUsername(request);
        String typeOfPayment = request.getParameter(servletConstants.TYPEOFPAYMENT);
        String AutoOrManuel = request.getParameter(servletConstants.AUTOPAYMENT);
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());

        if (typeOfPayment == null || AutoOrManuel == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        Gson gson = new Gson();
        Type type;
        if(typeOfPayment.equals("full")){
            type = new TypeToken<List<String>>() {
            }.getType();
        }
        else{
            type =  new TypeToken<List<LoanDTOs>>() {
            }.getType();
        }
            List<?> LoansToPayFor = gson.fromJson(request.getReader(), type);


        synchronized (getServletContext()){
            if(typeOfPayment.equals("full")){
                LoansCanBePaid = bankEngine.checkWhatLoansCanBeFullyPaidSystem((List<String>) LoansToPayFor, usernameFromSession);
                if(AutoOrManuel.equals("manual")) {
                    if (LoansCanBePaid.size() != LoansToPayFor.size()) {
                        StringJoiner joiner = new StringJoiner(",");
                        LoansCanBePaid.stream().forEach(C -> joiner.add(C));
                        String LoansCanBePaidForResponse = joiner.toString();
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(LoansCanBePaidForResponse);
                    }
                    else{
                        bankEngine.fullPaymentOnLoans(LoansCanBePaid, usernameFromSession);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }

                }
                else {
                    bankEngine.fullPaymentOnLoans(LoansCanBePaid,usernameFromSession);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
            if(typeOfPayment.equals("yazly")){
                List<LoanDTOs> filtterdLoans = (List<LoanDTOs>) LoansToPayFor;
                filtterdLoans = filtterdLoans.stream().filter(L->L.getNextYazPayment() == bankEngine.getCurrentYaz()).collect(Collectors.toList());
                Map<String,Integer> loansToPayAndAmountOfPayment = new HashMap<>();
                    if(filtterdLoans.size() != 0)
                        LoansCanBePaid = bankEngine.checkIfCanPayAllLoans(loansToPayAndAmountOfPayment,usernameFromSession);
                    else {
                        LoansCanBePaid = new ArrayList<>();
                    }
                    for(LoanDTOs curLoan : filtterdLoans){
                        loansToPayAndAmountOfPayment.put(curLoan.getNameOfLoan(),Integer.parseInt(curLoan.getAmountToPay()));
                    }
                if(AutoOrManuel.equals("manual")) {

                    if (LoansCanBePaid.size() != filtterdLoans.size()) {
                        StringJoiner joiner = new StringJoiner(", ");
                        LoansCanBePaid.stream().forEach(C -> joiner.add(C));
                        String LoansCanBePaidForResponse = joiner.toString();
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(LoansCanBePaidForResponse);
                    }
                    else{
                        bankEngine.YazlyPaymentForGivenLoans(loansToPayAndAmountOfPayment);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }

                }
                else {
                    bankEngine.YazlyPaymentForGivenLoans(loansToPayAndAmountOfPayment);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
        }
    }

}
