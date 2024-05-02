package sg.edu.ntu.javaproject.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import sg.edu.ntu.javaproject.entity.Account;
import sg.edu.ntu.javaproject.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
public class AccountController {
    private AccountService accountService;
    private ObjectMapper objectMapper;

    public AccountController(AccountService accountService, ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.objectMapper = objectMapper;
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

    @PostMapping({ "", "/" })
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account) throws JsonProcessingException {
        if (isAdmin()) {
            Account newAccount = accountService.createAccount(account);
            String accountJson = objectMapper.writeValueAsString(newAccount);
            log.info("new account created: " + accountJson);
            return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied!");
    }

    @GetMapping({ "", "/" })
    public ResponseEntity<ArrayList<Account>> getAllAccounts() throws JsonProcessingException {
        ArrayList<Account> allAccounts = accountService.getAllAccounts();
        String allAccountsJson = objectMapper.writeValueAsString(allAccounts);
        log.info("retreiving all accounts");
        log.info("account list: " + allAccountsJson);
        return new ResponseEntity<>(allAccounts, HttpStatus.OK);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Account> getAccountById(@PathVariable Integer id) throws JsonProcessingException {
        Account accountById = accountService.getAccountById(id);
        String accountJson = objectMapper.writeValueAsString(accountById);
        log.info("search account by account id " + id);
        log.info("account details: " + accountJson);
        return new ResponseEntity<>(accountById, HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> searchByCustomerId(@PathVariable Integer id)
            throws JsonProcessingException {
        ArrayList<Account> accountList = accountService.getAccountByCustomerId(id);
        String accountJson = objectMapper.writeValueAsString(accountList);
        log.info("search accounts   by customer id: " + id);
        log.info("account list: " + accountJson);
        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAccountById(@PathVariable Integer id) {
        if (isAdmin()) {
            accountService.deleteAccountById(id);
            return ResponseEntity.status(HttpStatus.OK).body("account id: " + id + " has been deleted");
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied!");
    }

    @PutMapping({ "/{id}", "/{id}/" })
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAccountById(@PathVariable Integer id, @RequestBody Account account)
            throws JsonProcessingException {
        if (isAdmin()) {
            Account updatedAccount = accountService.updateAccount(id, account);
            String accountJson = objectMapper.writeValueAsString(updatedAccount);
            log.info("updating account id: " + id);
            log.info("new account details: " + accountJson);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied!");
    }
}
