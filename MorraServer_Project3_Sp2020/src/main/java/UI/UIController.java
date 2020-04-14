package UI;

import java.io.FileInputStream;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UIController {
  private UINodes Nodes;
  public HashMap<String, Scene> sceneMap;
  public SceneCreator m_SceneCreator;
  public Stage primaryStage;
  public String currScene;

  public UIController(UINodes nds, Stage stg) {
    Nodes = nds;
    m_SceneCreator = new SceneCreator(Nodes);
    primaryStage = stg;
  }

  public void SetupServerUI() {
    Nodes.ServerlistItems = new ListView<String>();
  }

  public void SetupNodes() {
    this.SetupServerUI();
  }

  public void ChangeToScene(String sceneName) {
    Scene s = this.sceneMap.get(sceneName);
    if(s != null) {
      primaryStage.setScene(s);
      primaryStage.setTitle(sceneName);
      this.currScene = sceneName;
    } else {
      throw new Error("Wrong sceneName!");
    }
  } 

  public void SetupScenes() {
    // Scenes Storage
    sceneMap = new HashMap<String, Scene>();

    // Put Scenes
    sceneMap.put("server", m_SceneCreator.CreateServerScene());
 }

  
}