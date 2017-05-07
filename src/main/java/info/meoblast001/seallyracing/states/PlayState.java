package info.meoblast001.seallyracing.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import info.meoblast001.seallyracing.CoursePath;
import info.meoblast001.seallyracing.FollowCamera;
import info.meoblast001.seallyracing.PlayerInput;

/**
 * AppState for game play.
 */
public class PlayState extends AbstractAppState {
  // Name of user data specifying if a spatial is a player or not.
  public static final String IS_PLAYER_ATTR = "isPlayer";
  // Speed at which player moves.
  public static final float PLAYER_SPEED = 12f;
  // Torque at which player rotates charchter. For pitch, this does include
  // torque required to undo PLAYER_PITCH_NEUTRALISE_TORQUE.
  public static final float PLAYER_TORQUE = 0.5f;
  // Maximum allowed pitch (where 1 and -1 are completely up or down).
  public static final float PLAYER_MAX_PITCH = 0.5f;
  // Torque at which pitch is neutralised. This is performed at every frame.
  public static final float PLAYER_PITCH_NEUTRALISE_TORQUE = 0.1f;
  // Distance camera should maintain from player.
  public static final float CAMERA_DISTANCE = 10f;
  // Any pitch less than this amount (where 1 and -1 are completely up or down)
  // should no longer exist and is immediately neutralised to no pitch.
  public static final float PITCH_EXISTS_THRESHOLD = 0.0005f;

  private SimpleApplication app;
  private BulletAppState bullet;
  private Node rootNode;
  private Spatial player;
  private CoursePath coursePath;
  private PlayerInput playerInput;
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

    // Initialise physics.
    bullet = new BulletAppState();
    stateManager.attach(bullet);

    // Create ambient light for world.
    AmbientLight ambientLight = new AmbientLight();
    ambientLight.setColor(ColorRGBA.White.mult(1.3f));
    this.rootNode.addLight(ambientLight);

    // Load current course.
    Spatial course
        = app.getAssetManager().loadModel("Scenes/DeveloperCourse/Course.j3o");
    this.rootNode.attachChild(course);

    // Fetch and initialise the course path.
    coursePath = new CoursePath((Node) course, bullet);

    // Currently a simple box is used for the player.
    Box box = new Box(1, 1, 1);
    Geometry player = new Geometry("Player", box);

    Material mat = new Material(app.getAssetManager(),
                                "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    player.setMaterial(mat);

    this.rootNode.attachChild(player);
    coursePath.addPlayer(player);
    this.player = player;

    // Attribute that this is a player.
    this.player.setUserData(IS_PLAYER_ATTR, true);
    // Add physics to player
    CollisionShape shape = CollisionShapeFactory.createBoxShape(this.player);
    GhostControl playerPhysics = new GhostControl(shape);
    this.player.addControl(playerPhysics);
    bullet.getPhysicsSpace().add(playerPhysics);

    // Create the camera attached to the player.
    followCamera = new FollowCamera(this.app.getCamera(), this.player,
                                    CAMERA_DISTANCE);

    // Capture input to player.
    InputManager input = app.getInputManager();
    String[] mappingNames = new String[] { PlayerInput.MOVE_LEFT,
      PlayerInput.MOVE_RIGHT, PlayerInput.MOVE_UP, PlayerInput.MOVE_DOWN };
    playerInput = new PlayerInput(this.player);
    input.addListener(playerInput, mappingNames);
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

    // Update course to validate player positions.
    coursePath.update();

    // Update follow camera.
    followCamera.update();

    // Update player character.
    Vector3f forward = player.getLocalRotation().mult(Vector3f.UNIT_Z);
    player.move(forward.mult(-PLAYER_SPEED * tpf));

    // Neutralise player pitch at a rate lower than the effects of player input.
    if (!playerInput.isPitchApplied()) {
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
}
