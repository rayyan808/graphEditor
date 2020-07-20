
package graphEditor;

import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;
import graphEditor.view.DrawFrame;

import java.io.Serializable;
import java.util.ArrayList;

public class GraphEditor implements Serializable {
    GraphModel currentModel; //The model currently in use
    DrawFrame frame; //Graph Editor contains the draw frame
    graphEditor.controller.MouseListener mouseController; //The Mouse Listener passed from main
    public GraphEditor(){ //The Constructor for the Graph Editor
        //============ MODEL LOADER & DE-LOADER =========//
        this.currentModel = new GraphModel("Empty Graph");
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
    }
    public void setFrame(DrawFrame f){
        this.frame=f;
    } //Link Graph Editor to the draw frame

    public void addMouseListener(graphEditor.controller.MouseListener m) { //Initialize the Mouse Controller
        this.mouseController=m;
        m.updateData(currentModel); //Update the data that this controller will use
    }
    public void setCurrentModel(GraphModel newModel){
        this.currentModel=newModel;
        this.currentModel.addObserver(frame);
        frame.modelToDisplay(newModel); //Frame is given new data to display
        this.currentModel.update();
        mouseController.updateData(this.currentModel); //Controller is given new data to work with
        System.out.println("New current model set.\n");
    }
    public GraphModel getCurrentModel(){
        return this.currentModel;
    }
    public void addVertex(GraphVertex v){
        currentModel.addVertex(v);
        v.addObserver(frame); //New Vertex is being observed by the Draw Frame
        v.update();
        currentModel.update(); //Let our models observer know it's been changed
        mouseController.updateData(currentModel); //Let our Mouse Controller know model has been changed
    }
    public void removeVertex(GraphVertex v){
        currentModel.removeVertex(v);
        v.update();
        currentModel.update();
    }
    public ArrayList<GraphEdge> getVertexEdges(GraphVertex v) {
        ArrayList<GraphEdge> list = new ArrayList();
        for(GraphEdge e : currentModel.getEdgeList()){
            if(e.isInteracting(v)){
                list.add(e);
            }
        }
        return list;
    }
    //Add a new edge between two nodes
    public void addEdge(GraphEdge newE){
        //=== New Edge is added, All Observers notified ===//
        currentModel.getEdgeList().add(newE);
        newE.addObserver(frame);
        newE.update();
        currentModel.update();
    }

    public void removeEdge(GraphEdge remove){
        try {
            currentModel.removeEdge(remove);
            currentModel.update();
        } catch(NullPointerException e){
            System.out.println("An invalid edge was attempted to be removed.");
            e.printStackTrace();
        }
    }
}