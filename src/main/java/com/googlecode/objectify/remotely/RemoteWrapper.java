package com.googlecode.objectify.remotely;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Dynamic proxy that covers a wide variety of things in the possible chain of execution for a datastore call.
 * You'll typically start this by creating a remote wrapper of the AsyncDatastoreService.
 */
@Log
@RequiredArgsConstructor
public class RemoteWrapper implements InvocationHandler {

	/** */
	public static Object create(Object raw) {
		return Proxy.newProxyInstance(
				AsyncDatastoreService.class.getClassLoader(),
				new Class[] {
						Iterator.class,
						Iterable.class,
						QueryResultIterator.class,
						QueryResultIterable.class,
						List.class,
						PreparedQuery.class,
						AsyncDatastoreService.class
				},
				new RemoteWrapper(raw));
	}

	/** */
	private final Object raw;

	@Override
	public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
		if (Remotely.isEnabled()) {
			return Remotely.remoter.execute(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					Object result = method.invoke(raw, args);

					// It is almost certainly the case that Future objects need to be materialized
					// before the remote api is uninstalled. If not, we can comment out this behavior.
					if (result instanceof Future<?>) {
						return new Now<Object>(((Future<?>)result).get());
					}
					else if (result instanceof PreparedQuery
							|| result instanceof Iterable<?>
							|| result instanceof Iterator<?>) {
						return RemoteWrapper.create(result);
					} else {
						return result;
					}
				}
			});
		} else {
			return method.invoke(raw, args);
		}
	}
}
