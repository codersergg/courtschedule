package ru.lawyerworkflow.courtschedule.security;

import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;
import ru.lawyerworkflow.courtschedule.customer.Customer;

@Getter
@ToString(of = "customer")
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private final Customer customer;

    public AuthUser(@NonNull Customer customer) {
        super(customer.getEmail(), customer.getPassword(), customer.getRoles());
        this.customer = customer;
    }

    public Long id() {
        return customer.getId();
    }
}
