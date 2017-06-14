package info.meoblast001.seallyracing.network;

import com.jme3.network.serializing.Serializer;
import info.meoblast001.seallyracing.network.messages.GameStartMessage;
import info.meoblast001.seallyracing.network.messages.PlayerAnalogInputMessage;

/**
 * Registers all message types with serialiser and provides access to their
 * class objects.
 */
public class MessageRegistry {
  /**
   * Register all message types with the serialiser.
   */
  public static void registerMessages()
  {
    Serializer.registerClass(GameStartMessage.class);
    Serializer.registerClass(PlayerAnalogInputMessage.class);
  }
}
