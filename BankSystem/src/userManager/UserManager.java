package userManager;

import BankSystem.BankSystem;
import DTOs.BankSystemDTO;

import java.util.*;

public class    UserManager {

    private final Set<String> usersSet;
    private BankSystem bankEngine;
    private Map<Integer, BankSystemDTO> bankSystemVersionMap = new HashMap<>();
    private Boolean isAdminActive = false;

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public void setBankEngine(BankSystem bankEngine) {
        this.bankEngine = bankEngine;
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
        bankEngine.addCustomerToBank(username);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
        //TODO not sure if we need to remove him from customers list in bank
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public synchronized void addVersionToBankSystemVersionMap(BankSystemDTO bankSystemVersion){
        bankSystemVersionMap.put(bankSystemVersion.getCurYaz(), bankSystemVersion);
    }

    public synchronized BankSystemDTO getBankSystemDTOByYaz(Integer yaz){
        return bankSystemVersionMap.get(yaz);
    }
}
