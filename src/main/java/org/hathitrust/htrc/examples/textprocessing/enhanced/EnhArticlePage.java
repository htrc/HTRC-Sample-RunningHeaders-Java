package org.hathitrust.htrc.examples.textprocessing.enhanced;

import org.hathitrust.htrc.data.ops.PageOps;
import org.hathitrust.htrc.examples.textprocessing.ArticlePage;

public class EnhArticlePage extends ArticlePage implements PageOps {
    public EnhArticlePage(ArticlePage articlePage) {
        super(articlePage.getId(), articlePage.text());
    }
}
