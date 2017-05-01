package info.meoblast001.seallyracing;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;

import java.util.TreeMap;

/**
 * Locates the course path in the scene, gathers information about it, and
 * rotates nodes in path to create a smooth appearance.
 */
public class CoursePath {
  // Name of node containing course points.
  public static final String COURSE_PATH_NODE_NAME = "coursePath";
  // Name of user data on course point spatials which indicates the order the
  // player must reach them.
  public static final String COURSE_ORDER_USER_DATA_NAME = "courseOrder";

  // All spatials, in order, representing course points.
  private Spatial[] coursePoints;

  /**
   * Course path constructor. Locate the course path and organise the points
   * into an ordered list. Rotate these points to create a smooth path.
   * @param scene Root node of scene.
   */
  public CoursePath(Node scene) {
    // Locate each course point and create an array of those points ordered by
    // the order data in the user data of the spatials.
    Spatial coursePath = scene.getChild(COURSE_PATH_NODE_NAME);
    final TreeMap<Integer, Spatial> points = new TreeMap<Integer, Spatial>();
    coursePath.breadthFirstTraversal(new SceneGraphVisitor() {
      public void visit(Spatial spatial) {
        Integer order = spatial.getUserData(COURSE_ORDER_USER_DATA_NAME);
        if (order != null) {
          points.put(order, spatial);
        }
      }
    });
    coursePoints = points.values().toArray(new Spatial[0]);

    // To create a smooth rotation along the course, each course point is
    // rotated to look in the direction of the vector connecting the preceding
    // point with the succeeding point.
    for (int i = 0; i < coursePoints.length; ++i) {
      Spatial current = coursePoints[i];
      Spatial previous = i > 0
          ? coursePoints[i - 1] : coursePoints[coursePoints.length - 1];
      Spatial next = coursePoints[(i + 1) % coursePoints.length];

      Vector3f lookDirection
          = next.getLocalTranslation().subtract(previous.getLocalTranslation());
      current.getLocalRotation().lookAt(lookDirection,
                                        new Vector3f(0.0f, 1.0f, 0.0f));
    }
  }
}
