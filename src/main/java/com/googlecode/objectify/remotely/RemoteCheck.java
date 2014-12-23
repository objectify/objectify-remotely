package com.googlecode.objectify.remotely;

/**
 * Callback which remotely uses to determine whether a call should be remote or not
 */
public interface RemoteCheck {

	/** For now just checks namespace */
	boolean isRemote(String namespace);
}
