package ru.lawyerworkflow.courtschedule.customer;

import java.util.Optional;

public class CustomerUtil {
    public static CustomerDTO createCustomerDTO(Optional<Customer> customerOptional) {
        Customer customer = customerOptional.get();
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail());
    }
}
