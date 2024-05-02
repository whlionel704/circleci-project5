package sg.edu.ntu.javaproject.Implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sg.edu.ntu.javaproject.Exception.AccountNotFoundException;
import sg.edu.ntu.javaproject.Exception.AccountNumberIsNotExistException;
import sg.edu.ntu.javaproject.Exception.CustomerAndAccountNotFoundException;
import sg.edu.ntu.javaproject.Exception.CustomerNotFoundException;
import sg.edu.ntu.javaproject.Exception.ForbiddenAccessException;
import sg.edu.ntu.javaproject.Exception.InsufficientBalanceException;
import sg.edu.ntu.javaproject.Exception.NullException;
import sg.edu.ntu.javaproject.entity.Account;
import sg.edu.ntu.javaproject.entity.Customers;
import sg.edu.ntu.javaproject.entity.Transactions;
import sg.edu.ntu.javaproject.repository.AccountRepository;
import sg.edu.ntu.javaproject.repository.CustomerRepository;
import sg.edu.ntu.javaproject.repository.TransactionsRepository;
import sg.edu.ntu.javaproject.service.TransactionsService;

@Service
public class TransactionsServiceImpl implements TransactionsService {
    private TransactionsRepository transactionsRepository;
    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;

    public TransactionsServiceImpl(TransactionsRepository transactionsRepository, CustomerRepository customerRepository,
            AccountRepository accountRepository) {
        this.transactionsRepository = transactionsRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    private Customers getCurrentCustomer() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getName();
        return customerRepository.findByCustomerEmail(username);
    }

    @Override
    public Transactions withdrawTransaction(Transactions transaction) {
        Customers checkCustomer = getCurrentCustomer();
        // check if customerid with account type is exist
        if (!accountRepository.existByAccountType(checkCustomer.getCustomerId(), transaction.getAccountTypeId())) {
            throw new CustomerAndAccountNotFoundException(checkCustomer.getCustomerId(),
                    transaction.getAccountTypeId());
        }
        // check if customer have enough balance on their account type
        Optional<Account> optionalAccount = accountRepository
                .findByCustomerIdAndAccountTypeId(checkCustomer.getCustomerId(), transaction.getAccountTypeId());
        if (optionalAccount.isPresent()) {
            Account savedAccount = optionalAccount.get();
            if (savedAccount.getBalance() < transaction.getAmount()) {
                throw new InsufficientBalanceException(savedAccount.getBalance(), transaction.getAmount());
            }
        } else
            throw new CustomerAndAccountNotFoundException(checkCustomer.getCustomerId(),
                    transaction.getAccountTypeId());
        Account savedAccount = optionalAccount.get();
        int balanceBefore = savedAccount.getBalance();
        int balanceAfter = (balanceBefore - transaction.getAmount());
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setTransactionTypeId(2);
        transaction.setSourceAccount(savedAccount.getAccountNumber());
        transaction.setSourceCustomerId(savedAccount.getCustomerId());
        // transaction.setDestinationCustomerId(checkCustomer.getCustomerId());
        savedAccount.setBalance(balanceAfter);
        accountRepository.save(savedAccount);
        return transactionsRepository.save(transaction);

    }

    @Override
    public Transactions depositTransaction(Transactions transaction) {
        Customers checkCustomer = getCurrentCustomer();
        // check if customerid with account type is exist
        if (!accountRepository.existByAccountType(checkCustomer.getCustomerId(), transaction.getAccountTypeId())) {
            throw new CustomerAndAccountNotFoundException(checkCustomer.getCustomerId(),
                    transaction.getAccountTypeId());
        }
        Optional<Account> optionalAccount = accountRepository
                .findByCustomerIdAndAccountTypeId(checkCustomer.getCustomerId(), transaction.getAccountTypeId());
        if (!optionalAccount.isPresent())
            throw new CustomerNotFoundException(checkCustomer.getCustomerId());
        Account savedAccount = optionalAccount.get();
        int balanceBefore = savedAccount.getBalance();
        int balanceAfter = (balanceBefore + transaction.getAmount());
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setTransactionTypeId(1);
        // transaction.setSourceAccount(checkCustomer.getCustomerId());
        transaction.setDestinationAccount(savedAccount.getAccountNumber());
        transaction.setDestinationCustomerId(savedAccount.getCustomerId());
        savedAccount.setBalance(balanceAfter);
        accountRepository.save(savedAccount);
        return transactionsRepository.save(transaction);
    }

