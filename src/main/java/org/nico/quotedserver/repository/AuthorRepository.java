package org.nico.quotedserver.repository;

import org.nico.quotedserver.domain.Author;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    @Query("SELECT a FROM Author a " +
            "WHERE UPPER(a.firstName) LIKE CONCAT('%', UPPER(?1), '%') " +
            "AND UPPER(a.lastName) LIKE CONCAT('%', UPPER(?2), '%')")
    Optional<Author> findByName(String firstName, String lastName);

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM author
            WHERE author.id NOT IN (
                SELECT DISTINCT author_id
                FROM book
            );
            """
            , nativeQuery = true)
    void deleteAuthorsWithNoBooks();

}
