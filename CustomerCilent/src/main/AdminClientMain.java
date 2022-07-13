package main;

import adminMainApp.AdminMainAppController;
import common.CustomerBankResourcesConstans;
import customerMainApp.CustomerMainAppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import login.LoginController;

import java.net.URL;

public class AdminClientMain extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Load login page
        FXMLLoader loginLoader = new FXMLLoader();
        URL LoginLoaderFXML = getClass().getResource(CustomerBankResourcesConstans.LOGINCONTOLLER_FXML_RESOURCE_IDENTIFIER);
        loginLoader.setLocation(LoginLoaderFXML);
        GridPane LoginRoot = loginLoader.load();
        login.LoginController LoginController = loginLoader.getController();
        LoginController.setAdmin(true);

        //Load Customer app page
        FXMLLoader AdminLoader = new FXMLLoader();
        URL AdminLoaderFXML = getClass().getResource(CustomerBankResourcesConstans.ADMINCONTROLLER_FXML_RESOURCE_IDENTIFIER);
        AdminLoader.setLocation(AdminLoaderFXML);
        AnchorPane adminRoot = AdminLoader.load();
        AdminMainAppController adminViewController = AdminLoader.getController();
        Scene AdminScene = new Scene(adminRoot, 1200, 800);
        adminViewController.setLoginController(LoginController);

        adminViewController.setPrimaryStage(primaryStage);
        adminViewController.setRootPane(AdminScene);
        LoginController.setCustomerAppMainController(adminViewController);


        primaryStage.setTitle("Alternative Banking System");
        Scene scene = new Scene(LoginRoot, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