    @Override
    public Transactions transferTransaction(Integer accountNo, Transactions transaction) {
        Customers checkCustomer = getCurrentCustomer();
        // check if account number is exist
        if (!accountRepository.existsByAccountNumber(accountNo))
            throw new AccountNumberIsNotExistException(accountNo);
        // check mandatory paramter
        if (transaction.getAccountTypeId() == null || transaction.getAmount() == null)
            throw new NullException("accountTypeId and amount is mandatory");
        // check if source customer id and account type is exist
        if (!accountRepository.existByAccountType(checkCustomer.getCustomerId(), transaction.getAccountTypeId()))
            throw new CustomerAndAccountNotFoundException(checkCustomer.getCustomerId(),
                    transaction.getAccountTypeId());
        // get the source account details
        Optional<Account> optionalAccount = accountRepository
                .findByCustomerIdAndAccountTypeId(checkCustomer.getCustomerId(), transaction.getAccountTypeId());
        if (optionalAccount.isPresent()) {
            Account sourceAccount = optionalAccount.get();
            // check if the balance greater than the amount
            if (sourceAccount.getBalance() < transaction.getAmount())
                throw new InsufficientBalanceException(sourceAccount.getBalance(), transaction.getAmount());
        } else
            throw new CustomerNotFoundException(checkCustomer.getCustomerId());
        Account sourceAccount = optionalAccount.get();
        Account destinationAccount = accountRepository.findByAccountNumber(accountNo);
        int sourceBalanceBefore = sourceAccount.getBalance();
        int sourceBalanceAfter = sourceBalanceBefore - (transaction.getAmount());
        int destinationBalance = destinationAccount.getBalance() + transaction.getAmount();
        sourceAccount.setBalance(sourceBalanceAfter);
        destinationAccount.setBalance(destinationBalance);
        transaction.setBalanceBefore(sourceBalanceBefore);
        transaction.setBalanceAfter(sourceBalanceAfter);
        transaction.setTransactionTypeId(3);
        transaction.setSourceAccount(sourceAccount.getAccountNumber());
        transaction.setSourceCustomerId(sourceAccount.getCustomerId());
        transaction.setDestinationCustomerId(destinationAccount.getCustomerId());
        transaction.setDestinationAccount(accountNo);
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        return transactionsRepository.save(transaction);
    }

    @Override
    public ArrayList<Transactions> getAllTransactions() {
        Customers checkCustomer = getCurrentCustomer();
        List<Transactions> transactions;
        if (checkCustomer.getCustomerRole() == 1) {
            transactions = transactionsRepository.findAll();
            return (ArrayList<Transactions>) transactions;
        } else {
            transactions = transactionsRepository.findByDestinationCustomerId(checkCustomer.getCustomerId());
            List<Transactions> sourceTransactions = transactionsRepository
                    .findBySourceCustomerId(checkCustomer.getCustomerId());
            if (sourceTransactions != null) {
                for (Transactions sourceTransaction : sourceTransactions) {
                    if (!transactions.contains(sourceTransaction)) {
                        transactions.add(sourceTransaction);
                    }
                }
            }
            return (ArrayList<Transactions>) transactions;
        }
    }

    @Override
    public ArrayList<Transactions> getTransactionsByCustomerId(Integer id) {
        List<Transactions> transactions;
        Customers checkCustomer = getCurrentCustomer();
        if (checkCustomer.getCustomerRole() == 1
                || (checkCustomer.getCustomerRole() == 2 && checkCustomer.getCustomerId().equals(id))) {
            transactions = transactionsRepository.findByDestinationCustomerId(id);
            List<Transactions> sourceTransactions = transactionsRepository
                    .findBySourceCustomerId(id);
            if (sourceTransactions != null) {
                for (Transactions sourceTransaction : sourceTransactions) {
                    if (!transactions.contains(sourceTransaction)) {
                        transactions.add(sourceTransaction);
                    }
                }
            }
            return (ArrayList<Transactions>) transactions;
        } else
            throw new ForbiddenAccessException();
    }

    @Override
    @SuppressWarnings(value = { "null" })
    public Transactions getTransactionsById(Integer id) {
        Customers checkCustomer = getCurrentCustomer();
        Transactions transaction = transactionsRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        if (checkCustomer.getCustomerRole() == 1) {
            return transaction;
        } else if (transaction.getDestinationCustomerId() == checkCustomer.getCustomerId()
                || transaction.getSourceCustomerId() == checkCustomer.getCustomerId()) {
            return transaction;
        } else {
            throw new ForbiddenAccessException();
        }
    }

}
