package org.hathitrust.htrc.examples.textprocessing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.hathitrust.htrc.data.ops.TextOptions;
import org.hathitrust.htrc.examples.textprocessing.enhanced.EnhStructuredArticlePage;
import org.hathitrust.htrc.textprocessing.runningheaders.PageStructure;
import org.hathitrust.htrc.textprocessing.runningheaders.java.PageStructureParser;
import org.hathitrust.htrc.textprocessing.runningheaders.java.PageStructureParser.StructureParserConfig;
import scala.Enumeration.Value;
import scala.collection.JavaConverters;
import tdm.featureextractor.java.PageFeatureExtractor;
import tdm.featureextractor.java.features.PageFeatures;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) throws Exception {
        int numPages = 9;
        String volName = "vol1";

        // load a sample set of article pages
        List<ArticlePage> pages = new ArrayList<>(numPages);
        for (int i = 1; i <= numPages; i++) {
            String pageText;
            try (InputStream pageStream = Main.class.getResourceAsStream(String.format("/%s/%d.txt", volName, i))) {
                pageText = getTextFromStream(pageStream);
            }

            ArticlePage articlePage = new ArticlePage(i, pageText);
            pages.add(articlePage);
        }

        // do the running header/footer detection (aka page structure parsing)
        StructureParserConfig parserConfig = PageStructureParser.defaultConfig();
        List<StructuredArticlePage> structuredPages =
            PageStructureParser.parsePageStructure(pages, parserConfig, StructuredArticlePage::new);

        // now we have StructuredArticlePage objects that have header/footer information within

        // print all identified headers
        System.out.println("==== ALL HEADERS ====");
        structuredPages.stream().map(PageStructure::header).forEach(System.out::println);

        // print the body of the second page
        StructuredArticlePage secondPage = structuredPages.get(1);  // zero-based index
        System.out.println("==== SECOND PAGE BODY ====");
        System.out.println(secondPage.body());

        // we could perform feature extraction on these pages also, like so:
        // (created a new class TdmPageFeatures which captures the fact that pages contain a "seq" attribute for TDM)
        List<TdmPageFeatures> features =
            structuredPages.stream()
                           .map(page -> {
                               PageFeatures standardFeatures = PageFeatureExtractor.extractPageFeatures(page);
                               return TdmPageFeatures.withSeq(String.format("%08d", page.id), standardFeatures);
                           })
                           .collect(Collectors.toList());

        // Document is a class that captures the format of the EF structure for TDM
        Document doc = new Document(volName, new DocumentFeatures(features));

        // and print out the second page features as JSON
        System.out.println("==== EXTRACTED FEATURES FROM THE DOCUMENT ====");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(System.out, doc);

        // but wait, there's more!
        // we can augment our existing article model(s) to include some useful text operations,
        // like end-of-line de-hyphenation, and others
        // let's create an "enhanced" page

        EnhStructuredArticlePage enhSecondPage = new EnhStructuredArticlePage(secondPage);

        // now let's say we want to perform end-of-line de-hyphenation on the body of the page;
        // we'd also like to trim the text on each line and remove the empty lines
        // so that de-hyphenation works on all kinds of text

        List<Value> textOptions = Arrays.asList(
            TextOptions.TrimLines(),
            TextOptions.RemoveEmptyLines(),
            TextOptions.DehyphenateAtEol()
        );

        // need some black magic to make Scala happy (the JavaConverters part)
        String dehyphenatedBody = enhSecondPage.body(JavaConverters.asScalaBuffer(textOptions));

        System.out.println("==== DE-HYPHENATED SECOND PAGE BODY ====");
        System.out.println(dehyphenatedBody);
    }

    public static String getTextFromStream(InputStream stream) {
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
