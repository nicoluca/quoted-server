package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Quote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Long> {
    @Query(nativeQuery = true, value="SELECT * FROM quote WHERE id != ?1 ORDER BY RANDOM() LIMIT 1;") // PostgreSQL, RANDOM() - might be RAND() for other DBs
    Optional<Quote> findRandomQuote(long lastQuoteId);

    @Query("SELECT q FROM Quote q WHERE q.source.id = ?1")
    List<Quote> findBySourceId(long sourceId);

    @Query("""
                SELECT q
                FROM Quote q
                         LEFT JOIN q.source s
                         LEFT JOIN Book b ON s.id = b.id
                         LEFT JOIN Article a ON s.id = a.id
                         LEFT JOIN b.author auth
                WHERE UPPER(q.text) LIKE CONCAT('%', UPPER(?1), '%')
                   OR UPPER(s.title) LIKE CONCAT('%', UPPER(?1), '%')
                   OR UPPER(CONCAT(auth.firstName, auth.lastName)) LIKE CONCAT('%', UPPER(?1), '%')
                   OR UPPER(a.url) LIKE CONCAT('%', UPPER(?1), '%')
                """)
    List<Quote> findByTextContaining(String searchString);

}
