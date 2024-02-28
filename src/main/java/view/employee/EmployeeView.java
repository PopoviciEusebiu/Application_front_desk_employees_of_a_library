package view.employee;

import controller.employee.EmployeeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.User;
import repository.user.UserRepository;
import service.book.BookService;
import service.customer.CustomerService;
import view.login.LoginView;

import java.util.List;

public class EmployeeView {

    private final Scene scene;
    private TableView<Book> tableBooks;
    private final GridPane gridPane;
    private final TextField titleTextField;
    private final Button sellButton;
    private final Button logoutButton;
    private final Button createButton;
    private final Button readButton;
    private final Button updateButton;
    private final Button deleteButton;
    private final Button pdfButton;
    private final BookService bookService;
    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final ComponentFactory componentFactory;
    private final User user;

    public EmployeeView(Stage primaryStage, LoginView loginView, ComponentFactory componentFactory, User user) {
        primaryStage.setTitle("Employee Interface");
        this.bookService = componentFactory.getBookService();
        this.userRepository = componentFactory.getUserRepository();
        this.customerService = componentFactory.getCustomerService();
        this.componentFactory = componentFactory;
        this.user = user;

        gridPane = new GridPane();
        initializeGridPane(gridPane);
        scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);
        primaryStage.show();

        titleTextField = new TextField();
        titleTextField.setPromptText("Enter title for delete");

        gridPane.add(titleTextField, 1, 1);

        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER);

        // Adaugare butoane pentru operatiile CRUD
        createButton = new Button("Create");
        createButton.setMinWidth(80);
        buttonsHBox.getChildren().add(createButton);

        readButton = new Button("Read");
        readButton.setMinWidth(80);
        buttonsHBox.getChildren().add(readButton);

        updateButton = new Button("Update");
        updateButton.setMinWidth(80);
        buttonsHBox.getChildren().add(updateButton);

        deleteButton = new Button("Delete");
        deleteButton.setMinWidth(80);
        buttonsHBox.getChildren().add(deleteButton);

        sellButton = new Button("Sell");
        sellButton.setMinWidth(80);
        buttonsHBox.getChildren().add(sellButton);

        pdfButton = new Button("Generate PDF");
        pdfButton.setMinWidth(80);
        buttonsHBox.getChildren().add(pdfButton);

        gridPane.add(buttonsHBox, 0, 2, 2, 1); // Adauga HBox in gridPane

        logoutButton = new Button("Logout");
        logoutButton.setMinWidth(80);
        HBox.setMargin(logoutButton, new Insets(0, 10, 10, 0));
        gridPane.add(logoutButton, 3, 3, 1, 1);
        setTable();

        CreateBookView createView = new CreateBookView();
        UpdateBookView updateView = new UpdateBookView();
        SellBookView sellBookView = new SellBookView(componentFactory);

        new EmployeeController(this, componentFactory, primaryStage, loginView, createView, updateView, sellBookView, user);
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    public Scene getScene() {
        return scene;
    }

    public ObservableList<Book> getBook() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        List<Book> listOfBooks = bookService.findAll();

        for (Book b : listOfBooks) {
            books.add(b);
        }

        return books;
    }

    public void setTable() {
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(140);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setMinWidth(100);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> publishedDateColumn = new TableColumn<>("PublishedDate");
        publishedDateColumn.setMinWidth(150);
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        TableColumn<Book, String> stockColumn = new TableColumn<>("Stock");
        stockColumn.setMinWidth(100);
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tableBooks = new TableView<>();
        tableBooks.setItems(getBook());
        tableBooks.getColumns().addAll(titleColumn, authorColumn, publishedDateColumn, stockColumn);

        gridPane.add(tableBooks, 0, 0, 2, 1);



    }

    public String getTitleInput() {
        return titleTextField.getText();
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public void addSellButtonListener(EventHandler<ActionEvent> sellButtonListener) {
        sellButton.setOnAction(sellButtonListener);
    }

    public void addLogoutButtonListener(EventHandler<ActionEvent> logoutButtonListener) {
        logoutButton.setOnAction(logoutButtonListener);
    }

    public void addCreateButtonListener(EventHandler<ActionEvent> createButtonListener) {
        createButton.setOnAction(createButtonListener);
    }

    public void addReadButtonListener(EventHandler<ActionEvent> readButtonListener) {
        readButton.setOnAction(readButtonListener);
    }

    public void addUpdateButtonListener(EventHandler<ActionEvent> updateButtonListener) {
        updateButton.setOnAction(updateButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addPdfButtonListener(EventHandler<ActionEvent> pdfButtonListener)
    {
        pdfButton.setOnAction(pdfButtonListener);
    }
}
