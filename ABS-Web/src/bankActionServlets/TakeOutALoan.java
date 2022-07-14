package bankActionServlets;

import BankSystem.BankSystem;
import Costumers.Customer;
import DTOs.CustomerDTOs;
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
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(name = "TakeOutALoan",urlPatterns = {"/takeOutLoan"})
public class TakeOutALoan extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String nameOfLoan =  request.getParameter(servletConstants.LOANNAME);
        String principalAmount = request.getParameter(servletConstants.PRINCIPALAMOUNT);
        String durationOfLoan = request.getParameter(servletConstants.DURATION);
        String paymentFreq = request.getParameter(servletConstants.PAYMENTFREQ);
        String category = request.getParameter(servletConstants.CATEGORY);
        String interest = request.getParameter(servletConstants.INTEREST);
        String username = SessionUtils.getUsername(request);

        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());

        if (nameOfLoan.isEmpty() || principalAmount.isEmpty() || durationOfLoan.isEmpty() || paymentFreq.isEmpty() || category.isEmpty() || interest.isEmpty() || username.isEmpty()) {
            String errorMessage = "One or more parameters are missing!";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(errorMessage);
        }
        boolean valid = true;
        synchronized (getServletContext()) {
            if(checkIfLoanNameAlreadyExist(nameOfLoan,bankEngine)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print("There is already loan in bank with this name!");
                valid = false;
            }
            try {
                if (Integer.parseInt(principalAmount) <= 0) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print("original amount for the loan should be positive!");
                    valid = false;
                }
                if (Integer.parseInt(durationOfLoan) <= 0) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print("Duration of the loan should be positive!");
                    valid = false;
                }
                if (Integer.parseInt(paymentFreq) <= 0) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print("Payment Frequency for the loan should be positive!");
                    valid = false;
                }
                if (Integer.parseInt(interest) <= 0) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print("interest for the loan should be positive!");
                    valid = false;
                }
            }
            catch (Exception e){
                PrintWriter out = response.getWriter();
                out.print("please enter integer and not a decimal number");
                out.flush();
            }
            if(valid){
                response.setStatus(HttpServletResponse.SC_OK);
                LoanDTOs newLoan =  bankEngine.takeOutLoan(username,nameOfLoan,category,Integer.parseInt(principalAmount),Integer.parseInt(durationOfLoan),Integer.parseInt(paymentFreq),Integer.parseInt(interest));
                Gson gson = new Gson();
                String JsonResponse = (gson.toJson(newLoan));
                try (PrintWriter out = response.getWriter()) {
                    out.print(JsonResponse);
                    out.flush();
                }
            }
        }
    }

    private boolean checkIfLoanNameAlreadyExist(String loanName,BankSystem bankEngine){
        List<LoanDTOs> loansInBank = bankEngine.getListOfLoansDTO();
        boolean isExist = false;
        for(LoanDTOs curLoan : loansInBank){
            if(curLoan.getNameOfLoan().equals(loanName)){
                isExist = true;
            }
        }
        return isExist;
    }
}


