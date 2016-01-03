package es.udc.muei.riws.routeprofile.examples;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;

/**
 * Hello world!
 *
 */
public class SimpleReader1 {
    public static void main(String[] args) throws IOException, ParseException {
	if (args.length != 1) {
	    System.out.println("Usage: java SimpleReader1 <index_folder>");
	    return;
	}
	try (DirectoryReader indexReader = DirectoryReader.open(FSDirectory.open(new File(args[0])))) {

	    for (int i = 0; i < indexReader.maxDoc(); i++) {
		Document doc = indexReader.document(i);
		System.out.println("Documento " + i);
		// doc.get() returns null for the fields that were not stored
		System.out.println("modelRef = " + doc.get("modelRef"));
		System.out.println("modelAcronym = " + doc.get("modelAcronym"));
		System.out.println("modelDescription = " + doc.get("modelDescription"));
		System.out.println("theoreticalContent = " + doc.get("theoreticalContent"));
		System.out.println("practicalContent = " + doc.get("practicalContent"));
	    }
	    for (int i = 0; i < indexReader.maxDoc(); i++) {
		Document doc = indexReader.document(i);
		System.out.println("Documento " + i);
		for (IndexableField field : doc.getFields()) {
		    String fieldName = field.name();
		    System.out.println(fieldName + ": " + doc.get(fieldName));
		}
	    }
	} catch (CorruptIndexException e) {
	    System.out.println("Graceful message: exception " + e);
	    e.printStackTrace();
	} catch (IOException e) {
	    System.out.println("Graceful message: exception " + e);
	    e.printStackTrace();
	}
    }
}
