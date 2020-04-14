import java.io.Serializable;

public class MorraInfo implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8073692471669315543L;

  public Integer p1Points;
  public Integer p2Points;
  public Integer p1Plays;
  public Integer p2Plays;
  public Integer p1Guess;
  public Integer p2Guess;
  public Integer roomID;
  public Integer p1ID, p2ID;
  public Boolean have2players;
  public Boolean player1Played;
  public Boolean player2Played;

  public Boolean player1PlaysAgain;
  public Boolean player2PlaysAgain;

  public MorraInfo() {
    this.p1Points = 0;
    this.p2Points = 0;
    this.p1Plays = 0;
    this.p2Plays = 0;
    this.have2players = false;
    this.p1Guess = 0;
    this.p2Guess = 0;
    this.p1ID = this.p2ID = 0;
    this.player1Played = this.player2Played = false;
    this.roomID = 0;

    this.player1PlaysAgain = null;
    this.player2PlaysAgain = null;
  }

  public MorraInfo(MorraInfo data) {
    this.roomID = data.roomID;
    this.p1Points = data.p1Points;
    this.p2Points = data.p2Points;
    this.p1Plays = data.p1Plays;
    this.p2Plays = data.p2Plays;
    this.have2players = data.have2players;
    this.p1Guess = data.p1Guess;
    this.p2Guess = data.p2Guess;
    this.p1ID = data.p1ID;
    this.p2ID = data.p2ID;
    this.player1Played = data.player1Played;
    this.player2Played = data.player2Played;  
    this.player1PlaysAgain = data.player1PlaysAgain;
    this.player2PlaysAgain = data.player2PlaysAgain;
  }

  @Override
    public String toString() {
        return "MorraInfo{" +
                "p1ID=" + p1ID +
                ", p2ID=" + p2ID +
                ", p1Points=" + p1Points +
                ", p2Points=" + p2Points +
                ", p1Plays=" + p1Plays +
                ", p2Plays=" + p2Plays +
                ", p1Guess=" + p1Guess +
                ", p2Guess=" + p2Guess +
                ", RoomID=" + roomID +
                ", player1PlaysAgain=" + player1PlaysAgain +
                ", player2PlaysAgain=" + player2PlaysAgain +
                ", have2players=" + have2players +
                '}';
    }


}