package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.AccountTransactionDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.Constants.servletConstants;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "TransactionServlet",urlPatterns = {"/Transaction"})
public class TransactionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {//TODO not sure if doGet or doPost
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        String amount = request.getParameter(servletConstants.TRANSACTION_AMOUNT);
        String typeOfTransaction = request.getParameter(servletConstants.TYPE_OF_TRANSACTION);
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        if (amount == null/* || usernameFromSession == null*/ || typeOfTransaction == null) { //one of the required parameters is missing;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        int amountOfTransaction = Integer.parseInt(amount);
        AccountTransactionDTO transaction = null;
        synchronized (getServletContext()){
            if(typeOfTransaction.equals("DEPOSIT"))
                 transaction = bankEngine.DepositToAccount(amountOfTransaction,usernameFromSession);
            else if(typeOfTransaction.equals("WITHDRAW")){
                transaction = bankEngine.WithdrawFromTheAccount(amountOfTransaction,usernameFromSession);
            }
            if(transaction == null){
                String errorMessage = "you cant withdraw more money than you have";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print(errorMessage);
            }
            else{
                response.setStatus(HttpServletResponse.SC_OK);
                //create the response json string
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(transaction);
                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse);
                    out.flush();
                }

            }
        }


    }

}
