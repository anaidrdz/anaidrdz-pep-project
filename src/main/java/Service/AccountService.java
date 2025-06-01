package Service;

import DAO.AccountDAO;
import Model.Account;
import java.sql.SQLException;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account addNewAccount(Account account) throws SQLException {
        Account exists = accountDAO.getAccountWithUserName(account.getUsername());
        if(exists != null || 
        account.getUsername() == null || 
        account.getPassword() == null ||
        account.getUsername().isBlank() || 
        account.getPassword().length() < 4) {
            return null;
        }
        return accountDAO.addNewAccount(account);
    }
    
    public Account userLogin(String username, String password) throws SQLException {
        if(username == null || password == null || username.isBlank() || password.isBlank()) {
            return null;
        }
        return accountDAO.userLogin(username, password);
    }
}

