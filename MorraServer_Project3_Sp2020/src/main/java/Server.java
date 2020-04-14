import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Server {

  public ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
  public ArrayList<Room> rooms = new ArrayList<Room>();
  public Consumer<Serializable> callback;
  public ServerThread server;
  public int count = 1;

  public <T> void SendAllClients(T data) {
    //  for each client connection 
    for (int i = 0; i < clients.size(); i++) {
      ClientThread client_socket = clients.get(i);
      try {
        // send message to them
        client_socket.out.writeObject(data);
      } catch (Exception e) {
        // Handle Exception 
        e.printStackTrace();
      }
    }
  }

  Server(Consumer<Serializable> call) {
    callback = call;
    server = new ServerThread(this);
    server.start();
  }

  public void tryPairing() {
    if(clients.size() >= 2) {
      // Create Room
      Room newRoom = new Room(clients.remove(1), clients.remove(0));
      this.rooms.add(newRoom);
      this.callback.accept("Paired!");
      // Send to update users
      MorraInfo info = new MorraInfo();
      info.have2players = true;
      info.p1ID = newRoom.c1.id;
      info.p2ID = newRoom.c2.id;
      info.roomID = this.rooms.indexOf(newRoom);
      newRoom.updateRoomClients(info);
    }
  }

}
