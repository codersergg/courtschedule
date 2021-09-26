package ru.lawyerworkflow.courtschedule.cases;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.lawyerworkflow.courtschedule.customer.Customer;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity(name = "Cases")
@Table(
        name = "cases",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "cases_number_unique",
                        columnNames = "cases_number")
        }
)
public class Cases implements Serializable {

    @SequenceGenerator(
            name = "cases_id_sequence",
            sequenceName = "cases_id_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cases_id_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "cases_number",
            nullable = false,
            columnDefinition = "TEXT",
            length = 20
    )
    private String casesNumber;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "customer_id_fk"
            )
    )
    private Customer customer;

    public Cases(String casesNumber, Customer customer) {
        this.casesNumber = casesNumber;
        this.customer = customer;
    }
}
