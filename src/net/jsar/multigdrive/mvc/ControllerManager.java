package net.jsar.multigdrive.mvc;

import java.awt.Container;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;

import net.jsar.multigdrive.api.GDriveAPI;
import net.jsar.multigdrive.model.Account;
import net.jsar.multigdrive.model.Context;
import net.jsar.multigdrive.model.Service;
import net.jsar.multigdrive.model.config.ConfigApp;
import net.jsar.multigdrive.mvc.TaskThread.TaskProcessor;
import net.jsar.multigdrive.util.Log;
import net.jsar.multigdrive.util.Utils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.User;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;

public class ControllerManager {
	public static ControllerManager INSTANCE = null;
	protected Context ctx = null;
	protected TaskManager taskManager = new TaskManager();
	
	public ControllerManager(Context ctx) {
		INSTANCE = this;
		this.ctx = ctx;
	}
	
	public void start() {
		if (ConfigApp.getAccountsFile().exists()) { 			
			if (ctx.model.loadAccounts(ConfigApp.getAccountsFile()) == null) {
				ViewManager.showAlert("Cannot load accounts file.");				
			}
		}
		
		//if (!ctx.view.createTrayIcon()) {
		if (!ctx.view.reloadTrayIcon(ctx.model.getAccountList())) {
			exitApp(1);
		}
	}
	
	public boolean updateAccountFiles(Account acc) {
		String accountId = acc.getAccountId();
		
		// get all files
		List<File> files = acc.getDriveService().getFiles();
		if (files == null) {
			ViewManager.showAlert("Cannot retrieve files of account '" + accountId + "'");
			return false;
		}
		acc.setFileList(GDriveAPI.filterFiles(files));
				
		return true;
	}
	
	protected String syncFile(Account acc, File file) {	
		String localPath = "";
		String mime = file.getMimeType();
		String fileName = file.getOriginalFilename();
		
		Log.debug("File '"+file.getTitle()+"'["+mime+"] processing...");
		
		if (file.getLabels().getTrashed()) {
			Log.warn("File '"+file.getTitle()+"' is trashed. Skipping");
			return null;
		}
		
		About userInfo = acc.getUserInfo();
		if (userInfo == null) {
			Log.warn("The account '"+acc.getAccountId()+"' does not have userInfo");
			return null;
		}
		
		boolean isMine = false;
		for (User user : file.getOwners()) {
			if (user.getEmailAddress().equals(userInfo.getUser().getEmailAddress())
					&& user.getPermissionId().equals(userInfo.getUser().getPermissionId()))
			{
				isMine = true;
			}
		}
		if (!isMine) {
			Log.warn("File '"+file.getTitle()+"' does not belong to you. Skipping");
			return null;
		}
		//userInfo.getName();
		//userInfo.getRootFolderId();
		//userInfo.getUser().getIsAuthenticatedUser();			
		
		if (fileName == null && !mime.equals("application/vnd.google-apps.folder")) {
			Log.warn("File '"+file.getTitle()+"' is not a physical one. Skipping");
			return null;
		}			
		if (!file.getKind().equals("drive#file")) {
			Log.warn("File '"+file.getTitle()+"' is not a physical one. Skipping");
			return null;
		}			
		DateTime modified = file.getModifiedDate();
		
		// Parents
		for (ParentReference rf : file.getParents()) {			
			
			File parent = acc.getDriveService().getFileById(rf.getId());
			if (!parent.getMimeType().equals("application/vnd.google-apps.folder")) continue;
						
			if (rf.getIsRoot()) {
				localPath += acc.getLocalFolder().toString();
			}
			else {
				String parentPath = syncFile(acc,parent);
				if (parentPath == null) return null;
				localPath += java.io.File.separator+parentPath;				
			}
			break; // Solo uno por el momento
		}
		
		if (mime.equals("application/vnd.google-apps.folder")) {
			localPath += java.io.File.separator+file.getTitle();	
			Utils.mkdir(new java.io.File(localPath));
		}
		else {
			
			//if (fileName == null) {
			//	Log.warn("File '"+file.getTitle()+"' is not a physical one. Skipping");
			//	return null;
			//}
			//else {
				// Crear ruta
				//Utils.mkdir(new java.io.File(localPath));
				
				// Bajar el fichero				
				localPath += java.io.File.separator+file.getOriginalFilename();
				
				java.io.File fo = new java.io.File(localPath);				
				if (fo.isFile() && fo.exists()) {
					if (file.getModifiedDate().getValue() > fo.lastModified()) {
						Log.debug("DOWNLOADING FILE: "+localPath);
						try {
							OutputStream outputStream = new FileOutputStream(new java.io.File(localPath));
							IOUtils.copy(acc.getDriveService().downloadFile(file), outputStream);
							outputStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}		
					}
					else if (file.getModifiedDate().getValue() < fo.lastModified()) {
						//acc.getDriveService().update(file.getId(), file, localPath);
					}
					
				}
				
				
						
			//}
			
			//Log.debug("File '"+file.getTitle()+"' with unknown mimeType: "+mime);
			//continue;
		}		
		
		Log.debug("File '"+file.getTitle()+"' processed.");
		return localPath;
	}
	
