package ru.lawyerworkflow.courtschedule.util;

import lombok.experimental.UtilityClass;
import ru.lawyerworkflow.courtschedule.customer.Customer;
import ru.lawyerworkflow.courtschedule.exception.IllegalRequestDataException;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(Customer customer) {
        if (!customer.isNew()) {
            throw new IllegalRequestDataException(customer.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static <T> T checkNotFoundWithId(T object, Long id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, Long id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new IllegalRequestDataException("Not found entity with " + msg);
        }
    }

    public static void assureIdConsistent(Customer customer, Long id) {
        if (customer.isNew()) {
            customer.setId(id);
        } else if (customer.id() != id) {
            throw new IllegalRequestDataException(customer.getClass().getSimpleName() + " must has id=" + id);
        }
    }
}
