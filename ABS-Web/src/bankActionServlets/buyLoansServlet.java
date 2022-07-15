package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.LoanDTOs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;


@WebServlet(name = "buyLoansServlet",urlPatterns = {"/BuyLoans"})
public class buyLoansServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoanDTOs>>() {
        }.getType();
        List<LoanDTOs> LoansToBuy = gson.fromJson(request.getReader(), type);
        String buyersName = SessionUtils.getUsername(request);
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());


        if (LoansToBuy == null || buyersName.isEmpty()) { //one of the required parameters is missing;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            gson.toJson("There was a problem");
        }
        synchronized (getServletContext()) {
            Map<String, List<String>> sellersAndLoansMap = new HashMap<>();
            for (LoanDTOs curLoan : LoansToBuy) {
                if (sellersAndLoansMap.containsKey(curLoan.getSeller()))
                    sellersAndLoansMap.get(curLoan.getSeller()).add(curLoan.getNameOfLoan());
                else
                    sellersAndLoansMap.put(curLoan.getSeller(), new ArrayList<>(Collections.singleton(curLoan.getNameOfLoan())));
            }
            Boolean wasThereABuy = bankEngine.buyLoans(sellersAndLoansMap, buyersName);
            response.setStatus(HttpServletResponse.SC_OK);
            gson.toJson(wasThereABuy);
        }
    }
}

