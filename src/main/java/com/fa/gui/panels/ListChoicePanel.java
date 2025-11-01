package com.fa.gui.panels;

import com.fa.gui.AppWindow;
import com.fa.gui.ComponentFactory;
import com.fa.util.gui.GBC;
import com.fa.util.gui.components.RoundRectButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public abstract class ListChoicePanel<T> extends JPanel {

    protected AppWindow window;

    protected List<RoundRectButton> buttonList;

    public ListChoicePanel(AppWindow window) {
        this.window = window;
        setOpaque(false);

        buttonList = new ArrayList<>();
    }

    public void reloadList() {
        removeAll();
        buttonList.clear();

        T[] elements = loadElements();

        if (elements.length != 0) {
            GridLayout layout = new GridLayout(elements.length, 1);
            layout.setVgap(5);

            setLayout(layout);
            for (T element : elements) {
                JPanel elementPanel = createElementPanel(element);
                add(elementPanel);
            }
        }

        revalidate();
    }

    public JPanel createElementLabelPanel(String elementName) {
        JPanel elementPanel = ComponentFactory.createAccentRoundRecJPanel(new GridBagLayout());
        JLabel elementNameLabel = ComponentFactory.createAccentJLabel(elementName);

        elementPanel.add(elementNameLabel, new GBC(0,0).setWeight(1,1).setFill(GBC.HORIZONTAL).setInsets(5,15,5,10));

        return elementPanel;
    }

    public abstract JPanel createElementPanel(T element);

    @Override
    public void setEnabled(boolean isEnabled) {
        buttonList.forEach(roundRecButton -> roundRecButton.setEnabled(isEnabled));
    }

    protected abstract T[] loadElements();
}
