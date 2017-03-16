package info.meoblast001.seallyracing;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 * Camera that follows a spatial.
 */
public class FollowCamera {
  private Camera camera;
  private Spatial followTarget;
  private float distance;

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
