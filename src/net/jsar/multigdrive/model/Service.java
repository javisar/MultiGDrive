package net.jsar.multigdrive.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jsar.multigdrive.api.GDriveAPI;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Changes;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.Channel;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

public class Service {
	protected Drive service = null;

	public Service(Drive service) {
		this.service = service;
	}
	
	public About getUserInfo() {
		try {
			return GDriveAPI.userInfo(service);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public File insert(String filePath, String name, String description, String mime) throws Exception {		
		try {
			return GDriveAPI.insertFile(service, filePath, name, description, mime);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public File update(String fileId, File file, String localFile) {
		try {
			return GDriveAPI.updateFile(service, fileId, file, localFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<File> getFiles() {
		try {
			return GDriveAPI.retrieveAllFiles(service);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public InputStream downloadFile(File file) {
		try {
			return GDriveAPI.downloadFile(service, file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Channel watch(String fileId, String channelId, String channelType, String channelAddress) {
		try {
			return GDriveAPI.watchFile(service, fileId, channelId, channelType, channelAddress);		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Change> getChanges(Long startChangeId) {		
		try {
			return GDriveAPI.retrieveAllChanges(service, startChangeId);		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public File getFileById(String fileId) {
		try {
			return GDriveAPI.getFileById(service, fileId);		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String file2String(File file) {
		String out = "{ ";
		out += file.getCreatedDate()+", ";
		out += file.getModifiedDate()+", ";
		out += file.getTitle()+", ";
		out += file.getMimeType()+", "; //"application/vnd.google-apps.spreadsheet" "application/pdf"
		out += file.getKind()+", ";
		
		out += "parents: ";
		for (ParentReference pr : file.getParents()) {
			File parent = getFileById(pr.getId());
			out += file2String(parent);
		}
		out += ", ";

		out += file.getFileExtension()+", ";
		out += file.getOriginalFilename();
		return out+" }";
	}
}
