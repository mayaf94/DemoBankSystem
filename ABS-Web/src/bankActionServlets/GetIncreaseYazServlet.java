package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.AccountTransactionDTO;
import DTOs.BankSystemDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import userManager.UserManager;
import utils.Constants.servletConstants;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class GetIncreaseYazServlet {

    @WebServlet(name = "IncreaseYaz",urlPatterns = {"/IncreaseYaz"})
    public class TransactionServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setContentType("application/json");
            String usernameFromSession = SessionUtils.getUsername(request);
            BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
            BankSystemDTO DTO = null;
            UserManager userManager = ServletUtils.getUserManager(getServletContext());

            synchronized (getServletContext()){

                    userManager.addVersionToBankSystemVersionMap(bankEngine);
                    DTO = bankEngine.IncreaseYaz();
                    response.setStatus(HttpServletResponse.SC_OK);

                    //create the response json string
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(DTO);
                    try (PrintWriter out = response.getWriter()) {
                        out.print(jsonResponse);
                        out.flush();
                    }
                }
            }
        }
}
