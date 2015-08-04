package cn.ttron.lab.osgi.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component(provide = Servlet.class, properties = { "alias=/" })
public class HelloWorldServlet extends HttpServlet
{

	private static final long serialVersionUID = -2393380300535085400L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		resp.getWriter().append( "Hello World!" );
	}
}