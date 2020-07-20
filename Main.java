package graphEditor;

import graphEditor.controller.*;
import graphEditor.view.DrawFrame;

import javax.swing.*;
import javax.swing.undo.UndoManager;

public class Main {
    public static void main(String[] args){
        UndoManager undoManager = new UndoManager();
        GraphEditor graphEditor = new GraphEditor(); //Create the main controller
        DrawFrame frame = new DrawFrame(); //Create the Draw Frame (VIEW)
        frame.setSize(3840, 2160);
        graphEditor.setFrame(frame); //Link frame
        frame.modelToDisplay(graphEditor.getCurrentModel()); //Assign model to display
            //===== MENU ITEMS =======//
        JPopupMenu rightClickMenu = new JPopupMenu("Edit Name");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem newModel = new JMenuItem("New");
        JMenuItem undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.Event.CTRL_MASK));
        JMenuItem redo = new JMenuItem("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.Event.CTRL_MASK));
        JMenuBar menu = new JMenuBar();
        JMenu file_menu = new JMenu("File");
        JMenu graphMenu = new JMenu("Graph");
        JMenuItem add = new JMenuItem("Add Node");
        //===== Linking Menu's with Sub Menus =======//
        file_menu.add(save);
        file_menu.add(load);
        file_menu.add(newModel);
        file_menu.add(undo);
        file_menu.add(redo);
        graphMenu.add(add);
        menu.add(file_menu);
        menu.add(graphMenu);
        //==== ACTION LISTENERS FOR MENU ITEMS ====//
        MouseListener mouseControl = new MouseListener(graphEditor,frame,rightClickMenu,undoManager);
        UndoAction undoAction = new UndoAction(undoManager); //Sending the Action Listener a reference to undoManager and Frame (For catching IO Exception messages)
        RedoAction redoAction = new RedoAction(undoManager); //Same as above
        undo.addActionListener(undoAction);
        redo.addActionListener(redoAction);
        JFrame mainFrame = new JFrame();
        LoadModel loader = new LoadModel(graphEditor, load, mainFrame); //This is an Action Listener for the 'LOAD' menu item click (File)
        SaveModel saver = new SaveModel(graphEditor, save); //This is an Action Listener for the 'SAVE' menu item click (File)
        NewModel Creator = new NewModel(graphEditor, newModel, mainFrame); //This is an Action Listener for the 'NEW' menu item click (File)
        AddNode nodeAdder = new AddNode(graphEditor, add, undoManager); //This is an Action Listener for the 'Add Node' menu item click (Graph)
        rightClickMenu.addMouseListener(mouseControl);
        //========= MAIN FRAME SETTINGS =========//
        System.out.println("Argument length: " + args.length);
        if(args.length == 1){
            loader.load(args[0]);
        }
        frame.addListeners(mouseControl);
        mainFrame.getContentPane().add(frame);
        mainFrame.add(rightClickMenu);
        mainFrame.setSize(800, 600);
        mainFrame.setLocation(0, 0);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setJMenuBar(menu);
        mainFrame.setTitle("Graph Editor - " + graphEditor.getCurrentModel().getName());
        mainFrame.setVisible(true);
        JOptionPane.showMessageDialog(mainFrame,"Click two nodes to connect, repeat again to disconnect. \n" +
                "Right click on nodes to Edit them \n " +
                "All your files will be saved in .AWESOME format\n" +
                "Undo/Redo function with CTRL+Z and CTRL+R respectively.\n" +
                "===============  MADE BY RAYYAN JAFRI (S3687465) :) =================");
    }
}
