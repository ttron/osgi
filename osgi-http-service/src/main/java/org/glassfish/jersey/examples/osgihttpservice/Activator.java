/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.jersey.examples.osgihttpservice;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator
{
	private BundleContext context;

	private ServiceTracker tracker;

	private HttpService httpService = null;

	private static final Logger LOG = Logger.getLogger( Activator.class.getName() );


	@Override
	public synchronized void start(BundleContext bundleContext) throws Exception
	{
		this.context = bundleContext;

		LOG.info( "STARTING HTTP SERVICE BUNDLE" );

		this.tracker = new ServiceTracker( this.context, HttpService.class.getName(), null )
		{

			@Override
			public Object addingService(ServiceReference serviceRef)
			{
				httpService = (HttpService) super.addingService( serviceRef );
				try
				{
					httpService.registerResources( "/html", "html", null );
				}
				catch (NamespaceException e)
				{
					e.printStackTrace();
				}
				registerServlets();
				return httpService;
			}


			@Override
			public void removedService(ServiceReference ref, Object service)
			{
				if (httpService == service)
				{
					unregisterServlets();
					httpService = null;
				}
				super.removedService( ref, service );
			}
		};

		this.tracker.open();

		LOG.info( "HTTP SERVICE BUNDLE STARTED" );
	}


	@Override
	public synchronized void stop(BundleContext bundleContext) throws Exception
	{
		this.tracker.close();
	}


	private void registerServlets()
	{
		try
		{
			rawRegisterServlets();
		}
		catch (InterruptedException | NamespaceException | ServletException ie)
		{
			throw new RuntimeException( ie );
		}
	}


	private void rawRegisterServlets() throws ServletException, NamespaceException, InterruptedException
	{
		LOG.info( "JERSEY BUNDLE: REGISTERING SERVLETS" );
		LOG.info( "JERSEY BUNDLE: HTTP SERVICE = " + httpService.toString() );

		// TODO - temporary workaround
		// This is a workaround related to issue JERSEY-2093; grizzly (1.9.5) needs to have the correct
		// context
		// classloader set
		ClassLoader myClassLoader = getClass().getClassLoader();
		ClassLoader originalContextClassLoader = Thread.currentThread().getContextClassLoader();
		try
		{
			Thread.currentThread().setContextClassLoader( myClassLoader );
			httpService.registerServlet( "/jersey-http-service", new ServletContainer(), getJerseyServletParams(), null );
		}
		finally
		{
			Thread.currentThread().setContextClassLoader( originalContextClassLoader );
		}
		// END of workaround - after grizzly updated to the recent version, only the inner call from try block
		// will remain:
		// httpService.registerServlet("/jersey-http-service", new ServletContainer(),
		// getJerseyServletParams(), null);

		sendAdminEvent();
		LOG.info( "JERSEY BUNDLE: SERVLETS REGISTERED" );
	}


	private void sendAdminEvent()
	{
		ServiceReference ref = context.getServiceReference( EventAdmin.class.getName() );
		if (ref != null)
		{
			EventAdmin ea = (EventAdmin) context.getService( ref );
			ea.sendEvent( new Event( "jersey/test/DEPLOYED", new Hashtable<String, String>()
			{
				{
					put( "context-path", "/" );
				}
			} ) );
			context.ungetService( ref );
		}
	}


	private void unregisterServlets()
	{
		if (this.httpService != null)
		{
			LOG.info( "JERSEY BUNDLE: UNREGISTERING SERVLETS" );
			httpService.unregister( "/jersey-http-service" );
			httpService.unregister( "/html" );
			LOG.info( "JERSEY BUNDLE: SERVLETS UNREGISTERED" );
		}
	}


	@SuppressWarnings("UseOfObsoleteCollectionType")
	private Dictionary<String, String> getJerseyServletParams()
	{
		Dictionary<String, String> jerseyServletParams = new Hashtable<String, String>();
		jerseyServletParams.put( "javax.ws.rs.Application", JerseyApplication.class.getName() );
		return jerseyServletParams;
	}
}
