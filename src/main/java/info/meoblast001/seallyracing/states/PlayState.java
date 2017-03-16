package info.meoblast001.seallyracing.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import info.meoblast001.seallyracing.FollowCamera;

/**
 * AppState for game play.
 */
public class PlayState extends AbstractAppState {
  private final float PLAYER_SPEED = 12f;
  private final float PLAYER_TORQUE = 0.5f;
  private final float CAMERA_DISTANCE = 10f;

  private SimpleApplication app;
  private Node rootNode;
  private Spatial player;
  private FollowCamera followCamera;

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
    staticObject.setLocalTranslation(2, 0, -5);
    this.rootNode.attachChild(staticObject);

    // Create the camera attached to the player.
    followCamera = new FollowCamera(this.app.getCamera(), this.player,
                                    CAMERA_DISTANCE);
  }

  @Override
  public void update(float tpf) {
    super.update(tpf);

    // Update follow camera.
    followCamera.update();

    // Update player character.
    player.rotate(-PLAYER_TORQUE * tpf, 0.0f, 0.0f);
    Vector3f forward = player.getLocalRotation().mult(Vector3f.UNIT_Z).
      mult(-PLAYER_SPEED * tpf);
    player.move(forward);
  }
}
