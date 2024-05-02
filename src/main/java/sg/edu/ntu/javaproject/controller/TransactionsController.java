package sg.edu.ntu.javaproject.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import sg.edu.ntu.javaproject.entity.Transactions;
import sg.edu.ntu.javaproject.service.TransactionsService;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {
    private TransactionsService transactionsService;
    private ObjectMapper objectMapper;

    public TransactionsController(TransactionsService transactionsService, ObjectMapper objectMapper) {
        this.transactionsService = transactionsService;
        this.objectMapper = objectMapper;
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

    @PostMapping({ "/withdraw", "/withdraw/" })
    public ResponseEntity<?> withdrawTransaction(@Valid @RequestBody Transactions transaction)
            throws JsonProcessingException {
        if (!isAdmin()) {
            Transactions newTransaction = transactionsService.withdrawTransaction(transaction);
            String transactionJson = objectMapper.writeValueAsString(newTransaction);
            log.info("new withdraw transaction created: " + transactionJson);
            return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
        } else {
            log.error("unable to access /transactions/withdraw endpoint: access denied");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied!");
        }
    }

    @PostMapping({ "/deposit", "/deposit/" })
    public ResponseEntity<?> depositTransaction(@Valid @RequestBody Transactions transaction)
            throws JsonProcessingException {
        if (!isAdmin()) {
            Transactions newTransaction = transactionsService.depositTransaction(transaction);
            String transactionJson = objectMapper.writeValueAsString(newTransaction);
            log.info("new deposit transaction created: " + transactionJson);
            return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
        } else {
            log.error("unable to access /transactions/deposit endpoint: access denied");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied!");
        }
    }

    @PostMapping({ "/transfer" })
    public ResponseEntity<?> transaferTransaction(@RequestParam Integer accountNo,
            @Valid @RequestBody Transactions transaction) throws JsonProcessingException {
        if (!isAdmin()) {
            Transactions newTransaction = transactionsService.transferTransaction(accountNo, transaction);
            String transactionJson = objectMapper.writeValueAsString(newTransaction);
            log.info("new transfer transaction created: " + transactionJson);
            return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
        } else {
            log.error("unable to access /transactions/transfer endpoint: access denied");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied!");
        }
    }

    @GetMapping({ "", "/" })
    public ResponseEntity<ArrayList<Transactions>> getAllTransactions() throws JsonProcessingException {
        ArrayList<Transactions> transactions = transactionsService.getAllTransactions();
        String transactionJson = objectMapper.writeValueAsString(transactions);
        log.info("retreiving all transactions");
        log.info("transaction list: " + transactionJson);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<Transactions> getTransactionById(@PathVariable Integer id) throws JsonProcessingException {
        Transactions transaction = transactionsService.getTransactionsById(id);
        String transactionJson = objectMapper.writeValueAsString(transaction);
        log.info("retreiving transactions with id: " + id);
        log.info("transaction details: " + transactionJson);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @GetMapping({"/customer/{id}","/customer/{id}/"})
    public ResponseEntity<ArrayList<Transactions>> getTransactionsByCustomerId(@PathVariable Integer id) throws JsonProcessingException {
        ArrayList<Transactions> transactions = transactionsService.getTransactionsByCustomerId(id);
        String transactionJson = objectMapper.writeValueAsString(transactions);
        log.info("retreiving transactions for customer id: "+id);
        log.info("transactions list: "+transactionJson);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
