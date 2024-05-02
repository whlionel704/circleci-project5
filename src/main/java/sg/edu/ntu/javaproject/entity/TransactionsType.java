package sg.edu.ntu.javaproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction_type")
public class TransactionsType {
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "transaction_type_name")
    private String transactionTypeName;

    public TransactionsType(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }
}
