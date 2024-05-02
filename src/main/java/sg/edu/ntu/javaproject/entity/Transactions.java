package sg.edu.ntu.javaproject.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "source_account")
    private Integer sourceAccount;

    @Column(name = "source_customer_id")
    private Integer sourceCustomerId;

    @Column(name = "destination_account")
    private Integer destinationAccount;

    @Column(name = "destination_customer_id")
    private Integer destinationCustomerId;

    @Column(name = "transaction_type")
    private Integer transactionTypeId;

    @Column(name = "account_type")
    @NotNull(message = "Account Type is mandatory")
    private Integer accountTypeId;

    @Column(name = "amount")
    @NotNull(message = "Amount is mandatory")
    private Integer amount;

    @Column(name = "balance_before")
    private Integer balanceBefore;

    @Column(name = "balance_after")
    private Integer balanceAfter;

    @Column(name = "transaction_date")
    @CreationTimestamp
    private Date transactionDate;
}
