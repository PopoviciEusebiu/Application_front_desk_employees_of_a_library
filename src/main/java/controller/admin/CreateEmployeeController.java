package controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import launcher.ComponentFactory;
import model.validator.Notification;
import repository.security.RightsRolesRepository;
import service.user.AuthenticationService;
import view.admin.CreateEmployeeView;

import static database.Constants.Roles.EMPLOYEE;

public class CreateEmployeeController {

    private final CreateEmployeeView createEmployeeView;
    private final AuthenticationService authenticationService;

    public CreateEmployeeController(CreateEmployeeView createEmployeeView, ComponentFactory componentFactory) {
        this.createEmployeeView = createEmployeeView;
        this.authenticationService = componentFactory.getAuthenticationService();

        this.createEmployeeView.addCreateButtonListener(new CreateEmployeeController.CreateButtonListener());
    }

    private class CreateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String username = createEmployeeView.getUsernameTextField().getText();
            String password = createEmployeeView.getPasswordTextField().getText();

            Notification<Boolean> registerNotification = authenticationService.registerEmployee(username, password);

            if (registerNotification.hasErrors()) {
                showErrorAlert("Registration Error", registerNotification.getFormattedErrors());
            } else {
                showSuccessAlert("Registration Successful", "Employee registered successfully!");
                createEmployeeView.getUsernameTextField().clear();
                createEmployeeView.getPasswordTextField().clear();
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

    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
