package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.LoanDTOs;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Constants.servletConstants;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "InvestInLoans",urlPatterns = {"/investInLoans"})
public class InvestInLoansServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        String maxLoanOwenerShip = request.getParameter(servletConstants.MAXLOANOWENERSHIP);
        String amountToInvest = request.getParameter(servletConstants.AMOUNT_TO_INVEST);
        String loansNames = request.getParameter(servletConstants.LOANSNAMES);
        List<String> LoansNamesAsList = Arrays.asList(loansNames.split( ","));
        if (amountToInvest.isEmpty() || loansNames.isEmpty() || maxLoanOwenerShip.isEmpty() || usernameFromSession.isEmpty()) { //one of the required parameters is missing;
            String errorMessage = "One or more parameters are missing!";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(errorMessage);

        }
        Boolean valid = true;
        synchronized (getServletContext()) {
            //TODO validation such as maxLoanOwner <= 100 that user balance >= amount to invest and that loans names list are real names of loans in bank TODO
            if(!checkIfLoansAreExistInBank(LoansNamesAsList,bankEngine)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print("Please enter a valid value for amount to invest");
                valid = false;
            }
            try {
                if (Integer.parseInt(maxLoanOwenerShip) > 100 || Integer.parseInt(maxLoanOwenerShip) <= 0) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print("Please enter a valid value for max Loan ownerShip");
                    valid = false;
                }
                if (Integer.parseInt(amountToInvest) <= 0) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print("Please enter a valid value for amount to invest");
                    valid = false;
                }
            }
            catch (Exception e){
                PrintWriter out = response.getWriter();
                out.print("please enter positive integer and not a decimal or negative number");
                out.flush();
            }
            if(valid) {
                bankEngine.LoansInlay(LoansNamesAsList, Integer.parseInt(amountToInvest), usernameFromSession, Integer.parseInt(maxLoanOwenerShip));
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }


    Boolean checkIfLoansAreExistInBank(List<String> i_LoansNamesAsList,BankSystem bankEngine){
        List<LoanDTOs> loansInBank = bankEngine.getListOfLoansDTO();
        boolean valid = false;
        for(String loanName : i_LoansNamesAsList){
            for(LoanDTOs curLoan : loansInBank){
                curLoan.getNameOfLoan().equals(loanName);
                valid = true;
            }
            if(!valid){
                return false;
            }
            valid = false;
        }
        return true;
    }



}
