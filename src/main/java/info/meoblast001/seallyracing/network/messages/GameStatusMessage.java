package info.meoblast001.seallyracing.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Message telling clients what status the game is in.
 */
@Serializable
public class GameStatusMessage extends AbstractMessage {
  public enum Status {
    GAME_BEGIN
  }

  public Status status;

  public GameStatusMessage() {
    // Empty constructor.
  }

  public GameStatusMessage(Status status) {
    this.status = status;
  }
}
