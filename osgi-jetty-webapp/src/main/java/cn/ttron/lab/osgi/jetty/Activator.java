package cn.ttron.lab.osgi.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator
{
	private ServiceTracker logServiceTracker;

	private LogService logService;


	@Override
	public void start(BundleContext context) throws Exception
	{
		// create a tracker and track the log service
		logServiceTracker = new ServiceTracker( context, LogService.class.getName(), null );
		logServiceTracker.open();

		try
		{
			// Create a webapp context as a Service and target it at the "fooServer" Server instance
			// WebAppContext webapp = new WebAppContext();
			// Dictionary<String, String> props = new Hashtable<String, String>();
			// props.put( "war", "." );
			// props.put( "contextPath", "/acme" );
			// props.put( "managedServerName", "defaultJettyServer" );
			// context.registerService( ContextHandler.class.getName(), webapp, props );

			ServiceReference reference = context.getServiceReference( "org.eclipse.jetty.server.Server" );
			Server server = (Server) context.getService( reference );
			if (server != null)
			{
				ContextHandler handler = new ContextHandler();
				handler.setContextPath( "/hello" );
				handler.setResourceBase( "." );
				handler.setHandler( new HelloHandler() );
				handler.setClassLoader( Thread.currentThread().getContextClassLoader() );
				server.setHandler( handler );
				System.err.println( "register Context Handler" );
			}
		}
		catch (Exception e)
		{
			// grab the service
			logService = (LogService) logServiceTracker.getService();
			if (logService != null)
				logService.log( LogService.LOG_ERROR, e.getLocalizedMessage() );
			System.err.println( e.getLocalizedMessage() );
			e.printStackTrace();
		}
	}


	@Override
	public void stop(BundleContext context) throws Exception
	{
		if (logService != null)
			logService.log( LogService.LOG_INFO, "Yee ha, I'm logging!" );
		// close the service tracker
		logServiceTracker.close();
		logServiceTracker = null;
	}
}
