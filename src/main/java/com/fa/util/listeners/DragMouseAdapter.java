package com.fa.util.listeners;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DragMouseAdapter extends MouseAdapter {

    private int xDiff;
    private int yDiff;

    private final Component component;

    public DragMouseAdapter(Component component) {
        this.component = component;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        xDiff = e.getXOnScreen() - component.getX();
        yDiff = e.getYOnScreen() - component.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        component.setLocation(e.getXOnScreen() - xDiff, e.getYOnScreen() - yDiff);
    }

}
