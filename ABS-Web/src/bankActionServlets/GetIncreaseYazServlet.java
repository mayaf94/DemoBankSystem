package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.AccountTransactionDTO;
import DTOs.BankSystemDTO;
import DTOs.CustomerDTOs;
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


@WebServlet(name = "GetIncreaseYazServlet",urlPatterns = {"/IncreaseYaz"})
public class GetIncreaseYazServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        BankSystemDTO DTO = null;
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        synchronized (getServletContext()) {

            if(!bankEngine.isRewind())
                userManager.addVersionToBankSystemVersionMap(bankEngine.getBankSystemDTO());
            DTO = bankEngine.IncreaseYaz();
            response.setStatus(HttpServletResponse.SC_OK);

 /*           Gson gson2c = new GsonBuilder()
                    .registerTypeAdapter(CusomerDTOs.class, new CustomerDTOs.CustomerDTOAdapter())
                    .create();
            String resp = gson2c.toJson(DTO, CustomerDTOs.class);*/

            //create the response json string
           // Gson gson = new GsonBuilder()
                  //  .registerTypeAdapter(BankSystemDTO.class, new BankSystemDTODeserializer())
//                    .registerTypeAdapter(CustomerDTOs.class, new CustomerDTOs.CustomerDTOAdapter())
//                    .registerTypeAdapter(AccountTransactionDTO.class, new AccountTransactionDTO.TransactionsDTOAdapter())
//                    .setPrettyPrinting()
//                    .create();
           // Gson gson = new Gson();
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(DTO);
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
        }
    }
}
