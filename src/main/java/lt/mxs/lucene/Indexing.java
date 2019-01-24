package lt.mxs.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexing {

    public static Directory createDirectory() {
        return new ByteBuffersDirectory();
    }

    public static IndexWriter createIndexWriter(Directory directory) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(createAnalyzer());
        config.setMergeScheduler(new SerialMergeScheduler());
        return new IndexWriter(directory, config);
    }

    public static Analyzer createAnalyzer() {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Map<String, Analyzer> perField = new HashMap<>();
        perField.put("phoneticName", new PhoneticAnalyzer());

        return new PerFieldAnalyzerWrapper(analyzer, perField);
    }

    public static List<String> parse(Analyzer analyzer, String field, String keywords) throws IOException {
        try (TokenStream stream = analyzer.tokenStream(field, keywords)) {
            stream.reset();
            List<String> result = new ArrayList<>();
            while (stream.incrementToken()) {
                result.add(new String(stream.getAttribute(CharTermAttribute.class).buffer()));
            }
            stream.end();
            return result;
        }
    }

    private Indexing() {
    }
}
