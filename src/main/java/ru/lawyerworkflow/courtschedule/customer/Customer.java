package ru.lawyerworkflow.courtschedule.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;
import org.springframework.util.Assert;
import ru.lawyerworkflow.courtschedule.cases.Cases;
import ru.lawyerworkflow.courtschedule.security.JsonDeserializers;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password")
@Entity(name = "Customer")
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customer_email_unique",
                        columnNames = "email")
        }
)
public class Customer implements Serializable, Persistable<Long> {

    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = JsonDeserializers.PasswordDeserializer.class)
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "customer_roles",
            joinColumns = @JoinColumn(
                    name = "customer_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "customer_roles_customer_id_fk"
                    )
            ),
            uniqueConstraints = {@UniqueConstraint(
                    name = "customer_id_roles_unique",
                    columnNames = {
                            "customer_id",
                            "roles"})
            })
    @Column(name = "roles")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<CustomerRole> roles;

    @OneToOne(
            mappedBy = "customer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Cases cases;

    public Customer(String firstName, String lastName, String email, String password, Set<CustomerRole> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Customer(String firstName, String lastName, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public Long id() {
        Assert.notNull(id, "Entity must have id");
        return id;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) {
            return false;
        }
        Customer that = (Customer) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(id == null ? 0 : id);
    }

}
