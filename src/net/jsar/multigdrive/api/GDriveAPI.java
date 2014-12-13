package net.jsar.multigdrive.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jsar.multigdrive.MultiGDrive;
import net.jsar.multigdrive.model.Account;
import net.jsar.multigdrive.model.Context;
import net.jsar.multigdrive.model.Service;
import net.jsar.multigdrive.model.config.ConfigApp;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Changes;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.Channel;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GDriveAPI {
	
	public static About userInfo(Drive service) throws Exception {
		About about = service.about().get().execute();
		return about;
	}

	public static File insertFile(Drive service, String filePath, String name, String description,
			String mime) throws Exception {
		// Insert a file
		File body = new File();
		if (name != null)
			body.setTitle(name);
		if (description != null)
			body.setDescription(description);
		if (mime != null)
			body.setMimeType(mime);

		java.io.File fileContent = new java.io.File(filePath);
		System.out.println(fileContent.getAbsolutePath());
		FileContent mediaContent = new FileContent(mime, fileContent);

		File file = service.files().insert(body, mediaContent).execute();
		System.out.println("File ID: " + file.getId());
		return file;
	}
	
	public static File updateFile(Drive service, String fileId, File file, String localFile) throws Exception {

	      // First retrieve the file from the API.
	      //File file = service.files().get(fileId).execute();

	      // File's new metadata.
	      //file.setTitle(newTitle);
	      //file.setDescription(newDescription);
	      //file.setMimeType(newMimeType);

	      // File's new content.
	      java.io.File fileContent = new java.io.File(localFile);
	      FileContent mediaContent = new FileContent(file.getMimeType(), fileContent);

	      // Send the request to the API.
	      File updatedFile = service.files().update(fileId, file, mediaContent).execute();

	      return updatedFile;
	   
	  }
	
	public static File getFileById(Drive service, String fileId) throws Exception {				
			File file = service.files().get(fileId).execute();
			//System.out.println("File ID: " + file.getId());
			return file;
	}
	
	/**
	   * Download a file's content.
	   *
	   * @param service Drive API service instance.
	   * @param file Drive File instance.
	   * @return InputStream containing the file's content if successful,
	   *         {@code null} otherwise.
	   */
	  public static InputStream downloadFile(Drive service, File file) {
	    if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
	      try {
	        HttpResponse resp =
	            service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
	                .execute();
	        return resp.getContent();
	      } catch (IOException e) {
	        // An error occurred.
	        e.printStackTrace();
	        return null;
	      }
	    } else {
	      // The file doesn't have any content stored on Drive.
	      return null;
	    }
	  }

	public static List<File> retrieveAllFiles(Drive service) throws IOException {
		List<File> result = new ArrayList<File>();
		Files.List request = service.files().list();

		do {
			try {
				FileList files = request.execute();

				result.addAll(files.getItems());
				request.setPageToken(files.getNextPageToken());
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null
				&& request.getPageToken().length() > 0);

		return result;
	}

	public static Channel watchFile(Drive service, String fileId, String channelId,
			String channelType, String channelAddress) {
		Channel channel = new Channel();
		channel.setId(channelId);
		channel.setType(channelType);
		channel.setAddress(channelAddress);
		try {
			return service.files().watch(fileId, channel).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Change> retrieveAllChanges(Drive service, Long startChangeId)
			throws IOException {
		List<Change> result = new ArrayList<Change>();
		Changes.List request = service.changes().list();

		if (startChangeId != null) {
			request.setStartChangeId(startChangeId);
		}
		do {
			try {
				ChangeList changes = request.execute();

				result.addAll(changes.getItems());
				request.setPageToken(changes.getNextPageToken());
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null
				&& request.getPageToken().length() > 0);

		return result;
	}

	/*
	public FileList retrieveFilelist() throws IOException {
		FileList fileList = service.files().list().execute();
		System.out.println("File list size: " + fileList.size());
		return fileList;
	}
	*/

	public static Map<String, File> filterFiles(List<File> files) {
		Map<String, File> result = new HashMap<String, File>();
		for (File file : files) {
			result.put(file.getId(), file);
		}

		return result;
	}

	public static Credential authorize(FileDataStoreFactory dataStoreFactory) throws Exception {
		
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				Context.JSON_FACTORY,
				new InputStreamReader(MultiGDrive.class
						.getResourceAsStream(ConfigApp.CLIENT_SECRETS_FILE)));
		// GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
		// clientSecrets.getDetails().setClientId(CLIENT_ID);
		// clientSecrets.getDetails().setClientSecret(CLIENT_SECRET);
	
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			
			System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=plus");
			System.out.println("into plus-cmdline-sample/src/main/resources/client_secrets.json");
			return null;
		}
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				Context.HTTP_TRANSPORT, Context.JSON_FACTORY, clientSecrets,
				Arrays.asList(DriveScopes.DRIVE)).setDataStoreFactory(
				dataStoreFactory).build();
	
		/*
		 * GoogleAuthorizationCodeFlow flow = new
		 * GoogleAuthorizationCodeFlow.Builder( httpTransport, JSON_FACTORY,
		 * CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
		 * .setAccessType("online") .setApprovalPrompt("auto").build();
		 */
		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		// return new AuthorizationCodeInstalledApp(flow, new
		// LocalServerReceiver()).authorize(accountId);
	}

	public static Service createDriveService(Account account) {
		Drive.Builder builder = new Drive.Builder(Context.HTTP_TRANSPORT, Context.JSON_FACTORY,account.getCredential());
		builder.setApplicationName(ConfigApp.APPLICATION_NAME);
		Service newService = new Service(builder.build());
		return newService;
	}

}
