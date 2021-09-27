package ru.lawyerworkflow.courtschedule.customer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lawyerworkflow.courtschedule.exception.IllegalRequestDataException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.lawyerworkflow.courtschedule.customer.CustomerUtil.createCustomerDTO;

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomerService {

    Sort sortByFirstName = Sort.by("firstName").ascending();
    CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<CustomerDTO> getAllCustomersByPage(int page) {
        PageRequest pageRequest = PageRequest.of(
                page,
                3,
                sortByFirstName);

        Page<Customer> pageResponse = Optional.of(customerRepository
                .findAll(pageRequest))
                .orElseThrow(() -> new IllegalRequestDataException("No customers found"));

        return new PageImpl<>(pageResponse.stream()
                .map(customer -> createCustomerDTO((Optional.of(customer))))
                .toList());
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = Optional.of(customerRepository.findAll(sortByFirstName))
                .orElseThrow(() -> new IllegalRequestDataException("No customers found"));
        return customerList.stream()
                .map(customer -> createCustomerDTO((Optional.of(customer))))
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Long id) {
        return createCustomerDTO(
                Optional.of(
                        customerRepository
                                .findById(id)
                                .orElseThrow(() -> new IllegalRequestDataException("Customer with id: " + id + " not found"))
                )
        );
    }

    @Transactional
    @Modifying
    public Customer register(Customer customer) {
        String email = customer.getEmail();
        boolean isEmailIsUsed = customerRepository.findByEmailIgnoreCase(email).isPresent();
        if (isEmailIsUsed) {
            throw new IllegalRequestDataException("Customer with email: " + email + " already exists");
        }
        return customerRepository.save(customer);
    }

    @Transactional
    @Modifying
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void findByEmailIgnoreCase(String email) {
        customerRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalRequestDataException("Customer with email: " + email + " already exists"));
    }

    @Transactional
    @Modifying
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
}

