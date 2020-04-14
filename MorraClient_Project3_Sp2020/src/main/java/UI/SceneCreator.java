package UI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class SceneCreator {

  UINodes Nodes;

  public SceneCreator(UINodes nds) {
    Nodes = nds;
  }

  // Client
  public Scene CreateClientStartScene() {
    Nodes.ClientStartSceneContainer = new VBox(10, Nodes.IPBox, Nodes.ConnectBtn);
    Nodes.ClientStartSceneContainer.setStyle("-fx-background-color: blue");
    return new Scene(Nodes.ClientStartSceneContainer, 500, 500);
  }

  public Scene CreateClientGameScene() {
    return new Scene(Nodes.ClientGameSceneContainer, 500, 500);
  }

  public Scene CreateClientWaitRoomScene() {
    return new Scene(Nodes.ClientWatingSceneContainer, 500, 500);
  }

}