package net.jsar.multigdrive.view;

import java.awt.Desktop;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import net.jsar.multigdrive.model.Account;
import net.jsar.multigdrive.mvc.ViewManager;

public class Menu_Tray_Account extends Menu_General {
	
	public Menu_Tray_Account(Account account) {
		super();
		createAccountMenu(account);
	}
	
	public void createAccountMenu(final Account acc) {
		//Menu accMenu = new Menu(acc.getAccountId());
		this.setLabel(acc.getAccountId());
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
		add(openFolder);
		
		MenuItem sync = new MenuItem("Sync");
		sync.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.INSTANCE.notifySyncAccount(acc);
			}
		});
		add(sync);
		
		MenuItem update = new MenuItem("Update");
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.INSTANCE.notifyUpdateAccount(acc);
			}
		});
		add(update);
		
		MenuItem logout = new MenuItem("Logout");
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewManager.INSTANCE.notifyLogoutAccount(acc);	
			}
		});
		add(logout);
		
		return;
	}
	
}
