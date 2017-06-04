package info.meoblast001.seallyracing.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Message telling clients that the game is starting and information about the
 * game.
 */
@Serializable
public class GameStartMessage extends AbstractMessage {
  public int totalPlayers;
  public int playerIdx;

  public GameStartMessage() {
    // Empty constructor.
  }

  public GameStartMessage(int totalPlayers, int playerIdx) {
    this.totalPlayers = totalPlayers;
    this.playerIdx = playerIdx;
  }
}
