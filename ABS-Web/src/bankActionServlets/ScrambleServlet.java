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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "ScrambleServlet",urlPatterns = {"/Scramble"})
public class ScrambleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {//TODO not sure if doGet or doPost

        response.setContentType("application/json");
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        String minimumDuration = request.getParameter(servletConstants.MINDURATION);
        String minimumInterestForSingleYaz = request.getParameter(servletConstants.MININTREST);
        String maxOpenLoansForLoanOwner = request.getParameter(servletConstants.MAXLOANOWNER);
        String Categories = request.getParameter(servletConstants.CATEGORIES);
        Boolean maxOpenLoansForLoanOwnerSelected = Boolean.parseBoolean(request.getParameter(servletConstants.MAXOPENLOANSSELECTED));
        List<String> CategoriesList = Arrays.asList(Categories.split( ","));
        int maxOpenLoansForLoanOwnerIntVal = bankEngine.getListOfLoansDTO().size();
        if (usernameFromSession == null || minimumDuration == null || minimumInterestForSingleYaz == null || maxOpenLoansForLoanOwner == null || CategoriesList.size() == 0) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        if(maxOpenLoansForLoanOwnerSelected){
            maxOpenLoansForLoanOwnerIntVal = Integer.parseInt(maxOpenLoansForLoanOwner);
        }

        List<LoanDTOs> MatchingLoans = bankEngine.ActivationOfAnInlay(bankEngine.getAllCategories().getCategories(),Integer.parseInt(minimumDuration),Integer.parseInt(minimumInterestForSingleYaz),maxOpenLoansForLoanOwnerIntVal,usernameFromSession);
        if(MatchingLoans == null) {
            String errorMessage = "an error occurred";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(errorMessage);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            //create the response json string
            Gson gson = new Gson();
            String JsonResponse = (gson.toJson(MatchingLoans));
            try (PrintWriter out = response.getWriter()) {
                out.print(JsonResponse);
                out.flush();
            }
        }
    }

}
