package es.udc.muei.riws.routeprofile.dao.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import es.udc.muei.riws.routeprofile.dao.IRDao;
import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.IRException;

public class LuceneDao implements IRDao {

	private Properties props = null;

	public LuceneDao() {
		// TODO: Load the properties from lucene.properties file.
	}

	@Override
	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters) {
		// TODO: Remove next variable. Load it from lucene.properties file.
		String indexPath = "/home/jose/MASTER/RIWS/RI/solr-4.8.1/example/solr/collection1/data/index";
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);

			QueryParser parser = new QueryParser(Version.LUCENE_48, "url", new StandardAnalyzer(Version.LUCENE_48));
			Query query = parser.parse("probability");
			TopDocs topDocs = searcher.search(query, 10);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UserDTO createUser(UserDTO newUser) throws IRException {
		// TODO Auto-generated method stub
		return null;
	}
}
