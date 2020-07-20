package graphEditor.controller;

import graphEditor.GraphEditor;
import graphEditor.model.GraphEdge;
import graphEditor.model.GraphVertex;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

public class RemoveNode implements ActionListener, Serializable {
    private GraphVertex clickedVertex;
    private GraphEditor main;
    private UndoManager undoManager;
            public RemoveNode(GraphEditor main,UndoManager manager){
                this.undoManager=manager;
                this.main=main;
            }
    @Override
    public void actionPerformed(ActionEvent e) {
             System.out.println("Remove node clicked on: " + clickedVertex.getName());
             operation();
    }
    public void setCurrentVertex(GraphVertex v){
        this.clickedVertex=v;
    }
    private void operation(){
                GraphVertex vertex = clickedVertex;
        ArrayList<GraphEdge> connections = main.getVertexEdges(clickedVertex); //Get all connected edges of this Vertex
        main.removeVertex(clickedVertex);
        for(GraphEdge e : connections){
            main.removeEdge(e); //Remove all edges that were associated
        }
        AbstractUndoableEdit newEdit = new AbstractUndoableEdit(){
            GraphVertex v = vertex;
            ArrayList<GraphEdge> edges = connections;
            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                System.out.println("UNDO called on: " + getPresentationName());
                main.addVertex(v);
                for(GraphEdge e: edges) {
                    main.addEdge(e);
                }
            }
            @Override
            public void redo() throws CannotRedoException {
                try {
                    super.redo();
                    main.removeVertex(v);
                    for (GraphEdge e : edges) {
                        main.removeEdge(e);
                    }//todo: ALL EDGES SHOULD HAVE NEW ORIGIN POS
                }catch(CannotRedoException ex){
                    System.out.println("REDO is empty");
                }
            }
            public boolean canUndo() { return true; }
            public boolean canRedo() { return true; }
            public String getPresentationName() { return "Remove Node"; }
        };
        undoManager.addEdit(newEdit);
    }
}
