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

public class CreateBookView {

    private final Stage stage;
    private final TextField titleTextField;
    private final TextField authorTextField;
    private final TextField publishedDateTextField;
    private final TextField stockTextField;
    private final Button createButton;

    public CreateBookView() {
        stage = new Stage();
        stage.setTitle("Create Book");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        titleTextField = new TextField();
        titleTextField.setPromptText("Title");
        gridPane.add(titleTextField, 0, 0);

        authorTextField = new TextField();
        authorTextField.setPromptText("Author");
        gridPane.add(authorTextField, 0, 1);

        publishedDateTextField = new TextField();
        publishedDateTextField.setPromptText("Published Date");
        gridPane.add(publishedDateTextField, 0, 2);

        stockTextField = new TextField();
        stockTextField.setPromptText("Stock");
        gridPane.add(stockTextField, 0, 3);

        createButton = new Button("Create");
        gridPane.add(createButton, 0, 4);
        GridPane.setHalignment(createButton, Pos.CENTER.getHpos());

        Scene scene = new Scene(gridPane, 300, 200);
        stage.setScene(scene);

    }

    public void show() {
        stage.show();
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public TextField getAuthorTextField() {
        return authorTextField;
    }

    public TextField getPublishedDateTextField() {
        return publishedDateTextField;
    }

    public TextField getStockTextField() {
        return stockTextField;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public void addCreateButtonListener(EventHandler<ActionEvent> createButtonListener) {
        createButton.setOnAction(createButtonListener);
    }
}
