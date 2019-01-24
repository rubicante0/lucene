package lt.mxs.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class Application {

    public static void main(String[] args) throws IOException {
        new Application().work();
    }

    private Document createDocument(Person person) {
        Document document = new Document();
        document.add(new TextField("name", person.getName(), Field.Store.NO));
        document.add(new TextField("phoneticName", person.getName(), Field.Store.NO));
        return document;
    }

    private void buildIndex(Directory directory) throws IOException {
        List<Person> persons = Persons.buildPersons();
        try (IndexWriter writer = Indexing.createIndexWriter(directory)) {
            for (Person person : persons) {
                writer.addDocument(createDocument(person));
            }
            writer.commit();
        }
    }

    private void performSearch(Directory directory, Query query) throws IOException {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs topDocs = searcher.search(query, 100);
            System.out.println("Number of search results: " + topDocs.scoreDocs.length);
            for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                int documentId = topDocs.scoreDocs[i].doc;
                Document foundDocument = searcher.doc(documentId);
                System.out.println(foundDocument);
            }
        }
    }

    private void work() throws IOException {
        try (Directory directory = Indexing.createDirectory()) {
            buildIndex(directory);
            performSearch(directory, new TermQuery(new Term("name", "alpha")));
        }
    }
}
