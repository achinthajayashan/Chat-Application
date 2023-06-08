package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientFormController extends Thread{
    public TextField txtMessage;
    public Label chatName;
    public TextArea txtMsgArea;
    public VBox chatVBox;

    BufferedReader reader;
    PrintWriter printWriter;
    Socket socket;

    private FileChooser imgChooser;
    private File imgPath;

    public void sendMessageOnAction(MouseEvent mouseEvent) {
        String messageText = txtMessage.getText();
        printWriter.println(chatName.getText() + ": " + messageText);
        txtMessage.clear();

        if(messageText.equals("!Bye") || (messageText.equals("logout"))) {
            System.exit(0);

        }
    }

    public void initialize() throws IOException {
        String userName=LoginFormController.userName;
        this.chatName.setText(userName);
        try {
            socket = new Socket("localhost", 9000);
            System.out.println("server connected!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            this.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        try {
            while (true) {


                String message = reader.readLine();
                String[] splits = message.split(" ");
                String senderName = splits[0];


                StringBuilder fullMessage = new StringBuilder();
                for (int i = 1; i < splits.length; i++) {
                    fullMessage.append(splits[i]+" ");
                }


                String[] msgArray = message.split(" ");
                String store = "";
                for (int i = 0; i < msgArray.length - 1; i++) {
                    store += msgArray[i + 1] + " ";
                }


                Text text = new Text(store);
                String firstletters = "";
                if (store.length() > 3) {
                    firstletters = store.substring(0, 3);

                }


                if (firstletters.equalsIgnoreCase("pic")) {

                    store = store.substring(3, store.length() - 1);


                    File file = new File(store);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    imageView.setFitHeight(200);
                    imageView.setFitWidth(200);


                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);


                    if (!senderName.equals(chatName.getText())) {

                        chatVBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);


                        Text text1 = new Text("  " + senderName + " :");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);

                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                    }

                    Platform.runLater(() -> chatVBox.getChildren().addAll(hBox));


                } else {

                    TextFlow textFlow = new TextFlow();


                    if (!senderName.equals(chatName.getText() + ":")) {
                        Text txtName = new Text(senderName + " ");
                        txtName.getStyleClass().add("txtName");
                        textFlow.getChildren().add(txtName);

                        textFlow.setStyle(
                                "-fx-background-color: #FCC8D1;" +
                                " -fx-background-radius: 5px"
                        );

                        textFlow.setPadding(new Insets(3,10,3,10));
                    }

                    textFlow.getChildren().add(text);
                    textFlow.setMaxWidth(200);

                    TextFlow flow = new TextFlow(textFlow);

                    HBox hBox = new HBox(12);




                    if (!senderName.equals(chatName.getText() + ":")) {


                        chatVBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(flow);
                        hBox.setPadding(new Insets(2,5,2,10));

                    } else {

                        Text text2 = new Text(""+fullMessage);
                        TextFlow flow2 = new TextFlow(text2);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                        hBox.setPadding(new Insets(2,5,2,10));

                        flow2.setStyle(
                                "-fx-background-color: #F4EEE0;" +
                                "-fx-background-radius: 5px");
                        flow2.setPadding(new Insets(3,10,3,10));

                    }

                    Platform.runLater(() -> chatVBox.getChildren().addAll(hBox));
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectImgOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        imgChooser = new FileChooser();
        this.imgPath = imgChooser.showOpenDialog(stage);
        printWriter.println(chatName.getText() + " " + "pic" + imgPath.getPath());
    }
}
