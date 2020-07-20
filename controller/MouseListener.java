package graphEditor.controller;
import graphEditor.GraphEditor;
import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;
import graphEditor.view.DrawFrame;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class MouseListener extends MouseInputAdapter implements Serializable {
    private GraphModel currentModel;
    private ArrayList<GraphVertex> nodeList;
    private GraphVertex clickedNode = null;
    private GraphVertex movingNode = null;
    private boolean addingEdge = false;
    private DrawFrame frame;
    private GraphEditor main;
    private GraphEdge currentEdge;
    private JPopupMenu rightClickMenu;
    private RenameNode vertexRenamer;
    private RemoveNode vertexRemover;
    private ResizeNode vertexResizer;

    public MouseListener(graphEditor.GraphEditor main, DrawFrame f, JPopupMenu rightClickMenu, UndoManager m) {
        this.frame = f;this.rightClickMenu=rightClickMenu; this.main=main;
        main.addMouseListener(this);
        //======== Right-Click Menu Items ======//
        JMenuItem rename = new JMenuItem("Rename");
        JMenuItem removeNode = new JMenuItem("Remove Node");
        JMenuItem changeRect = new JMenuItem("Change Width & Height");
        vertexRenamer = new RenameNode(m);
        rename.addActionListener(vertexRenamer);
        vertexRemover = new RemoveNode(main,m);
        removeNode.addActionListener(vertexRemover);
        vertexResizer = new ResizeNode(m);
        changeRect.addActionListener(vertexResizer);
        rightClickMenu.add(rename);
        rightClickMenu.add(removeNode);
        rightClickMenu.add(changeRect);
    }
    public void mouseDragged(MouseEvent e){
        if(movingNode != null){
            currentModel.updatePositions(movingNode, e.getX(),e.getY());
        }
    }
    public void mouseMoved(MouseEvent e){
       if(addingEdge == true){
          currentEdge.setDestinationPos(e.getX(),e.getY());
       }
    }
    public void mouseClicked(MouseEvent e){
        GraphVertex clicked = inBound(e);
        if(e.getButton() == MouseEvent.BUTTON3) { //Right click
            if(clicked != null){ //Not null >> Clicked on a Vertex
                vertexRenamer.setCurrentVertex(clicked);
                vertexRemover.setCurrentVertex(clicked);
                vertexResizer.setCurrentVertex(clicked);
                rightClickMenu.show(frame,e.getX(),e.getY()); //Show the right click-menu
                return;
            }
        }else { //LEFT OR MIDDLE CLICK DETECTED
            if (!addingEdge) { //No node selected previously
                clickedNode = inBound(e); //Assign the new node either null (unselected) or link to selected vertex
                if (inBound(e) != null) { //Pressed on a vertex
                    System.out.println("Vertex Selected: " + clickedNode.getName());
                    clickedNode.setClicked(true); //This vertex is now selected
                    currentEdge = new GraphEdge(clickedNode); //Create a new edge to follow the cursor
                    currentEdge.setTemporary(true); //This is temporary until a desination node is clicked
                    addingEdge = true; //The process of adding edge is TRUE now
                    main.addEdge(currentEdge); //Temporarily add the edge to the model, so it can be called in the same draw function
                }
            } else { //A node has already been clicked
                GraphVertex nextNode = inBound(e); //If the click is on a vertex, that vertex must be the next node
                if (nextNode == null || nextNode == clickedNode) { //User did not select a node to attach/double clicked node
                    //Set clicked to false
                    System.out.println("Connection of Vertex cancelled.");
                    clickedNode.setClicked(false); //Vertex unclicked
                    clickedNode = null; //Erase this Vertex from our variable
                    main.removeEdge(currentEdge); //Remove this so it is no longer draw
                    currentEdge = null;
                    addingEdge = false;
                    return;
                }
                    if(deletion(nextNode) == false) {
                        currentEdge.setTemporary(false); //No longer a temporary edge
                            currentEdge.connectTo(nextNode); //Connect to a the clicked (Destination) Node
                            clickedNode.setClicked(false); //Set our node clicked value back to FALSE
                            System.out.println("Vertex: " + clickedNode.getName() + " has been connected with: " + nextNode.getName());
                            clickedNode = null; //RESET our variables
                            currentEdge = null;
                            addingEdge = false;
                        } else {
                            main.removeEdge(currentEdge);
                            currentEdge=null;
                        }
            }
        }
    }
    public void mousePressed(MouseEvent e){
           movingNode = inBound(e);
    }
    public void updateData(GraphModel model){ //Whenever a LOAD or NEW is called, the listener needs to know where to access
        //Vertex data from
        this.currentModel = model;
        this.nodeList = model.getVertexList();
    }
    private GraphVertex inBound(MouseEvent e){
        int posX=e.getX();
        int posY=e.getY();
        Rectangle r;
        for(GraphVertex v : nodeList){
            r=v.getRect();
            Rectangle n = new Rectangle(posX,posY,1,1);
            if(r.contains(n)){ //Mouse pos within this vertex
                return v;
            }
        }
        return null;
    }
    private boolean deletion(GraphVertex nextNode) {
        boolean toDelete = false;
        ArrayList<GraphEdge> dispose = new ArrayList();
        for (GraphEdge edge : currentModel.getEdgeList()) {
            if (edge.isInteracting(clickedNode) && edge.isInteracting(nextNode)) {
                //If there is SOME edge e that interacts with both clicked and next node
                //The edge already exists, so delete this edge
                dispose.add(edge); //This is added to another edge to iterate aftwards, as deleting whilst looping throws Exception
                toDelete=true;
                currentEdge.setTemporary(true);
                addingEdge = false;
                clickedNode.setClicked(false);
            }
        }
        for (GraphEdge edge : dispose) {
            main.removeEdge(edge); //Empty all edges in our disposal list
        }
        return toDelete;
    }
}
