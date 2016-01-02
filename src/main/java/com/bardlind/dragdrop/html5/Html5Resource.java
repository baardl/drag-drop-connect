package com.bardlind.dragdrop.html5;

import com.codahale.metrics.annotation.Metered;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by baardl on 23.10.15.
 */
@Path("html5")
@Api("html5")
@Produces(MediaType.TEXT_HTML)
@Controller
public class Html5Resource {
    private static final Logger log = getLogger(Html5Resource.class);



    @Metered
    @GET
    @Path("/")
    public Html5View getService(@Context HttpServletResponse response) {
        return new Html5View();
    }


}
