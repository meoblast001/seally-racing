package info.meoblast001.seallyracing;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.Network;
import info.meoblast001.seallyracing.states.PlayState;

import java.io.IOException;

/**
 * Game application for client.
 */
public class ClientApplication extends SimpleApplication {
  private String host;
  private int port;

  /**
   * Constructor.
   * @param host Host with which to connect.
   * @param port Port on which to connect.
   */
  public ClientApplication(String host, int port) {
    this.host = host;
    this.port = port;
  }

  /**
   * Initialise the game.
   * @see SimpleApplication#simpleInitApp()
   */
  @Override
  public void simpleInitApp() {
    try {
      Client client = Network.connectToServer(host, port);
      client.start();
      System.out.println("Connected!");
    } catch (IOException e) {
      // TODO: Handle this exception.
      System.err.println("Error connecting to server.");
    }

    this.flyCam.setEnabled(false);
    stateManager.attach(new PlayState());
  }
}
