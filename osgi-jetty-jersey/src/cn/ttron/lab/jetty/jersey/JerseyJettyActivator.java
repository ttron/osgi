package cn.ttron.lab.jetty.jersey;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class JerseyJettyActivator implements BundleActivator
{

	@Override
	public void start(BundleContext context) throws Exception
	{
		ServletContextHandler jerseyHandler = new ServletContextHandler( ServletContextHandler.NO_SESSIONS );
		jerseyHandler.setContextPath( "/jersey-http-service" );
		ServletHolder servletHolder = jerseyHandler.addServlet( ServletContainer.class, "/api/*" );
		servletHolder.setInitParameter( "javax.ws.rs.Application", "cn.ttron.lab.jetty.jersey.JerseyApplication" );
		// servletHolder.setInitParameter( ServerProperties.PROVIDER_PACKAGES, "cn.ttron.lab.jetty.jersey" );
		// LOG.info( "add jersey Hanlder" );
		context.registerService( ContextHandler.class.getName(), jerseyHandler, null );

	}


	@Override
	public void stop(BundleContext context) throws Exception
	{
		// TODO Auto-generated method stub

	}
}
