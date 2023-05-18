package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Quote;
import org.nico.quotedserver.domain.Source;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "quote", types = { Quote.class })
public interface QuoteProjection {
    // Using this projection, author will be embedded in book
    long getId();
    String getText();
    Source getSource();
}
