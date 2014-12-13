package net.jsar.multigdrive.model.config;

public class ConfigProxy {
	public String proxyType;
	public String proxyHost;
	public String proxyPort;
	public String proxyUser;
	public String proxyPass;
	public String proxyDomain;
	
	public static void configProxy(ConfigProxy config) {
		if (config.proxyType == null) {
			ConfigProxy.configProxyEnvVars("http",config);
			ConfigProxy.configProxyEnvVars("https",config);
		}
		else {
			ConfigProxy.configProxyEnvVars(config.proxyType,config);
		}
	}
	public static void configProxyEnvVars(String proxyType, ConfigProxy config) {
		
		if (config.proxyHost != null) {
			if (config.proxyPort == null) config.proxyPort = "80";			
			System.setProperty(proxyType+".proxyHost", config.proxyHost);
			System.setProperty(proxyType+".proxyPort", config.proxyPort);
		}
		if (config.proxyUser != null && config.proxyPass != null) {
			if (config.proxyDomain == null) config.proxyDomain = "127.0.0.1";
			System.setProperty(proxyType+".proxyUser", config.proxyUser);
			System.setProperty(proxyType+".proxyPassword", config.proxyPass);			
		}
	}
}