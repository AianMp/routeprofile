package es.udc.muei.riws.routeprofile.examples;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * Hello world!
 *
 */
public class SimpleDemo {
    public static void main(String[] args) throws IOException, ParseException {
	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
	// Store the index in memory:
	Directory directory = new RAMDirectory();
	// To store an index on disk, use this instead:
	// Directory directory = FSDirectory.open("/tmp/testindex");
	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);
	IndexWriter iwriter = new IndexWriter(directory, config);
	Document doc = new Document();
	String text = "Hello world, how are you?";
	doc.add(new TextField("fieldname", text, Field.Store.YES));
	iwriter.addDocument(doc);
	iwriter.close();

	// Now search the index:
	DirectoryReader ireader = DirectoryReader.open(directory);
	IndexSearcher isearcher = new IndexSearcher(ireader);

	// Parse a simple query that searches for "text":
	QueryParser parser = new QueryParser(Version.LUCENE_48, "fieldname", analyzer);
	Query query = parser.parse("world");
	ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
	// Iterate through the results:
	for (ScoreDoc hit : hits) {
	    Document hitDoc = isearcher.doc(hit.doc);
	    System.out.println("This is the text of the indexed document that was a result for the "
		    + "query ’world’:\n" + hitDoc.get("fieldname"));
	}
	ireader.close();
	directory.close();
    }
}
