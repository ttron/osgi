package cn.ttron.lab.ogsi.jetty;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.jetty.server.Server;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{

	@Override
	public void start(BundleContext context) throws Exception
	{
		System.setProperty( "jetty.home.bundle", "M:/W/amdatu-do/osgi-jetty-server/conf/" );

		Server server = new Server();
		// do any setup on Server in here
		String serverName = "defaultJettyServer";
		Dictionary<String, String> serverProps = new Hashtable<String, String>();
		// define the unique name of the server instance
		serverProps.put( "managedServerName", serverName );
		serverProps.put( "jetty.http.port", "8080" );
		// let Jetty apply some configuration files to the Server instance
		serverProps.put( "jetty.etc.config.urls", "file:/M:/W/amdatu-do/osgi-jetty-server/conf/jetty.xml,file:/M:/W/amdatu-do/osgi-jetty-server/conf/jetty-http.xml" );// ,file:/M:/W/amdatu-do/osgi-jetty-server/conf/jetty-deploy.xml"
																																										// );
		// register as an OSGi Service for Jetty to find
		context.registerService( Server.class.getName(), server, serverProps );
		System.out.println( "register Jetty Server" );
	}


	@Override
	public void stop(BundleContext context) throws Exception
	{
	}
}