package ru.lawyerworkflow.courtschedule;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.lawyerworkflow.courtschedule.cases.Cases;
import ru.lawyerworkflow.courtschedule.cases.CasesRepository;
import ru.lawyerworkflow.courtschedule.customer.Customer;
import ru.lawyerworkflow.courtschedule.customer.CustomerRole;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class CourtscheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourtscheduleApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            CasesRepository casesRepository) {

        return args -> {
            Customer admin = new Customer(
                    "firstCustomerFirstName",
                    "firstCustomerLastName",
                    "firstCustomer@google.com",
                    "{noop}123456789",
                    Set.of(CustomerRole.ADMIN, CustomerRole.USER)
            );
            Cases cases1 = new Cases(
                    "A40-12345/2021",
                    admin
            );
            casesRepository.save(cases1);

            Customer admin2 = new Customer(
                    "secondCustomerFirstName",
                    "secondCustomerLastName",
                    "secondCustomer@google.com",
                    "{noop}123456789",
                    Set.of(CustomerRole.ADMIN, CustomerRole.USER)
            );
            Cases cases2 = new Cases(
                    "A40-23456/2021",
                    admin2
            );
            casesRepository.saveAll(List.of(cases1, cases2));

            generatorRandomAppuser(casesRepository);
        };
    }

    private void generatorRandomAppuser(CasesRepository casesRepository) {
        for (int i = 0; i < 10; i++) {
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String password = String.format("{noop}%s", firstName);
            String email = String.format("%s.%s@codersergg.com", firstName, lastName);
            Customer randomUser = new Customer(firstName, lastName, email, password, Set.of(CustomerRole.USER));

            String casesNumber = String.format("A40-%s/2021", (int) (Math.random() * 100000));
            Cases cases = new Cases(
                    casesNumber,
                    randomUser
            );
            casesRepository.save(cases);
        }
    }
}