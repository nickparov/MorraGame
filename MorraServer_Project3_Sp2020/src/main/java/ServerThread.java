import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class ServerThread extends Thread {

  private Server server;
  private ServerSocket s_socket;

  ServerThread(Server s) {
    this.server = s;
    try {
      this.s_socket = new ServerSocket(5555);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void run() {
    try {
      System.out.println("Server is waiting for a client!");
      // Listen for new clients

      while (true) {
        Integer new_client_id = new Integer(this.server.count);

        ClientThread c = new ClientThread(s_socket.accept(), new_client_id, server);
        // call callback of server 
        this.server.callback.accept("client has connected to server: " + "client #" + new_client_id);
        // add new client connection
        this.server.clients.add(c);
        // start client connection  
        c.start();
        
        // increase server counter
        this.server.count++;
        // check for clients to pair in a room
        
        try { 
          Thread.sleep(500); 
        } catch (InterruptedException e) {
           e.printStackTrace(); 
        }
        server.tryPairing();
      }

    } catch (Exception e) {
      server.callback.accept("Server socket did not launch");
    }
  }
  
}