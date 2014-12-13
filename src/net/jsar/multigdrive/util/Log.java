package net.jsar.multigdrive.util;

public class Log {
	public static void info(String sms) {	System.out.println("[INFO] "+sms);	}
	public static void debug(String sms) {	System.out.println("[DEBUG] "+sms);	}
	public static void error(String sms) {	System.out.println("[ERROR] "+sms);	}
	public static void warn(String sms) {	System.out.println("[WARN] "+sms);	}
	
	public static void log(String sms) {	info(sms);	}
	public static void log(Exception ex) {	ex.printStackTrace(System.out);	}
}
