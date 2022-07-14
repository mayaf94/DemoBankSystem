package customerServlets;

import BankSystem.BankSystem;
import DTOs.BankSystemDTO;
import DTOs.CategoriesDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetAllLoansAndCustomersServlet",urlPatterns = {"/GetAllLoansAndCustomers"})
public class GetAllLoansAndCustomersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        BankSystemDTO bankSystemDTO = null;
        synchronized (this) {
            bankSystemDTO = bankEngine.getBankSystemDTO();
        }

        response.setStatus(HttpServletResponse.SC_OK);
        //create the response json string
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(bankSystemDTO);
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}
