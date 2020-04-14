
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

public class ClientThread extends Thread {

  public Server server;
  public ObjectOutputStream out;
  public ObjectInputStream in;
  public Socket connection;
  int id;

  ClientThread(Socket socket, int id, Server s) {
    this.connection = socket;
    this.id = id;
    this.server = s;
  }

  public void run() {
    try {
      // setup
      in = new ObjectInputStream(connection.getInputStream());
      out = new ObjectOutputStream(connection.getOutputStream());
      connection.setTcpNoDelay(true);
    } catch (Exception e) {
      System.out.println("Streams not open");
    }
    // notify all clients about new connection
    this.server.SendAllClients("new client on server: client #" + id);
    // send ID to client
    try {
      out.writeObject(id);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // start listening for data from client side
    this.ListenData();
  }// end of run

  private void ListenData() {
    while (true) {
      try {
        Serializable d = (Serializable) in.readObject();

        if(d instanceof MorraInfo) {
          // Info from Client
          MorraInfo data = (MorraInfo) d;
          // Log on server
          server.callback.accept("Accepted some data from Client!");
          // handle received data
          this.ReceivedMorraData(data);
        } 

        if(d instanceof String) {
          this.server.callback.accept((String)d);
        }

        if(d instanceof Integer) {
          this.server.callback.accept((Integer)d);
        }
      } catch (Exception e) {
        // if we lost Client Side connection
        server.callback.accept("OOOOPPs...Something wrong with the socket from client: " + id + "....closing down!");
        server.SendAllClients("Client #" + id + " has left the server!");
        server.clients.remove(this);
        break;
      }
    }
  }

  private void ReceivedMorraData(MorraInfo d) {
    this.server.callback.accept(d);
    // if they played
    server.rooms.get(d.roomID).updateRoomClients(d);
    
    if (d.p1Plays != 0 && d.p2Plays != 0) {
      // if they both selected some choice after game was played:
      if(d.player1PlaysAgain == null && d.player2PlaysAgain == null) {
        int totalSum = d.p1Plays + d.p2Plays;
        
        if(d.p1Guess != totalSum || d.p2Guess != totalSum) {
          if(d.p1Guess == totalSum) {
            d.p1Points += 1;
          } else if(d.p2Guess == totalSum) {
            d.p2Points += 1;
          }
        }

        // Update Morra 
        server.rooms.get(d.roomID).updateRoomClients(d);

      } else if (d.player1PlaysAgain != null && d.player2PlaysAgain != null) {
        int playerToDissconnectID = -1;
        int roomID = d.roomID;
        //  if one of them is false
        if (d.player1PlaysAgain.booleanValue() == false && d.player2PlaysAgain.booleanValue() == true) {
          // player 1 exited
          playerToDissconnectID = d.p1ID.intValue();
          this.HandlePlayerDissconected(playerToDissconnectID, roomID);
        } else if (d.player2PlaysAgain.booleanValue() == false && d.player1PlaysAgain.booleanValue() == true){
          // player 2 exited
          playerToDissconnectID = d.p2ID.intValue();
          this.HandlePlayerDissconected(playerToDissconnectID, roomID);
        } else if(d.player1PlaysAgain.booleanValue() == false && d.player2PlaysAgain.booleanValue() == false) {
          playerToDissconnectID = d.p1ID.intValue();
          this.HandlePlayerDissconected(playerToDissconnectID, roomID);
          playerToDissconnectID = d.p2ID.intValue();
          this.HandlePlayerDissconected(playerToDissconnectID, roomID);
        }
      } 
    }
  }


  private void HandlePlayerDissconected(int id, int roomID) { 
    // remove player from room to clients 
    Room found_room = this.server.rooms.get(roomID);

    // get needed client connection to dissconect
    if(found_room.c1.id == id) {
      this.server.clients.add(found_room.c2);
    } else {
      this.server.clients.add(found_room.c1);
    }

    this.server.rooms.remove(found_room);
  }

  public <T>void sendToClient(T data) {
    try {
      out.writeObject(data);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void sendID(Integer id) {
    try {
      out.writeObject(id);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}// end of client thread