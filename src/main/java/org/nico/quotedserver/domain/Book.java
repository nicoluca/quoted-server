package org.nico.quotedserver.domain;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Needed for JPA
@Getter @Setter
@Entity
@DiscriminatorValue("book")
public class Book extends Source {

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER) // TODO Changed from CascadeType.ALL to CascadeType.REMOVE
    @JoinColumn(name = "author_id")
    @Nonnull
    // @JsonIncludeProperties({"id"})
    private Author author;

    @Column(name = "coverpath")
    private String coverPath; // TODO Currently unused, intention to be used in future export to MD feature

    @Column(name = "isbn")
    private String isbn; // TODO Currently unused, intention to be used in future export to MD feature

    public Book(String title, Author author) {
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
