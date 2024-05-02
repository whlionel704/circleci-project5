package sg.edu.ntu.javaproject.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer id;

    @Column(name = "account_no")
    @NotNull(message = "accountNumber is mandatory")
    private Integer accountNumber;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "account_type_id")
    @NotNull(message = "accountTypeId is mandatory")
    private Integer accountTypeId;

    @Column(name = "created_date")
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private Date updatedDate;

    @Column(name = "customer_id")
    @NotNull(message = "customerId is mandatory")
    private Integer customerId;

    // @ManyToOne
    // @JoinColumn(name = "account_type_id", referencedColumnName =
    // "account_type_id")
    // @JsonBackReference
    // private AccountType accountType;

    // @Transient
    // private Integer accountTypeId;
    // @ManyToOne
    // @JoinColumn(name = "account_type_id")
    // private AccountType accountType;

    @Transient
    private String accountTypeName;

    public Account(Integer accountNo, Integer balance, Integer accountType, Integer customerId) {
        this.accountNumber = accountNo;
        this.balance = balance;
        this.accountTypeId = accountType;
        this.customerId = customerId;
    }

}
