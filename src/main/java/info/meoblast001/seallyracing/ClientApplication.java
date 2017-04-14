package info.meoblast001.seallyracing;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.Network;
import info.meoblast001.seallyracing.network.ClientNetListener;
import info.meoblast001.seallyracing.network.MessageRegistry;
import info.meoblast001.seallyracing.states.PlayState;
import info.meoblast001.seallyracing.states.WaitingState;

import java.io.IOException;

/**
 * Game application for client.
 */
public class ClientApplication extends SimpleApplication {
  private String host;
  private int port;
  private Client client;
  private PlayState playState;
  private WaitingState waitingState;

  /**
   * Constructor.
   * @param host Host with which to connect.
   * @param port Port on which to connect.
   */
  public ClientApplication(String host, int port) {
    this.host = host;
    this.port = port;
    MessageRegistry.registerMessages();
  }

  /**
   * Initialise the game.
   * @see SimpleApplication#simpleInitApp()
   */
  @Override
  public void simpleInitApp() {
    try {
      client = Network.connectToServer(host, port);
      client.start();
      new ClientNetListener(this, client);
    } catch (IOException e) {
      // TODO: Handle this exception.
      System.err.println("Error connecting to server.");
    }

    this.flyCam.setEnabled(false);
    waitingState = new WaitingState();
    stateManager.attach(waitingState);
  }

  /**
   * @see SimpleApplication#destroy()
   */
  @Override
  public void destroy() {
    client.close();
    super.destroy();
  }

  /**
   * Begin the game.
   */
  public void beginGame() {
    playState = new PlayState();
    waitingState.setEnabled(false);
    stateManager.attach(playState);
  }
}
