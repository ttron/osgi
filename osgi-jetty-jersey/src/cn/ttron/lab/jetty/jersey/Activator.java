package cn.ttron.lab.jetty.jersey;

import java.util.logging.Logger;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{
	private static final Logger LOG = Logger.getLogger( Activator.class.getName() );


	@Override
	public void start(BundleContext context) throws Exception
	{
		// ServletContextHandler jerseyHandler = new ServletContextHandler( ServletContextHandler.SESSIONS );
		// jerseyHandler.setContextPath( "/ctx0" );
		// jerseyHandler.addServlet( new ServletHolder( new HelloServlet() ), "/*" );
		// jerseyHandler.addServlet( new ServletHolder( new HelloServlet( "Buongiorno Mondo" ) ), "/it/*" );
		ServletContextHandler jerseyHandler = new ServletContextHandler( ServletContextHandler.NO_SESSIONS );
		jerseyHandler.setContextPath( "/jersey-http-service" );
		ServletHolder servletHolder = jerseyHandler.addServlet( org.glassfish.jersey.servlet.ServletContainer.class, "/api/*" );
		servletHolder.setInitParameter( "javax.ws.rs.Application", "cn.ttron.lab.jetty.jersey.JerseyApplication" );
		// servletHolder.setInitParameter( ServerProperties.PROVIDER_PACKAGES, "cn.ttron.lab.jetty.jersey" );
		LOG.info( "add jersey Hanlder" );
		context.registerService( ContextHandler.class.getName(), jerseyHandler, null );
	}


	@Override
	public void stop(BundleContext context) throws Exception
	{

	}

	// @SuppressWarnings("UseOfObsoleteCollectionType")
	// private Dictionary<String, String> getJerseyServletParams()
	// {
	// Dictionary<String, String> jerseyServletParams = new Hashtable<String, String>();
	// jerseyServletParams.put( "javax.ws.rs.Application", JerseyApplication.class.getName() );
	// return jerseyServletParams;
	// }
}
