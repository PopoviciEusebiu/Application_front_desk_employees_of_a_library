package view.admin;

import controller.admin.AdminController;
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
import model.User;
import repository.user.UserRepository;
import service.admin.AdminService;
import service.customer.CustomerService;
import view.employee.UpdateEmployeeView;
import view.login.LoginView;

import java.util.List;

public class AdminView {

    private final Scene scene;
    private TableView<User> tableEmployees;
    private final GridPane gridPane;
    private final TextField titleTextField;
    private final Button logoutButton;
    private final Button createButton;
    private final Button readButton;
    private final Button updateButton;
    private final Button deleteButton;
    private final Button pdfButton;
    private final AdminService adminService;
    private final UserRepository userRepository;
    private final CustomerService customerService;
    private final ComponentFactory componentFactory;


    public AdminView(Stage primaryStage, LoginView loginView, ComponentFactory componentFactory) {
        primaryStage.setTitle("Admin Interface");
        this.adminService = componentFactory.getAdminService();
        this.userRepository = componentFactory.getUserRepository();
        this.customerService = componentFactory.getCustomerService();
        this.componentFactory = componentFactory;

        gridPane = new GridPane();
        initializeGridPane(gridPane);
        scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);
        primaryStage.show();

        titleTextField = new TextField();
        titleTextField.setPromptText("Enter username for delete");

        gridPane.add(titleTextField, 1, 1);

        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER);

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

        pdfButton = new Button("Generate PDF");
        pdfButton.setMinWidth(80);
        buttonsHBox.getChildren().add(pdfButton);

        gridPane.add(buttonsHBox, 0, 2, 2, 1);

        logoutButton = new Button("Logout");
        logoutButton.setMinWidth(80);
        HBox.setMargin(logoutButton, new Insets(0, 10, 10, 0));
        gridPane.add(logoutButton, 3, 3, 1, 1);
        setTable();

        CreateEmployeeView createEmployeeView = new CreateEmployeeView();
        UpdateEmployeeView updateEmployeeView = new UpdateEmployeeView();
        new AdminController(this, componentFactory, primaryStage, loginView, createEmployeeView, updateEmployeeView);
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

    public ObservableList<User> getEmployee() {
        ObservableList<User> users = FXCollections.observableArrayList();
        List<User> listOfUsers = adminService.findAllEmployees();

        for (User u : listOfUsers) {
            users.add(u);
        }

        return users;
    }

    public void setTable() {
        TableColumn<User, Long> idColumn = new TableColumn<>("Id");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(250);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        tableEmployees = new TableView<>();
        tableEmployees.setItems(getEmployee());
        tableEmployees.getColumns().addAll(idColumn, usernameColumn);

        gridPane.add(tableEmployees, 0, 0, 2, 1);
    }


    public String getTitleInput() {
        return titleTextField.getText();
    }

    public TextField getTitleTextField() {
        return titleTextField;
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
