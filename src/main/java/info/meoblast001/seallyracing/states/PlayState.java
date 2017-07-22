package info.meoblast001.seallyracing.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import info.meoblast001.seallyracing.*;

/**
 * AppState for game play.
 */
public class PlayState extends AbstractAppState {
  // Torque at which player rotates charchter. For pitch, this does include
  // torque required to undo PLAYER_PITCH_NEUTRALISE_TORQUE.
  public static final float PLAYER_TORQUE = 0.5f;
  // Maximum allowed pitch (where 1 and -1 are completely up or down).
  public static final float PLAYER_MAX_PITCH = 0.5f;

  // Distance camera should maintain from player.
  public static final float CAMERA_DISTANCE = 10f;

  private boolean isServer;
  private int totalPlayers, localPlayerIdx;
  private SimpleApplication app;
  private BulletAppState bullet;
  private Node rootNode;
  private CoursePath coursePath;
  private PlayerManager playerManager;
  private PlayerInput playerInput = null;
  private FollowCamera followCamera = null;

  /**
   * Prevents neutralisation of all attempts to change pitch. Pitch is only
   * completely neutralised (set to 0) once after PITCH_EXISTS_THRESHOLD is no
   * longer met.
   */
  private boolean pitchNeutralised = true;

  /**
   * Constructor for client.
   * @param totalPlayers Total number of players in game.
   * @param localPlayerIdx Order of client player starting at 0.
   */
  public PlayState(int totalPlayers, int localPlayerIdx) {
    this.isServer = false;
    this.totalPlayers = totalPlayers;
    this.localPlayerIdx = localPlayerIdx;
  }

  /**
   * Constructor for server.
   * @param totalPlayers Total number of players in game.
   */
  public PlayState(int totalPlayers) {
    this.isServer = true;
    this.totalPlayers = totalPlayers;
  }

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

    // Create player manager and input either for server or client.
    if (isServer) {
      playerManager = new PlayerManager(this.app, coursePath, bullet,
                                        totalPlayers);
      ServerApplication serverApp = (ServerApplication) this.app;
      for (Spatial player : playerManager.getPlayers()) {
        new PlayerInput(player, serverApp.getServerNetListener());
      }
    } else {
      playerManager = new PlayerManager(this.app, coursePath, bullet,
                                        totalPlayers, localPlayerIdx);

      // Create the camera attached to the player.
      followCamera = new FollowCamera(this.app.getCamera(),
          playerManager.getLocalPlayer(), CAMERA_DISTANCE);

      // Capture input to player.
      InputManager input = app.getInputManager();
      String[] mappingNames = new String[] { PlayerInput.MOVE_LEFT,
          PlayerInput.MOVE_RIGHT, PlayerInput.MOVE_UP, PlayerInput.MOVE_DOWN };
      ClientApplication clientApp = (ClientApplication) this.app;
      playerInput = new PlayerInput(playerManager.getLocalPlayer(),
                                    clientApp.getClient());
      input.addListener(playerInput, mappingNames);
      input.addMapping(PlayerInput.MOVE_LEFT, new KeyTrigger(KeyInput.KEY_A));
      input.addMapping(PlayerInput.MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
      input.addMapping(PlayerInput.MOVE_UP, new KeyTrigger(KeyInput.KEY_S));
      input.addMapping(PlayerInput.MOVE_DOWN, new KeyTrigger(KeyInput.KEY_W));
    }
  }

  /**
   * Update for next frame.
   * @param tpf Milliseconds since last frame.
   */
  @Override
  public void update(float tpf) {
    super.update(tpf);

    // Update all players.
    playerManager.update(tpf);
    // Update course to validate player positions.
    coursePath.update();
    // Update follow camera.
    if (followCamera != null) {
      followCamera.update();
    }

    // Neutralise player pitch at a rate lower than the effects of player input.
    Spatial player = playerManager.getLocalPlayer();
    if (player != null && playerInput != null
        && !playerInput.isPitchApplied()) {
      Vector3f forward = player.getLocalRotation().mult(Vector3f.UNIT_Z);
      float pitch = Vector3f.UNIT_Y.dot(forward);
      if (pitch > PlayerManager.PITCH_EXISTS_THRESHOLD) {
        player.rotate(PlayerManager.PLAYER_PITCH_NEUTRALISE_TORQUE * tpf,
                      0.0f, 0.0f);
        pitchNeutralised = false;
      } else if (pitch < -PlayerManager.PITCH_EXISTS_THRESHOLD) {
        player.rotate(-PlayerManager.PLAYER_PITCH_NEUTRALISE_TORQUE * tpf,
                      0.0f, 0.0f);
        pitchNeutralised = false;
      } else if (!pitchNeutralised) {
        player.rotate(pitch * 0.5f * FastMath.PI, 0.0f, 0.0f);
        pitchNeutralised = true;
      }
    }
  }
}
