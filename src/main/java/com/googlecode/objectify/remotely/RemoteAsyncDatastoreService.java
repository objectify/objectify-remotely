package com.googlecode.objectify.remotely;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import lombok.extern.java.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Future;

/**
 */
@Log
public class RemoteAsyncDatastoreService implements InvocationHandler {

	/** */
	public static AsyncDatastoreService create(AsyncDatastoreService raw) {
		return (AsyncDatastoreService)Proxy.newProxyInstance(
				AsyncDatastoreService.class.getClassLoader(),
				new Class[] { AsyncDatastoreService.class },
				new RemoteAsyncDatastoreService(raw));
	}

	/** */
	private final AsyncDatastoreService raw;

	private RemoteAsyncDatastoreService(AsyncDatastoreService raw) {
		this.raw = raw;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Remotely.isEnabled()) {
			RemoteApiInstaller installer = new RemoteApiInstaller();
			installer.install(Remotely.getOptions());
			try {
				Object result = method.invoke(raw, args);

				// It is almost certainly the case that Future objects need to be materialized
				// before the remote api is uninstalled. If not, we can comment out this behavior.
				if (result instanceof Future<?>)
					((Future<?>)result).get();

				return result;
			} finally {
				installer.uninstall();
			}
		} else {
			return method.invoke(raw, args);
		}
	}
}
