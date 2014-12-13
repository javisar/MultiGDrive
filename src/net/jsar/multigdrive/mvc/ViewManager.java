package net.jsar.multigdrive.mvc;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.jsar.multigdrive.model.Account;
import net.jsar.multigdrive.model.Context;
import net.jsar.multigdrive.util.Log;
import net.jsar.multigdrive.view.Menu_Tray;
import net.jsar.multigdrive.view.UI_GeneralView;
import net.jsar.multigdrive.view.UI_LoginAccount;

public class ViewManager {
	
	public static ViewManager INSTANCE = null;
	
	protected Context ctx = null;
	protected List<Container> views = new ArrayList<Container>();
	protected TrayIcon trayIcon = null;	
	protected JFrame mainFrame = null;
	
	public ViewManager(Context ctx) {
		INSTANCE = this;
		this.ctx = ctx;		
	}
	
	public JFrame getMainFrame() {
		if (mainFrame != null) return mainFrame;
		mainFrame = new JFrame("MultiGDrive");
		mainFrame.setUndecorated(true);
		mainFrame.setResizable(false);
		return mainFrame;
	}
	
	public boolean reloadTrayIcon(List<Account> accounts) {
		removeTrayIcon();
		//if (!createTrayIcon(createTrayMenu(accounts))) {
		if (!createTrayIcon(new Menu_Tray(accounts))) {
			ViewManager.showAlert("Fatal error creating the SystemTray menu");		
			return false;
		}
		return true;
	}
	
	public boolean createTrayIcon(final PopupMenu trayPopupMenu) {
		// checking for support
		if (!SystemTray.isSupported()) {
			System.out.println("System tray is not supported !!! ");
			return false;
		}		
		
		//final Frame frame = new Frame("");
        //frame.setUndecorated(true);
        
        
		// get the systemTray of the system
		SystemTray systemTray = SystemTray.getSystemTray();

		// get default toolkit
		// Toolkit toolkit = Toolkit.getDefaultToolkit();
		// get image
		// Toolkit.getDefaultToolkit().getImage("src/resources/busylogo.jpg");
		Image image = Toolkit.getDefaultToolkit().getImage(
				"src/images/gdrive16x16.png");
			

		// setting tray icon
		trayIcon = new TrayIcon(image, "SystemTray", trayPopupMenu);
		// adjust to default size as per system recommendation
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    getMainFrame().add(trayPopupMenu);
                    trayPopupMenu.show(getMainFrame(), e.getXOnScreen(), e.getYOnScreen());
                }
            }
        });

		try {
            //frame.setResizable(false);
            //frame.setVisible(true);
            systemTray.add(trayIcon);
        } catch (AWTException e) {
        	e.printStackTrace();
            Log.error("TrayIcon could not be added.");
            return false;
        }
				
		return true;
	}
	
	public boolean removeTrayIcon() {
		// checking for support
		if (!SystemTray.isSupported()) {
			System.out.println("System tray is not supported !!! ");
			return false;
		}
		// get the systemTray of the system
		SystemTray systemTray = SystemTray.getSystemTray();
		
		if (trayIcon != null) systemTray.remove(trayIcon);
		return true;
	}
	
	/*
	public Container openView(Class clazz) {
		Container newView = UI_GeneralView.createView(clazz, this);
		if (newView != null) addView(newView);		
		return newView;
	}
	
	
	public void closeView(Container view) {
		removeView(view);
		UI_GeneralView.closeView(view);
	}
	*/
	
	public static void showAlert(Container frame, String sms) {
		JOptionPane.showMessageDialog(frame, sms);
	}
	public static void showAlert(String sms) {
		showAlert(new JFrame(), sms);
	}

	public void notifyLoginAccount(String accountId, String localFolder) {
		ctx.controller.loginAccount(accountId, localFolder);
	}	
	
	public void notifyUpdateAccount(Account acc) {
		ctx.controller.startUpdateAccountFiles(acc);
	}
	
	public void notifySyncAccount(Account acc) {
		ctx.controller.startSyncronizeAccount(acc);
	}
	
	public void notifyLogoutAccount(Account acc) {
		ctx.controller.logoutAccount(acc);
	}
	
	public static void createAndShowView(final Class clazz) {
		//final ViewManager vManager = this;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//UI_GeneralView frame = new UI_GeneralView();
					Component comp = (Component) clazz.newInstance();
					/*
					try {
						Method mt = clazz.getMethod("setViewManager", ViewManager.class);
						mt.invoke(comp, vManager);
					}
					catch (Exception ex) {	}					
					*/				
					comp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	

	public void closeView(Container view) {
		removeView(view);
		view.dispatchEvent(new WindowEvent((Window) view, WindowEvent.WINDOW_CLOSING));				
	}	
	
	public void addView(Container view) {
		if (view != null) views.add(view);
	}
	public void removeView(Container view) {
		if (view != null) views.remove(view);
	}

	

	/*
	public Menu createAccountMenu(Account acc) {
		Menu accMenu = new Menu(acc.getAccountId());
		
		final File accFolder = acc.getLocalFolder();
		
		MenuItem openFolder = new MenuItem("Open Folder");
		openFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(accFolder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		accMenu.add(openFolder);
		
		MenuItem update = new MenuItem("Update");
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		accMenu.add(update);
		
		MenuItem logout = new MenuItem("Logout");
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		accMenu.add(logout);
		
		return accMenu;
	}
	
	public PopupMenu createTrayMenu(List<Account> accounts) {
		// popupmenu
		PopupMenu trayPopupMenu = new PopupMenu();

		if (accounts.size() > 0) {
			for (Account acc : accounts) {
				final String accountIdF = acc.getAccountId();
				final Account accF = acc;
				
				//final MenuItem action = new MenuItem(accountIdF+"...");
				//action.addActionListener(new ActionListener() {
				//	@Override
				//	public void actionPerformed(ActionEvent e) {
						//openView(UI_LoginAccount.class);
						//showAlert("Clicked on account: "+accountId);
						//JFrame comp = new JFrame("");
						//createAccountMenu(accF).show(comp, 0, 0);
						//trayIcon.setPopupMenu(createAccountMenu(accF));
						//trayIcon.getPopupMenu().add(createAccountMenu(accF));
						//mainFrame.add();
						//mainFrame.setVisible(true);						
				//	}
				//});
				
				Menu action = createAccountMenu(accF);
				trayPopupMenu.add(action);
			}
			
			trayPopupMenu.addSeparator();
		}
		
		// 1t menuitem for popupmenu
		MenuItem action = new MenuItem("Login to new account...");
		action.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//openView(UI_LoginAccount.class);
				createAndShowView(UI_LoginAccount.class);
			}
		});
		trayPopupMenu.add(action);

		// 2nd menuitem of popupmenu
		MenuItem close = new MenuItem("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControllerManager.exitApp(0);
			}
		});
		trayPopupMenu.add(close);
		
		//CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        //CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
		return trayPopupMenu;
	}
	*/

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
}
