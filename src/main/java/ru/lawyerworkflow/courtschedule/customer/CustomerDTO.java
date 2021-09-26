package ru.lawyerworkflow.courtschedule.customer;

import java.io.Serializable;

public record CustomerDTO(
        Long id,
        String firstName,
        String lastName,
        String email
) implements Serializable {
}
