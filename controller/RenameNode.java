package graphEditor.controller;

import graphEditor.model.GraphVertex;

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class RenameNode implements ActionListener, Serializable {
    private GraphVertex currentVertex;
    private UndoManager undoManager;
    private String vertexName;

    public RenameNode(UndoManager manager) {
        this.undoManager = manager;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Rename Clicked");
        String newName = JOptionPane.showInputDialog(null, "Enter new name for Vertex");
        if (newName != null) { //Cancel wasn't clicked
            if (!newName.equals("")) {
                operation(newName);
            } else { //Empty filename
                JOptionPane.showInputDialog(null, "ERROR: You must enter a filename. ");
            }
        }
    }
    public void setCurrentVertex(GraphVertex v){
        this.currentVertex=v;this.vertexName=currentVertex.getName();
    }
    public void operation(String name){
        String oldName=vertexName;
        currentVertex.setName(name);
        AbstractUndoableEdit newEdit = new AbstractUndoableEdit(){
            String oldN = oldName;
            String newN = name;
            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                try {
                    System.out.println("UNDO called on: " + getPresentationName());
                    currentVertex.setName(oldN);
                }catch(CannotUndoException ex){
                    System.out.println("UNDO is empty");
                }
            }
            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                try {
                    currentVertex.setName(newN);
                }catch(CannotRedoException ex){
                    System.out.println("REDO is empty");
                }
            }
            public boolean canUndo() { return true; }
            public boolean canRedo() { return true; }
            public String getPresentationName() { return "Rename Node"; }
        };
        undoManager.addEdit(newEdit);
    }
}
