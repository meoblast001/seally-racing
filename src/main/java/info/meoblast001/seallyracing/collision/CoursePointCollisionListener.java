package info.meoblast001.seallyracing.collision;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Spatial;
import info.meoblast001.seallyracing.CoursePath;
import info.meoblast001.seallyracing.PlayerManager;

/**
 * Handles collisions between player characters and course points and updates
 * user data in the player to reflect progression in the course.
 */
public class CoursePointCollisionListener implements PhysicsCollisionListener {
  // Course path being followed.
  private CoursePath coursePath;

  /**
   * Construct listener.
   * @param coursePath Course path that players are following.
   */
  public CoursePointCollisionListener(CoursePath coursePath) {
    this.coursePath = coursePath;
  }

  /**
   * Handle collision between player characters and course points.
   * @see PhysicsCollisionListener#collision(PhysicsCollisionEvent)
   */
  public void collision(PhysicsCollisionEvent event) {
    Spatial nodeA = event.getNodeA();
    Spatial nodeB = event.getNodeB();

    Spatial player = null;
    Spatial coursePoint = null;
    // Determine which node is the player.
    if (nodeA != null
        && nodeA.getUserData(PlayerManager.IS_PLAYER_ATTR) != null
        && (Boolean) nodeA.getUserData(PlayerManager.IS_PLAYER_ATTR)) {
      player = nodeA;
    } else if (nodeB != null
               && nodeB.getUserData(PlayerManager.IS_PLAYER_ATTR) != null
               && (Boolean) nodeB.getUserData(PlayerManager.IS_PLAYER_ATTR)) {
      player = nodeB;
    }
    // Determine which node is the course point.
    if (nodeA != null
        && nodeA.getUserData(CoursePath.COURSE_ORDER_ATTR) != null) {
      coursePoint = nodeA;
    } else if (nodeB != null
               && nodeB.getUserData(CoursePath.COURSE_ORDER_ATTR) != null) {
      coursePoint = nodeB;
    }
    // If collision is player and course point, check if the course
    // point is the player's target.
    if (player != null && coursePoint != null) {
      int playerTarget
          = player.getUserData(CoursePath.PLAYER_TARGET_POINT_ATTR);
      int courseOrder = coursePoint.getUserData(CoursePath.COURSE_ORDER_ATTR);
      if (playerTarget == courseOrder) {
        // Target reached. Set next target.
        int totalCoursePoints = coursePath.getCoursePoints().length;
        player.setUserData(CoursePath.PLAYER_TARGET_POINT_ATTR,
                           (playerTarget + 1) % totalCoursePoints);
        player.setUserData(CoursePath.PLAYER_ON_COURSE_ATTR, true);
      }
    }
  }
}
