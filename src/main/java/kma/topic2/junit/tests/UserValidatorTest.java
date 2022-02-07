package kma.topic2.junit.tests;

import kma.topic2.junit.exceptions.ConstraintViolationException;
import kma.topic2.junit.exceptions.LoginExistsException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.repository.UserRepository;
import kma.topic2.junit.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.Executor;

public class UserValidatorTest {

    UserValidator userValidator = new UserValidator(new UserRepository());

    @Test
    public void validatePasswordShort() {
        NewUser newUser = NewUser.builder().login("login").password("12").fullName("fullName").build();
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class,
                () -> userValidator.validateNewUser(newUser), "Password should not be less than 3 symbols");

        Assertions.assertTrue(exception.getMessage().contains("errors"));
    }

    @Test
    public void validatePasswordLong() {
        NewUser newUser = NewUser.builder().login("login").password("12345678").fullName("fullName").build();
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class,
                () -> userValidator.validateNewUser(newUser), "Password should not be more than 7 symbols");

        Assertions.assertTrue(exception.getMessage().contains("errors"));
    }

    @Test
    public void validatePasswordRegexIncorrect() {
        NewUser newUser = NewUser.builder().login("login").password("@s$π™∑").fullName("fullName").build();
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class,
                () -> userValidator.validateNewUser(newUser), "Password should be invalid due to regex");

        Assertions.assertTrue(exception.getMessage().contains("errors"));
    }

    @Test
    public void validatePasswordCorrect() {
        NewUser newUser = NewUser.builder().login("login").password("A1b23C").fullName("fullName").build();
        Assertions.assertDoesNotThrow(() -> userValidator.validateNewUser(newUser),
                "Password should be correct");
    }

}
