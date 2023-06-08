package util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class Client extends Thread{

        private ArrayList<Client> clients;
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public Client(Socket socket, ArrayList<Client> clients) {
            try {
                this.socket = socket;
                this.clients = clients;
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.equals("end")) {
                        break;
                    }
                    for (Client cl : clients) {
                        cl.writer.println(message);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            finally {
                try {
                    reader.close();
                    writer.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
}
