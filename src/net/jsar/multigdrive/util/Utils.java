package net.jsar.multigdrive.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.SocketPermission;
import java.net.URL;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.PropertyPermission;
import java.util.regex.Pattern;

import com.google.api.client.util.Throwables;

public class Utils {

	public static void setPermissionsToOwnerOnly(java.io.File file) throws IOException {
	    // Disable access by other users if O/S allows it and set file permissions to readable and
	    // writable by user. Use reflection since JDK 1.5 will not have these methods
	    try {	    	
	      Method setReadable = java.io.File.class.getMethod("setReadable", boolean.class, boolean.class);
	      Method setWritable = java.io.File.class.getMethod("setWritable", boolean.class, boolean.class);
	      Method setExecutable = java.io.File.class.getMethod("setExecutable", boolean.class, boolean.class);
	      if (!(Boolean) setReadable.invoke(file, false, false)
	          || !(Boolean) setWritable.invoke(file, false, false)
	          || !(Boolean) setExecutable.invoke(file, false, false)) {
	        Log.warn("unable to change permissions for everybody: " + file);
	      }
	      if (!(Boolean) setReadable.invoke(file, true, true)
	          || !(Boolean) setWritable.invoke(file, true, true)
	          || !(Boolean) setExecutable.invoke(file, true, true)) {
	    	  Log.warn("unable to change permissions for owner: " + file);
	      }
	    } catch (InvocationTargetException exception) {
	      Throwable cause = exception.getCause();
	      Throwables.propagateIfPossible(cause, IOException.class);
	      // shouldn't reach this point, but just in case...
	      throw new RuntimeException(cause);
	    } catch (NoSuchMethodException exception) {
	    	Log.warn("Unable to set permissions for " + file
	          + ", likely because you are running a version of Java prior to 1.6");
	    } catch (SecurityException exception) {
	      // ignored
	    } catch (IllegalAccessException exception) {
	      // ignored
	    } catch (IllegalArgumentException exception) {
	      // ignored
	    }
	    
	    //Log.debug("file.setReadable(false, false) = "+file.setReadable(false, false));
	    //Log.debug("file.setWritable(false, false) = "+file.setWritable(false, false));
	    //Log.debug("file.setExecutable(false, false) = "+file.setExecutable(false, false));
	    //SecurityManager
	    //Log.debug("file.setReadable(true) = "+file.canRead());
	    //Log.debug("file.setWritable(true) = "+file.canWrite());
	    //Log.debug("file.setExecutable(true) = "+file.canExecute());
	
	  }


	public class MinimalPolicy extends Policy {

	    protected PermissionCollection perms;

	    public MinimalPolicy() {
	        super();
	        if (perms == null) {
	            perms = new MyPermissionCollection();
	            addPermissions();
	        }
	    }

	    @Override
	    public PermissionCollection getPermissions(CodeSource codesource) {
	        return perms;
	    }

	    private void addPermissions() {
	    	//AllPermission all = new AllPermission();
	        SocketPermission socketPermission = new SocketPermission("*:1024-", "connect, resolve");
	        PropertyPermission propertyPermission = new PropertyPermission("*", "read, write");
	        FilePermission filePermission = new FilePermission("<<ALL FILES>>", "read, write, execute");

	        perms.add(socketPermission);
	        perms.add(propertyPermission);
	        perms.add(filePermission);
	        //perms.add(all);
	    }

	    
	}
	
	public class MyPermissionCollection extends PermissionCollection {

	    //private static final long serialVersionUID = 614300921365729272L;

	    ArrayList<Permission> perms = new ArrayList<Permission>();

	    public void add(Permission p) {
	        perms.add(p);
	    }

	    public boolean implies(Permission p) {
	        for (Iterator<Permission> i = perms.iterator(); i.hasNext();) {
	            if (((Permission) i.next()).implies(p)) {
	                return true;
	            }
	        }
	        return false;
	    }

	    public Enumeration<Permission> elements() {
	        return Collections.enumeration(perms);
	    }

	    public boolean isReadOnly() {
	        return false;
	    }

	}

