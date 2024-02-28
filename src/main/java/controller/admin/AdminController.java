package controller.admin;

import com.itextpdf.text.DocumentException;
import model.User;
import model.pdf.GeneratePdfEmployeeActivity;
import service.admin.AdminService;
import view.admin.AdminView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import view.admin.CreateEmployeeView;
import view.employee.UpdateEmployeeView;
import view.login.LoginView;

import java.io.IOException;
import java.util.Map;

public class AdminController {

    private final AdminView adminView;
    private final UpdateEmployeeView updateEmployeeView;
    private final CreateEmployeeView createEmployeeView;
    private final Stage stage;
    private final LoginView loginView;
    private final AdminService adminService;
    private final ComponentFactory componentFactory;

    public AdminController(AdminView adminView, ComponentFactory componentFactory, Stage stage, LoginView loginView, CreateEmployeeView createEmployeeView, UpdateEmployeeView updateEmployeeView) {
        this.adminView = adminView;
        this.stage = stage;
        this.loginView = loginView;
        this.adminService = componentFactory.getAdminService();
        this.createEmployeeView = createEmployeeView;
        this.componentFactory = componentFactory;
        this.updateEmployeeView = updateEmployeeView;

        this.adminView.addCreateButtonListener(new CreateButtonListener());
        this.adminView.addReadButtonListener(new ReadButtonListener());
        this.adminView.addUpdateButtonListener(new UpdateButtonListener());
        this.adminView.addDeleteButtonListener(new DeleteButtonListener());
        this.adminView.addLogoutButtonListener(new LogoutButtonListener());
        this.adminView.addPdfButtonListener(new PdfButtonListener());
    }

    private class CreateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            createEmployeeView.show();
            new CreateEmployeeController(createEmployeeView, componentFactory);

        }
    }

    private class ReadButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            adminView.setTable();
        }
    }

    private class UpdateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            updateEmployeeView.show();
            new UpdateEmployeeController(updateEmployeeView, componentFactory, adminView);
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String username = adminView.getTitleInput();
            if (username.isEmpty()) {
                showErrorAlert("Username is required", "Please enter the username of the user to delete.");
            } else {
                if (adminService.deleteEmployeeByUsername(username)) {
                    adminView.setTable();
                    showInformationAlert("Employee Deleted", "The employee has been deleted successfully!");
                    adminView.getTitleTextField().clear();
                } else {
                    showErrorAlert("Delete Error", "An error occurred while deleting the employee.");
                }
            }
        }
    }

    private class PdfButtonListener implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            GeneratePdfEmployeeActivity generatePdfEmployeeActivity = new GeneratePdfEmployeeActivity();
            Map<User,String> activity = adminService.findEmployeeActivity();
            try {
                generatePdfEmployeeActivity.generatePdf("reportEmployeeActivty.pdf", activity);
                System.out.println("PDF GENERATED SUCCESSFULLY!");
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private class LogoutButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            loginView.setPasswordField("");
            loginView.setUserTextField("");
            loginView.setActionTargetText("");
            stage.setScene(loginView.getScene());
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
