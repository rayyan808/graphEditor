package graphEditor.model;

import java.io.Serializable;
import java.util.Observable;

public class GraphEdge extends Observable implements Serializable {
    private GraphVertex A, B; //A = Origin Vertex, B = Destination Vertex
    private int posAX,posAY;
    private int posBY,posBX;
    private boolean temporary = false;
    private static final long serialversionUID =
            129347L;
    public GraphEdge(GraphVertex A){ //Start with just the origin
          this.A=A; this.posAX= (int)A.getRect().getX();this.posAY= (int)A.getRect().getY();
    }
    //Verify if this edge connects the two given vertices
    public void connectTo(GraphVertex B){
        if(!(B == A)){
            this.B=B; this.posBX = (int)B.getRect().getX(); this.posBY= (int)B.getRect().getY();
        }
        setChanged();
        notifyObservers();
    }
    //=== Function to check if a passed Vertex is connected to this edge ===//
    //=== Returns A if it is the origin vertex, B if destination
    public String interacts(GraphVertex v){
        if(this.A == v){
            return "A";
        }else if(this.B==v){
            return "B";
        }
        return null;
    }

    public void update() {
        setChanged();
        notifyObservers();
    }
    public boolean isInteracting(GraphVertex v){ if(A ==v || B == v ) { return true; } return false;}
    public int getX(int x){
        if(x==0) { return posAX; }
        else { return posBX; }
    }
    public int getY(int y){
        if(y==0) { return posAY; }
        else { return posBY; }
    }
    public void setDestinationPos(int x, int y){
        this.posBX=x;
        this.posBY=y;
        setChanged();
        notifyObservers();
    }
    public void setOriginPos(int x, int y){
        this.posAX=x;
        this.posAY=y;
        setChanged();
        notifyObservers();
    }
    public String getOriginName(){ return A.getName(); }
    public String getDestinationName() { return B.getName(); }

    public boolean isTemporary() {
        return this.temporary;
    }

    public void setTemporary(boolean val) {
        this.temporary = val;
    }
}
