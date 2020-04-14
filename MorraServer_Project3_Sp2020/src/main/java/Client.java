import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

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

  public void run() {
    // connect
    try {
      // setup
      socketClient = new Socket(IP, new Integer(PORT));
      out = new ObjectOutputStream(socketClient.getOutputStream());
      in = new ObjectInputStream(socketClient.getInputStream());
      socketClient.setTcpNoDelay(true);
    } catch (Exception e) {
      // Handle exception
      e.printStackTrace();
    }
    this.ListenForData();
  }

  // Send data to server
  public <T> void sendData(T data) {
    try {
      out.writeObject(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Listen for incoming data
  private void ListenForData() {
    while (true) {
      try {
        Serializable data = (Serializable)in.readObject();
        callback.accept(data);
      } catch (Exception e) {
        // Handle exception
        e.printStackTrace();
      }
    }
  }

}
