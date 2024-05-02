package sg.edu.ntu.javaproject.service;

import java.util.ArrayList;

import sg.edu.ntu.javaproject.entity.Account;

public interface AccountService {
    Account createAccount(Account account);

    Account getAccountById(Integer id);

    ArrayList<Account> getAllAccounts();

    Account updateAccount(Integer id, Account account);

    void deleteAccountById(Integer id);

    ArrayList<Account> getAccountByCustomerId(Integer id);
}
