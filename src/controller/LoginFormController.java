package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoginFormController {

    public TextField txtName;

    public static String userName;
    public Button btnCreateAccount;

    public void createAccountOnAction(ActionEvent actionEvent) throws IOException {
        userName = txtName.getText();

        Parent root = FXMLLoader.load(getClass().getResource("/View/clientForm.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Let's Connect");
        //stage.getIcons().add(new Image("Assets/Images/AppIcon.png"));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();


        txtName.clear();
    }

}
