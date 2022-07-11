package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.CategoriesDTO;
import DTOs.LoanDTOs;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LoansFromFileServlet",urlPatterns = {"/LoansFromFile"})
public class GetNewLoansFromFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {//TODO not sure if doGet or doPost
        response.setContentType("application/json");
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        String filePath = request.getParameter(servletConstants.XML_PATH);
        String usernameFromSession = SessionUtils.getUsername(request);
        List<LoanDTOs> loansFromFile;
        if(filePath == null || usernameFromSession == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        CategoriesDTO categoriesBeforeFileReading = bankEngine.getAllCategories();
        loansFromFile = bankEngine.ReadingTheSystemInformationFile(filePath, usernameFromSession);
        if(loansFromFile == null){
            String errorMessage = "XML file is invalid";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(errorMessage);
        }
        else{
            response.setStatus(HttpServletResponse.SC_OK);
            //create the response json string
            Gson gson = new Gson();
            String JsonResponse = (gson.toJson(loansFromFile));
            String AreNewCategories = "false";
            if(categoriesBeforeFileReading.getCategories().size() < bankEngine.getAllCategories().getCategories().size()){
                AreNewCategories = "true";
            }
            response.addHeader("NEW_CATEGORIES", AreNewCategories);
            try (PrintWriter out = response.getWriter()) {
                out.print(JsonResponse);
                out.flush();
            }
        }


    }
}
