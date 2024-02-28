package view.employee;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UpdateEmployeeView {

    private final Stage stage;
    private final TextField idTextField;
    private final TextField usernameTextField;
    private final Button updateButton;

    public UpdateEmployeeView() {
        stage = new Stage();
        stage.setTitle("Update User");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        idTextField = new TextField();
        idTextField.setPromptText("ID");
        gridPane.add(idTextField, 0, 0);

        usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");
        gridPane.add(usernameTextField, 0, 1);

        updateButton = new Button("Update");
        gridPane.add(updateButton, 0, 2);
        GridPane.setHalignment(updateButton, Pos.CENTER.getHpos());

        Scene scene = new Scene(gridPane, 300, 200);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    public String getId() {
        return idTextField.getText();
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public TextField getIdTextField() {
        return idTextField;
    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }

    public void addUpdateButtonListener(EventHandler<ActionEvent> updateButtonListener) {
        updateButton.setOnAction(updateButtonListener);
    }
}
