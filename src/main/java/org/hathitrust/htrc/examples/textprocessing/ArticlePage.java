package org.hathitrust.htrc.examples.textprocessing;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;
import org.hathitrust.htrc.textprocessing.runningheaders.Page;
import scala.collection.IndexedSeq;
import scala.collection.JavaConverters;

public class ArticlePage implements Page {
    protected final List<String> lines;
    protected final int id;

    public ArticlePage(int id, String text) {
        BufferedReader br = new BufferedReader(new StringReader(text));
        this.id = id;
        this.lines = br.lines().collect(Collectors.toList());
    }

    public ArticlePage(int id, List<String> lines) {
        this.id = id;
        this.lines = lines;
    }

    @Override
    public IndexedSeq<String> textLines() {
        return JavaConverters.asScalaIterator(lines.iterator()).toIndexedSeq();
    }

    public int getId() {
        return id;
    }
}