package com.example.service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) {
        // Check if username exists
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            return null; 
        }

        return accountRepository.save(account);
    }

    public Account login(Account loginAccount) {
        Account account = accountRepository.findByUsername(loginAccount.getUsername());
        // Checks if username and password provided in the request body JSON match a real account existing on the database
        if (account != null && account.getPassword().equals(loginAccount.getPassword())) {
            return account;
        }

        return null;
    }


    public boolean existsById(int accountId) {
        return accountRepository.existsById(accountId);
    }


}
