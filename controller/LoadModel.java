package graphEditor.controller;

import graphEditor.GraphEditor;
import graphEditor.model.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
public class LoadModel implements ActionListener {
    private GraphEditor main;
    private JFrame mainFrame;

    public LoadModel(GraphEditor main, JMenuItem t, JFrame mainFrame) {
        this.main=main;
        this.mainFrame=mainFrame; //The main frame is passed as loading a model changes the FRAME TITLE
        t.addActionListener(this); //This is listening to the Load button
    }
    public void actionPerformed(ActionEvent e){
         System.out.println("Load clicked");
         String dir =
                 JOptionPane.showInputDialog(null, "Enter the filename. (Excluding .awesome extension");
        if (dir != null && dir != "") {
            load(dir);
        } else { //CANCEL was clicked or empty string was given
            JOptionPane.showMessageDialog(null, "You cancelled.");
        }
    }
    public void load(String dir){
        if(!dir.isEmpty()){
            dir = System.getProperty("user.dir") + "\\" + dir +".awesome";
            try
            {
                // Reading the object from a file
                FileInputStream file = new FileInputStream(dir);
                ObjectInputStream in = new ObjectInputStream(file);
                // Method for deserialization of object
                GraphModel newModel = (GraphModel) in.readObject();
                main.setCurrentModel(newModel); //Send new Model DATA to Graph Editor
                mainFrame.setTitle("Graph Editor - " + newModel.getName()); //Update the MAIN frame's title
                in.close();
                file.close();
            }
            catch(IOException ex)
            {
                JOptionPane.showMessageDialog(null, "ERROR: File could not be loaded \n" +
                        "(Try checking the name or extension, all files should be .awesome)");
            }
            catch(ClassNotFoundException ex)
            {
                System.out.println("ClassNotFoundException is caught");
            }
        }
        else {
            System.out.println("INVALID FILE NAME");
            JOptionPane.showMessageDialog(null, "Invalid file name.");
        }
    }
}

