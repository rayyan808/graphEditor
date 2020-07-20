package graphEditor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public class GraphModel extends Observable implements Serializable {
    private ArrayList<GraphVertex> vertexList;
    private ArrayList<GraphEdge> edgeList;
    private static final long serialversionUID =
            129348938L;
    private String name;
    public GraphModel(String s){
             this.name=s;
             this.vertexList = new ArrayList<>();
             this.edgeList = new ArrayList<>();
    }
    //This will be called from 'Add Node' controller/action
    public void addVertex(GraphVertex v){
        vertexList.add(v);
        setChanged();
        notifyObservers();
    }
    public void update(){
        setChanged();
        notifyObservers();
    }
    public void removeVertex(GraphVertex v){
        vertexList.remove(v);
        setChanged();
        notifyObservers();
    }
    public void removeEdge(GraphEdge e){
        edgeList.remove(e);
        setChanged();
        notifyObservers();
    }
    public void updatePositions(GraphVertex vertexMoved, int posX, int posY){
        //Whenever a vertex has been moved, it's relevent edge must also be updated
        vertexMoved.updatePos(posX,posY);
        for(GraphEdge edge : edgeList){
            String result = edge.interacts(vertexMoved); //This edge is connected to the vertex
            if(result=="A"){
                edge.setOriginPos(posX,posY);
            } else if(result=="B"){
                edge.setDestinationPos(posX,posY);
            }
        }
        setChanged();
        notifyObservers();
    }
    public ArrayList<GraphEdge> getEdgeList() { return this.edgeList; }
    public ArrayList<GraphVertex> getVertexList(){
        return this.vertexList;
    }
    public String getName(){
        return this.name;
    }
}
