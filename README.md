# Use A Remote App Engine Datastore

This library allows your GAE/Java application to execute datastore operations against a remote service. The remote
datastore could be another GAE application, an AppScale application, or anything else that implements the GAE remote
API.

Despite the name, this library does not depend on Objectify in any way. It is, however, convenient to use with
Objectify.

## Code

https://github.com/stickfigure/objectify-remotely

## Design

When you install the remote API, all GAE service calls go to the remote server. Objectify-remotely limits this
to only datastore operations; used in conjunction with Objectify (or other tools), your application can continue
to use local memcache and other services while shunting only datastore operations to the remote service.

Objectify-remotely includes two pieces:

1. An executor that lets you execute a unit-of-work in a remote context
2. A wrapper for AsyncDatastoreService that works in concert with the executor

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

### Use the RemoteAsyncDatastoreService

If you are using the low level API directly, simply wrap your AsyncDatastoreService:

```java
	AsyncDatastoreService raw = DatastoreServiceFactory.getAsyncDatastoreService();
	AsyncDatastoreService myService = RemoteAsyncDatastoreService.create(raw);
```

If you are using Objectify, subclass `ObjectifyFactory` and override this method:

```java
	@Override
	protected AsyncDatastoreService createRawAsyncDatastoreService(DatastoreServiceConfig cfg) {
		AsyncDatastoreService raw = super.createRawAsyncDatastoreService(cfg);
		return RemoteAsyncDatastoreService.create(raw);
	}
```

### Configure the remote service

```java
	RemoteApiOptions options = new RemoteApiOptions()
    	.server("remote.example.com", 443)
    	.credentials(username, password);

    Remotely.setOptions(options);
```

### Execute work remotely

```java
	String foo = Remotely.execute(new Callable<String>() {
		public String call() {
			ofy().load()... etc, do some datastore work
			return "some string value";
		}
	});
```

or for void work:

```java
	Remotely.execute(new VoidCallable() {
		public void run() {
			ofy().load()... etc, do some datastore work
		}
	});
```

## More

If you have questions, ask on the Objectify Google Group:

http://groups.google.com/group/objectify-appengine

## License

Released under the MIT License.

## Thanks

Huge thanks to BetterCloud (http://www.bettercloud.com/) for funding this project!
