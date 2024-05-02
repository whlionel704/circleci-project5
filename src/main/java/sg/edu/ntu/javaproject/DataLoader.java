package sg.edu.ntu.javaproject;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import sg.edu.ntu.javaproject.entity.Account;
import sg.edu.ntu.javaproject.entity.AccountType;
import sg.edu.ntu.javaproject.entity.Customers;
import sg.edu.ntu.javaproject.entity.TransactionsType;
import sg.edu.ntu.javaproject.repository.AccountRepository;
import sg.edu.ntu.javaproject.repository.AccountTypeRepository;
import sg.edu.ntu.javaproject.repository.CustomerRepository;
import sg.edu.ntu.javaproject.repository.TransactionsTypeRepository;
import sg.edu.ntu.javaproject.service.BCryptPasswordEncoderService;

@Component
public class DataLoader {
    private AccountTypeRepository accountTypeRepository;
    private TransactionsTypeRepository transactionsTypeRepository;
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private BCryptPasswordEncoderService passwordEncoderService;

    public DataLoader(AccountTypeRepository accountTypeRepository,
            TransactionsTypeRepository transactionsTypeRepository, AccountRepository accountRepository,
            CustomerRepository customerRepository, BCryptPasswordEncoderService passwordEncoderService) {
        this.accountTypeRepository = accountTypeRepository;
        this.transactionsTypeRepository = transactionsTypeRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoderService = passwordEncoderService;

    }

    @PostConstruct
    public void loadData() {
        accountTypeRepository.deleteAll();
        accountTypeRepository.save(new AccountType("savings"));
        accountTypeRepository.save(new AccountType("current"));
        transactionsTypeRepository.deleteAll();
        transactionsTypeRepository.save(new TransactionsType("deposit"));
        transactionsTypeRepository.save(new TransactionsType("withdraw"));
        transactionsTypeRepository.save(new TransactionsType("transfer"));
        accountRepository.deleteAll();
        accountRepository.save(new Account(121, 100, 1, 2));
        accountRepository.save(new Account(221, 100, 2, 2));
        accountRepository.save(new Account(122, 1000, 1, 3));
        accountRepository.save(new Account(222, 1001, 2, 3));
        customerRepository.deleteAll();
        Customers customer1 = new Customers("admin", "admin@email.com", "contact", "address", "admin1", 1);
        Customers customer2 = new Customers("customer2", "customer2@email.com", "contact", "address", "password2", 2);
        Customers customer3 = new Customers("customer3", "customer3@email.com", "contact", "address", "password3", 2);
        Customers customer4 = new Customers("customer4", "customer4@email.com", "contact", "address", "password4", 2);

        customer1.setPassword(passwordEncoderService.encodePassword(customer1.getPassword()));
        customer2.setPassword(passwordEncoderService.encodePassword(customer2.getPassword()));
        customer3.setPassword(passwordEncoderService.encodePassword(customer3.getPassword()));
        customer4.setPassword(passwordEncoderService.encodePassword(customer4.getPassword()));

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);

    }
}
