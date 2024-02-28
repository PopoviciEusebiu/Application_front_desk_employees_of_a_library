package view.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.Text;

public class CreateEmployeeView {

    private final Stage stage;
    private final TextField usernameTextField;
    private final TextField passwordTextField;
    private final Button createButton;

    public CreateEmployeeView() {
        stage = new Stage();
        stage.setTitle("Create Employee");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");
        gridPane.add(usernameTextField, 0, 0);

        passwordTextField = new TextField();
        passwordTextField.setPromptText("Password");
        gridPane.add(passwordTextField, 0, 1);


        createButton = new Button("Create");
        gridPane.add(createButton, 0, 2);
        GridPane.setHalignment(createButton, Pos.CENTER.getHpos());


        Scene scene = new Scene(gridPane, 300, 200);
        stage.setScene(scene);

    }

    public void show() {
        stage.show();
    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }

    public TextField getPasswordTextField() {
        return passwordTextField;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public void addCreateButtonListener(EventHandler<ActionEvent> createButtonListener) {
        createButton.setOnAction(createButtonListener);
    }
}
