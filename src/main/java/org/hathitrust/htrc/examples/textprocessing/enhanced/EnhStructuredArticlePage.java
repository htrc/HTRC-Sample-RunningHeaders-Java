package org.hathitrust.htrc.examples.textprocessing.enhanced;

import org.hathitrust.htrc.data.ops.PageOps;
import org.hathitrust.htrc.data.ops.StructuredPageOps;
import org.hathitrust.htrc.examples.textprocessing.StructuredArticlePage;

public class EnhStructuredArticlePage extends StructuredArticlePage implements PageOps, StructuredPageOps {
    public EnhStructuredArticlePage(StructuredArticlePage articlePage) {
        super(articlePage, articlePage.numHeaderLines(), articlePage.numFooterLines());
    }
}
