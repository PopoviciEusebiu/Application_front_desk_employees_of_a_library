package controller.employee;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import launcher.ComponentFactory;
import model.Book;
import model.builder.BookBuilder;
import service.book.BookService;
import view.employee.CreateBookView;

import java.time.LocalDate;

public class CreateBookController {

    private final CreateBookView createView;
    private final BookService bookService;

    public CreateBookController(CreateBookView createView, ComponentFactory componentFactory) {
        this.createView = createView;
        this.bookService = componentFactory.getBookService();

        this.createView.addCreateButtonListener(new CreateBookController.CreateButtonListener());
    }

    private class CreateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String title = createView.getTitleTextField().getText();
            String author = createView.getAuthorTextField().getText();
            String publishedDate = createView.getPublishedDateTextField().getText();
            String stockStr = createView.getStockTextField().getText();

            Book checkBook = bookService.findByTitle(title);
            if(checkBook == null){
            try {
                int stock = Integer.parseInt(stockStr);
                Book book = new BookBuilder()
                        .setTitle(title)
                        .setAuthor(author)
                        .setPublishedDate(LocalDate.parse(publishedDate))
                        .setStock(stock)
                        .build();


                if (bookService.save(book)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Book Created Successfully");
                    alert.setHeaderText(null);
                    alert.setContentText("The book has been created successfully!");
                    alert.showAndWait();

                    createView.getTitleTextField().clear();
                    createView.getAuthorTextField().clear();
                    createView.getPublishedDateTextField().clear();
                    createView.getStockTextField().clear();
                } else {
                    showErrorAlert("Error Creating Book", "An error occurred while creating the book.");
                }
                    } catch (NumberFormatException e) {
                showErrorAlert("Invalid Stock", "Please enter a valid integer for the stock.");
                    }
            }else {
                showErrorAlert("Book Exists", "Book already exists");
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
}
