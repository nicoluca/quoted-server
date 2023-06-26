package org.nico.quotedserver.domain;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor // Needed for JPA
@Getter @Setter

@Entity
@DiscriminatorValue("book")
public class Book extends Source {

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    @Nonnull
    @JsonIncludeProperties({"firstName", "lastName"})
    private Author author;

    @Column(name = "coverpath")
    private String coverPath;

    @Column(name = "isbn")
    private String isbn; // TODO Currently unused, intention to be used in future feature

    @Builder
    public Book(String title, @NonNull Author author) {
        super(title);
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return this.getTitle().equalsIgnoreCase(book.getTitle())
                && this.author.equals(book.author);
    }

    @Override
    public String originToString() {
        try {
            return this.getAuthor().getLastName() + ", " + this.getAuthor().getFirstName();
        } catch (NullPointerException e) {
            return "Unknown author";
        }
    }
}
