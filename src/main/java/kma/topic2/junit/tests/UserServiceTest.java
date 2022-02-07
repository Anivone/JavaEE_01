package kma.topic2.junit.tests;

import kma.topic2.junit.exceptions.ConstraintViolationException;
import kma.topic2.junit.exceptions.UserNotFoundException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.model.User;
import kma.topic2.junit.repository.UserRepository;
import kma.topic2.junit.service.UserService;
import kma.topic2.junit.validation.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserServiceTest {
    UserRepository userRepository = new UserRepository();
    UserService userService = new UserService(userRepository, new UserValidator(userRepository));

    @Test
    public void userServiceCreatesAndReturnsUser() {
        String login = "login";
        NewUser newUser = NewUser.builder()
                .login(login).password("123abc").fullName("fullName")
                .build();
        userService.createNewUser(newUser);
        User user = userService.getUserByLogin(login);

        Assertions.assertEquals(newUser.getLogin(), user.getLogin());
        Assertions.assertEquals(newUser.getPassword(), user.getPassword());
        Assertions.assertEquals(newUser.getFullName(), user.getFullName());
    }

    @Test
    public void userServiceUserNotFound() {
        Exception exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserByLogin("random"),
                "should not find user with given login");

        Assertions.assertTrue(exception.getMessage().contains("Can't find user"));
    }

    @Test
    public void userServiceDoesNotCreateUserWithIncorrectPassword() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class,
                () -> userService.createNewUser(NewUser.builder()
                        .login("test").password("12").fullName("fullName").build()),
                "should throw exception due to incorrect password");

        Assertions.assertTrue(exception.getMessage().contains("errors"));
    }

}
