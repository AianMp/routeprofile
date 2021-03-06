package es.udc.muei.riws.routeprofile.examples;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Hello world!
 *
 */
public class SimpleSearch {
    public static void main(String[] args) throws IOException, ParseException {
	if (args.length != 1) {
	    System.out.println("Usage: java SimpleSearch <index_folder>");
	    return;
	}
	try (IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(args[0])))) {

	    IndexSearcher searcher = new IndexSearcher(reader);

	    QueryParser parser = new QueryParser(Version.LUCENE_48, "content", new StandardAnalyzer(Version.LUCENE_48));
	    Query query = parser.parse("Moderado");
	    TopDocs topDocs = searcher.search(query, 10);
	    System.out.println("\n" + topDocs.totalHits + " results for query \"" + query.toString()
		    + "\" showing for the first 10 documents the doc id, "
		    + "score and the content of the modelDescription field");
	    for (int i = 0; i < topDocs.totalHits; i++)
		System.out.println(topDocs.scoreDocs[i].doc + " -- score: " + topDocs.scoreDocs[i].score + " -- "
			+ reader.document(topDocs.scoreDocs[i].doc).get("content") + " -- URL: "
			+ reader.document(topDocs.scoreDocs[i].doc).get("url"));
	    // An example using a numeric field for sorting the results
	    // by default this does not compute the scores of docs, since the
	    // ranking is imposed by the sorting
	    topDocs = searcher.search(query, 10, new Sort(new SortField("practicalContent", SortField.Type.INT, true)));
	    System.out.println("\n" + topDocs.totalHits + " results for query \"" + query.toString()
		    + "in the sort given by the field practicalContent, " + "\" showing for the first " + 10
		    + " documents the doc id, " + "score and the content of the modelDescription field");
	    for (int i = 0; i < topDocs.totalHits; i++)
		System.out.println(topDocs.scoreDocs[i].doc + " -- score: " + topDocs.scoreDocs[i].score + " -- "
			+ reader.document(topDocs.scoreDocs[i].doc).get("modelDescription"));

	    // An example of a simple programmatic query
	    BooleanQuery booleanQuery = new BooleanQuery();
	    Query vector = new TermQuery(new Term("modelDescription", "vector"));
	    Query space = new TermQuery(new Term("modelDescription", "space"));
	    booleanQuery.add(vector, Occur.MUST);
	    booleanQuery.add(space, Occur.MUST);
	    topDocs = searcher.search(booleanQuery, 10);
	    System.out.println("\n" + topDocs.totalHits + " results for query \"" + booleanQuery.toString()
		    + "\" showing for the first " + "10 documents the doc id, score and the content of the "
		    + "modelDescription field");
	    for (int i = 0; i < topDocs.totalHits; i++)
		System.out.println(topDocs.scoreDocs[i].doc + " -- score: " + topDocs.scoreDocs[i].score + " -- "
			+ reader.document(topDocs.scoreDocs[i].doc).get("modelDescription"));
	} catch (CorruptIndexException e1) {
	    System.out.println("Graceful message: exception " + e1);
	    e1.printStackTrace();
	} catch (IOException e1) {
	    System.out.println("Graceful message: exception " + e1);
	    e1.printStackTrace();
	} catch (ParseException e1) {
	    System.out.println("Graceful message: exception " + e1);
	    e1.printStackTrace();
	}
    }
}
