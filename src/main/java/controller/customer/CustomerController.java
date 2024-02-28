package controller.customer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.User;
import repository.customer.CustomerRepository;
import repository.user.UserRepository;
import service.customer.CustomerService;
import view.customer.CustomerView;
import view.login.LoginView;

public class CustomerController {

    private final CustomerView customerView;
    private final String username;
    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final Stage stage;
    private final LoginView loginView;



    public CustomerController(CustomerView customerView, ComponentFactory componentFactory, Stage stage, LoginView loginView) {
        this.customerView = customerView;
        this.username = loginView.getUsername();
        this.userRepository = componentFactory.getUserRepository();
        this.customerService = componentFactory.getCustomerService();
        this.stage = stage;
        this.loginView = loginView;

        this.customerView.addBuyButtonListener(new BuyButtonListener());
        this.customerView.addSellButtonListener(new SellButtonListener());
        this.customerView.addLogoutButtonListener(new LogoutButtonListener());
    }

    private class BuyButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

            String title = customerView.getTitleInput();
            User user = userRepository.findUserByUsername(username);
            if(customerService.buyBookByTitle(title, user))
            {
                customerView.setTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book Purchased Successfully");
                alert.setHeaderText(null);
                alert.setContentText("The book has been purchased successfully!");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book Cannot Be Purchased");
                alert.setHeaderText(null);
                alert.setContentText("The book is not in stock!");
                alert.showAndWait();
            }
        }
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String title = customerView.getTitleInput();
            User user = userRepository.findUserByUsername(username);
            if(customerService.sellBookByTitle(title, user))
            {
                customerView.setTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book Sold Successfully");
                alert.setHeaderText(null);
                alert.setContentText("The book has been sold successfully!");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book Cannot Be Sold");
                alert.setHeaderText(null);
                alert.setContentText("You can't sell this book!");
                alert.showAndWait();
            }
        }
    }

    private class LogoutButtonListener implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event) {
            loginView.setPasswordField("");
            loginView.setUserTextField("");
            loginView.setActionTargetText("");
            stage.setScene(loginView.getScene());
        }
    }

}
