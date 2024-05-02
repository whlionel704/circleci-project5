package sg.edu.ntu.javaproject.Implementation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sg.edu.ntu.javaproject.entity.Customers;
import sg.edu.ntu.javaproject.repository.CustomerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customers customer = customersRepository.findByCustomerEmail(username);
        if (customer == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new MyUserDetails(customer);
    }
}
