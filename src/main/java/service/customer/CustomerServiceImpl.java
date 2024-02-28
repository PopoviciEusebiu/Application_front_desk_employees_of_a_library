package service.customer;

import model.User;
import repository.customer.CustomerRepository;

import java.util.List;

public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository)
    {
        this.customerRepository = customerRepository;
    }
    @Override
    public boolean buyBookByTitle(String title, User user) {
        return customerRepository.buyBookByTitle(title, user);
    }

    @Override
    public boolean sellBookByTitle(String title, User user) {
        return customerRepository.sellBookByTitle(title, user);
    }
    @Override
    public List<User> findAllCustomers()
    {
        return customerRepository.findAllCustomers();
    }
}
