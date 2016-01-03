package es.udc.muei.riws.routeprofile.examples;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Hello world!
 *
 */
public class SimpleIndexing {
    public static void main(String[] args) throws IOException, ParseException {
	if (args.length != 1) {
	    System.out.println("Usage: java SimpleIndexing indexFolder");
	    return;
	}
	String modelRef[] = new String[] { "RM000", "RM001", "RM002", "RM003" };
	String modelDescription[] = new String[4];
	String modelAcronym[] = new String[] { "BM", "VSM", "CPM", "LM" };
	int theoreticalContent = 10;
	int practicalContent = 10;

	modelDescription[0] = "The boolean model is a simple retrieval model where "
		+ "queries are interpreted as boolean expressions and documents are bag " + "of words";
	modelDescription[1] = "The vector space model is a simple retrieval model "
		+ "where queries and documents are vectors of terms and similarity of "
		+ "queries and documents is computed with the cosine distance";
	modelDescription[2] = "In the classic probabilistic retrieval model the "
		+ "probability of relevance of a document given a query is computed "
		+ "under the binary and independence assumptions";
	modelDescription[3] = "The use of language models for retrieval implies the "
		+ "estimation of the probability of generating a query given a document";

	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);
	/* try-with-resources clause (see Java 7 documentation) */
	try (IndexWriter writer = new IndexWriter(FSDirectory.open(new File(args[0])), config)) {
	    for (int i = 0; i < modelRef.length; i++) {
		Document doc = new Document();
		/*
		 * Each document has five fields. modelRef is a StringField
		 * which is indexed and not tokenized. modelAcronym is a
		 * StringField which is indexed and not tokenized, addtionally
		 * it is stored. modelDescription is a TextField which is
		 * indexed and tokenized, additionally it is stored.
		 * theoreticalContent is a NumericField that is indexed.
		 * practicalContent is a NumericField that is indexed,
		 * additionally it is stored.
		 */
		doc.add(new StringField("modelRef", modelRef[i], Field.Store.NO));
		doc.add(new StringField("modelAcronym", modelAcronym[i], Field.Store.YES));
		doc.add(new TextField("modelDescription", modelDescription[i], Field.Store.YES));
		doc.add(new IntField("theoreticalContent", theoreticalContent++, Field.Store.NO));
		doc.add(new IntField("practicalContent", practicalContent++, Field.Store.YES));
		writer.addDocument(doc);
		System.out.println("wrote document " + i + " in the index");
	    }
	    writer.commit();
	} catch (CorruptIndexException e) {
	    System.out.println("Graceful message: exception " + e);
	    e.printStackTrace();
	} catch (IOException e) {
	    System.out.println("Graceful message: exception " + e);
	    e.printStackTrace();
	}
    }
}
