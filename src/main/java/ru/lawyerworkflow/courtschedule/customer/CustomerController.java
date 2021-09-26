package ru.lawyerworkflow.courtschedule.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.lawyerworkflow.courtschedule.security.AuthUser;
import ru.lawyerworkflow.courtschedule.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.lawyerworkflow.courtschedule.util.ValidationUtil.checkNew;

@RequestMapping(CustomerController.URL)
@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {
    static final String URL = "api/v1/account";
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        log.info("Get All Customers ");
        return customerService.getAllCustomers();
    }

    @GetMapping(path = "/page{pageNumber}")
    public Page<Customer> getAllCustomersByPage(
            @PathVariable("pageNumber") int page) {
        log.info("GetCustomersByPage {} ", page);
        return customerService.getAllCustomersByPage(page);
    }

    @GetMapping(
            path = "{customerId}")
    public Optional<CustomerDTO> getCustomer(
            @PathVariable("customerId") Long id) {
        log.info("Get customerDTO by id {} ", id);
        return customerService.getCustomer(id);
    }

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Customer> register(
            @Valid @RequestBody Customer customer) {
        log.info("Register {} ", customer);
        checkNew(customer);
        customer.setRoles(Set.of(CustomerRole.USER));
        customer = customerService.register(customer);
        URI uriOfNewResource = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(URL)
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(customer);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomer(
            @Valid @RequestBody Customer customer,
            @AuthenticationPrincipal AuthUser authUser) {
        log.info("updateCustomer {} to {}", authUser, customer);
        Customer oldCustomer = authUser.getCustomer();
        ValidationUtil.assureIdConsistent(customer, oldCustomer.id());
        customer.setRoles(oldCustomer.getRoles());
        customer.setEmail(oldCustomer.getEmail());
        if (customer.getPassword() == null) {
            customer.setPassword(oldCustomer.getPassword());
        }
        customerService.updateCustomer(customer);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@AuthenticationPrincipal AuthUser authUser) {
        log.info("Delete customer by id= {}", authUser.id());
        customerService.deleteById(authUser.id());
    }
}