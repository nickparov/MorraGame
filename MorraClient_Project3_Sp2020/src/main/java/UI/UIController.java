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


  public void SetupClientUI() {
    // ***************************** Start Scene Nodes Setup *****************************
      Nodes.IPBox = new VBox();
      Nodes.IPAdressField = new TextField();
      Nodes.IPAdressField.setText("127.0.0.1");

      Nodes.PortField = new TextField();
      Nodes.PortField.setText("5555");

      Nodes.ConnectBtn = new Button("Connect");
      Nodes.IPBox.getChildren().addAll(Nodes.IPAdressField, Nodes.PortField);
    // ***************************** END Start Scene *****************************

    // ***************************** Game Scene *****************************
      this.setupGameScene();
    // ***************************** GAME SCENE END *****************************

    // ***************************** WAITING ROOM START *****************************
    Nodes.WaitingText = new Text("Waiting for your opponent...");
    Nodes.ClientWatingSceneContainer = new VBox(Nodes.WaitingText);

    // ********* GAME AGAIN ******
    Nodes.PlayAgainBtn = new Button("Play Again?");
    Nodes.ExitBtn = new Button("Exit?");

  }


  public void showOpponentInfo(Integer opponentPlayed, Integer opponentGuessed) {
    Nodes.ClientGameSceneContainer.getChildren().addAll(
    new Text("Opponent Played: " + opponentPlayed + " Guessed: " + opponentGuessed), this.Nodes.PlayAgainBtn,
    this.Nodes.ExitBtn);
  }

  public void refreshGameScene() {
    this.setupGameScene();
  }

  public void setupGameScene() {
     //Setting the image view 
     Nodes.OneFingerView = new ImageView(new Image("/counter_one.png")); 
     Nodes.TwoFingerView = new ImageView(new Image("/counter_two.png")); 
     Nodes.ThreeFingerView = new ImageView(new Image("/counter_three.png")); 
     Nodes.FourFingerView = new ImageView(new Image("/counter_four.png")); 
     Nodes.FiveFingerView = new ImageView(new Image("/counter_five.png")); 
     
     Nodes.OneFingerView.getStyleClass().add("1");
     Nodes.TwoFingerView.getStyleClass().add("2");
     Nodes.ThreeFingerView.getStyleClass().add("3");
     Nodes.FourFingerView.getStyleClass().add("4");
     Nodes.FiveFingerView.getStyleClass().add("5");


     //setting the fit height and width of the image view 
     Nodes.OneFingerView.setFitHeight(110); 
     Nodes.OneFingerView.setFitWidth(100); 

     Nodes.TwoFingerView.setFitHeight(110); 
     Nodes.TwoFingerView.setFitWidth(100); 

     Nodes.ThreeFingerView.setFitHeight(110); 
     Nodes.ThreeFingerView.setFitWidth(100); 

     Nodes.FourFingerView.setFitHeight(110); 
     Nodes.FourFingerView.setFitWidth(100); 

     Nodes.FiveFingerView.setFitHeight(110); 
     Nodes.FiveFingerView.setFitWidth(100); 

     Nodes.ImagesChoiceBox = new HBox(
       Nodes.OneFingerView,
       Nodes.TwoFingerView, 
       Nodes.ThreeFingerView, 
       Nodes.FourFingerView, 
       Nodes.FiveFingerView
     );

     Nodes.GuessField = new TextField();
     Nodes.PlayBtn = new Button("Play!");


     Nodes.PlayerPoints = new TextField("Player Points: 0");
     Nodes.PlayerPoints.setDisable(true);

     Nodes.ClientGameSceneContainer = new VBox();
     Nodes.ClientGameSceneContainer.getChildren().addAll(
       Nodes.PlayerPoints,
       Nodes.ImagesChoiceBox, 
       Nodes.GuessField, 
       Nodes.PlayBtn
    );
  }


  public void SetupNodes() {
    this.SetupClientUI();
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
    sceneMap.put("client_start", m_SceneCreator.CreateClientStartScene());
    sceneMap.put("client_game", m_SceneCreator.CreateClientGameScene());
    sceneMap.put("client_waiting", m_SceneCreator.CreateClientWaitRoomScene());
 }

  
}