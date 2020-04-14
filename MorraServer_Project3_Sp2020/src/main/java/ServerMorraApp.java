import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
 
import UI.UIController;
import UI.UINodes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerMorraApp extends Application {
  
  public Scene startScene;
  public Server serverConnection;
  public Client clientConnection;
  public UINodes Nodes;
  public UIController UI;
  public Stage primaryStage;


  public static void main(String[] args) {
    launch(args);
  }
        
  @Override
  public void start(Stage pStage) throws Exception {
    this.primaryStage = pStage;
    this.primaryStage.setTitle("The Networked Client/Server GUI Example");

    // Init
    Nodes = new UINodes();
    UI = new UIController(Nodes, primaryStage);

    // UI Nodes Setup
    UI.SetupNodes();

    // Setup Scenes
    UI.SetupScenes();

    // ON CLOSE WINDOW Action
    this.SetCloseWindowListener();

    // DEFAULT SCENE LOGIC
    UI.ChangeToScene("server");
    
    //  Create server on start scene
    serverConnection = new Server(data -> {
      Platform.runLater(() -> {
        if(data instanceof String) 
          Nodes.ServerlistItems.getItems().addAll((String)data);
        if(data instanceof Integer)  
          System.out.println(data);
        if(data instanceof MorraInfo)
          System.out.println(data.toString());
      });
    });
    
    this.primaryStage.show();
  }


  private void SetCloseWindowListener() {
    this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent t) {
        Platform.exit();
        System.exit(0);
      }
    });
  }
}