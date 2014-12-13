package net.jsar.multigdrive.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jsar.multigdrive.api.GDriveAPI;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;

public class Account implements Serializable {
	protected String accountId = null;
	protected java.io.File credentialsFolder = null;
	protected boolean valid = false;
	public boolean isValid() { return valid; }
	public void setValid(boolean valid) { this.valid = valid; }
	
	//protected List<File> fileList = null;
	protected Map<String, File> fileList = null;	
	protected Credential credential = null;
	protected Service service = null;
	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to
	 * make it a single globally shared instance across your application.
	 */
	protected FileDataStoreFactory dataStoreFactory = null;
	protected java.io.File localFolder = null;
	private About userInfo;
	

	public Account() {
		
	}
	
	public Account(String accountId) {
		this.accountId = accountId;
	}
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Map<String, File> getFileList() {
		return fileList;
	}

	public void setFileList(Map<String, File> fileList) {
		this.fileList = fileList;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public Service getDriveService() {
		return service;
	}

	public void setDriveService(Service service) {
		this.service = service;
	}

	
	// https://developers.google.com/drive/v2/reference/files/list
	// https://developers.google.com/drive/web/folder
	// https://developers.google.com/drive/v2/reference/files
	public static JsonGenerator toJson(Account acc, JsonGenerator jGenerator) throws IOException {
		jGenerator.writeStartObject();

		jGenerator.writeFieldName("accountId");
		jGenerator.writeString(acc.accountId);
		
		jGenerator.writeFieldName("credentialsFolder");
		jGenerator.writeString(acc.credentialsFolder.toString());
		
		jGenerator.writeFieldName("localFolder");
		jGenerator.writeString(acc.localFolder.toString());
		/*
		jGenerator.writeFieldName("files");
		jGenerator.writeStartArray();
		Iterator<File> it = fileList.iterator();
		while (it.hasNext()) {
			File file = it.next();
			if (file.getLabels().getTrashed())
				continue;
			// if (file.getKind().equals("drive#file")) continue;
			// if
			// (!file.getMimeType().equals("application/vnd.google-apps.folder"))
			// continue;
			if (!file.getUserPermission().getRole().equals("owner"))
				continue;

			
			//jGenerator.writeStartObject(); jGenerator.writeFieldName("id");
			//jGenerator.writeString(file.getId());
			//jGenerator.writeFieldName("title");
			//jGenerator.writeString(file.getTitle());
			//jGenerator.writeEndObject();
			
			jGenerator.serialize(file);
		}
		jGenerator.writeEndArray();
		*/

		jGenerator.writeEndObject();
		return jGenerator;

	}

	public static Account fromJson(ArrayMap map) throws IOException {
		
		Account acc = new Account((String)map.get("accountId"));
		acc.setCredentialsFolder(new java.io.File((String)map.get("credentialsFolder")));
		acc.setLocalFolder(new java.io.File((String)map.get("localFolder")));
		return acc;

	}
	
	public FileDataStoreFactory getDataStoreFactory() {
		return dataStoreFactory;
	}

	public void setDataStoreFactory(FileDataStoreFactory dataStoreFactory) {
		this.dataStoreFactory = dataStoreFactory;
	}

	public java.io.File getCredentialsFolder() {
		return credentialsFolder;
	}

	public void setCredentialsFolder(java.io.File credentialsFile) {
		this.credentialsFolder = credentialsFile;
	}
	/*
	public static class AccountT {
		public String accountId;
		public String credentialsFolder;
		
		public AccountT() {
			
		}
	}
	*/
	public java.io.File getLocalFolder() {
		return this.localFolder ;
	}
	
	public void setLocalFolder(java.io.File localFolder) {
		this.localFolder = localFolder;
	}
	
	public void setUserInfo(About userInfo) {
		this.userInfo = userInfo;
		
	}
	public About getUserInfo() {
		return this.userInfo;
	}
}