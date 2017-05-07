package info.meoblast001.seallyracing.collision;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Spatial;
import info.meoblast001.seallyracing.CoursePath;
import info.meoblast001.seallyracing.states.PlayState;

/**
 * Handles collisions between player characters and course points and updates
 * user data in the player to reflect progression in the course.
 */
public class CoursePointCollisionListener implements PhysicsCollisionListener {
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
        && nodeA.getUserData(PlayState.IS_PLAYER_ATTR) != null
        && (Boolean) nodeA.getUserData(PlayState.IS_PLAYER_ATTR)) {
      player = nodeA;
    } else if (nodeB != null
               && nodeB.getUserData(PlayState.IS_PLAYER_ATTR) != null
               && (Boolean) nodeB.getUserData(PlayState.IS_PLAYER_ATTR)) {
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
        player.setUserData(CoursePath.PLAYER_TARGET_POINT_ATTR,
                           playerTarget + 1);
      }
    }
  }
}
