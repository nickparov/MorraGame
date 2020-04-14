import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.lang.model.type.NullType;

import UI.UIController;
import UI.UINodes;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ActionController {
  private UINodes Nodes;
  private UIController UI;
  private Client clientConnection;
  private Stage primaryStage;
  private Integer ID;
  private MorraInfo ClientMorraData;
  private Boolean bothPlayersPlayed = false;

  // Scene Variables
  // GameScene Variables
  private Boolean wasFingerSelected;
  private Boolean wasNumberGuessed;
  private Integer betFingerNumber;
  private Integer guessedNumber;

  ActionController(UINodes nds, Client c_c, Stage pStage, UIController UI) {
    this.Nodes = nds;
    this.clientConnection = c_c;
    this.primaryStage = pStage;
    this.UI = UI;
    this.ID = null;

    this.SetupClientVariables();
  }

  private void sleep() {
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void MorraInfoReceived(MorraInfo data) {
    // Sleep
    this.sleep();
    // Update Client Morra
    this.SetClientMorra(data);
    // Get curr scene
    String CurrScene = new String(this.UI.currScene);

    if (CurrScene.equals("client_waiting") && data.have2players.booleanValue() == true) {
      if(this.ClientMorraData.p1ID.equals(this.ID)) {
        // player 1
        this.Nodes.PlayerPoints.setText("Player Points: " + this.ClientMorraData.p1Points);
      } else {
        this.Nodes.PlayerPoints.setText("Player Points: " + this.ClientMorraData.p2Points);
      }

      this.UI.ChangeToScene("client_game");
    }

    if (CurrScene.equals("client_game")) {
        if((data.player1Played.booleanValue() == true && data.player2Played.booleanValue() == true)
        && this.bothPlayersPlayed.booleanValue() == false) {
        
      this.bothPlayersPlayed = true;
      // UI got some updates from other client through server
      this.sleep();
      this.BothPlayesPlayed();
      System.out.println("Player 1 guessed: " + data.p1Guess + " Player 2 guessed: " + data.p2Guess);
      System.out.println("Player 1 played: " + data.p1Plays + " Player 2 played: " + data.p2Plays);
      }
    }

    if (CurrScene.equals("client_game") && this.bothPlayersPlayed.booleanValue() == true) {
      // check played again
      if (data.player1PlaysAgain != null && data.player2PlaysAgain != null) {
        if (data.player1PlaysAgain.booleanValue() == true && data.player2PlaysAgain.booleanValue() == true) {
          // If both of them play again
          this.BothPlayedAgainHandler();
        } else {
          //  if one of them is false
          if (this.ID.equals(data.p1ID)) {
            // Player 1
            if (data.player1PlaysAgain.booleanValue() == true) {
              this.PlayerPlaysAgainHandler();
            } else {
              this.ClientExited();
            }
          } else {
            // Player 2
            if (data.player2PlaysAgain.booleanValue() == true) {
              this.PlayerPlaysAgainHandler();
            } else {
              this.ClientExited();
            }
          }
        }
      }
    }
  }

  private void PlayerPlaysAgainHandler() {
    this.sleep();

    this.SetupClientVariables();

    this.bothPlayersPlayed = false;

    this.UI.refreshGameScene();

    this.UI.sceneMap.remove("client_game");
    this.UI.sceneMap.put("client_game", this.UI.m_SceneCreator.CreateClientGameScene());

    this.Setup_Client_GameSceneActions();

    this.setPlayBtnListener();

    this.UI.ChangeToScene("client_waiting");
  }

  private void BothPlayedAgainHandler() {
    this.sleep();
    
    this.ClientMorraData.p2Plays = this.ClientMorraData.p1Plays = 0;
    this.ClientMorraData.p2Guess = this.ClientMorraData.p1Guess = 0;
    this.ClientMorraData.player1Played = this.ClientMorraData.player2Played = false;
    this.ClientMorraData.player1PlaysAgain = this.ClientMorraData.player2PlaysAgain = null;

    this.SetupClientVariables();

    this.bothPlayersPlayed = false;

    this.UI.refreshGameScene();

    if(this.ClientMorraData.p1ID.equals(this.ID)) {
      // player 1
      this.Nodes.PlayerPoints.setText("Player Points: " + this.ClientMorraData.p1Points);
    } else {
      this.Nodes.PlayerPoints.setText("Player Points: " + this.ClientMorraData.p2Points);
    }

    this.UI.sceneMap.remove("client_game");

    this.UI.sceneMap.put("client_game", this.UI.m_SceneCreator.CreateClientGameScene());

    this.Setup_Client_GameSceneActions();

    this.setPlayBtnListener();

    this.UI.ChangeToScene("client_game");

    this.clientConnection.sendData(this.ClientMorraData);
  }

  private void ClientExited() {
    this.sleep();

    this.SetupClientVariables();

    this.bothPlayersPlayed = false;

    this.UI.refreshGameScene();

    this.UI.sceneMap.remove("client_game");
    this.UI.sceneMap.put("client_game", this.UI.m_SceneCreator.CreateClientGameScene());

    this.Setup_Client_GameSceneActions();


    this.UI.ChangeToScene("client_start");
  }

  // Actions
  private void Setup_Client_StartScene_Actions() {
    // Client Window Actions
    Nodes.ConnectBtn.setOnAction(e -> {
      String IPAdress = Nodes.IPAdressField.getText();
      String Port = Nodes.PortField.getText();

      this.clientConnection = new Client(data -> {
        Platform.runLater(() -> {
          if (data instanceof MorraInfo)
            MorraInfoReceived((MorraInfo) data);
          if (data instanceof String)
            System.out.println((String) data);
          if (data instanceof Integer)
            SetClientID((Integer) data);
        });
      }, IPAdress, Port);

      UI.ChangeToScene("client_waiting");

      this.clientConnection.start();

      // Send info
      // **************
    });
  }

  private void Setup_Client_GameSceneActions() {
    // Finger Nodes Listener
    ArrayList<ImageView> FingersNodes = new ArrayList<ImageView>(Arrays.asList(Nodes.OneFingerView, Nodes.TwoFingerView,
        Nodes.ThreeFingerView, Nodes.FourFingerView, Nodes.FiveFingerView));

    for (ImageView imgV : FingersNodes) {
      imgV.setOnMouseClicked(e -> {
        ImageView ClickedImageView = (ImageView) e.getTarget();
        // Creating the scale transformation
        Scale scale = new Scale();
        // Setting the dimensions for the transformation
        scale.setX(0.8);
        scale.setY(0.8);

        Integer betFingerChoice = new Integer(
            ClickedImageView.getStyleClass().get(ClickedImageView.getStyleClass().size() - 1));

        if (this.wasFingerSelected == false) {
          this.wasFingerSelected = true;
          ClickedImageView.getTransforms().addAll(scale);
          this.betFingerNumber = betFingerChoice;
        }
      });
    }

    // force the field to be numeric only
    Nodes.GuessField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> obs, String oldVal, String newVal) {
        // TODO Auto-generated method stub
        if (!newVal.matches("\\d*")) {
          Nodes.GuessField.setText(newVal.replaceAll("[^\\d]", ""));
        }
        wasNumberGuessed = true;
      }
    });
  }

  private void BothPlayesPlayed() {
    Integer opponentPlayed = -1;
    Integer opponentGuessed = -1;

    System.out.println("My id is : " + this.ID);

    if (this.ID.equals(this.ClientMorraData.p1ID)) {
      System.out.println("I'm player 1");
      this.Nodes.PlayerPoints.setText("Player Points: " + this.ClientMorraData.p1Points);
      opponentPlayed = this.ClientMorraData.p2Plays;
      opponentGuessed = this.ClientMorraData.p2Guess;
    } else {
      System.out.println("I'm player 2");
      this.Nodes.PlayerPoints.setText("Player Points: " + this.ClientMorraData.p2Points);
      opponentPlayed = this.ClientMorraData.p1Plays;
      opponentGuessed = this.ClientMorraData.p1Guess;
    }

    // ************** PLAY AGAIN HANDLER *****************
    this.Nodes.PlayAgainBtn.setOnAction(e -> {
      if (this.ID.equals(this.ClientMorraData.p1ID)) {
        this.ClientMorraData.player1PlaysAgain = true;
      } else {
        this.ClientMorraData.player2PlaysAgain = true;
      }
      MorraInfo copyData = new MorraInfo(this.ClientMorraData);
      this.clientConnection.sendData(copyData);
    });

    // ************** EXIT HANDLER *****************
    this.Nodes.ExitBtn.setOnAction(e -> {
      if (this.ID.equals(this.ClientMorraData.p1ID)) {
        this.ClientMorraData.player1PlaysAgain = false;
      } else {
        this.ClientMorraData.player2PlaysAgain = false;
      }
      MorraInfo copyData = new MorraInfo(this.ClientMorraData);
      this.clientConnection.sendData(copyData);
    });

    this.sleep();
    this.UI.showOpponentInfo(opponentPlayed, opponentGuessed);
  }


  private void setPlayBtnListener() {
  // ************************* PLAY BTN HANDLER START *************************
    Nodes.PlayBtn.setOnAction(e -> {
        if (this.wasFingerSelected == true && this.wasNumberGuessed == true && Nodes.GuessField.getText().length() > 0) {
          this.guessedNumber = new Integer(Nodes.GuessField.getText());
          // Set Morra Data
          if (this.ID.equals(ClientMorraData.p1ID)) {
            this.ClientMorraData.p1Guess = this.guessedNumber;
            this.ClientMorraData.p1Plays = this.betFingerNumber;
            this.ClientMorraData.player1Played = true;
          } else {
            this.ClientMorraData.p2Guess = this.guessedNumber;
            this.ClientMorraData.p2Plays = this.betFingerNumber;
            this.ClientMorraData.player2Played = true;
          }
          // send it to server
          this.sleep();
          this.clientConnection.sendData(this.ClientMorraData);
        }
      });
  }


  private void SETUP_CLIENT_ACTIONS() {
    this.Setup_Client_StartScene_Actions();
    this.Setup_Client_GameSceneActions();
  }

  public void SETUP_ALL_ACTIONS() {
    this.SETUP_CLIENT_ACTIONS();
  }


  private void SetupClientVariables() {
    this.wasFingerSelected = false;
    this.wasNumberGuessed = false;
    this.betFingerNumber = 0;
    this.guessedNumber = 0;
  }


  private void SetClientID(Integer ID) {
    this.ID = ID;
    System.out.println(ID);
    this.setPlayBtnListener();
  }

  private void SetClientMorra(MorraInfo m_i) {
    this.ClientMorraData = m_i;
    System.out.println("Received morra data: " + m_i.toString());
    System.out.println("My id is : " + this.ID);
  }
  
}