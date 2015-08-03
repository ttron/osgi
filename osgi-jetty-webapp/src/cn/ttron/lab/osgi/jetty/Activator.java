package cn.ttron.lab.osgi.jetty;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{

	@Override
	public void start(BundleContext context) throws Exception
	{
		// Create a webapp context as a Service and target it at the "fooServer" Server instance
		WebAppContext webapp = new WebAppContext();
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put( "war", "." );
		props.put( "contextPath", "/acme" );
		props.put( "managedServerName", "fooServer" );
		context.registerService( ContextHandler.class.getName(), webapp, props );
		System.out.println( "register Context Handler" );
	}


	@Override
	public void stop(BundleContext context) throws Exception
	{
	}
}
