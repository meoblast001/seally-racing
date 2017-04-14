package info.meoblast001.seallyracing;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import info.meoblast001.seallyracing.states.PlayState;

import java.io.IOException;

/**
 * Game application for server.
 */
public class ServerApplication extends SimpleApplication {
  private int port;

  /**
   * Constructor.
   * @param port Port on which to run the server.
   */
  public ServerApplication(int port) {
    this.port = port;
  }

  /**
   * Initialise the game.
   * @see SimpleApplication#simpleInitApp()
   */
  @Override
  public void simpleInitApp() {
    try {
      Server server = Network.createServer(port);
      server.start();
    } catch (IOException e) {
      // TODO: Handle exception.
      System.err.println("Error creating server.");
    }

    this.flyCam.setEnabled(false);
    stateManager.attach(new PlayState());
  }
}
