package cn.ttron.lab.osgi.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class HelloHandler extends AbstractHandler
{
	static int h = 0;

	int hello = h++;


	@Override
	public void handle(String arg0, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		baseRequest.setHandled( true );
		response.setStatus( HttpServletResponse.SC_OK );
		response.setContentType( "text/html" );
		response.getWriter().println( "<h1>Hello OneContext " + hello + "" );
	}
}
