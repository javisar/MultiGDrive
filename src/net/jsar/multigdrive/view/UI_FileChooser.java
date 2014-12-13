package net.jsar.multigdrive.view;

import javax.swing.*;

import net.jsar.multigdrive.util.Log;

import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.util.*;


public class UI_FileChooser extends JPanel {
   //protected JButton go;
   
   protected JFileChooser chooser;
   protected String choosertitle = "File Chooser";
   protected int selectionMode = JFileChooser.FILES_AND_DIRECTORIES;
   
   public UI_FileChooser() {
	   init(this.selectionMode, choosertitle);
   }
   
  public UI_FileChooser(int selectionMode) {
	  this.selectionMode = selectionMode;
	  init(this.selectionMode, choosertitle);	 
   }
  
  protected void init(int mode, String title) {
	  chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle(title);
	    chooser.setFileSelectionMode(mode);
	    
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
  }
  
  //public void addActionListener(ActionListener al) {
  //  go.addActionListener(al);
  //}
  
  protected File _selectFile() {
    //int result;
        
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
      System.out.println("getCurrentDirectory(): " +  chooser.getCurrentDirectory());
      System.out.println("getSelectedFile() : " +  chooser.getSelectedFile());
      return chooser.getSelectedFile();
      }
    else {
      System.out.println("No Selection ");
      return null;
      }
     }
  
  public File selectFile() {
  		final UI_GeneralView frame = new UI_GeneralView();
	    //UI_FileChooser panel = new UI_FileChooser(selectionMode);
	   
	    frame.getContentPane().add(this,"Center");
	    frame.setSize(this.getPreferredSize());
	    //frame.setVisible(true);
	    File file = this._selectFile();
	    frame.close();
		return file;		
  }
   
  public Dimension getPreferredSize(){
    return new Dimension(200, 200);
  }   
  
  /*
  public static File createChooser() {
	  return  _createFileChooser(JFileChooser.FILES_AND_DIRECTORIES);
  }
  
  public static File createFileChooser() {
	  return  _createFileChooser(JFileChooser.FILES_ONLY);
  }
  
  public static File createFolderChooser() {
	 return  _createFileChooser(JFileChooser.DIRECTORIES_ONLY);
  }
   
  protected static File _createFileChooser(int selectionMode) {
	final JFrame frame = new JFrame("");
    UI_FileChooser panel = new UI_FileChooser(selectionMode);
   
    frame.getContentPane().add(panel,"Center");
    frame.setSize(panel.getPreferredSize());
    //frame.setVisible(true);
    File file = panel.selectFile();
    UI_GeneralView.closeView(frame);
	return file;
  }
  */
  
  public static void main(String[] args) {
		createAndShow();
	}
	
	public static void createAndShow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI_FileChooser frame = new UI_FileChooser();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}