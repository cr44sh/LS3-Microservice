package service;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * @author Carol Schaefer <carol.schaefer@student.kit.edu>
 */

@SuppressWarnings("deprecation")
@Path("/ls3algorithm")
public class Resource {

	// SLF4J is provided with Dropwizard
	// Logger log = LoggerFactory.getLogger(Resource.class);

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
	 * @param petrinetnames The names/identifier of the petrinets to be analyzed
	 * within the MongoDB, concatenated with ","
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
	@Path("/{petrinets}/{k}/{theta}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ls3execute(@PathParam("petrinets") String petrinetNames, @PathParam("k") int k,
			@PathParam("theta") float theta) throws IOException {

		/*********************************************************************************/
		/*********************************************************************************/
		// Schritt 1: alle Dateien aus der MongoDB holen und abspeichern
		/*********************************************************************************/

		/*
		 * MongoDB-Anbindung initialisieren
		 * https://www.mkyong.com/mongodb/java-mongodb-save-image-example/
		 */

		System.out.println("Request received!");
		System.out.println(petrinetNames);
		System.out.println(k);
		System.out.println(theta);
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("databasename");

		/*
		 * GridFS-Objekt erzeugen, um in MongoDB suchen zu können
		 */
		GridFS gfsPnml = new GridFS(db, "files");
		GridFSDBFile pnmlOutput;		

		java.nio.file.Path destination;
		String[] petrinetList = petrinetNames.split(",");

		try {

			/*
			 * .pnml-Dateien auf der Festplatte abspeichern
			 */
			for (int i = 0; i < petrinetList.length; i++) {
				/*
				 * Petrinetz in MongoDB suchen
				 */
				pnmlOutput = gfsPnml.findOne(new ObjectId(petrinetList[i]));				
				if (pnmlOutput == null) {
					throw new FileNotFoundException("File " +  petrinetList[i] +" not found in MongoDB.");
				}

				/*
				 * .pnml als Datei abspeichern
				 * http://stackoverflow.com/questions/22663904/efficient-way-to-
				 * write-inputstream-to-a-file-in-java-version-6
				 */

				destination = java.nio.file.Paths.get(
						"C:\\Users\\Carol\\ECLIPSE_WORKSPACE\\git\\MicroService\\LS3-Microservice\\src\\main\\resources\\petrinetze\\"
								+ petrinetList[i] + ".pnml");
				try {
					InputStream in = pnmlOutput.getInputStream();
					Files.copy(in, destination, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
		} catch (com.mongodb.MongoException me) {
			System.out.println("Mongostyle");
			me.printStackTrace();
		}

		finally {
			mongoClient.close();
		}

		/*********************************************************************************/
		/*********************************************************************************/
		// Schritt 2: LS3-Algorithmus mit Parametern starten
		/*********************************************************************************/

		List<Map> result;
		Ls3Algorithm ls3Algorithm = new Ls3Algorithm();
		
		print("Before ls3.execute() in Resource.java.");
		
		result = ls3Algorithm.execute(new File(
				"C:\\Users\\Carol\\ECLIPSE_WORKSPACE\\git\\MicroService\\LS3-Microservice\\src\\main\\resources\\petrinetze\\")
						.getAbsolutePath(),
				k, theta);
		
		print("LS3-Result:");
		print(result.toString());
		print("####");

		/*********************************************************************************/
		/*********************************************************************************/
		// Schritt 3: .pnml-Dateien auf Festplatte wieder löschen und Ergebnis
		// ausgeben
		/*********************************************************************************/

		File petrinetzOrdner = new File(
				"C:\\Users\\Carol\\ECLIPSE_WORKSPACE\\git\\MicroService\\LS3-Microservice\\src\\main\\resources\\petrinetze\\");
		for (File pnmlDatei : petrinetzOrdner.listFiles()) {
			if (!pnmlDatei.delete()) {
				System.err.println(pnmlDatei + " konnte nicht gelöscht werden.");
			}
		}

		Response response = Response.ok(result).build();

		return response;

		/*********************************************************************************/
		/*********************************************************************************/
	}
	
	public static void print(String string) {
		System.out.println(string);
	}
}