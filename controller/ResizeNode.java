package graphEditor.controller;

import graphEditor.model.GraphVertex;

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class ResizeNode implements ActionListener, Serializable {
    private UndoManager undoManager;
    private GraphVertex clickedNode;
    private JPanel resizePanel;
    private JTextField xField = new JTextField(5);
    private JTextField yField = new JTextField(5);

    public ResizeNode(UndoManager undoManager) {
        this.undoManager = undoManager;
        resizePanel = new JPanel();
        resizePanel.add(new JLabel("x:"));
        resizePanel.add(xField);
        resizePanel.add(Box.createHorizontalStrut(15)); // a spacer
        resizePanel.add(new JLabel("y:"));
        resizePanel.add(yField);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO: Show dialog with X and Y input
        int result = JOptionPane.showConfirmDialog(null, resizePanel,
                "Values must be (Min: 11 MAX: 399)", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                if ((x > 10 && x < 400) && (y > 10 && y < 400)) {
                    resizeRect(x, y);
                } else {
                    JOptionPane.showMessageDialog(null, "ERROR: Enter values within the given Range");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ERROR: Invalid Character input!");
            }
        }
    }

    public void setCurrentVertex(GraphVertex v) {
        this.clickedNode = v;
    }

    private void resizeRect(int x, int y) {
        GraphVertex v = clickedNode;
        Rectangle oldRect = clickedNode.getRect();
        Rectangle newRect = new Rectangle(oldRect.x, oldRect.y, x, y); //Create new rectangle with old position
        clickedNode.setRect(newRect);
        AbstractUndoableEdit newEdit = new AbstractUndoableEdit() {
            Rectangle newR = newRect;
            Rectangle oldR = oldRect;
            GraphVertex node = v;

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                try {
                    System.out.println("UNDO called on: " + getPresentationName());
                    node.setRect(oldR);
                    node.update();
                } catch (CannotUndoException ex) {
                    System.out.println("UNDO is empty");
                }
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                try {
                    node.setRect(newR);
                    node.update();
                } catch (CannotRedoException ex) {
                    System.out.println("REDO is empty");
                }
            }

            public boolean canUndo() {
                return true;
            }

            public boolean canRedo() {
                return true;
            }

            public String getPresentationName() {
                return "Resize Node";
            }
        };
        undoManager.addEdit(newEdit);
        v.update();
    }
}
