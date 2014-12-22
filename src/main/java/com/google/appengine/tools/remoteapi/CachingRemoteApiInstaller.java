package com.google.appengine.tools.remoteapi;

import java.io.IOException;

/**
 * Caches the remote api client globally so we don't perform auth every time we install. Assumes
 * there is only ever one set of credentials used.
 */
public class CachingRemoteApiInstaller extends RemoteApiInstaller {

	private static volatile RemoteApiClient client;

	@Override
	RemoteApiClient login(RemoteApiOptions options) throws IOException {
		if (client == null)
			client = super.login(options);

		return client;
	}
}
