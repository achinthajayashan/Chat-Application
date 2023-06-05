import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            ServerSocket serverSocket=new ServerSocket(3000);
            System.out.println("server started. waiting for clients");

            while (true) {
                Socket socket=serverSocket.accept();
                System.out.println("client connected successfully");
//                Client clientThread = new Client(accept, clients);
//                clients.add(clientThread);
//                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
