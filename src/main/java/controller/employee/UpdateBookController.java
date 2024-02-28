package controller.employee;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import launcher.ComponentFactory;
import service.book.BookService;
import view.employee.EmployeeView;
import view.employee.UpdateBookView;

public class UpdateBookController {

    private final UpdateBookView updateView;
    private final BookService bookService;
    private final EmployeeView employeeView;

    public UpdateBookController(UpdateBookView updateView, ComponentFactory componentFactory, EmployeeView employeeView) {
        this.updateView = updateView;
        this.bookService = componentFactory.getBookService();
        this.employeeView = employeeView;

        this.updateView.addUpdateButtonListener(new UpdateButtonListener());
    }

    private class UpdateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String title = updateView.getTitle();
            String stringStock = updateView.getStock();

            if (title.isEmpty() || stringStock.isEmpty()) {
                showErrorAlert("Title and Stock are required", "Please enter the title and stock of the book.");
            } else {
                try {
                    int stock = Integer.parseInt(stringStock);

                    if (bookService.updateStockByTitle(title, stock)) {
                        showInformationAlert("Book Updated", "The book has been updated successfully!");
                        employeeView.setTable();
                        updateView.getTitleTextField().clear();
                        updateView.getStockTextField().clear();

                    } else {
                        showErrorAlert("Update Error", "An error occurred while updating the book.");
                    }
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid Stock", "Please enter a valid integer for the stock.");
                }
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
