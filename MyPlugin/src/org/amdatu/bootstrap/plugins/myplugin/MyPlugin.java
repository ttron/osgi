/*
 * Copyright (c) 2014 The Amdatu Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.amdatu.bootstrap.plugins.myplugin;

import java.util.List;

import org.amdatu.bootstrap.command.Command;
import org.amdatu.bootstrap.command.Description;
import org.amdatu.bootstrap.command.InstallResult;
import org.amdatu.bootstrap.command.Parameters;
import org.amdatu.bootstrap.command.Required;
import org.amdatu.bootstrap.core.BootstrapPlugin;
import org.amdatu.bootstrap.services.Dependency;
import org.amdatu.bootstrap.services.DependencyBuilder;
import org.amdatu.bootstrap.services.Prompt;
import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;

@Component
public class MyPlugin implements BootstrapPlugin
{

	interface MyPluginArgs extends Parameters
	{
		@Description("Your name")
		String name();
	}


	@Command
	public void demo(MyPluginArgs args, Prompt prompt)
	{
		prompt.printf( "Hello, %s", args.name() );
	}


	@Override
	public String getName()
	{
		return "myplugin";
	}
}
