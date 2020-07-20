package graphEditor.model;

import java.awt.*;
import java.io.Serializable;
import java.util.Observable;

public class GraphVertex extends Observable implements Serializable  {
    private String name;
    private Rectangle rect;
    private boolean clicked = false;
    private static final long serialversionUID =
            1293478241L;
    public GraphVertex(String n, Rectangle r){
        this.name=n;
        this.rect=r;
    }
    //Get the name of this vertex
    public String getName(){
        return name;
    }
    public void setName(String s) { this.name=s; update(); }
    public Rectangle getRect(){
        return rect;
    }

    public void setRect(Rectangle r) {
        this.rect = r;
        setChanged();
        notifyObservers();
    }
    public void update(){
        setChanged();
        notifyObservers();
    }
    public void setClicked(boolean t){
        clicked=t;
        //=== Notify observers so the colour changed to red immediately ===//
        setChanged();
        notifyObservers();
    }
    public boolean getClicked(){
        return clicked;
    }
    public void updatePos(int x,int y){
        rect.x = x;
        rect.y = y;
        setChanged();
        notifyObservers();
    }
}
