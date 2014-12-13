package net.jsar.multigdrive.example;

import net.jsar.multigdrive.model.Account;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.drive.Drive;

public class Examples {
	 
	  /*
public static void loginNewAccountNoUsar(String accountId) throws Exception {
	 
	 httpTransport = GoogleNetHttpTransport.newTrustedTransport();		 
	 
	
    //HttpTransport httpTransport = new NetHttpTransport();
    //JsonFactory jsonFactory = new JacksonFactory();
    
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        httpTransport, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
        .setAccessType("online")
        .setApprovalPrompt("auto").build();
    
    //Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(null);
    
    Credential credential = authorize(accountId);
    
    Account account = new Account();
    account.accountId = accountId;
    
    
    
	
    
    
    String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    System.out.println("Please open the following URL in your browser then type the authorization code:");
    System.out.println("  " + url);
    
    //Desktop.getDesktop().browse(URI.create(url));
    //createHTML(url);
    //SimpleWebBrowserExample.startBrowser(url);
    
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String code = br.readLine();
    
    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
    
    //Create a new authorized API client
    Drive.Builder builder = new Drive.Builder(httpTransport, JSON_FACTORY, credential);
    builder.setApplicationName("MultiGDrive");
    service = builder.build();

    
    //FileList fileList = list();
    
    List<File> fileList = retrieveAllFiles();
    account.fileList = fileList;
    
    java.io.File accountFileData = new java.io.File(System.getProperty("user.home"), ".multigdrive/"+accountId);
	FileOutputStream accountData =  new FileOutputStream(accountFileData);    
    JsonGenerator jGenerator = JSON_FACTORY.createJsonGenerator((OutputStream)accountData, Charset.forName("UTF-8"));
    account.toJson(jGenerator);       
	jGenerator.close();
	
    
    
    File inserted = insert("document.txt","document.txt","A test document",null);
    try {
    	Thread.sleep(60*1000);
    }
    catch (Exception ex) {
    	ex.printStackTrace();
    }
    List<Change> changes = retrieveAllChanges(null);
    for (Change change: changes) {
    	System.out.println(change.toString());
    }
    
    
  }	 	
	  */
	
	  /*
	  protected static void createHTML(final String url)
	  {
	    SwingUtilities.invokeLater(new Runnable()
	    {
	      public void run()
	      {
	        // create jeditorpane
	        JEditorPane jEditorPane = new JEditorPane();
	        
	        // make it read-only
	        jEditorPane.setEditable(false);
	        
	        // create a scrollpane; modify its attributes as desired
	        JScrollPane scrollPane = new JScrollPane(jEditorPane);
	        
	        // add an html editor kit
	        HTMLEditorKit kit = new HTMLEditorKit();
	        jEditorPane.setEditorKit(kit);
	        
	        // add some styles to the html
	        StyleSheet styleSheet = kit.getStyleSheet();
	        styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
	        styleSheet.addRule("h1 {color: blue;}");
	        styleSheet.addRule("h2 {color: #ff0000;}");
	        styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");

	        // create some simple html as a string
	        String htmlString = "<html>\n"
	                          + "<body>\n"
	                          + "<h1>Welcome!</h1>\n"
	                          + "<h2>This is an H2 header</h2>\n"
	                          + "<p>This is some sample text</p>\n"
	                          + "<p><a href=\"http://devdaily.com/blog/\">devdaily blog</a></p>\n"
	                          + "</body>\n";
	        
	        // create a document, set it on the jeditorpane, then add the html
	        //Document doc = kit.createDefaultDocument();
	        //jEditorPane.setDocument(doc);
	        //jEditorPane.setText(htmlString);
	        try {
				jEditorPane.setPage(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}

	        // now add it all to a frame
	        JFrame j = new JFrame("HtmlEditorKit Test");
	        j.getContentPane().add(scrollPane, BorderLayout.CENTER);

	        // make it easy to close the application
	        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        // display the frame
	        j.setSize(new Dimension(300,200));
	        
	        // pack it, if you prefer
	        //j.pack();
	        
	        // center the jframe, then make it visible
	        j.setLocationRelativeTo(null);
	        j.setVisible(true);
	      }
	    });
	  }
	  */

}
