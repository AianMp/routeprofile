package es.udc.muei.riws.routeprofile.dao.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import es.udc.muei.riws.routeprofile.dao.IRDao;
import es.udc.muei.riws.routeprofile.dao.util.RouteLocationScore;
import es.udc.muei.riws.routeprofile.dao.util.RouteProfileScore;
import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.FilterRangeDTO;
import es.udc.muei.riws.routeprofile.model.dto.FilterValueDTO;
import es.udc.muei.riws.routeprofile.model.dto.LocationDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteProfileDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.DuplicateUserException;
import es.udc.muei.riws.routeprofile.model.exception.IRException;
import es.udc.muei.riws.routeprofile.util.ConfigurationParametersManager;
import es.udc.muei.riws.routeprofile.util.FieldsEnum;

public class LuceneDao implements IRDao {

	private static final String indexPath = ConfigurationParametersManager.getParameter("lucene.index.path");
	private IndexReader reader = null;
	private IndexSearcher searcher = null;

	public LuceneDao() {
		Directory dir = null;
		try {
			File file = new File(indexPath);
			dir = FSDirectory.open(file);
			reader = DirectoryReader.open(dir);
			searcher = new IndexSearcher(reader);
		} catch (IOException e) {
			System.out.println("Corrupt Index Exception" + e);
		}
	}

	private void close() throws IOException {
		reader.close();
	}

	@Override
	public Collection<RouteDTO> findRoutes(UserDTO user, Collection<FilterDTO> filters, int count) throws IRException {
		Collection<RouteDTO> result = new ArrayList<RouteDTO>();
		try {
			BooleanQuery finalQuery = new BooleanQuery();
			QueryParser parser;
			for (FilterDTO filter : filters) {
				if (filter instanceof FilterRangeDTO) {
					finalQuery.add(TermRangeQuery.newStringRange(filter.getField().name(), ((FilterRangeDTO) filter)
							.getMin().toString(), ((FilterRangeDTO) filter).getMax().toString(), true, true),
							Occur.MUST);
				}
				if (filter instanceof FilterValueDTO) {
					parser = new QueryParser(Version.LUCENE_48, filter.getField().name(), new StandardAnalyzer(
							Version.LUCENE_48));
					finalQuery.add(parser.parse(((FilterValueDTO) filter).getValue().toString()), Occur.MUST);
				}
			}
			TopDocs topDocs = null;
			topDocs = searcher.search(finalQuery, count);

			// TODO only for testing
			// System.out
			// .println("\n Found " + topDocs.totalHits + " results for query
			// \"" + finalQuery.toString() + "\"");
			// printSearchDoc(count, reader, topDocs);

			result = convertToRouteDTO(user, topDocs, count);
		} catch (IOException e) {
			throw new IRException("Error I/O", e);
		} catch (ParseException e) {
			throw new IRException("Error when parse the query", e);
		}
		return result;
	}

	@Override
	public Collection<RouteDTO> findRoutesRouteProfileScore(UserDTO user, Collection<FilterDTO> filters,
			RouteProfileDTO routeProfile, int count) throws IRException {
		Collection<RouteDTO> result = new ArrayList<RouteDTO>();
		try {
			BooleanQuery finalQuery = new BooleanQuery();
			QueryParser parser;
			// First condition: url must be a route.
			finalQuery.add(new QueryParser(Version.LUCENE_48, FieldsEnum.url.name(), new StandardAnalyzer(
					Version.LUCENE_48)).parse("view"), Occur.MUST);
			if (filters != null && !filters.isEmpty()) {
				for (FilterDTO filter : filters) {
					if (filter instanceof FilterRangeDTO) {
						finalQuery.add(TermRangeQuery.newStringRange(filter.getField().name(),
								((FilterRangeDTO) filter).getMin().toString(), ((FilterRangeDTO) filter).getMax()
										.toString(), true, true), Occur.MUST);
					}
					if (filter instanceof FilterValueDTO) {
						parser = new QueryParser(Version.LUCENE_48, filter.getField().name(), new StandardAnalyzer(
								Version.LUCENE_48));
						finalQuery.add(parser.parse(((FilterValueDTO) filter).getValue().toString()), Occur.MUST);
					}
				}
			}
			TopDocs topDocs = null;
			CustomScoreQuery scoreQuery = new RouteProfileScore(finalQuery, routeProfile);
			topDocs = searcher.search(scoreQuery, count);

			// TODO only for testing
			// System.out
			// .println("\n Found " + topDocs.totalHits + " results for query
			// \"" + scoreQuery.toString() + "\"");
			// printSearchDoc(count, reader, topDocs);

			result = convertToRouteDTO(user, topDocs, count);
		} catch (IOException e) {
			throw new IRException("Error I/O", e);
		} catch (ParseException e) {
			throw new IRException("Error when parse the query", e);
		}
		return result;
	}

