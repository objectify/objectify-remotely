package com.googlecode.objectify.remotely;


/**
 * For executing transactions, this is a unit of work.
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public interface RemoteWork<R>
{
	R run();
}
