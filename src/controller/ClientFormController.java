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
    public VBox clientVBox;
    public TextField txtMessage;
    public Label chatName;
    public TextArea txtMsgArea;
    public VBox vBoxChat;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    private FileChooser fileChooser;
    private File filePath;

    public void sendMessageOnAction(MouseEvent mouseEvent) {
        String msg = txtMessage.getText();
        writer.println(chatName.getText() + ": " + msg);
        txtMessage.clear();

        if(msg.equalsIgnoreCase("!Bye") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);

        }
    }

    public void initialize() throws IOException {
        String userName=LoginFormController.userName;
        this.chatName.setText(userName);
        try {
            socket = new Socket("localhost", 9000);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {


                String message = reader.readLine();
                String[] msgDiv = message.split(" ");
                String senderName = msgDiv[0];


                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < msgDiv.length; i++) {
                    fullMsg.append(msgDiv[i]+" ");
                }


                String[] msgToAr = message.split(" ");
                String st = "";
                for (int i = 0; i < msgToAr.length - 1; i++) {
                    st += msgToAr[i + 1] + " ";
                }


                Text text = new Text(st);
                String firstChars = "";
                if (st.length() > 3) {
                    firstChars = st.substring(0, 3);

                }


                if (firstChars.equalsIgnoreCase("pic")) {
                    //for the Images

                    st = st.substring(3, st.length() - 1);


                    File file = new File(st);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    imageView.setFitHeight(150);
                    imageView.setFitWidth(200);


                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);


                    if (!senderName.equalsIgnoreCase(chatName.getText())) {

                        vBoxChat.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);


                        Text text1 = new Text("  " + senderName + " :");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);

                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                        Text text1 = new Text(" ");
                        hBox.getChildren().add(text1);


                    }

                    Platform.runLater(() -> vBoxChat.getChildren().addAll(hBox));


                } else {

                    TextFlow tempFlow = new TextFlow();


                    if (!senderName.equalsIgnoreCase(chatName.getText() + ":")) {
                        Text txtName = new Text(senderName + " ");
                        txtName.getStyleClass().add("txtName");
                        tempFlow.getChildren().add(txtName);

                        tempFlow.setStyle( "-fx-background-color: #FCC8D1;" +
                                " -fx-background-radius: 5px"
                        );

                        tempFlow.setPadding(new Insets(3,10,3,10));
                    }

                    tempFlow.getChildren().add(text);
                    tempFlow.setMaxWidth(200);

                    TextFlow flow = new TextFlow(tempFlow);

                    HBox hBox = new HBox(12);




                    if (!senderName.equalsIgnoreCase(chatName.getText() + ":")) {


                        vBoxChat.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(flow);
                        hBox.setPadding(new Insets(2,5,2,10));

                    } else {

                        Text text2 = new Text(fullMsg + " ");
                        TextFlow flow2 = new TextFlow(text2);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                        hBox.setPadding(new Insets(2,5,2,10));

                        flow2.setStyle( "-fx-background-color: #F4EEE0;" +
                                "-fx-background-radius: 5px");
                        flow2.setPadding(new Insets(3,10,3,10));

                    }

                    Platform.runLater(() -> vBoxChat.getChildren().addAll(hBox));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectImgOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        //fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        writer.println(chatName.getText() + " " + "pic" + filePath.getPath());
    }
}