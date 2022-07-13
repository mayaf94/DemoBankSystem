package userManagemantServlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import userManager.UserManager;
import utils.Constants.servletConstants;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

import static utils.Constants.servletConstants.USERNAME;

@WebServlet(name = "LoginServlet",urlPatterns = {"/login"})
public class LoginServlets extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String isAdmin = request.getParameter("admin");

        if (usernameFromSession == null) { //user is not logged in yet
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict
                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if(isAdmin.equals("true")) {
                        if (ServletUtils.getAdminName(getServletContext())) {
                            String errorMessage = "The Bank already has admin!!!";
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getOutputStream().print(errorMessage);
                        }
                    }
                    if(isAdmin.equals("false") || !ServletUtils.getAdminName(getServletContext())) {
                        if (userManager.isUserExists(usernameFromParameter)) {
                            String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                            // stands for unauthorized as there is already such user with this name
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getOutputStream().print(errorMessage);
                        } else {
                            //add the new user to the users list
                            userManager.addUser(usernameFromParameter);
                            request.getSession(true).setAttribute(servletConstants.USERNAME, usernameFromParameter);
                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
