package info.meoblast001.seallyracing;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.scene.Spatial;
import info.meoblast001.seallyracing.network.ServerNetListener;
import info.meoblast001.seallyracing.network.messages.PlayerAnalogInputMessage;
import info.meoblast001.seallyracing.states.PlayState;

/**
 * Collect player input and respond to it.
 */
public class PlayerInput implements AnalogListener, ActionListener {
  // Movement directions.
  public static final String MOVE_LEFT = "Left";
  public static final String MOVE_RIGHT = "Right";
  public static final String MOVE_UP = "Up";
  public static final String MOVE_DOWN = "Down";

  private boolean isServer;
  // Player character.
  private Spatial player;
  private Client client;
  // Is pitch being applied?
  private boolean pitchAppliedUp;
  private boolean pitchAppliedDown;

  /**
   * Initialise for the client. Apply changes and send them to the server.
   * @param player Player in world affected by player input.
   * @param client Client connection to use.
   */
  public PlayerInput(Spatial player, Client client) {
    isServer = false;
    this.player = player;
    this.client = client;
  }

  /**
   * Initialise for the server. Set as listener to ServerNetListener, which will
   * call methods to invoke input.
   * @param player Player in world which this PlayerInput controls.
   * @param serverNetListener ServerNetListener with which this PlayerInput is
   *                          installed as a callback.
   */
  public PlayerInput(Spatial player, ServerNetListener serverNetListener) {
    isServer = true;
    this.player = player;
    serverNetListener.registerPlayerInput(player.getName(), this);
  }

  /**
   * Handle input which triggers a single aaction.
   * @see ActionListener#onAction(String, boolean, float)
   */
  public void onAction(String name, boolean keyPressed, float tpf) {
    // Record whether pitch is being applied in either direction.
    if (name.equals(MOVE_UP)) {
      pitchAppliedUp = keyPressed;
    } else if (name.equals(MOVE_DOWN)) {
      pitchAppliedDown = keyPressed;
    }
  }

  /**
   * Handle input which may have intensity and occurs over multiple frames.
   * @see AnalogListener#onAnalog(String, float, float)
   */
  public void onAnalog(String name, float value, float tpf) {
    // Get pitch where 1 is facing upward, -1 is facing downward, and 0 is
    // neutral.
    Vector3f forward = player.getLocalRotation().mult(Vector3f.UNIT_Z);
    float pitch = Vector3f.UNIT_Y.dot(forward);

    if (name.equals(MOVE_LEFT)) {
      Quaternion rotation = new Quaternion();
      rotation.fromAngleAxis(PlayState.PLAYER_TORQUE * tpf, Vector3f.UNIT_Y);
      rotation.multLocal(player.getLocalRotation());
      player.setLocalRotation(rotation);
    } else if (name.equals(MOVE_RIGHT)) {
      Quaternion rotation = new Quaternion();
      rotation.fromAngleAxis(-PlayState.PLAYER_TORQUE * tpf, Vector3f.UNIT_Y);
      rotation.multLocal(player.getLocalRotation());
      player.setLocalRotation(rotation);
    } else if (name.equals(MOVE_UP) && pitch < PlayState.PLAYER_MAX_PITCH) {
      player.rotate(PlayState.PLAYER_TORQUE * tpf, 0.0f, 0.0f);
    } else if (name.equals(MOVE_DOWN) && pitch > -PlayState.PLAYER_MAX_PITCH) {
      player.rotate(-PlayState.PLAYER_TORQUE * tpf, 0.0f, 0.0f);
    } else {
      return;
    }

    if (!isServer) {
      // Send over network. Need not be reliable because analog events usually
      // happen very frequently.
      PlayerAnalogInputMessage message
          = new PlayerAnalogInputMessage(name, value);
      message.setReliable(false);
      client.send(message);
    }
  }

  /**
   * Is pitch being applied by player?
   * @return True if pitch is being applied, else false.
   */
  public boolean isPitchApplied() {
    return pitchAppliedUp || pitchAppliedDown;
  }
}
