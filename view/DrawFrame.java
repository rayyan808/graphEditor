package graphEditor.view;

import graphEditor.controller.MouseListener;
import graphEditor.model.GraphEdge;
import graphEditor.model.GraphModel;
import graphEditor.model.GraphVertex;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public class DrawFrame extends JPanel implements Observer, Serializable {
    private GraphModel currentModel;

    public DrawFrame() {
        //=========== FRAME SETTINGS ===========//
        setSize(800, 600);
        setLocation(0, 0);
        setBackground(Color.CYAN);
        setVisible(true);
    }
    @Override public void paintComponent(Graphics g){
        super.paintComponent(g);
        paintModel(g);
    }
    public void modelToDisplay(GraphModel m) { //Update draw Frame whenever Model changes
        try {
            this.currentModel = m;
            m.addObserver(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void addListeners(MouseListener m){ //Add Mouse Listener to the frame
        this.addMouseMotionListener(m);
        this.addMouseListener(m);
    }
    public void update(Observable o, Object ob){
        repaint();
    }
    private void paintModel(Graphics g){
        if(currentModel != null) {
            for (GraphVertex node : currentModel.getVertexList()) { //LOOP through all Vertices in Model
                Rectangle r = node.getRect(); //Get the Rectangle Data
                if(node.getClicked()){ //IF clicked node, Highlight as RED
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLACK); ///ELSE it is BLACK
                }
                g.drawRect((int)r.getX(), (int)r.getY(), r.width, r.height); //Draw the Vertex
                g.drawString(node.getName(), r.x + (r.width / 2), r.y + (r.height / 2)); //Draw the Name in the Centre
            }
            g.setColor(Color.BLACK);
            for(GraphEdge edge : currentModel.getEdgeList()){
                if (edge.isTemporary()) {
                    //===Special Case edge, it has an origin Vertex and other end is the Mouse Cursor pos
                    g.drawLine(edge.getX(1),edge.getY(1),edge.getX(0),edge.getY(0));
                    g.setColor(Color.YELLOW);
                 } else {
                    g.setColor(Color.BLACK);
                    g.drawLine(edge.getX(0), edge.getY(0), edge.getX(1), edge.getY(1));
                }
            }
        }
    }
}
