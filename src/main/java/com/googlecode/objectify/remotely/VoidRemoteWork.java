package com.googlecode.objectify.remotely;


/**
 * Using RemoteWork<Void> is annoying because you must return a value from the run() method.  Using
 * VoidRemoteWork eliminates that annoyance.  Unfortunately we can't override the return value of
 * a method so we must rename run() to vrun().
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
abstract public class VoidRemoteWork implements RemoteWork<Void>
{
	public final Void run() {
		vrun();
		return null;
	}
	
	public abstract void vrun();
}
