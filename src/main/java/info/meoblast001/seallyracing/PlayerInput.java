package info.meoblast001.seallyracing;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.scene.Spatial;
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

  // Player character.
  private Spatial player;

  public PlayerInput(Spatial player) {
    this.player = player;
  }

  /**
   * Handle input which triggers a single aaction.
   * @see ActionListener#onAction(String, boolean, float)
   */
  public void onAction(String name, boolean keyPressed, float tpf) {
    // Currently no actions.
  }

  /**
   * Handle input which may have intensity and occurs over multiple frames.
   * @see AnalogListener#onAnalog(String, float, float)
   */
  public void onAnalog(String name, float value, float tpf) {
    if (name.equals(MOVE_LEFT)) {
      player.rotate(0.0f, PlayState.PLAYER_TORQUE * tpf, 0.0f);
    } else if (name.equals(MOVE_RIGHT)) {
      player.rotate(0.0f, -PlayState.PLAYER_TORQUE * tpf, 0.0f);
    } else if (name.equals(MOVE_UP)) {
      player.rotate(-PlayState.PLAYER_TORQUE * tpf, 0.0f, 0.0f);
    } else if (name.equals(MOVE_DOWN)) {
      player.rotate(PlayState.PLAYER_TORQUE * tpf, 0.0f, 0.0f);
    }
  }
}
