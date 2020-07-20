package graphEditor.controller;

import graphEditor.GraphEditor;
import graphEditor.model.GraphEdge;
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
import java.util.ArrayList;

public class AddNode implements ActionListener, Serializable {
    private GraphEditor main;
    private UndoManager undoManager;
    private GraphVertex node;

    public AddNode(GraphEditor main, JMenuItem a, UndoManager manager) {
        this.main=main;
        this.undoManager=manager;
        a.addActionListener(this);

    }
    public void actionPerformed(ActionEvent e){
        System.out.println(("Add Node clicked"));
        String name = JOptionPane.showInputDialog(null, "Enter Vertex Name");
        if (name != null) { //Avoid NullPointerException
            if (name != "") {
                operation(name);
            }
        }
    }
    public void operation(String name){
        node =  new GraphVertex(name, new Rectangle(50,50,100,100));
        main.addVertex(node);
        AbstractUndoableEdit newEdit = new AbstractUndoableEdit(){
            GraphVertex node1 = node;
            ArrayList<GraphEdge> connections = main.getVertexEdges(node1); //Get all connected edges to this Vertex
            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                System.out.println("UNDO called on: " + getPresentationName());
                for(GraphEdge e : connections){
                    main.removeEdge(e);
                }
                main.removeVertex(node1);
            }
            @Override
            public void redo() throws CannotRedoException {
                try {
                    super.redo();
                    main.addVertex(node1);
                    for (GraphEdge e : connections) {
                        main.addEdge(e);
                    }
                }catch(CannotRedoException ex){
                    System.out.println("REDO IS EMPTY");
                }
            }
            public boolean canUndo() { return true; }
            public boolean canRedo() { return true; }
            public String getPresentationName() { return "Add Node"; }
        };
        undoManager.addEdit(newEdit);
    }
}
