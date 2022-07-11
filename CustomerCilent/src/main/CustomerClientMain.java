package main;

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

public class CustomerClientMain extends Application{
    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
      //  CSSFX.start();

        //Load login page
        FXMLLoader loginLoader = new FXMLLoader();
        URL LoginLoaderFXML = getClass().getResource(CustomerBankResourcesConstans.LOGINCONTOLLER_FXML_RESOURCE_IDENTIFIER);
        loginLoader.setLocation(LoginLoaderFXML);
        GridPane LoginRoot = loginLoader.load();
        LoginController LoginController = loginLoader.getController();

        //Load Customer app page
        FXMLLoader CustomerLoader = new FXMLLoader();
        URL CustomerLoaderFXML = getClass().getResource(CustomerBankResourcesConstans.CUSTOMERCONTROLLER_FXML_RESOURCE_IDENTIFIER);
        CustomerLoader.setLocation(CustomerLoaderFXML);
        AnchorPane customerRoot = CustomerLoader.load();
        CustomerMainAppController customerViewController = CustomerLoader.getController();
        Scene CustomerScene = new Scene(customerRoot, 1200, 800);
        customerViewController.setLoginController(LoginController);

        customerViewController.setPrimaryStage(primaryStage);
        customerViewController.setRootPane(CustomerScene);
        LoginController.setCustomerAppMainController(customerViewController);


        primaryStage.setTitle("Alternative Banking System");
        Scene scene = new Scene(LoginRoot, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();





    }
}
