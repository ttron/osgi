/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.foo.log.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

/**
 * Partial implementation of the OSGi LogService API.
 **/
public class Activator implements BundleActivator
{

	/**
	 * @param context The context of the bundle.
	 **/
	@Override
	public void start(BundleContext context)
	{
		context.registerService( LogService.class.getName(), new DummyLogServiceFactory(), null );
	}


	/**
	 * @param context The context of the bundle.
	 **/
	@Override
	public void stop(BundleContext context)
	{
	}

	/**
	 * Customize the LogService for each bundle that requests it.
	 **/
	static class DummyLogServiceFactory implements ServiceFactory
	{
		@Override
		public Object getService(final Bundle bundle, final ServiceRegistration registration)
		{

			LogService service = new LogService()
			{
				@Override
				public void log(int level, String message)
				{

					String tid = "thread=\"" + Thread.currentThread().getName() + "\"";
					String bid = "bundle=" + bundle.getBundleId();

					Object sid;
					try
					{
						sid = registration.getReference().getProperty( Constants.SERVICE_ID );
					}
					catch (RuntimeException re)
					{
						sid = "!!"; // this service is no longer valid and shouldn't be used
					}

					System.out.println( "<" + sid + "> " + tid + ", " + bid + " : " + message );
				}


				@Override
				public void log(int level, String message, Throwable exception)
				{
				}


				@Override
				public void log(ServiceReference sr, int level, String message)
				{
				}


				@Override
				public void log(ServiceReference sr, int level, String message, Throwable exception)
				{
				}
			};

			service.log( LogService.LOG_INFO, "logging ON" );

			return service;
		}


		@Override
		public void ungetService(Bundle bundle, ServiceRegistration registration, Object service)
		{
			((LogService) service).log( LogService.LOG_INFO, "logging OFF" );
		}
	}
}
