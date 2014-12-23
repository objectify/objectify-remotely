# Use A Remote App Engine Datastore

This library allows your GAE/Java application to selectively execute datastore operations against a remote service.
At the moment this only works against Appscale applications or other remote API service providers which return keys
that match the appid of the calling application.

Despite the name, this library does not depend on Objectify in any way. It is, however, convenient to use with
Objectify.

## Code

https://github.com/stickfigure/objectify-remotely

## Design

When you install the remote API, all GAE service calls go to the remote server. Objectify-remotely limits this
to only datastore operations; used in conjunction with Objectify (or other tools), your application can continue
to use local memcache and other services while shunting only datastore operations to the remote service.

## Usage

### Include the jar

```xml
	<dependencies>
		<dependency>
			<groupId>com.googlecode.objectify</groupId>
			<artifactId>objectify-remotely</artifactId>
			<version>${objectify-remotely.version}</version>
		</dependency>
	</dependencies>
```

### Create the Remotely

You will need to implement the callback that Remotely will use to determine whether calls should be made remotely.

```java
	RemoteApiOptions options = new RemoteApiOptions()
			.server("remote.example.com", 443)
			.credentials("user", "password");

	RemoteCheck check = new RemoteCheck() {
		public boolean isRemote(String namespace) {
			return namespace.equals("remotestuffnamespace");
		}
	};

	Remotely remotely = new Remotely(options, check);
```

If you are using the low level API directly, wrap your `AsyncDatastoreService`. Note that each instance of
the `AsyncDatastoreService` is permanently either remote or not; you should create and intercept new `AsyncDatastoreService`
instances for every action.

```java
	AsyncDatastoreService raw = DatastoreServiceFactory.getAsyncDatastoreService();
	AsyncDatastoreService myService = remotely.intercept(raw);
	// myService will be a remote service or a local service depending on the check
```

If you are using Objectify, subclass `ObjectifyFactory` and override this method:

```java
	@Override
	protected AsyncDatastoreService createRawAsyncDatastoreService(DatastoreServiceConfig cfg) {
		AsyncDatastoreService raw = super.createRawAsyncDatastoreService(cfg);
		return remotely.intercept(raw);
	}
```

## More

If you have questions, ask on the Objectify Google Group:

http://groups.google.com/group/objectify-appengine

## License

Released under the MIT License.

## Thanks

Huge thanks to BetterCloud (http://www.bettercloud.com/) for funding this project!
