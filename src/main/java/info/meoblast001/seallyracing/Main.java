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
      System.err.println("Requires three parameters: Mode, host, and port.");
      return;
    }
    String mode = args[0];
    String host = args[1];
    int port = Integer.parseInt(args[2]);

    if (mode.equals("client")) {
      ClientApplication app = new ClientApplication(host, port);
      app.start();
    } else if (mode.equals("server")) {
      ServerApplication app = new ServerApplication(port);
      app.start(JmeContext.Type.Headless);
    } else {
      System.err.println("Mode must be \"client\" or \"server\".");
    }
  }
}
