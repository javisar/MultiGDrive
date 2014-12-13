package net.jsar.multigdrive.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.jsar.multigdrive.mvc.ViewManager;

public class UI_GeneralView extends JFrame {
	//protected static Container instance = null;
	//protected static ViewManager viewManager;
	
	public UI_GeneralView() {
		
	}
	
	//public void setViewManager(ViewManager viewManager) {
	//	this.viewManager = viewManager;
	//}
	/*
	public UI_GeneralView(ViewManager viewManager) {
		//instance = this;
		this.viewManager = viewManager;
	}
	*/
	//public Container getInstance() {
	//	return instance;
	//}
	
	public void showAlert(String sms) {
		JOptionPane.showMessageDialog(this, sms);
	}
	/*
	public static Container createView(Class clazz) {
		Container newView = null;
		try {
			//newView = (Container) clazz.newInstance();
			newView = (Container) clazz.getConstructor(ViewManager.class).newInstance(viewManager);
		} catch (Exception e) {
			e.printStackTrace();
		}			
		
		return newView;
	}
	
	public static void destroyView(Container view) {
		view.dispatchEvent(new WindowEvent((Window) view, WindowEvent.WINDOW_CLOSING));		
	}
	*/	
	
	public void close() {
		this.dispatchEvent(new WindowEvent((Window) this, WindowEvent.WINDOW_CLOSING));		
	}
	
	/*
	public static Container createView(Class clazz, ViewManager viewManager) {
		Container newView = null;
		try {
			//if (clazz.getSuperclass().equals(UI_GeneralView.class)) {
			//	Method mOpen = clazz.getMethod("open", Class.class);
			//	return (Container) mOpen.invoke(null, clazz);
			//}
			//else {
				newView = (Container) clazz.getConstructor(ViewManager.class).newInstance(viewManager);
			//}
			//new UI_LoginAccount();
		} catch (Exception e) {
			e.printStackTrace();
		}			
				
		return newView;
	}
	*/
	
	public static void main(String[] args) {
		createAndShow();
	}
	
	public static void createAndShow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI_GeneralView frame = new UI_GeneralView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
