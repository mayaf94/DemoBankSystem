package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.AccountTransactionDTO;
import DTOs.BankSystemDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

@WebServlet(name = "RefreshTableServlet",urlPatterns = {"/RefreshAdminTables"})
public class RefreshTableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {//TODO not sure if doGet or doPost
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        String isRewind = request.getParameter(servletConstants.ISREWIND);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        BankSystemDTO bankSystemDTO = null;
        synchronized (getServletContext()) {
            if (isRewind.equals("true"))
                bankSystemDTO = userManager.getBankSystemDTOByYaz(bankEngine.getRewindYaz());
            else
                bankSystemDTO = bankEngine.getBankSystemDTO();

            response.setStatus(HttpServletResponse.SC_OK);

            //create the response json string
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(BankSystemDTO.class, new BankSystemDTODeserializer())
                    .registerTypeAdapter(CustomerDTOs.class, new CustomerDTOs.CustomerDTOAdapter())
                    .registerTypeAdapter(AccountTransactionDTO.class, new AccountTransactionDTO.TransactionsDTOAdapter())
                    .setPrettyPrinting()
                    .create();

            String jsonResponse = gson.toJson(bankSystemDTO);
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }

        }
    }
}

