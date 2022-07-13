package bankActionServlets;

import BankSystem.BankSystem;
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
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "InvestInLoans",urlPatterns = {"/investInLoans"})
public class InvestInLoans extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        String maxLoanOwenerShip = request.getParameter(servletConstants.MAXLOANOWENERSHIP);
        String amountToInvest = request.getParameter(servletConstants.AMOUNT_TO_INVEST);
        String loansNames = request.getParameter(servletConstants.LOANSNAMES);
        List<String> LoansNamesAsList = Arrays.asList(loansNames.split( ","));
        if (amountToInvest == null || loansNames.isEmpty() || maxLoanOwenerShip.isEmpty() || usernameFromSession.isEmpty()) { //one of the required parameters is missing;
            String errorMessage = "One or more parameters are missing!";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(errorMessage);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }

        synchronized (getServletContext()) {
            //validation such as maxLoanOwner <= 100 that user balance >= amount to invest and that loans names list are real names of loans in bank TODO
            //if validations are Ok so

            bankEngine.LoansInlay(LoansNamesAsList,Integer.parseInt(amountToInvest),usernameFromSession,Integer.parseInt(maxLoanOwenerShip));
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
