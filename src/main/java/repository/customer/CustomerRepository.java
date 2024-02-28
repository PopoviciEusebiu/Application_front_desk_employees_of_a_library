package repository.customer;

import model.User;

import java.util.List;

public interface CustomerRepository {

    boolean buyBookByTitle(String title, User user);
    boolean sellBookByTitle(String title, User user);
    List<User> findAllCustomers();
}
