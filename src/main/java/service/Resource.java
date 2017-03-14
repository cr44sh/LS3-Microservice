package service;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;

import java.io.IOException;

import java.util.List;
import java.util.Map;

/**
 * @author Carol Schaefer <carol.schaefer@student.kit.edu>
 * @author Caroline Dieterich <caroline.dieterich@student.kit.edu>
 * @date 2017-01-29
 */

@Path("/ls3algorithm")
public class Resource {

	public Resource() {
		System.out.println("Neue Instanz von Resource: " + this);
	}

	/*
	 * Basic method to execute the LS3 algorithm queryKAll(...)
	 * 
	 * QueryKAll executes all queries for a model collection. I.e., all models
	 * in the collection are used as input and LSSM similarity values between
	 * all models are calculated. Therefore, the reduced k-dimensional SVD is
	 * used. The models having a higher similarity value than theta are stored
	 * in an QueryAllResult object and returned by this method.
	 *
	 * @param pfad The path to the directory of the petri nets
	 * 
	 * @param k The dimensionality parameter k for the reduced SVD matrices
	 * 
	 * @param theta The threshold parameter theta for determining similar models
	 * 
	 * @return A List<Map> in which each Map (HashMap) contains - name : name of
	 * the petrinet - similar petrinets: names of similar petrinets,
	 * concatenated with ","
	 */
	@SuppressWarnings("rawtypes")
	@GET
	@Path("/{pfad}/{k}/{theta}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ls3execute(@PathParam("pfad") String pfad, @PathParam("k") int k,
			@PathParam("theta") float theta) throws IOException {

		System.out.println("Request received!");
		System.out.println("Petrinetnames: " + pfad);
		System.out.println("k: " + k);
		System.out.println("theta: " + theta);


		List<Map> result;
		Ls3Algorithm ls3Algorithm = new Ls3Algorithm();
		
		result = ls3Algorithm.execute(new File(
						pfad )
						.getAbsolutePath(),
				k, theta);


		Response response = Response.ok(result).build();

		return response;
	}
}