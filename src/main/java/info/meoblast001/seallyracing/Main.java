/**
 * Copyright (C) 2017 Braden Walters
 *
 * This software may be modified and distributed under the terms of the MIT
 * license. See the LICENSE file for details.
 */

package info.meoblast001.seallyracing;

import com.jme3.system.JmeContext;

/**
 * Main application class.
 */
public class Main {
  /**
   * Application entrance.
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
     // TODO: Make client completely configurable in game. Make server
     // configurable through config file.
    if (args.length < 3) {
      System.err.println("Requires parameters: client <host> <port>, "
          + "server <port> <expectedClients>");
      return;
    }
    String mode = args[0];

    if (mode.equals("client")) {
      String host = args[1];
      int port = Integer.parseInt(args[2]);

      ClientApplication app = new ClientApplication(host, port);
      app.start();
    } else if (mode.equals("server")) {
      int port = Integer.parseInt(args[1]);
      int expectedClients = Integer.parseInt(args[2]);

      ServerApplication app = new ServerApplication(port, expectedClients);
      app.start(JmeContext.Type.Headless);
    } else {
      System.err.println("Mode must be \"client\" or \"server\".");
    }
  }
}
