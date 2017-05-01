package info.meoblast001.seallyracing.network;

import com.jme3.network.*;
import info.meoblast001.seallyracing.ServerApplication;
import info.meoblast001.seallyracing.network.messages.GameStatusMessage;

import java.util.HashSet;

/**
 * Handles network activity and messages for the server.
 */
public class ServerNetListener
    implements MessageListener<HostedConnection>, ConnectionListener {
  private static final String REASON_DUPLICATE_CLIENT = "DUPLICATE_CLIENT";

  private int expectedClients;
  private HashSet<Integer> collectedClients = new HashSet<Integer>();

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
    server.addMessageListener(this, GameStatusMessage.class);
  }

  /**
   * @see ConnectionListener#connectionAdded(Server, HostedConnection)
   */
  public void connectionAdded(Server server, HostedConnection client)
  {
    if(collectedClients.contains(client.getId())) {
      client.close(REASON_DUPLICATE_CLIENT);
    }
    collectedClients.add(client.getId());
    // Once all clients have connected, broadcast the start signal.
    if(collectedClients.size() == expectedClients) {
      GameStatusMessage message
          = new GameStatusMessage(GameStatusMessage.Status.GAME_BEGIN);
      server.broadcast(message);
    }
  }

  /**
   * @see ConnectionListener#connectionRemoved(Server, HostedConnection)
   */
  public void connectionRemoved(Server server, HostedConnection client)
  {
    if(collectedClients.contains(client.getId())) {
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
