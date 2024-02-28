package controller.employee;

import com.itextpdf.text.DocumentException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.User;
import model.pdf.GeneratePdfSoldBooks;
import service.book.BookService;
import view.employee.CreateBookView;
import view.employee.EmployeeView;
import view.employee.SellBookView;
import view.employee.UpdateBookView;
import view.login.LoginView;

import java.io.IOException;
import java.util.List;

public class EmployeeController {

    private final EmployeeView employeeView;
    private final SellBookView sellBookView;
    private final CreateBookView createBookView;
    private final UpdateBookView updateBookView;
    private final Stage stage;
    private final LoginView loginView;
    private final BookService bookService;
    private final ComponentFactory componentFactory;
    private final User user;

    public EmployeeController(EmployeeView employeeView, ComponentFactory componentFactory, Stage stage, LoginView loginView, CreateBookView createBookView, UpdateBookView updateBookView, SellBookView sellBookView, User user) {
        this.employeeView = employeeView;
        this.stage = stage;
        this.loginView = loginView;
        this.bookService = componentFactory.getBookService();
        this.createBookView = createBookView;
        this.updateBookView = updateBookView;
        this.componentFactory = componentFactory;
        this.sellBookView = sellBookView;
        this.user = user;

        this.employeeView.addCreateButtonListener(new CreateButtonListener());
        this.employeeView.addReadButtonListener(new ReadButtonListener());
        this.employeeView.addUpdateButtonListener(new UpdateButtonListener());
        this.employeeView.addDeleteButtonListener(new DeleteButtonListener());
        this.employeeView.addSellButtonListener(new SellButtonListener());
        this.employeeView.addLogoutButtonListener(new LogoutButtonListener());
        this.employeeView.addPdfButtonListener(new PdfButtonListener());
    }

    private class CreateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            createBookView.show();
            new CreateBookController(createBookView, componentFactory);
        }
    }

    private class ReadButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            employeeView.setTable();
        }
    }

    private class UpdateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            updateBookView.show();
            new UpdateBookController(updateBookView, componentFactory, employeeView);
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String title = employeeView.getTitleInput();
            if (title.isEmpty()) {
                showErrorAlert("Title is required", "Please enter the title of the book to delete.");
            } else {
                if (bookService.deleteBookByTitle(title)) {
                    employeeView.setTable();
                    showInformationAlert("Book Deleted", "The book has been deleted successfully!");
                    employeeView.getTitleTextField().clear();
                } else {
                    showErrorAlert("Delete Error", "An error occurred while deleting the book.");
                }
            }
        }
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            sellBookView.show();
            new SellBookController(sellBookView, componentFactory, employeeView, user);

        }
    }

    private class PdfButtonListener implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            GeneratePdfSoldBooks generatePdfSoldBooks = new GeneratePdfSoldBooks();
            List<Book> books = bookService.findSoldBooks();
            try {
                generatePdfSoldBooks.generatePdf("reportSoldBooks.pdf", books);
                System.out.println("PDF GENERATED SUCCESSFULLY!");
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class LogoutButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            loginView.setPasswordField("");
            loginView.setUserTextField("");
            loginView.setActionTargetText("");
            stage.setScene(loginView.getScene());
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
