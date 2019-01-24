package lt.mxs.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.phonetic.BeiderMorseFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.Collections;

public class PhoneticAnalyzer extends Analyzer {
    private static final BeiderMorseFilterFactory factory = new BeiderMorseFilterFactory(Collections.emptyMap());

    protected TokenStreamComponents createComponents(String fieldName) {
        final StandardTokenizer source = new StandardTokenizer();
        TokenStream filter = new LowerCaseFilter(source);
        filter = factory.create(filter);
        return new TokenStreamComponents(source, filter);
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }
}
