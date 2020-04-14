import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

import javafx.application.Platform;

public class Client extends Thread {

  Socket socketClient;
  String IP;
  String PORT;

  ObjectOutputStream out;
  ObjectInputStream in;

  public Consumer<Serializable> callback;

  Client(Consumer<Serializable> call, String IPAdress, String Port) {
    callback = call;
    IP = IPAdress;
    PORT = Port;
  }

  public void connect() {
    try {
      this.socketClient = new Socket(IP, new Integer(PORT));
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void run() {
    // connect
    try {
      this.connect();
      // setup
      out = new ObjectOutputStream(socketClient.getOutputStream());
      in = new ObjectInputStream(socketClient.getInputStream());
      socketClient.setTcpNoDelay(true);
    } catch (Exception e) {
      // Handle exception
      e.printStackTrace();
    }
    // Listen for incoming data
    while (true) {
      try {
        Serializable data = (Serializable) in.readObject();
        callback.accept(data);
      } catch (Exception e) {
        // Handle exception
        e.printStackTrace();
      }
    }
  }

  // Send data to server
  public <T> void sendData(T data) {
    Platform.runLater(() -> {
      try {
        out.writeObject(data);
        out.reset();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

}
