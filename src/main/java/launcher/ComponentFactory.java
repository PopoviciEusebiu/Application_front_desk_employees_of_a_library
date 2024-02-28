package launcher;

import controller.login.LoginController;
import database.DataBaseConnectionFactory;
import javafx.stage.Stage;
import model.Book;
import model.User;
import model.builder.UserBuilder;
import repository.admin.AdminRepository;
import repository.admin.AdminRepositoryMySQL;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySql;
import repository.customer.CustomerRepository;
import repository.customer.CustomerRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.superAdmin.SuperAdminRepository;
import repository.superAdmin.SuperAdminRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.admin.AdminService;
import service.admin.AdminServiceImpl;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.customer.CustomerService;
import service.customer.CustomerServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.login.LoginView;

import java.sql.Connection;

public class ComponentFactory {
    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    private final AdminRepository adminRepository;
    private final AdminService adminService;
    private final SuperAdminRepository superAdminRepository;
    private static volatile ComponentFactory instance;

    public static ComponentFactory getInstance(Boolean componentsForTests, Stage stage)
    {
        if(instance == null) {
            synchronized (ComponentFactory.class) {
                if (instance == null) {
                    instance = new ComponentFactory(componentsForTests, stage);
                }
            }
        }
        return instance;
    }

    public ComponentFactory(Boolean componentsForTests, Stage stage)
    {
        Connection connection = DataBaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);

        this.userRepository = new UserRepositoryMySQL(connection,rightsRolesRepository);

        this.bookRepository = new BookRepositoryMySql(connection);

        this.bookService = new BookServiceImpl(bookRepository);

        this.customerRepository = new CustomerRepositoryMySQL(connection);

        this.customerService = new CustomerServiceImpl(customerRepository);

        this.adminRepository = new AdminRepositoryMySQL(connection, rightsRolesRepository, bookRepository, userRepository);

        this.adminService = new AdminServiceImpl(adminRepository);

        this.superAdminRepository = new SuperAdminRepositoryMySQL(connection, rightsRolesRepository);

        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository, adminRepository, superAdminRepository);

        this.loginView = new LoginView(stage);
        //insertAdmin();


    }

    public LoginView getLoginView() {
        return loginView;
    }


    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public AdminService getAdminService()
    {
        return adminService;
    }
    public void insertAdmin()
    {
        String username = "admin@gmail.com";
        String password = "admin123!";
        authenticationService.registerAdmin(username, password);
    }
}
