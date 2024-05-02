package sg.edu.ntu.javaproject.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
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
import sg.edu.ntu.javaproject.entity.Customers;
import sg.edu.ntu.javaproject.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@Slf4j
public class CustomerController {
    private CustomerService customerService;
    private ObjectMapper objectMapper;

    public CustomerController(CustomerService customerService, ObjectMapper objectMapper) {
        this.customerService = customerService;
        this.objectMapper = objectMapper;
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

    @PostMapping({ "", "/" })
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customers customer)
            throws JsonProcessingException {
        if (isAdmin()) {
            Customers newcustomer = customerService.createCustomers(customer);
            String customerJson = objectMapper.writeValueAsString(newcustomer);
            log.info("New customer created : " + customerJson);
            return new ResponseEntity<>(newcustomer, HttpStatus.CREATED);
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied!");
    }

    @GetMapping({ "", "/" })
    public ResponseEntity<ArrayList<Customers>> getAllCustomers() throws JsonProcessingException {
        ArrayList<Customers> allCustomers = customerService.getAllCustomers();
        String customerJson = objectMapper.writeValueAsString(allCustomers);
        log.info("Retrieved all customers : " + customerJson);
        return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }

    @GetMapping({ "/{id}", "/{id}/" })
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) throws JsonProcessingException {

        Customers customer = customerService.getCustomerById(id);
        String customerJson = objectMapper.writeValueAsString(customer);
        log.info("Retrieved Customer By Id : " + customerJson);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @DeleteMapping({ "/{id}", "/{id}/" })
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCustomerById(@PathVariable Integer id)
            throws JsonProcessingException {
        if (isAdmin()) {
            customerService.deleteCustomerById(id);
            return ResponseEntity.status(HttpStatus.OK).body("customer id: " + id + " has been deleted");
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied!");
    }

    @PutMapping({ "/{id}", "/{id}/", "", "/" })
    public ResponseEntity<Customers> updateCustomer(@PathVariable(required = false) Integer id,
            @RequestBody Customers customer)
            throws JsonProcessingException {
        Customers updatedCustomer = customerService.updateCustomer(id, customer);
        if (id != null) {
            log.info("updating customer with id: " + id);
        }
        String customerJSon = objectMapper.writeValueAsString(updatedCustomer);
        log.info("new account details: " + customerJSon);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);

    }

}