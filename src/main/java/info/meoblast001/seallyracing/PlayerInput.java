package info.meoblast001.seallyracing;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
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
  // Is pitch being applied?
  private boolean pitchAppliedUp;
  private boolean pitchAppliedDown;

  public PlayerInput(Spatial player) {
    this.player = player;
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
