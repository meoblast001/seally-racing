package info.meoblast001.seallyracing;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import info.meoblast001.seallyracing.network.MessageRegistry;
import info.meoblast001.seallyracing.network.ServerNetListener;
import info.meoblast001.seallyracing.states.PlayState;

import java.io.IOException;

/**
 * Game application for server.
 */
public class ServerApplication extends SimpleApplication {
  private int port;
  private int expectedClients;
  Server server;

  /**
   * Constructor.
   * @param port Port on which to run the server.
   */
  public ServerApplication(int port, int expectedClients) {
    this.port = port;
    this.expectedClients = expectedClients;
    MessageRegistry.registerMessages();
  }

  /**
   * Initialise the game.
   * @see SimpleApplication#simpleInitApp()
   */
  @Override
  public void simpleInitApp() {
    try {
      server = Network.createServer(port);
      server.start();
      new ServerNetListener(this, server, expectedClients);
    } catch (IOException e) {
      // TODO: Handle exception.
      System.err.println("Error creating server.");
    }

    this.flyCam.setEnabled(false);
    stateManager.attach(new PlayState(expectedClients));
  }

  /**
   * @see SimpleApplication#destroy()
   */
  @Override
  public void destroy() {
    server.close();
    super.destroy();
  }
}
