
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.ui;

//~--- JDK imports ------------------------------------------------------------

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Administrator
 */
public class IndexedFocusTraversalPolicy extends FocusTraversalPolicy {
    private ArrayList<Component> components = new ArrayList<Component>();

    public void addIndexedComponent(Component component) {
        components.add(component);
    }

    @Override
    public Component getComponentAfter(Container aContainer, Component aComponent) {
        int atIndex = components.indexOf(aComponent);
        int nextIndex = (atIndex + 1) % components.size();

        return components.get(nextIndex);
    }

    @Override
    public Component getComponentBefore(Container aContainer, Component aComponent) {
        int atIndex = components.indexOf(aComponent);
        int nextIndex = (atIndex + components.size() - 1) % components.size();

        return components.get(nextIndex);
    }

    @Override
    public Component getFirstComponent(Container aContainer) {
        return components.get(0);
    }

    @Override
    public Component getLastComponent(Container aContainer) {
        return components.get(components.size() - 1);
    }

    @Override
    public Component getDefaultComponent(Container aContainer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}



