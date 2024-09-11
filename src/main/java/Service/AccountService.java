package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) { {

            Account existingAccount = accountDAO.getAccountByUsername(account);
            if (existingAccount != null) {
                return null;
            }
            return accountDAO.insertAccount(account);
        }
    }
    public boolean validateLogin(String username, String password) {
        return accountDAO.verifyLogin(username, password);
    }


}
