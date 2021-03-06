package org.foo.shell;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

public class HistoryDecorator implements Command, History, FrameworkListener, BundleListener
{
	private final List<String> m_history = Collections.synchronizedList( new ArrayList<String>() );

	private final Command m_next;


	public HistoryDecorator(Command next, List<String> history)
	{
		m_next = next;
		m_history.addAll( history );
	}


	@Override
	public void exec(String args, PrintStream out, PrintStream err) throws Exception
	{
		try
		{
			m_next.exec( args, out, err );
		}
		finally
		{
			m_history.add( args );
		}
	}


	@Override
	public List<String> get()
	{
		return new ArrayList<String>( m_history );
	}


	@Override
	public void frameworkEvent(FrameworkEvent event)
	{
		m_history.add( "\tFrameworkEvent(type=" + event.getType() + ",bundle=" + event.getBundle() + ",source="
				+ event.getSource() + ",throwable=" + event.getThrowable() + ")" );
	}


	@Override
	public void bundleChanged(BundleEvent event)
	{
		m_history.add( "\tBundleEvent(type=" + event.getType() + ",bundle=" + event.getBundle() + ",source=" + event.getSource()
				+ ")" );
	}
}