	@Override
	public UserDTO createUser(UserDTO newUser) throws IRException, DuplicateUserException {
		Collection<String> usernames = new ArrayList<String>();
		usernames.add(newUser.getUsername());
		Collection<UserDTO> users = findUsers(usernames);
		if (users.contains(newUser))
			throw new DuplicateUserException(newUser.getUsername());
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
			close();
			return (newUser);
		} catch (CorruptIndexException e) {
			throw new IRException("Corrupt index", e);
		} catch (IOException e) {
			throw new IRException("Error I/O", e);
		}
	}

	@Override
	public void updateUser(UserDTO updatedUser) throws IRException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);
		config.setOpenMode(OpenMode.APPEND);
		try {
			IndexWriter writer = new IndexWriter(FSDirectory.open(new File(indexPath)), config);
			Document doc = new Document();
			doc.add(new StringField("username", updatedUser.getUsername(), Field.Store.YES));
			doc.add(new StringField("password", updatedUser.getPassword(), Field.Store.YES));
			doc.add(new TextField("routes", routeIdsToStr(updatedUser), Field.Store.YES));
			writer.updateDocument(new Term("username", updatedUser.getUsername()), doc);
			writer.commit();
			writer.close();
			close();
		} catch (CorruptIndexException e) {
			throw new IRException("Corrupt index", e);
		} catch (IOException e) {
			throw new IRException("Error I/O", e);
		}
	}

	@Override
	public Collection<UserDTO> findUsers(Collection<String> usernames) throws IRException {
		try {
			Collection<UserDTO> result = new ArrayList<UserDTO>();
			if (usernames != null && !usernames.isEmpty()) {
				String valueQuery = "";
				for (String username : usernames)
					valueQuery += (valueQuery.isEmpty() ? "" : " ") + username;
				QueryParser parser = new QueryParser(Version.LUCENE_48, "username", new StandardAnalyzer(
						Version.LUCENE_48));
				Query query = parser.parse(valueQuery);
				TopDocs topDocs = searcher.search(query, usernames.size());
				// TODO only for testing
				// System.out.println("\n Found " + topDocs.totalHits + "
				// results for query \"" + query.toString() + "\"");
				// printSearchDoc(topDocs.totalHits, reader, topDocs);
				result = convertToUserDTO(topDocs);
			}
			return (result);
		} catch (IOException e) {
			throw new IRException("Error I/O", e);
		} catch (ParseException e) {
			throw new IRException("Error when parse the query", e);
		}
	}

	@Override
	public Collection<RouteDTO> findRoutesById(UserDTO user, Collection<String> routeIds) throws IRException {

		Collection<RouteDTO> result = new ArrayList<RouteDTO>();
		if (routeIds == null || routeIds.isEmpty())
			return result;
		try {
			String routeIdsQueryValue = "";
			for (String id : routeIds) {
				routeIdsQueryValue += (routeIdsQueryValue.isEmpty() ? "" : " ")
						+ id.replace("http://es.wikiloc.com/wikiloc/view.do?id=", "");
			}
			QueryParser parser = new QueryParser(Version.LUCENE_48, FieldsEnum.url.name(), new StandardAnalyzer(
					Version.LUCENE_48));
			Query query = parser.parse(routeIdsQueryValue);
			TopDocs topDocs = searcher.search(query, routeIds.size());
			// TODO only for testing
			// System.out.println("\n Found " + topDocs.totalHits + " results
			// for query \"" + query.toString() + "\"");
			// printSearchDoc(routeIds.size(), reader, topDocs);
			result = convertToRouteDTO(user, topDocs, topDocs.totalHits);
			return (result);
		} catch (IOException e) {
			throw new IRException("Error I/O", e);
		} catch (ParseException e) {
			throw new IRException("Error when parse the query", e);
		}
	}

	@Override
	public Collection<RouteDTO> findRoutesByLocationScore(UserDTO user, Collection<FilterDTO> filters,
			LocationDTO location, int count) throws IRException {
		Collection<RouteDTO> result = new ArrayList<RouteDTO>();
		try {
			BooleanQuery finalQuery = new BooleanQuery();
			QueryParser parser;
			// First condition: url must be a route.
			finalQuery.add(new QueryParser(Version.LUCENE_48, FieldsEnum.url.name(), new StandardAnalyzer(
					Version.LUCENE_48)).parse("view"), Occur.MUST);
			if (filters != null && !filters.isEmpty()) {

				for (FilterDTO filter : filters) {
					if (filter instanceof FilterRangeDTO) {
						finalQuery.add(TermRangeQuery.newStringRange(filter.getField().name(),
								((FilterRangeDTO) filter).getMin().toString(), ((FilterRangeDTO) filter).getMax()
										.toString(), true, true), Occur.MUST);
					}
					if (filter instanceof FilterValueDTO) {
						parser = new QueryParser(Version.LUCENE_48, filter.getField().name(), new StandardAnalyzer(
								Version.LUCENE_48));
						finalQuery.add(parser.parse(((FilterValueDTO) filter).getValue().toString()), Occur.MUST);
					}
				}
			}
			TopDocs topDocs = null;
			CustomScoreQuery scoreQuery = new RouteLocationScore(finalQuery, location);
			topDocs = searcher.search(scoreQuery, count);

			// TODO only for testing
			System.out.println("\n Found " + topDocs.totalHits + " results for query\"" + scoreQuery.toString() + "\"");
			printSearchDoc(count, reader, topDocs);

			result = convertToRouteDTO(user, topDocs, count);
		} catch (IOException e) {
			throw new IRException("Error I/O", e);
		} catch (ParseException e) {
			throw new IRException("Error when parse the query", e);
		}
		return result;
	}

	@Override
	public Collection<RouteDTO> findAllRoutes(UserDTO user, int topCount) throws IRException {
		Collection<RouteDTO> result = new ArrayList<RouteDTO>();
		try {
			QueryParser parser = new QueryParser(Version.LUCENE_48, FieldsEnum.url.name(), new StandardAnalyzer(
					Version.LUCENE_48));
			Query query = parser.parse("view");
			TopDocs topDocs = searcher.search(query, topCount);
			result = convertToRouteDTO(user, topDocs, topCount);
			return (result);
		} catch (IOException e) {
			throw new IRException("Error I/O", e);
		} catch (ParseException e) {
			throw new IRException("Error when parse the query", e);
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

	private Collection<UserDTO> convertToUserDTO(TopDocs docs) throws IRException {
		Collection<UserDTO> result = new ArrayList<UserDTO>();
		try {
			for (int i = 0; i < docs.totalHits; i++) {
				String routeIdsStr = reader.document(docs.scoreDocs[i].doc).get("routes");
				result.add(new UserDTO(reader.document(docs.scoreDocs[i].doc).get("username"), reader.document(
						docs.scoreDocs[i].doc).get("password"),
						(routeIdsStr != null && !routeIdsStr.isEmpty()) ? Arrays.asList(routeIdsStr.split(","))
								: new ArrayList<String>()));
			}
			close();
			return (result);
		} catch (IOException e) {
			throw new IRException("Error when converting documents to " + UserDTO.class, e);
		}
	}

	private Collection<RouteDTO> convertToRouteDTO(UserDTO user, TopDocs docs, int count) throws IRException {
		Collection<RouteDTO> result = new ArrayList<RouteDTO>();
		try {
			for (int i = 0; i < Math.min(count, docs.totalHits); i++) {
				String id = reader.document(docs.scoreDocs[i].doc).get(FieldsEnum.url.name());
				System.out.println("Convirtiendo ruta: " + id);
				Double distance = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get(
						FieldsEnum.PR_DISTANCE.name()));
				Boolean looped = Boolean.valueOf(reader.document(docs.scoreDocs[i].doc).get(FieldsEnum.PR_LOOP.name()));
				Double maxElevation = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get(
						FieldsEnum.PR_ELEVATION_MAX.name()));
				Double minElevation = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get(
						FieldsEnum.PR_ELEVATION_MIN.name()));
				Double elevationGainUp = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get(
						FieldsEnum.PR_ELEVATION_GAIN_UP_HILL.name()));
				Double elevationGainDown = Double.valueOf(reader.document(docs.scoreDocs[i].doc).get(
						FieldsEnum.PR_ELEVATION_GAIN_DOWN_HILL.name()));
				String latitude = reader.document(docs.scoreDocs[i].doc).get(FieldsEnum.PR_LATITUDE.name());
				String longitude = reader.document(docs.scoreDocs[i].doc).get(FieldsEnum.PR_LONGITUDE.name());
				String name = reader.document(docs.scoreDocs[i].doc).get("title");
				result.add(new RouteDTO(id, name, distance, looped, maxElevation, minElevation, elevationGainUp,
						elevationGainDown, user.getRouteIds().contains(id), latitude, longitude));
			}
			close();
			return (result);
		} catch (IOException e) {
			throw new IRException("Error when converting documents to " + UserDTO.class, e);
		}
	}

	// TODO only for testing
	private void printSearchDoc(int max, IndexReader reader, TopDocs topDocs) throws IOException {
		for (int i = 0; i < Math.min(max, topDocs.totalHits); i++) {
			System.out.println(topDocs.scoreDocs[i].doc);
			System.out.println(" -- score: " + topDocs.scoreDocs[i].score);
			printDoc(reader.document(topDocs.scoreDocs[i].doc));
		}
	}

	// TODO only for testing
	private void printDoc(Document document) {
		System.out.println("" + FieldsEnum.url + " = " + document.get(FieldsEnum.url.name()));
		System.out.println("" + FieldsEnum.PR_DISTANCE + " = " + document.get(FieldsEnum.PR_DISTANCE.name()));
		System.out.println("" + FieldsEnum.PR_ELEVATION_GAIN_UP_HILL + " = "
				+ document.get(FieldsEnum.PR_ELEVATION_GAIN_UP_HILL.name()));
		System.out.println("" + FieldsEnum.PR_ELEVATION_MAX + " = " + document.get(FieldsEnum.PR_ELEVATION_MAX.name()));
		System.out.println("" + FieldsEnum.PR_ELEVATION_GAIN_DOWN_HILL + " = "
				+ document.get(FieldsEnum.PR_ELEVATION_GAIN_DOWN_HILL.name()));
		System.out.println("" + FieldsEnum.PR_ELEVATION_MIN + " = " + document.get(FieldsEnum.PR_ELEVATION_MIN.name()));
		System.out.println("" + FieldsEnum.PR_LOOP + " = " + document.get(FieldsEnum.PR_LOOP.name()));
		System.out.println(" ---------------------- ");
	}

}
