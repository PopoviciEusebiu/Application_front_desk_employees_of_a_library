package controller.employee;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import launcher.ComponentFactory;
import model.User;
import repository.user.UserRepository;
import service.book.BookService;
import view.employee.EmployeeView;
import view.employee.SellBookView;
import view.employee.UpdateBookView;

public class SellBookController {

    private final SellBookView sellBookView;
    private final BookService bookService;
    private final EmployeeView employeeView;
    private final UserRepository userRepository;
    private final User user;

    public SellBookController(SellBookView sellBookView, ComponentFactory componentFactory, EmployeeView employeeView,User user) {
        this.sellBookView = sellBookView;
        this.bookService = componentFactory.getBookService();
        this.employeeView = employeeView;
        this.userRepository = componentFactory.getUserRepository();
        this.user = user;

        this.sellBookView.addSellButtonListener(new SellButtonListener());
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String title = sellBookView.getTitleInput();
            String customerUsername = sellBookView.getCustomerUsernameInput();

            User customer = userRepository.findUserByUsername(customerUsername);

            if (customer != null) {
                boolean success = bookService.sellBook(title, customer, user);

                if (success) {
                    showInformationAlert("Sale Successful", "Book sold successfully.");
                    employeeView.setTable();
                } else {
                    showErrorAlert("Sale Error", "Book sale failed. Check the book availability.");
                }
            } else {
                showErrorAlert("Customer Not Found", "Customer with username " + customerUsername + " not found.");
            }


        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
