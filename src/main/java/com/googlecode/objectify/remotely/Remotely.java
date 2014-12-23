package com.googlecode.objectify.remotely;


import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

/**
 * Manages the logic of creating a remote datastore service or a local one.
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class Remotely
{
	/** */
	private final Remoter remoter;
	private final RemoteCheck check;

	public Remotely(RemoteApiOptions remoteApiOptions, RemoteCheck check) {
		this.remoter = new Remoter(remoteApiOptions);
		this.check = check;
	}

	/** @return true if we should use the remote api right now */
	private boolean isEnabled() {
		return check.isRemote(NamespaceManager.get());
	}

	public AsyncDatastoreService intercept(AsyncDatastoreService raw) {
		if (isEnabled())
			return (AsyncDatastoreService)RemoteWrapper.create(raw, remoter);
		else
			return raw;
	}
}
