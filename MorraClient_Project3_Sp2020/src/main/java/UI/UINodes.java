package UI;

import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UINodes {

  // Main Window
  public BorderPane startPane;
  public Button serverChoice;
  public Button clientChoice;
  public HBox buttonBox;

  // Client 
    // ***************************** Start Scene *****************************
    public VBox ClientStartSceneContainer;
    public VBox IPBox;
    public TextField IPAdressField;
    public TextField PortField;
    public Button ConnectBtn;
    // ***************************** Game Scene *****************************
    public VBox ClientGameSceneContainer;
    public HBox ImagesChoiceBox;

    public Image OneFingerImg;
    public Image TwoFingersImg;
    public Image ThreeFingersImg;
    public Image FourFingersImg;
    public Image FiveingersImg;

    public ImageView OneFingerView;
    public ImageView TwoFingerView;
    public ImageView ThreeFingerView;
    public ImageView FourFingerView;
    public ImageView FiveFingerView;

    public TextField GuessField;
    public Button PlayBtn;

    public Button PlayAgainBtn;
    public Button ExitBtn;

    public TextField PlayerPoints;
    //  ***************************** WAIT SCENE *****************************
    public VBox ClientWatingSceneContainer;
    public Text WaitingText;




}
