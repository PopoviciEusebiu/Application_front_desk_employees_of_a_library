package launcher;

import controller.login.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(String[] args){
        launch(args);
    }

    // Iterative Programming
    @Override
    public void start(Stage primaryStage) throws Exception {
        ComponentFactory componentFactory = ComponentFactory.getInstance(false, primaryStage);
        new LoginController(componentFactory.getLoginView(), primaryStage, componentFactory);
    }
}
