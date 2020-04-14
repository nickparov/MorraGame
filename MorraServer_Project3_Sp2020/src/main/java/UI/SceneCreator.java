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

  // Server
  public Scene CreateServerScene() {
    BorderPane pane = new BorderPane();
    pane.setPadding(new Insets(70));
    pane.setStyle("-fx-background-color: coral");
    pane.setCenter(Nodes.ServerlistItems);
    return new Scene(pane, 500, 500);
  }

}