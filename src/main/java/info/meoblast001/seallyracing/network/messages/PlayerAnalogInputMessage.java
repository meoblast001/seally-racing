package info.meoblast001.seallyracing.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Message sent to server containing analog user input. Does not need to be
 * reliable.
 */
@Serializable
public class PlayerAnalogInputMessage extends AbstractMessage {
  public String name;
  public float value;

  public PlayerAnalogInputMessage() {
    // Empty constructor.
  }

  public PlayerAnalogInputMessage(String name, float value) {
    this.name = name;
    this.value = value;
  }
}
