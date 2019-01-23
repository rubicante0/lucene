package lt.mxs.lucene;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TieredMergePolicy;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Application {

    public static void main(String[] args) throws IOException {
        new Application().work();
    }

    private Directory buildDirectory() {
        return new ByteBuffersDirectory();
    }

    private IndexWriter buildIndexWriter(Directory directory) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig();
        config.setMergeScheduler(new SerialMergeScheduler());

        TieredMergePolicy mergePolicy = new TieredMergePolicy();
        mergePolicy.setMaxMergedSegmentMB(200);

        config.setMergePolicy(mergePolicy);

        return new IndexWriter(directory, config);
    }

    private String readResource(String name) throws IOException {
        try (InputStream inputStream = new ClassPathResource(name).getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

    private void indexDocuments(Directory directory) throws IOException {
        try (IndexWriter writer = buildIndexWriter(directory)) {
            String content = readResource("sample1.txt");

            Document document = new Document();
            document.add(new TextField("data", content, Field.Store.YES));

            writer.addDocument(document);
            writer.commit();
        }
    }

    private void performSearch(Directory directory) throws IOException {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);

            Query query = new TermQuery(new Term("data","eden"));
            TopDocs topDocs = searcher.search(query, 100);
            for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                int documentId = topDocs.scoreDocs[i].doc;
                Document foundDocument = searcher.doc(documentId);
                System.out.println(foundDocument.get("data"));
            }
        }
    }

    private void work() throws IOException {
        try (Directory directory = buildDirectory()) {
            indexDocuments(directory);
            performSearch(directory);
        }
    }
}
