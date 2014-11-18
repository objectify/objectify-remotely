/*
 */

package com.googlecode.objectify.remotely.test.util;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * All tests should extend this class to set up the GAE environment.
 * @see <a href="http://code.google.com/appengine/docs/java/howto/unittesting.html">Unit Testing in Appengine</a>
 *
 * Also sets up any Mockito annotated fields.
 *
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class TestBase
{
	/** */
	private final LocalServiceTestHelper helper =
			new LocalServiceTestHelper(
					// Our tests assume strong consistency
					new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy(),
					new LocalTaskQueueTestConfig());
	/** */
	@BeforeMethod
	public void setUp() {
		this.helper.setUp();
		MockitoAnnotations.initMocks(this);
	}

	/** */
	@AfterMethod
	public void tearDown() {
		this.helper.tearDown();
	}

	/** */
	protected void runInNamespace(String namespace, Runnable runnable) {
		String oldNamespace = NamespaceManager.get();
		try {
			NamespaceManager.set(namespace);

			runnable.run();
		} finally {
			NamespaceManager.set(oldNamespace);
		}
	}
}
