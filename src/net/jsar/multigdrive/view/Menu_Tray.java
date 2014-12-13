package net.jsar.multigdrive.view;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import net.jsar.multigdrive.model.Account;
import net.jsar.multigdrive.mvc.ControllerManager;
import net.jsar.multigdrive.mvc.ViewManager;

public class Menu_Tray extends Menu_General {
	
	public Menu_Tray(List<Account> accounts) {
		super();
		createTrayMenu(accounts);
	}	
	
	public void createTrayMenu(List<Account> accounts) {
		
		if (accounts.size() > 0) {
			for (Account acc : accounts) {
				final String accountIdF = acc.getAccountId();
				final Account accF = acc;
				
				Menu action = new Menu_Tray_Account(accF);
				add(action);
			}
			
			addSeparator();
		}
		
		// 1t menuitem for popupmenu
		MenuItem action = new MenuItem("Login to new account...");
		action.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//openView(UI_LoginAccount.class);
				ViewManager.createAndShowView(UI_LoginAccount.class);
			}
		});
		add(action);

		// 2nd menuitem of popupmenu
		MenuItem close = new MenuItem("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ControllerManager.exitApp(0);
			}
		});
		add(close);		
	}
}
