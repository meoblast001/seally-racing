package info.meoblast001.seallyracing;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import java.util.Vector;

/**
 * Manages players in the game. Creates all visible players and updates them
 * for the client. Manages all player logic for the server.
 */
public class PlayerManager {
  // Name of user data specifying if a spatial is a player or not.
  public static final String IS_PLAYER_ATTR = "isPlayer";
  // Distance between player and first ring at start.
  private static final float PLAYER_START_DISTANCE = 20.0f;
  // Speed at which player moves.
  private static final float PLAYER_SPEED = 12f;
  // Torque at which pitch is neutralised. This is performed at every frame.
  public static final float PLAYER_PITCH_NEUTRALISE_TORQUE = 0.1f;
  // Any pitch less than this amount (where 1 and -1 are completely up or down)
  // should no longer exist and is immediately neutralised to no pitch.
  public static final float PITCH_EXISTS_THRESHOLD = 0.0005f;

  private SimpleApplication application;
  private CoursePath coursePath;
  private BulletAppState bullet;
  private Vector<Spatial> players = new Vector<Spatial>();
  private Spatial localPlayer = null;

  /**
   * Constructor for the client.
   * @param app Game application.
   * @param coursePath Course path the players follow.
   * @param bullet Bullet app state with which to create player physics.
   * @param totalPlayers Total number of players in game.
   * @param localPlayerIdx Order of client player starting at 0.
   */
  public PlayerManager(SimpleApplication app, CoursePath coursePath,
                       BulletAppState bullet, int totalPlayers,
                       int localPlayerIdx) {
    this.application = app;
    this.coursePath = coursePath;
    this.bullet = bullet;
    for (int i = 0; i < totalPlayers; ++i) {
      Spatial player = createPlayer(this.application.getRootNode(), i);
      this.players.add(player);
      if (i == localPlayerIdx) {
        this.localPlayer = player;
      }
    }
  }

  /**
   * Constructor for the server.
   * @param app Game application.
   * @param coursePath Course path the players follow.
   * @param bullet Bullet app state with which to create player physics.
   * @param totalPlayers Total number of players in game.
   */
  public PlayerManager(SimpleApplication app, CoursePath coursePath,
                       BulletAppState bullet, int totalPlayers) {
    this.application = app;
    this.coursePath = coursePath;
    this.bullet = bullet;
    for (int i = 0; i < totalPlayers; ++i) {
      Spatial player = createPlayer(this.application.getRootNode(), i);
      this.players.add(player);
    }
  }

  /**
   * Create a new player.
   * @param parentNode Parent of player. Should be root node.
   * @param playerIdx Order of created player starting at 0.
   * @return Spatial of new player.
   */
  private Spatial createPlayer(Node parentNode, int playerIdx) {
    // Currently a simple box is used for the player.
    Box box = new Box(1, 1, 1);
    Geometry player = new Geometry("PLAYER" + playerIdx, box);

    Material mat = new Material(application.getAssetManager(),
        "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    player.setMaterial(mat);

    // Move player in front of first course point.
    Spatial[] coursePoints = coursePath.getCoursePoints();
    if (coursePoints.length > 0) {
      Spatial startPoint = coursePoints[0];
      Vector3f forward = startPoint.getWorldRotation().mult(Vector3f.UNIT_Z);
      player.move(forward.mult(PLAYER_START_DISTANCE));
    }

    // Attribute that this is a player.
    player.setUserData(IS_PLAYER_ATTR, true);
    // Add physics to player
    CollisionShape shape = CollisionShapeFactory.createBoxShape(player);
    GhostControl playerPhysics = new GhostControl(shape);
    player.addControl(playerPhysics);
    bullet.getPhysicsSpace().add(playerPhysics);

    // Attach to root node and course path.
    parentNode.attachChild(player);
    coursePath.addPlayer(player);
    return player;
  }

  /**
   * Update all players.
   * @param tpf Milliseconds since last frame.
   */
  public void update(float tpf) {
    for (Spatial player : players) {
      // Update player character.
      Vector3f forward = player.getLocalRotation().mult(Vector3f.UNIT_Z);
      player.move(forward.mult(-PLAYER_SPEED * tpf));
    }
  }

  /**
   * Get the player associated with the current client.
   * @return Player's spatial.
   */
  public Spatial getLocalPlayer() {
    return localPlayer;
  }
}
