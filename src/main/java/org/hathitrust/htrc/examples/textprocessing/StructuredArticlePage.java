package org.hathitrust.htrc.examples.textprocessing;

import org.hathitrust.htrc.textprocessing.runningheaders.java.PageStructureParser.StructuredPage;

public class StructuredArticlePage extends ArticlePage implements StructuredPage {
    protected final int numHeaderLines;
    protected final int numFooterLines;

    public StructuredArticlePage(ArticlePage articlePage, int numHeaderLines, int numFooterLines) {
        super(articlePage.id, articlePage.lines);
        this.numHeaderLines = numHeaderLines;
        this.numFooterLines = numFooterLines;
    }

    @Override
    public int numHeaderLines() {
        return numHeaderLines;
    }

    @Override
    public int numFooterLines() {
        return numFooterLines;
    }
}