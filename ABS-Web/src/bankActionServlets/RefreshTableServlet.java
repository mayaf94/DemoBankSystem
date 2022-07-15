package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.BankSystemDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import userManager.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RefreshTableServlet",urlPatterns = {"/RefreshTables"})
public class RefreshTableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        BankSystemDTO bankSystemDTO = null;
        synchronized (getServletContext()) {
            if (bankEngine.isRewind()) {
                bankSystemDTO = userManager.getBankSystemDTOByYaz(bankEngine.getRewindYaz());
                bankSystemDTO.setRewind(bankSystemDTO.getRewind());
            }
            else
                bankSystemDTO = bankEngine.getBankSystemDTO();

            response.setStatus(HttpServletResponse.SC_OK);

            //create the response json string
//            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(BankSystemDTO.class, new BankSystemDTODeserializer())
//                    .registerTypeAdapter(CustomerDTOs.class, new CustomerDTOs.CustomerDTOAdapter())
//                    .registerTypeAdapter(AccountTransactionDTO.class, new AccountTransactionDTO.TransactionsDTOAdapter())
//                    .setPrettyPrinting()
//                    .create();

            Gson gson = new Gson();
            String jsonResponse = gson.toJson(bankSystemDTO);
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }

        }
    }
}