	public boolean syncronizeAccount(Account acc) {
		int max = 500;
		String accountId = acc.getAccountId();
		if (!acc.isValid()) return false;
		
		Map<String,File> files = acc.getFileList();		
		for (File file : files.values()) {
			if (--max <= 0) break;
			//Log.debug(acc.getService().file2String(file));
			//application/vnd.google-apps.folder
			String localPath = syncFile(acc,file);
			if (localPath == null) {
				Log.error("File '"+file.getTitle()+"' cannot be synced.");
				continue;
			}
			Log.debug("File '"+file.getTitle()+"' synced!");
		}
				
		return true;
	}
	
	
	
	public boolean configureAccount(Account acc) {
		String accountId = acc.getAccountId();
		try {
			java.io.File file = ConfigApp.getCredentialFolder(accountId);			
			
			acc.setCredentialsFolder(ConfigApp.getCredentialFolder(accountId));
			acc.setDataStoreFactory(new FileDataStoreFactory(acc.getCredentialsFolder()));		
			acc.setCredential(GDriveAPI.authorize(acc.getDataStoreFactory()));	
			
		} catch (Exception ex) {
			Log.log(ex);
			return false;
		}
		if (acc.getCredential() == null) {
			ViewManager.showAlert("Cannot create credentials for account '" + accountId + "'");			
			return false;			
		}
		
		
		// Create a new authorized API client
		acc.setDriveService(GDriveAPI.createDriveService(acc));
		if (acc.getDriveService() == null) {
			ViewManager.showAlert("Cannot create service for account '" + accountId + "'");
			return false;
		}
			
		acc.setUserInfo(acc.getDriveService().getUserInfo());
		
		/*
		try {
			Log.debug("userinfo: "+acc.getDriveService().getUserInfo().toPrettyString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//https://www.googleapis.com/oauth2/v1/userinfo?alt=json
		//String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+acc.getCredential().getAccessToken();
		//String response = Utils.httpGet(url);
		//Log.debug("response: "+response);
		//
		//Oauth2 oauth2 = new Oauth2.Builder(Context.HTTP_TRANSPORT, Context.JSON_FACTORY, acc.getCredential()).setApplicationName(
		//          ConfigApp.APPLICATION_NAME).build();
		//Tokeninfo tokeninfo = oauth2.tokeninfo().setAccessToken(acc.getCredential().getAccessToken()).execute();
		// om.google.api.services.oauth2.Oauth2.Tokeninfo tokeninfo = new com.google.api.services.oauth2.Oauth2.Tokeninfo();
		//tokeninfo.setAccessToken(acc.getCredential().getAccessToken()).execute();
		//Oauth2.T
		//
		return true;
	}
	
	public Account loginAccount(String accountId, String localFolder) {

		if (ctx.model.findAccount(accountId) != null) {
			//Log.error("An account name '" + accountId + "' already exists");
			ViewManager.showAlert("An account name '" + accountId + "' already exists");
			return null;
		}
		
		Account newAccount = new Account(accountId);
		newAccount.setLocalFolder(new java.io.File(localFolder));
		
		//if (!updateAccount(newAccount)) return null;	
		if (!configureAccount(newAccount)) {
			ViewManager.showAlert("Cannot configure the account '"+newAccount.getAccountId()+"'");
			return null;	
		}
		startUpdateAccountFiles(newAccount);		
		ctx.model.addAccount(newAccount);
		
		if (!ctx.model.saveAccounts(ConfigApp.getAccountsFile())) {
			ViewManager.showAlert("Cannot save accounts file");
		}
				
		if (!ctx.view.reloadTrayIcon(ctx.model.getAccountList())) {			
			exitApp(1);
		}
		return newAccount;
	}
	
	public void logoutAccount(Account acc) {
		ctx.model.removeAccount(acc);
		
		if (!ctx.model.saveAccounts(ConfigApp.getAccountsFile())) {
			ViewManager.showAlert("Cannot save accounts file");			
		}
		
		if (!ctx.view.reloadTrayIcon(ctx.model.getAccountList())) {			
			exitApp(1);
		}
	}

	public static void exitApp(int code) {
		System.exit(code);
	}

	public void startUpdateAccountFiles(final Account account) {
		Thread th = taskManager.createTask("updateAccount", new TaskProcessor() {			
			@Override
			public void onStart() {
				Log.debug("Updating files of account '"+account.getAccountId()+"'");
			}
			
			@Override
			public boolean onProcess() {	
				account.setValid(false);
				if (!updateAccountFiles(account)) {
					Log.error("Cannot update the account files");
					return false;		
				}					
				account.setValid(true);
				return true;
			}

			@Override
			public void onEnd() {				
				Log.debug("Updated files of account '"+account.getAccountId()+"'");				
			}						
		});
		
		th.start();		
	}
	
	public void startSyncronizeAccount(final Account account) {
		Thread th = taskManager.createTask("syncronizeAccount", new TaskProcessor() {			
			@Override
			public void onStart() {
				Log.debug("Syncronizing files of account '"+account.getAccountId()+"'");
			}
			
			@Override
			public boolean onProcess() {	
				//account.setValid(false);
				if (!syncronizeAccount(account)) {
					Log.error("Cannot syncronize the account files");
					return false;		
				}					
				//account.setValid(true);
				return true;
			}

			@Override
			public void onEnd() {				
				Log.debug("Syncronized files of account '"+account.getAccountId()+"'");				
			}						
		});
		
		th.start();		
	}

	
}
