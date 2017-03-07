/**
 * Copyright (C) 2017 Braden Walters
 *
 * This software may be modified and distributed under the terms of the MIT
 * license. See the LICENSE file for details.
 */

package info.meoblast001.seallyracing;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Main extends SimpleApplication {
  public static void main(String[] args) {
    Main app = new Main();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    Box b = new Box(1, 1, 1);
    Geometry geom = new Geometry("Box", b);

    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    geom.setMaterial(mat);

    rootNode.attachChild(geom);
  }

  @Override
  public void simpleUpdate(float tpf) {
    //TODO: add update code
  }

  @Override
  public void simpleRender(RenderManager rm) {
    //TODO: add render code
  }
}
