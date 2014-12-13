package net.jsar.multigdrive.example;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import net.jsar.multigdrive.mvc.ControllerManager;

public class SystemTrayMenu{

	//start of main method
	public static void main(String[] args){
	    createTrayMenu();
	}//end of main
	
	public static boolean createTrayMenu() {
		//checking for support
	    if(!SystemTray.isSupported()){
	        System.out.println("System tray is not supported !!! ");
	        return false;
	    }
	    //get the systemTray of the system
	    SystemTray systemTray = SystemTray.getSystemTray();

	    //get default toolkit
	    //Toolkit toolkit = Toolkit.getDefaultToolkit();
	    //get image   Toolkit.getDefaultToolkit().getImage("src/resources/busylogo.jpg");
	    Image image = Toolkit.getDefaultToolkit().getImage("src/images/gdrive16x16.png");

	    //popupmenu
	    PopupMenu trayPopupMenu = new PopupMenu();

	    //1t menuitem for popupmenu
	    MenuItem action = new MenuItem("Login to new account...");
	    action.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            //JOptionPane.showMessageDialog(null, "Action Clicked");          
	        }
	    });     
	    trayPopupMenu.add(action);

	    //2nd menuitem of popupmenu
	    MenuItem close = new MenuItem("Close");
	    close.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	System.exit(0);
	        }
	    });
	    trayPopupMenu.add(close);

	    //setting tray icon
	    TrayIcon trayIcon = new TrayIcon(image, "SystemTray", trayPopupMenu);
	    //adjust to default size as per system recommendation 
	    trayIcon.setImageAutoSize(true);

	    try{
	        systemTray.add(trayIcon);
	    }catch(AWTException awtException){
	        awtException.printStackTrace();
	    }
	    //System.out.println("end of main");
	    return true;
	}

}