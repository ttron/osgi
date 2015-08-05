package cn.ttron.lab.jetty.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Jakub Podlesak (jakub.podlesak at oracle.com)
 */
@Path("status")
public class StatusResource
{
	@GET
	@Produces("text/plain")
	public String getStatus()
	{
		return "active";
	}
}
