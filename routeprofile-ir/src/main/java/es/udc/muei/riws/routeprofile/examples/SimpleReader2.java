package es.udc.muei.riws.routeprofile.examples;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;

/**
 * Hello world!
 *
 */
public class SimpleReader2 {
    public static void main(String[] args) throws IOException, ParseException {
	if (args.length != 1) {
	    System.out.println("Usage: java SimpleReader <index_folder>");
	    return;
	}
	try (DirectoryReader indexReader = DirectoryReader.open(FSDirectory.open(new File(args[0])))) {
	    // Get the context for each leaf (segment)
	    for (AtomicReaderContext leaf : indexReader.getContext().leaves())
		try (AtomicReader atomicReader = leaf.reader()) {
		    Fields fields = atomicReader.fields();
		    for (String field : fields)
			if (!(field.equals("practicalContent") || field.equals("theoreticalContent"))) {
			    System.out.println("Field = " + field);
			    Terms terms = fields.terms(field);
			    TermsEnum termsEnum = terms.iterator(null);
			    while (termsEnum.next() != null) {
				String tt = termsEnum.term().utf8ToString();
				// totalFreq equals -1 if the value was not
				// stored in the codification of this index
				System.out.println(tt + " totalFreq()=" + termsEnum.totalTermFreq() + " docFreq="
					+ termsEnum.docFreq());
			    }
			}
		    Term term = new Term("modelDescription", "probability");
		    DocsEnum docsEnum = atomicReader.termDocsEnum(term);
		    int doc;
		    while ((doc = docsEnum.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
			System.out.println("Term(field=modelDescription,text=probability) appears in doc num: " + doc);
			Document d = atomicReader.document(doc);
			System.out.println("modelDescription= " + d.get("modelDescription"));
		    }
		}
	}
    }
}
