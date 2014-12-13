package net.jsar.multigdrive.mvc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mortbay.log.Log;

import com.google.api.client.json.CustomizeJsonParser;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.api.client.util.ArrayMap;

import net.jsar.multigdrive.model.Account;
import net.jsar.multigdrive.model.Context;
import net.jsar.multigdrive.model.Service;
import net.jsar.multigdrive.model.config.ConfigApp;
import net.jsar.multigdrive.util.Utils;

public class ModelManager {
	public static ModelManager INSTANCE = null;
	
	public Map<String, Account> accounts = new HashMap<String, Account>();
	public Map<Account, Service> services = new HashMap<Account, Service>();

	protected Context ctx = null;

	public ModelManager(Context ctx) {
		INSTANCE = this;
		this.ctx = ctx;
	}

	public List<Account> getAccountList() {
		return new ArrayList<Account>(accounts.values());
	}
		
	/*
	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public Map<Account, Service> getServices() {
		return services;
	}
	*/
	
	public boolean saveAccounts(java.io.File file) {
		try { _saveAccounts(file); return true; } catch (Exception e) { e.printStackTrace();}
		ConfigApp.getAccountsFile().delete();
		return false;
	}
	
	public List<Account> loadAccounts(java.io.File file) {
		try { return _loadAccounts(file); } catch (Exception e) { e.printStackTrace();}
		ConfigApp.getAccountsFile().delete();
		return null;
	}
	
	public void _saveAccounts(java.io.File file) throws Exception {
		FileOutputStream accountData =  new FileOutputStream(file);    
	    JsonGenerator jGenerator = Context.JSON_FACTORY.createJsonGenerator(accountData, Charset.forName("UTF-8"));
	    
	    jGenerator.writeStartArray();
	    //account.toJson(jGenerator);
	    for (Entry<String, Account> acc : accounts.entrySet()) {
	    	Account.toJson(acc.getValue(), jGenerator);
	    	Log.debug("Saved account '"+acc.getValue().getAccountId()+"'");
	    }
	    jGenerator.writeEndArray();
		jGenerator.close();
		Log.debug("Saved "+accounts.size()+" account(s)");
	}
	
	public List<Account> _loadAccounts(java.io.File file) throws Exception {
		FileInputStream accountData = new FileInputStream(file);
		JsonParser jParser = Context.JSON_FACTORY.createJsonParser(accountData);
		List<Account> list = new ArrayList<Account>();
				
		List<ArrayMap> accs = new ArrayList<ArrayMap>();
		
		//accs = (List<AccountL>) jParser.parseArray(accs.getClass(), AccountL.class);
		accs = jParser.parse(accs.getClass());
		jParser.close();
		for (int i=0; i<accs.size(); i++) {
			list.add(Account.fromJson(accs.get(i)));
			//ArrayMap accL = (ArrayMap) accs.get(i);
			//Account acc = new Account((String)accL.get("accountId"));
			//acc.setCredentialsFolder(new java.io.File((String)accL.get("credentialsFolder")));
			//list.add(acc);
		}
		
		for (Account acc : list) {
			if (!ctx.controller.configureAccount(acc)) {
				ViewManager.showAlert("Cannot configure the account '"+acc.getAccountId()+"'");
				continue;
			}			
			ctx.controller.startUpdateAccountFiles(acc);			
			addAccount(acc);
			
			Log.debug("Loaded account '"+acc.getAccountId()+"'");
		}
		Log.debug("Loaded "+list.size()+" account(s)");
		return list;
	}	

	public Account findAccount(String accountId) {
		for (Entry<String, Account> account : accounts.entrySet()) {
			if (account.getKey().equals(accountId)) return account.getValue();	    	
	    }
		return null;
	}

	public void addAccount(Account acc) {
		services.put(acc, acc.getDriveService());
		accounts.put(acc.getAccountId(), acc);	
	}
	
	public void removeAccount(Account acc) {
		accounts.remove(acc.getAccountId());
		services.remove(acc);
		Utils.delete(acc.getCredentialsFolder());
	}
}
