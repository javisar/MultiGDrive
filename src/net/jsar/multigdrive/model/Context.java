package net.jsar.multigdrive.model;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import net.jsar.multigdrive.mvc.ControllerManager;
import net.jsar.multigdrive.mvc.ModelManager;
import net.jsar.multigdrive.mvc.ViewManager;

public class Context {	
	public ControllerManager controller = null;
	public ViewManager view = null;
	public ModelManager model = null;
	
	public Context() {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			//HTTP_TRANSPORT = new NetHttpTransport();
		} catch (Exception e) {
			e.printStackTrace();
			ControllerManager.exitApp(1);
		}
	}
	/** Global instance of the HTTP transport. */
	public static HttpTransport HTTP_TRANSPORT = null;

	/** Global instance of the JSON factory. */
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	// private static String CLIENT_ID =
		// "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		// private static String CLIENT_SECRET = "xxxxxxxxxxxxxxxxxxxxxx";
		// private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

		/** Directory to store user credentials. */
		// private static final java.io.File DATA_STORE_DIR = new
		// java.io.File(System.getProperty("user.home"),
		// ".multigdrive/plus_sample");

		
		
}
