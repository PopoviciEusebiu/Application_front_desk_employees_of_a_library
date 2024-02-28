package controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import launcher.ComponentFactory;
import model.User;
import model.builder.UserBuilder;
import model.validator.UserValidator;
import service.admin.AdminService;
import view.admin.AdminView;
import view.employee.UpdateEmployeeView;

public class UpdateEmployeeController {

    private final UpdateEmployeeView updateEmployeeView;
    private final AdminService adminService;
    private final AdminView adminView;

    public UpdateEmployeeController(UpdateEmployeeView updateEmployeeView, ComponentFactory componentFactory, AdminView adminView) {
        this.updateEmployeeView = updateEmployeeView;
        this.adminService = componentFactory.getAdminService();
        this.adminView = adminView;

        this.updateEmployeeView.addUpdateButtonListener(new UpdateButtonListener());
    }

    private class UpdateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String stringId = updateEmployeeView.getId();
            String username = updateEmployeeView.getUsername();

            User user = new UserBuilder()
                    .setUsername(username)
                    .setPassword("password")
                    .build();

            UserValidator userValidator = new UserValidator(user);
            boolean userValid = userValidator.validateUsername();

            if (stringId.isEmpty() || username.isEmpty()) {
                showErrorAlert("Id and Username are required", "Please enter the id and username of the employee.");
            } else if (!userValid) {
                showErrorAlert("Invalid User Data", "Please enter valid user data.");
            } else {
                try {
                    int id = Integer.parseInt(stringId);

                    if (adminService.updateEmployee(id, username)) {
                        showInformationAlert("Employee Updated", "The employee has been updated successfully!");
                        adminView.setTable();
                        updateEmployeeView.getIdTextField().clear();
                        updateEmployeeView.getUsernameTextField().clear();
                    } else {
                        showErrorAlert("Update Error", "An error occurred while updating the employee.");
                    }
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid Id", "Please enter a valid integer for the id.");
                }
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

    private void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
