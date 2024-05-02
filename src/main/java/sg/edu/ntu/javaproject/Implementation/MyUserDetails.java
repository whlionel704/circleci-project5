package sg.edu.ntu.javaproject.Implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import sg.edu.ntu.javaproject.entity.Customers;

public class MyUserDetails implements UserDetails {

    private final Customers customer;

    public MyUserDetails(Customers customer) {
        this.customer = customer;
    }

    @Override
    public String getUsername() {
        return customer.getCustomerEmail();
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (customer.getCustomerRole() == 1) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else
            authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assuming accounts don't expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assuming accounts don't get locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assuming credentials don't expire
    }

    @Override
    public boolean isEnabled() {
        // Implement logic to check if customer account is enabled
        return true; // Assuming all accounts are enabled by default
    }
}