	public static boolean delete(File file) {
		if (file.isFile()) return file.delete();
		
		String[]entries = file.list();
		for(String s: entries){
		    File currentFile = new File(file.getPath(),s);
		    currentFile.delete();
		}
		return file.delete();
	}
	
	public static boolean mkdir(File file) {
		if (file.isFile() || file.exists()) return false;
		return file.mkdirs();
	}
	/*
	public static boolean validateFileName(String fileName) {
	    return fileName.matches("^[^.\\\\/:*?\"<>|]?[^\\\\/:*?\"<>|]*") 
	    && getValidFileName(fileName).length()>0;
	}

	public static String getValidFileName(String fileName) {
	    String newFileName = fileName.replaceAll("^[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", "");
	    if(newFileName.length()==0)
	        throw new IllegalStateException(
	                "File Name " + fileName + " results in a empty fileName!");
	    return newFileName;
	}
	*/
	
	public static boolean isEmpty(final String str) {
		return str == null || str.length() == 0;
	}
	
	protected static final Pattern ILLEGAL_CURRENT_FOLDER_PATTERN = Pattern
			.compile("^[^/]|[^/]$|/\\.{1,2}|\\\\|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}");

	public static String sanitize(final File file) {
		if (file.isFile()) return sanitizeFileName(file.toString());
		if (file.isDirectory()) return sanitizeFolderName(file.toString());
		return file.toString();
	}
	/**
	 * Sanitizes a filename from certain chars.<br />
	 * 
	 * This method enforces the <code>forceSingleExtension</code> property and
	 * then replaces all occurrences of \, /, |, :, ?, *, &quot;, &lt;, &gt;,
	 * control chars by _ (underscore).
	 * 
	 * @param filename
	 *            a potentially 'malicious' filename
	 * @return sanitized filename
	 */
	public static String sanitizeFileName(final String filename) {

		if (Utils.isEmpty(filename))
			return filename;

		String name = filename;
		// String name = forceSingleExtension(filename);

		// Remove \ / | : ? * " < > 'Control Chars' with _
		return name.replaceAll("\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
	}

	/**
	 * Sanitizes a folder name from certain chars.<br />
	 * 
	 * This method replaces all occurrences of \, /, |, :, ?, *, &quot;, &lt;,
	 * &gt;, control chars by _ (underscore).
	 * 
	 * @param folderName
	 *            a potentially 'malicious' folder name
	 * @return sanitized folder name
	 */
	public static String sanitizeFolderName(final String folderName) {

		if (Utils.isEmpty(folderName))
			return folderName;

		// Remove . \ / | : ? * " < > 'Control Chars' with _
		return folderName.replaceAll(
				"\\.|\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
	}
	
	/*
	 * Checks whether a path complies with the FCKeditor File Browser <a href="http://docs.fckeditor.net/FCKeditor_2.x/Developers_Guide/Server_Side_Integration#File_Browser_Requests"
	 * target="_blank">rules</a>.
	 * 
	 * @param path
	 *            a potentially 'malicious' path
	 * @return <code>true</code> if path complies with the rules, else
	 *         <code>false</code>
	 */
	public static boolean isValidPath(final String path) {
		if (Utils.isEmpty(path))
			return false;

		if (ILLEGAL_CURRENT_FOLDER_PATTERN.matcher(path).find())
			return false;
		
		return true;
	}

	/**
	 * Replaces all dots in a filename with underscores except the last one.
	 * 
	 * @param filename
	 *            filename to sanitize
	 * @return string with a single dot only
	 */
	public static String forceSingleExtension(final String filename) {
		return filename.replaceAll("\\.(?![^.]+$)", "_");
	}

	/**
	 * Checks if a filename contains more than one dot.
	 * 
	 * @param filename
	 *            filename to check
	 * @return <code>true</code> if filename contains severals dots, else
	 *         <code>false</code>
	 */
	public static boolean isSingleExtension(final String filename) {
		return filename.matches("[^\\.]+\\.[^\\.]+");
	}

	//
	
	
	public static String httpGet(String urlToRead) {
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      
	      String result = "";
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	         }
	         rd.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return result;
	   }
}
