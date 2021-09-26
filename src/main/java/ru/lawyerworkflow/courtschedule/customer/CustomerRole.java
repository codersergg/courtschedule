package ru.lawyerworkflow.courtschedule.customer;

import org.springframework.security.core.GrantedAuthority;

public enum CustomerRole implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
