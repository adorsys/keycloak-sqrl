package de.adorsys.smartlogin.rest;

import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.adorsys.smartlogin.service.SqrlAuthException;
import de.adorsys.smartlogin.service.SqrlAuthenticationPreparationData;
import de.adorsys.smartlogin.service.SqrlAuthenticationService;
import de.adorsys.smartlogin.service.SqrlResponse;
import de.adorsys.smartlogin.service.SqrlWebApplicationService;

/**
 * Created by alexg on 07.12.16.
 */
@Path("auth")
@ApplicationScoped
public class AuthResource {

    @Inject
    private SqrlWebApplicationService sqrlService;

    @Inject
    private SqrlAuthenticationService sqrlAuthenticationService;

    @GET
    @Path("/sqrl-uri")
    public String createSqrlUri(@Context UriInfo uriInfo) {
        return sqrlService.initCreateSqrlUrl(uriInfo);
    }

    @GET
    @Path("/sqrl-qr-code")
    @Produces("image/png")
    public Response createQrCode(@QueryParam("nut") String nut, @Context UriInfo uriInfo, @Context HttpServletResponse response) throws IOException {
        File qr = sqrlService.createQrCode(nut, uriInfo, response);

        String mt = new MimetypesFileTypeMap().getContentType(qr);
        return Response.ok(qr, mt).build();
    }

    @POST
    @Path("/sqrl-prepare")
    @Consumes(MediaType.APPLICATION_JSON)
    public void prepare(@QueryParam("nut") String nut, SqrlAuthenticationPreparationData data){
        sqrlService.prepare(nut, data);
    }

    @GET
    @Path("/sqrl-state")
    public String getSqrlState(@QueryParam("nut") String nut){
        return sqrlService.getSqrlState(nut).name();
    }

    @GET
    @Path("/sqrl-response")
    @Produces(MediaType.APPLICATION_JSON)
    public SqrlResponse provideResponseData(@QueryParam("nut") String nut){
        return sqrlService.provideResponseData(nut);
    }

    @GET
    @Path("/sqrl-exists")
    public boolean existsSqrlIdentityFor(@QueryParam("nut") String nut, @QueryParam("userid") String userId){
        return sqrlService.existsSqrlIdentityFor(nut, userId);
    }

    @DELETE
    @Path("/sqrl")
    public void requestSqrlDeletion(@QueryParam("nut") String nut, @QueryParam("userid") String userId){
        sqrlService.requestSqrlDeletion(nut, userId);
    }

    @POST
    @Path("/sqrl")
    @Consumes("application/x-www-form-urlencoded")
    public void handleSqrlRequest(@Context HttpServletRequest request, @Context HttpServletResponse response) throws SqrlAuthException {
        sqrlAuthenticationService.handleSqrlRequest(request, response);
    }
}
