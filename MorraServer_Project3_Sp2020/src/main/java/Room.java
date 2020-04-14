import java.io.IOException;

public class Room {
  public ClientThread c1;
  public ClientThread c2;

  public Room(ClientThread c1, ClientThread c2) {
    this.c1 = c1;
    this.c2 = c2;
  }

  public <T> void updateRoomClients(T data) {
    try {
        c1.out.writeObject(data);
        c2.out.writeObject(data);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}