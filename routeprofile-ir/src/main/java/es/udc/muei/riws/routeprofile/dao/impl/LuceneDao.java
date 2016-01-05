package es.udc.muei.riws.routeprofile.dao.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import es.udc.muei.riws.routeprofile.dao.IRDao;
import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.IRException;

public class LuceneDao implements IRDao {

	private static final String indexPath = "/home/jose/MASTER/RIWS/RI/solr-4.8.1/example/solr/collection1/data/index";
	private Properties props = null;

	public LuceneDao() {
		// TODO: Load the properties from lucene.properties file.
	}

	@Override
	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters) {
		// TODO: Remove next variable. Load it from lucene.properties file.
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
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);
		config.setOpenMode(OpenMode.APPEND);
		try {
			IndexWriter writer = new IndexWriter(FSDirectory.open(new File(indexPath)), config);
			Document doc = new Document();
			doc.add(new StringField("username", newUser.getUsername(), Field.Store.YES));
			doc.add(new StringField("password", newUser.getPassword(), Field.Store.YES));
			doc.add(new TextField("routes", routeIdsToStr(newUser), Field.Store.YES));
			writer.addDocument(doc);
			writer.commit();
			writer.close();
			return (newUser);
		} catch (CorruptIndexException e) {
			throw new IRException();
		} catch (IOException e) {
			throw new IRException();
		}
	}

	@Override
	public Collection<UserDTO> findUsers(Collection<String> usernames) throws IRException {
		try {
			Collection<UserDTO> result = new ArrayList<UserDTO>();
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			if (usernames != null && !usernames.isEmpty()) {
				String valueQuery = "";
				for (String username : usernames)
					valueQuery += (valueQuery.isEmpty() ? "" : " ") + username;
				Query query = new TermQuery(new Term("username", valueQuery));
				TopDocs topDocs = searcher.search(query, usernames.size());
				result = convertToUserDTO(reader, topDocs);
			}
			return (result);
		} catch (IOException e) {
			throw new IRException();
		}
	}

	@Override
	public Collection<RouteDTO> findRoutesById(Collection<String> routeIds) throws IRException {

		Collection<RouteDTO> result = new ArrayList<RouteDTO>();
		try {
			String routeIdsQueryValue = "*";
			if (routeIds != null && !routeIds.isEmpty()) {
				for (String id : routeIds)
					routeIdsQueryValue += (routeIdsQueryValue.isEmpty() ? "" : " ") + id;
			}
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Query query = new TermQuery(new Term("url", routeIdsQueryValue));
			TopDocs topDocs = searcher.search(query, routeIds.size());
			result = convertToRouteDTO(reader, topDocs);
			return (result);
		} catch (IOException e) {
			throw new IRException();
		}
	}

	private String routeIdsToStr(UserDTO user) {
		String result = "";
		if (user.getRouteIds() != null && !user.getRouteIds().isEmpty()) {
			for (String id : user.getRouteIds())
				result += (result.isEmpty() ? "" : ",") + id;
		}
		return (result);
	}

	private Collection<UserDTO> convertToUserDTO(IndexReader reader, TopDocs docs) throws IRException {
		Collection<UserDTO> result = new ArrayList<UserDTO>();
		try {
			for (int i = 0; i < docs.totalHits; i++) {
				String routeIdsStr = reader.document(docs.scoreDocs[i].doc).get("routeIds");
				result.add(new UserDTO(reader.document(docs.scoreDocs[i].doc).get("username"), reader.document(
						docs.scoreDocs[i].doc).get("password"), Arrays.asList(routeIdsStr.split(","))));
			}
			return (result);
		} catch (IOException e) {
			throw new IRException();
		}
	}

	private Collection<RouteDTO> convertToRouteDTO(IndexReader reader, TopDocs docs) throws IRException {
		Collection<RouteDTO> result = new ArrayList<RouteDTO>();
		try {
			for (int i = 0; i < docs.totalHits; i++) {
				String id = reader.document(docs.scoreDocs[i].doc).get("url");
				Double distance = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get("PR_DISTANCE"));
				Boolean looped = Boolean.valueOf(reader.document(docs.scoreDocs[i].doc).get("PR_LOOP"));
				Double maxElevation = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get("PR_ELEVATION_MAX"));
				Double minElevation = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get("PR_ELEVATION_MIN"));
				Double elevationGainUp = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get(
						"PR_ELEVATION_GAIN_UP_HILL"));
				Double elevationGainDown = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get(
						"PR_ELEVATION_GAIN_DOWN_HILL"));
				result.add(new RouteDTO(id, distance, looped, maxElevation, minElevation, elevationGainUp,
						elevationGainDown));
			}
			return (result);
		} catch (IOException e) {
			throw new IRException();
		}
	}
}
