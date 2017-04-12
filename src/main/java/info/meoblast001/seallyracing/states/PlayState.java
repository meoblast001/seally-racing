package info.meoblast001.seallyracing.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import info.meoblast001.seallyracing.FollowCamera;
import info.meoblast001.seallyracing.PlayerInput;

/**
 * AppState for game play.
 */
public class PlayState extends AbstractAppState {
  // Speed at which player moves.
  public static final float PLAYER_SPEED = 12f;
  // Torque at which player rotates charchter. For pitch, this does include
  // torque required to undo PLAYER_PITCH_NEUTRALISE_TORQUE.
  public static final float PLAYER_TORQUE = 0.5f;
  // Torque at which pitch is neutralised. This is performed at every frame.
  public static final float PLAYER_PITCH_NEUTRALISE_TORQUE = 0.05f;
  // Distance camera should maintain from player.
  public static final float CAMERA_DISTANCE = 10f;
  // Any pitch less than this amount (where 1 and -1 are completely up or down)
  // should no longer exist and is immediately neutralised to no pitch.
  public static final float PITCH_EXISTS_THRESHOLD = 0.0005f;

  private SimpleApplication app;
  private Node rootNode;
  private Spatial player;
  private FollowCamera followCamera;

  /**
   * Prevents neutralisation of all attempts to change pitch. Pitch is only
   * completely neutralised (set to 0) once after PITCH_EXISTS_THRESHOLD is no
   * longer met.
   */
  private boolean pitchNeutralised = true;

  /**
   * Initialise play state.
   * @param stateManager The application state manager.
   * @param app The game application object.
   */
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app = (SimpleApplication) app;
    this.rootNode = this.app.getRootNode();

    // Currently a simple box is used for the player.
    Box box = new Box(1, 1, 1);
    Geometry player = new Geometry("Player", box);

    Material mat = new Material(app.getAssetManager(),
                                "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    player.setMaterial(mat);

    this.rootNode.attachChild(player);
    this.player = player;

    // Create a static object sitting forward and to the side.
    Geometry staticObject = new Geometry("StaticObject", box);
    staticObject.setMaterial(mat);
    staticObject.setLocalTranslation(2, 0, 100);
    this.rootNode.attachChild(staticObject);

    // Create the camera attached to the player.
    followCamera = new FollowCamera(this.app.getCamera(), this.player,
                                    CAMERA_DISTANCE);

    // Capture input to player.
    InputManager input = app.getInputManager();
    String[] mappingNames = new String[] { PlayerInput.MOVE_LEFT,
      PlayerInput.MOVE_RIGHT, PlayerInput.MOVE_UP, PlayerInput.MOVE_DOWN };
    input.addListener(new PlayerInput(this.player), mappingNames);
    input.addMapping(PlayerInput.MOVE_LEFT, new KeyTrigger(KeyInput.KEY_A));
    input.addMapping(PlayerInput.MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
    input.addMapping(PlayerInput.MOVE_UP, new KeyTrigger(KeyInput.KEY_S));
    input.addMapping(PlayerInput.MOVE_DOWN, new KeyTrigger(KeyInput.KEY_W));
  }

  /**
   * Update for next frame.
   * @param tpf Milliseconds since last frame.
   */
  @Override
  public void update(float tpf) {
    super.update(tpf);

    // Update follow camera.
    followCamera.update();

    // Update player character.
    Vector3f forward = player.getLocalRotation().mult(Vector3f.UNIT_Z);
    player.move(forward.mult(PLAYER_SPEED * tpf));

    // Neutralise player pitch at a rate lower than the effects of player input.
    float pitch = Vector3f.UNIT_Y.dot(forward);
    if (pitch > PITCH_EXISTS_THRESHOLD) {
      player.rotate(PLAYER_PITCH_NEUTRALISE_TORQUE * tpf, 0.0f, 0.0f);
      pitchNeutralised = false;
    } else if (pitch < -PITCH_EXISTS_THRESHOLD) {
      player.rotate(-PLAYER_PITCH_NEUTRALISE_TORQUE * tpf, 0.0f, 0.0f);
      pitchNeutralised = false;
    } else if (!pitchNeutralised) {
      player.rotate(pitch * 0.5f * FastMath.PI, 0.0f, 0.0f);
      pitchNeutralised = true;
    }
  }
}
