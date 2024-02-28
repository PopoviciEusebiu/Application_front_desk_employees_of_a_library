package view.employee;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.User;
import service.customer.CustomerService;

import java.util.List;

public class SellBookView {

    private final Stage stage;
    private final Scene scene;
    private final GridPane gridPane;
    private final TextField titleTextField;
    private final TextField customerUsernameTextField;
    private final Button sellButton;
    private TableView<User> tableCustomers;
    private final CustomerService customerService;

    public SellBookView(ComponentFactory componentFactory) {
        this.customerService = componentFactory.getCustomerService();
        stage = new Stage();
        stage.setTitle("Sell Book");

        gridPane = new GridPane();
        initializeGridPane(gridPane);
        scene = new Scene(gridPane, 480, 480);
        stage.setScene(scene);

        setTable();

        titleTextField = new TextField();
        titleTextField.setPromptText("Enter title for sell");
        gridPane.add(titleTextField, 0, 1);

        customerUsernameTextField = new TextField();
        customerUsernameTextField.setPromptText("Enter username for customer");
        gridPane.add(customerUsernameTextField, 1, 1);

        sellButton = new Button("Sell");
        sellButton.setMinWidth(80);
        HBox sellButtonHBox = new HBox(10);
        sellButtonHBox.setAlignment(Pos.CENTER);
        sellButtonHBox.getChildren().add(sellButton);

        VBox mainVBox = new VBox(20);
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.getChildren().addAll(tableCustomers, titleTextField, customerUsernameTextField, sellButtonHBox);

        gridPane.add(mainVBox, 0, 0);
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

    public ObservableList<User> getCustomer() {
        ObservableList<User> customers = FXCollections.observableArrayList();
        List<User> listOfCustomers = customerService.findAllCustomers();

        customers.addAll(listOfCustomers);

        return customers;
    }

    public void setTable() {
        TableColumn<User, String> idColumn = new TableColumn<>("Id");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(150);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        tableCustomers = new TableView<>();
        tableCustomers.setItems(getCustomer());
        tableCustomers.getColumns().addAll(idColumn, usernameColumn);
    }

    public void show() {
        stage.show();
    }

    public String getTitleInput() {
        return titleTextField.getText();
    }

    public String getCustomerUsernameInput() {
        return customerUsernameTextField.getText();
    }

    public void addSellButtonListener(EventHandler<ActionEvent> sellButtonListener) {
        sellButton.setOnAction(sellButtonListener);
    }
}
