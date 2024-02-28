package service.customer;

import model.User;

import java.util.List;

public interface CustomerService {
    boolean buyBookByTitle(String title, User user);
    List<User> findAllCustomers();

    boolean sellBookByTitle(String title, User user);
}
