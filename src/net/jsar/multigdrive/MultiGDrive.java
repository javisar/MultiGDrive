package net.jsar.multigdrive;

import java.io.IOException;

import net.jsar.multigdrive.model.Context;
import net.jsar.multigdrive.model.config.ConfigArgs;
import net.jsar.multigdrive.model.config.ConfigProxy;
import net.jsar.multigdrive.mvc.ControllerManager;
import net.jsar.multigdrive.mvc.ModelManager;
import net.jsar.multigdrive.mvc.ViewManager;
import net.jsar.multigdrive.util.Log;
import net.jsar.multigdrive.view.UI_LoginAccount;

//https://code.google.com/p/google-api-java-client/source/browse/plus-cmdline-sample/src/main/java/com/google/api/services/samples/plus/cmdline/PlusSample.java?repo=samples
//https://console.developers.google.com/project/multi-gdrive/apiui/credential?authuser=2
//https://code.google.com/p/google-api-java-client/wiki/OAuth2
//https://developers.google.com/drive/web/quickstart/quickstart-java

public class MultiGDrive {
	
	protected static MultiGDrive MultiGDrive = null;
	
	protected ConfigArgs argsConfig = null;	
	protected Context ctx = new Context();
	

	public static void main(String[] args) throws IOException {
		
		// Configure Proxy
		//System.setProperty("https.proxyHost", "xxxxxxxxxxxxxxxx");
		//System.setProperty("https.proxyPort", "3128");
		//		
		MultiGDrive = new MultiGDrive(args);		
	}
	
	public MultiGDrive(String[] args) {
		argsConfig = ConfigArgs.parseArgs(args);
		if (!init()) {
			Log.error("Cannot init MultiGDrive");
			ControllerManager.exitApp(1);
		}
		if (argsConfig.testing) test();
	}
	
	protected boolean init() {
		//FileDataStoreFactory.setPermissionsToOwnerOnly
		//Policy.setPolicy(new MinimalPolicy());		
		ConfigProxy.configProxy(this.argsConfig.proxyConfig);
		
		ctx.model = new ModelManager(ctx);
		ctx.view = new ViewManager(ctx);
		ctx.controller = new ControllerManager(ctx);
		ctx.controller.start();
		return true;
	}
	
	
	
	protected void test() {
		//ctx.view.openView(UI_LoginAccount.class);
		//ctx.controller.loginNewAccount("javisar");
		ViewManager.createAndShowView(UI_LoginAccount.class);
	}
	
}