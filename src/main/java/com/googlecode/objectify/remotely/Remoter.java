package com.googlecode.objectify.remotely;

import com.google.appengine.tools.remoteapi.CachingRemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import lombok.Data;
import lombok.extern.java.Log;
import java.util.concurrent.Callable;

/**
 * Works like the RemoteApiInstaller, but caches the ApiProxy in a thread local so that each installation does not
 * require reauthentication. With this, you authenticate at most once per thread.
 */
@Log
@Data
public class Remoter {
	/** */
	private final RemoteApiOptions options;

	public <T> T execute(Callable<T> work) throws Exception {
		CachingRemoteApiInstaller installer = new CachingRemoteApiInstaller();
		installer.install(options);

		try {
			return work.call();
		} finally {
			installer.uninstall();
		}
	}
}
