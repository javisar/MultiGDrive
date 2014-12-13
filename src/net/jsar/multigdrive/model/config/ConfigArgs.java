package net.jsar.multigdrive.model.config;

public class ConfigArgs {
	public ConfigProxy proxyConfig = new ConfigProxy();
	public boolean testing;
	public static ConfigArgs parseArgs(String[] args) {
		ConfigArgs config = new ConfigArgs();
		int narg = 0;
		while (true) {
			if (narg > args.length-1) break;
			
			if (args[narg].equals("-test")) {
				config.testing = true;
			}
			else if (args[narg].equals("-proxy")) {
				config.proxyConfig.proxyType = args[++narg];
			}
			else if (args[narg].equals("-proxy.host")) {
				config.proxyConfig.proxyHost = args[++narg];
			}			
			else if (args[narg].equals("-proxy.port")) {
				config.proxyConfig.proxyPort = args[++narg];
			}
			else if (args[narg].equals("-proxy.user")) {
				config.proxyConfig.proxyUser = args[++narg];
			}
			else if (args[narg].equals("-proxy.pass")) {
				config.proxyConfig.proxyPass = args[++narg];
			}
			else if (args[narg].equals("-proxy.domain")) {
				config.proxyConfig.proxyDomain = args[++narg];
			}
			narg++;
			
		}
		return config;
	}
}