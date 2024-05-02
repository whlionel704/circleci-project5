package sg.edu.ntu.javaproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.ntu.javaproject.entity.Customers;
import sg.edu.ntu.javaproject.repository.CustomerRepository;

@Service
public class UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoderService passwordEncoderService;

    public Customers saveCustomer(Customers customer) {
        customer.setPassword(passwordEncoderService.encodePassword(customer.getPassword()));
        return customerRepository.save(customer);
    }

    public Customers findByEmail(String username) {
        Customers customer = customerRepository.findByCustomerEmail(username);
        return customer;
    }

    // Other customer service methods
}