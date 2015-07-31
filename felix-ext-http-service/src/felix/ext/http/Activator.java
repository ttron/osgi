package felix.ext.http;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator
{
	private ServiceTracker httpTracker;


	@Override
	public void start(BundleContext context) throws Exception
	{
		httpTracker = new ServiceTracker( context, ExtHttpService.class.getName(), null )
		{
			@Override
			public void removedService(ServiceReference reference, Object service)
			{
				// HTTP service is no longer available, unregister our resources...
				try
				{
					((ExtHttpService) service).unregister( "/static" );
				}
				catch (IllegalArgumentException exception)
				{
					// Ignore; servlet registration probably failed earlier on...
				}
			}


			@Override
			public Object addingService(ServiceReference reference)
			{
				// HTTP service is available, register our resources...
				ExtHttpService httpService = (ExtHttpService) this.context.getService( reference );
				try
				{
					httpService.registerFilter( new HelloWorldFilter(), "/hello/.*", null, 0, null );
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
				}
				return httpService;
			}
		};
		// start tracking all HTTP services...
		httpTracker.open();
	}


	@Override
	public void stop(BundleContext context) throws Exception
	{
		// stop tracking all HTTP services...
		httpTracker.close();
	}
}
