/**
 * Copyright (C) 2017 Braden Walters
 *
 * This software may be modified and distributed under the terms of the MIT
 * license. See the LICENSE file for details.
 */

package info.meoblast001.seallyracing;

import com.jme3.app.SimpleApplication;
import info.meoblast001.seallyracing.states.PlayState;

/**
 * Main application class.
 */
public class Main extends SimpleApplication {
  /**
   * Application entrance.
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    Main app = new Main();
    app.start();
  }

  /**
   * Initialise the game.
   * @see SimpleApplication#simpleInitApp()
   */
  @Override
  public void simpleInitApp() {
    this.flyCam.setEnabled(false);
    stateManager.attach(new PlayState());
  }
}
