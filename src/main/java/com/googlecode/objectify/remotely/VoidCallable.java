package com.googlecode.objectify.remotely;


import java.util.concurrent.Callable;

/**
 * Using Callable<Void> is annoying because you must return a value from the run() method.  Using
 * VoidCallable eliminates that annoyance.
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
abstract public class VoidCallable implements Callable<Void>
{
	@Override
	public final Void call() throws Exception {
		run();
		return null;
	}
	
	public abstract void run() throws Exception;
}
