package info.meoblast001.seallyracing.network;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import info.meoblast001.seallyracing.ClientApplication;
import info.meoblast001.seallyracing.network.messages.GameStartMessage;

import java.util.concurrent.Callable;

/**
 * Handles network activity and messages for the client.
 */
public class ClientNetListener implements MessageListener, ClientStateListener {
  private ClientApplication application;

  /**
   * Constructor.
   * @param application Client's application singleton.
   * @param client Network client connection.
   */
  public ClientNetListener(ClientApplication application, Client client) {
    this.application = application;
    client.addClientStateListener(this);
    client.addMessageListener(this, GameStartMessage.class);
  }

  /**
   * @see ClientStateListener#clientConnected(Client)
   */
  public void clientConnected(Client c)
  {
    // Nothing to do when connected.
  }

  /**
   * @see ClientStateListener#clientDisconnected(Client, DisconnectInfo)
   */
  public void clientDisconnected(Client c, DisconnectInfo info)
  {
    // TODO: Handle a disconnection.
  }

  /**
   * @see MessageListener#messageReceived(Object, Message)
   */
  public void messageReceived(Object source, Message message)
  {
    if (message instanceof GameStartMessage) {
      final GameStartMessage msg = (GameStartMessage) message;
      application.enqueue(new Callable<Void>() {
        public Void call() throws Exception {
          application.beginGame(msg.totalPlayers, msg.playerIdx);
          return null;
        }
      });
    }
  }
}
