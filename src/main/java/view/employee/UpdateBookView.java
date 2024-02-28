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

public class UpdateBookView {

    private final Stage stage;
    private final TextField titleTextField;
    private final TextField stockTextField;
    private final Button updateButton;

    public UpdateBookView() {
        stage = new Stage();
        stage.setTitle("Update Book");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        titleTextField = new TextField();
        titleTextField.setPromptText("Title");
        gridPane.add(titleTextField, 0, 0);

        stockTextField = new TextField();
        stockTextField.setPromptText("Stock");
        gridPane.add(stockTextField, 0, 1);

        updateButton = new Button("Update");
        gridPane.add(updateButton, 0, 2);
        GridPane.setHalignment(updateButton, Pos.CENTER.getHpos());

        Scene scene = new Scene(gridPane, 300, 150);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    public String getTitle() {
        return titleTextField.getText();
    }

    public String getStock() {
        return stockTextField.getText();
    }


    public TextField getTitleTextField() {
        return titleTextField;
    }

    public TextField getStockTextField() {
        return stockTextField;
    }

    public void addUpdateButtonListener(EventHandler<ActionEvent> updateButtonListener) {
        updateButton.setOnAction(updateButtonListener);
    }
}
