package sg.edu.ntu.javaproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
@Entity
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email", unique = true)
    @NotBlank(message = "customer email is mandatory")
    private String customerEmail;

    @Column(name = "customer_contact")
    private String customerContact;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "customer_role")
    private Integer customerRole;

    @Column(name = "password")
    @NotBlank(message = "password is mandatory")
    private String password;

    public Customers(String name, String email, String contact, String address, String password, Integer customerRole) {
        this.customerName = name;
        this.customerEmail = email;
        this.customerContact = contact;
        this.customerAddress = address;
        this.password = password;
        this.customerRole = customerRole;
    }

}
