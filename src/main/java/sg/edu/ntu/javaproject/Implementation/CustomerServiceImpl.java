package sg.edu.ntu.javaproject.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.ntu.javaproject.Exception.CustomerNotFoundException;
import sg.edu.ntu.javaproject.Exception.EmailIsExistException;
import sg.edu.ntu.javaproject.Exception.ForbiddenAccessException;
import sg.edu.ntu.javaproject.entity.Customers;
import sg.edu.ntu.javaproject.repository.CustomerRepository;
import sg.edu.ntu.javaproject.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    private BCryptPasswordEncoder passwordEncoder;
    // private ObjectMapper objectMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private Customers getCurrentCustomer() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = authentication.getName();
        return customerRepository.findByCustomerEmail(username);
    }

    public Customers createCustomers(Customers customer) {
        if (customer.getCustomerRole() == null)
            customer.setCustomerRole(2);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        customer.setPassword("*****");
        return customer;
    }

    @Override
    public ArrayList<Customers> getAllCustomers() {
        Customers checkCustomer = getCurrentCustomer();
        if (checkCustomer.getCustomerRole() == 1) {
            List<Customers> allCustomers = customerRepository.findAll();
            for (Customers customer : allCustomers) {
                customer.setPassword("*******");
            }
            return (ArrayList<Customers>) allCustomers;
        } else {
            checkCustomer.setPassword("*****");
            ArrayList<Customers> displayCustomer = new ArrayList<>();
            displayCustomer.add(checkCustomer);
            return displayCustomer;
        }
    }

    @Override
    @SuppressWarnings(value = { "null" })
    public Customers getCustomerById(Integer customer_id) {
        Customers customer = customerRepository.findById(customer_id)
                .orElseThrow(() -> new CustomerNotFoundException(customer_id));
        customer.setPassword("*****");
        Customers checkCustomer = getCurrentCustomer();
        if (checkCustomer.getCustomerRole() == 1)
            return customer;
        else if (checkCustomer.getCustomerId() == customer.getCustomerId())
            return customer;
        else
            throw new ForbiddenAccessException();
    }

    @Override
    @SuppressWarnings(value = { "null" })
    public void deleteCustomerById(Integer customer_id) {
        Customers customer = customerRepository.findById(customer_id)
                .orElseThrow(() -> new CustomerNotFoundException(customer_id));
        customerRepository.deleteById(customer.getCustomerId());
    }

    @Override
    @SuppressWarnings(value = { "null" })
    public Customers updateCustomer(Integer customer_id, Customers customer) {
        Customers checkCustomer = getCurrentCustomer();
        Customers customerToUpdate;
        // check if path variable is not null and role is admin
        // set customer to update based on id in path variable
        if (customer_id != null && checkCustomer.getCustomerRole() == 1) {
            customerToUpdate = customerRepository.findById(customer_id)
                    .orElseThrow(() -> new CustomerNotFoundException(customer_id));
            // check if path variable is null and customer role either user or admin
            // set customer to update to their own customer id
        } else if (customer_id == null
                && (checkCustomer.getCustomerRole() == 2 || checkCustomer.getCustomerRole() == 1)) {
            customerToUpdate = customerRepository.findById(checkCustomer.getCustomerId()).get();
            // else throw exception
        } else {
            throw new ForbiddenAccessException();
        }
        if (customer.getCustomerName() != null) {
            customerToUpdate.setCustomerName(customer.getCustomerName());
        }
        // check if email in request body is diffrent from customer to update email
        // check if email is exist
        if (customer.getCustomerEmail() != null) {
            if (!customerToUpdate.getCustomerEmail().equals(customer.getCustomerEmail())) {
                Customers emailIsExist = customerRepository.findByCustomerEmail(customer.getCustomerEmail());
                if (emailIsExist == null) {
                    customerToUpdate.setCustomerEmail(customer.getCustomerEmail());
                } else
                    throw new EmailIsExistException(customer.getCustomerEmail());
            } else
                customerToUpdate.setCustomerEmail(customer.getCustomerEmail());
        }
        if (customer.getCustomerContact() != null) {
            customerToUpdate.setCustomerContact(customer.getCustomerContact());
        }
        if (customer.getCustomerAddress() != null) {
            customerToUpdate.setCustomerAddress(customer.getCustomerAddress());
        }
        if (customer.getCustomerRole() != null && checkCustomer.getCustomerRole() == 1) {
            customerToUpdate.setCustomerRole(customer.getCustomerRole());
        }
        if (customer.getPassword() != null) {
            customerToUpdate.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        customerRepository.save(customerToUpdate);
        customerToUpdate.setPassword("********");
        return customerToUpdate;

    }

}