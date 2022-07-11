package utils;

import BankSystem.BankSystem;
import BankSystem.SystemImplement;
import jakarta.servlet.ServletContext;
import userManager.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String Bank_SYSTEM_ATTRIBUTE_NAME = "BankSystem";
    private static final Object userManagerLock = new Object();
    private static final Object bankSystemLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                UserManager userManager = new UserManager();
                userManager.setBankEngine(getBankSystem(servletContext));
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, userManager);
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static BankSystem getBankSystem(ServletContext servletContext){
        synchronized (bankSystemLock) {
            if (servletContext.getAttribute(Bank_SYSTEM_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(Bank_SYSTEM_ATTRIBUTE_NAME, new SystemImplement());

            }
        }
        return (BankSystem) servletContext.getAttribute(Bank_SYSTEM_ATTRIBUTE_NAME);
    }
}
