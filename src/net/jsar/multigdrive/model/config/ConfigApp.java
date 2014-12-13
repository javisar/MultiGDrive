package net.jsar.multigdrive.model.config;

import java.io.File;
import java.io.FilePermission;
import java.net.SocketPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyPermission;

public class ConfigApp {

	public static java.io.File getCredentialFolder(String accountId) {
		return new java.io.File(
				System.getProperty("user.home"),
				".multigdrive/credentials/" + accountId+ "_credentials");
	}

	public static java.io.File getAccountsFile() {
		return new java.io.File(
				System.getProperty("user.home"),
				".multigdrive/accounts_list");
	}

	public static final String CLIENT_SECRETS_FILE = "/client_secrets.json";
	public static final String APPLICATION_NAME = "MultiGDrive";

	/*
	public static final List<String> OAUTH_SCOPES = Arrays.asList(
		      "https://www.googleapis.com/auth/userinfo.profile",
		      "https://www.googleapis.com/auth/userinfo.email");
	 */
}
