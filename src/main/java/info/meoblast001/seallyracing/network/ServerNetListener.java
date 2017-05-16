package info.meoblast001.seallyracing.network;

import com.jme3.network.*;
import info.meoblast001.seallyracing.ServerApplication;
import info.meoblast001.seallyracing.network.messages.GameStartMessage;

import java.util.HashMap;

/**
 * Handles network activity and messages for the server.
 */
public class ServerNetListener
    implements MessageListener<HostedConnection>, ConnectionListener {
  private static final String REASON_DUPLICATE_CLIENT = "DUPLICATE_CLIENT";

  private int expectedClients;
  private HashMap<Integer, HostedConnection> collectedClients
      = new HashMap<Integer, HostedConnection>();

  /**
   * Constructor.
   * @param application Server's application singleton.
   * @param server Network server.
   * @param expectedClients Amount of clients needed to start game.
   */
  public ServerNetListener(ServerApplication application, Server server,
                           int expectedClients) {
    this.expectedClients = expectedClients;
    server.addConnectionListener(this);
    server.addMessageListener(this, GameStartMessage.class);
  }

  /**
   * @see ConnectionListener#connectionAdded(Server, HostedConnection)
   */
  public void connectionAdded(Server server, HostedConnection client)
  {
    if (collectedClients.containsKey(client.getId())) {
      client.close(REASON_DUPLICATE_CLIENT);
    }
    collectedClients.put(client.getId(), client);
    // Once all clients have connected, broadcast the start signal.
    if (collectedClients.size() == expectedClients) {
      sendStartMessages();
    }
  }

  /**
   * Send individualised message to each client to start the game.
   */
  private void sendStartMessages()
  {
    HostedConnection[] clients
        = collectedClients.values().toArray(new HostedConnection[0]);
    for (int i = 0; i < clients.length; ++i) {
      HostedConnection client = clients[i];
      GameStartMessage message = new GameStartMessage(clients.length, i);
      client.send(message);
    }
  }

  /**
   * @see ConnectionListener#connectionRemoved(Server, HostedConnection)
   */
  public void connectionRemoved(Server server, HostedConnection client)
  {
    if (collectedClients.containsKey(client.getId())) {
      collectedClients.remove(client.getId());
    }
  }

  /**
   * @see MessageListener#messageReceived(Object, Message)
   */
  public void messageReceived(HostedConnection source, Message message)
  {
    // TODO: Implement message receiving functionality.
  }
}
