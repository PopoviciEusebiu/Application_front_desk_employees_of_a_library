package controller.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.User;
import model.validator.Notification;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import service.book.BookService;
import service.customer.CustomerService;
import service.user.AuthenticationService;
import view.admin.AdminView;
import view.customer.CustomerView;
import view.employee.EmployeeView;
import view.login.LoginView;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final Stage primaryStage;
    private final BookService bookService;
    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final RightsRolesRepository rightsRolesRepository;
    private final ComponentFactory componentFactory;


    public LoginController(LoginView loginView, Stage primaryStage, ComponentFactory componentFactory) {
        this.loginView = loginView;
        this.authenticationService = componentFactory.getAuthenticationService();
        this.primaryStage = primaryStage;
        this.bookService = componentFactory.getBookService();
        this.userRepository = componentFactory.getUserRepository();
        this.customerService = componentFactory.getCustomerService();
        this.componentFactory = componentFactory;
        this.rightsRolesRepository = componentFactory.getRightsRolesRepository();

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }


    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("LogIn Successfull!");
                User u = userRepository.findUserByUsername(username);
                if(rightsRolesRepository.findRoleIdByUserId(u.getId()) == 3)
                {
                    primaryStage.setScene(loginView.getScene());
                    primaryStage.close();
                    openCustomerView(primaryStage);
                } else if (rightsRolesRepository.findRoleIdByUserId(u.getId()) == 2)
                {
                    primaryStage.setScene(loginView.getScene());
                    primaryStage.close();
                    openEmployeeView(primaryStage, u);
                }else{
                    primaryStage.setScene(loginView.getScene());
                    primaryStage.close();
                    openAdminView(primaryStage);
                }
            }

        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
    private void openCustomerView(Stage stage){
        CustomerView customerView = new CustomerView(stage, loginView, componentFactory);
        stage.setScene(customerView.getScene());
    }
    private void openEmployeeView(Stage stage, User user){
        EmployeeView employeeView = new EmployeeView(stage, loginView, componentFactory, user);
        stage.setScene(employeeView.getScene());

    }
    private void openAdminView(Stage stage){
        AdminView adminView = new AdminView(stage, loginView, componentFactory);
        stage.setScene(adminView.getScene());
    }



}