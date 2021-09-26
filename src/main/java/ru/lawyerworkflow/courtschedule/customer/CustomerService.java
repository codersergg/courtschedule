package ru.lawyerworkflow.courtschedule.customer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    CustomerRepository customerRepository;

    Sort sortByFirstName = Sort.by("firstName").ascending();

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<Customer> getAllCustomersByPage(int page) {
        PageRequest pageRequest = PageRequest.of(
                page,
                20,
                sortByFirstName);
        return customerRepository.findAll(pageRequest);
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = customerRepository.findAll(sortByFirstName);
        return customerList.stream()
                .map(customer -> createCustomerDTO((Optional.of(customer))))
                .collect(Collectors.toList());
    }

    public Optional<CustomerDTO> getCustomer(Long id) {
        boolean isEmpty = customerRepository.findById(id).isEmpty();
        if (isEmpty) {
            throw new IllegalRequestDataException("Customer with id: " + id + " not found");
        }
        return Optional.of(createCustomerDTO(customerRepository.findById(id)));
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

    public Optional<Customer> findByEmailIgnoreCase(String email) {
        return customerRepository.findByEmailIgnoreCase(email);
    }

    @Transactional
    @Modifying
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
}

