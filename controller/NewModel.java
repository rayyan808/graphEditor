package graphEditor.controller;

import graphEditor.GraphEditor;
import graphEditor.model.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewModel implements ActionListener {
    private GraphEditor main;
    private JFrame mainFrame;

    public NewModel(GraphEditor main, JMenuItem n, JFrame mainFrame) {
        this.main=main;
        this.mainFrame=mainFrame; //Main Frame is referenced as title needs to be changed
        n.addActionListener(this);
    }
        public void actionPerformed(ActionEvent e){
            System.out.println(("Add Node clicked"));
            String s = JOptionPane.showInputDialog(null, "Enter Name");
            GraphModel newModel = new GraphModel(s);
            main.setCurrentModel(newModel);
            mainFrame.setTitle("Graph Editor - " + s);

        }
    }

