package systemManagementUtils;

import BankSystem.BankSystem;
import DTOs.CategoriesDTO;
import DTOs.LoanDTOs;
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
import java.util.List;

import static utils.Constants.servletConstants.USERNAME;

@WebServlet(name = "GetAllCategoriesServlet",urlPatterns = {"/GetCategories"})
public class GetAllCategories extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        CategoriesDTO categoriesBeforeFileReading = null;
        synchronized (this) {
            categoriesBeforeFileReading = bankEngine.getAllCategories();
        }
        if(categoriesBeforeFileReading == null) {
            categoriesBeforeFileReading = new CategoriesDTO(null);
            String errorMessage = "There is no categories in system";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(errorMessage);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        //create the response json string
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(categoriesBeforeFileReading);
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
    }
}

