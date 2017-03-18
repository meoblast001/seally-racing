package info.meoblast001.seallyracing;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 * Camera that follows a spatial.
 */
public class FollowCamera {
  // Camera to control.
  private Camera camera;
  // Target that the camera should follow.
  private Spatial followTarget;
  // Distance the camera should maintain from the camera.
  private float distance;

  /**
   * Constructor.
   * @param camera Camera in scene which this FollowCamera controls.
   * @param followTarget Target spatial to follow.
   * @param distance Standard distance from the spatial.
   */
  public FollowCamera(Camera camera, Spatial followTarget, float distance) {
    this.camera = camera;
    this.followTarget = followTarget;
    this.distance = distance;
  }

  public void update() {
    Vector3f targetTranslation = followTarget.getWorldTranslation();
    Vector3f distanceTranslation = followTarget.getLocalRotation().
      mult(Vector3f.UNIT_Z).mult(distance);
    camera.setLocation(targetTranslation.add(distanceTranslation));
    camera.lookAt(followTarget.getWorldTranslation(),
                  new Vector3f(0.0f, 1.0f, 0.0f));
  }
}
