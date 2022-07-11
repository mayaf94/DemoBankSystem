package bankActionServlets;


import BankSystem.BankSystem;
import DTOs.AccountTransactionDTO;
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

@WebServlet(name = "SellLoansServlet",urlPatterns = {"/SellLoans"})
public class SellLoansServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> LoansToSell = gson.fromJson(request.getReader(), type);
        String SellerName = SessionUtils.getUsername(request);
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());


        if (LoansToSell == null || SellerName.isEmpty()) { //one of the required parameters is missing;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        synchronized (getServletContext()) {
            bankEngine.putLoanOnSale(LoansToSell, SellerName);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
