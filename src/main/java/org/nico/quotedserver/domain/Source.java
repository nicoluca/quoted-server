package org.nico.quotedserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nico.quotedserver.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

// Lombok annotations
@NoArgsConstructor // Needed for JPA
@AllArgsConstructor
@Getter @Setter

// JPA annotations
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // JOINED to to have a dedicated source table, TABLE_PER_CLASS to have a table per subclass
@DiscriminatorColumn(name="type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Book.class, name = "book"),
        @JsonSubTypes.Type(value = Article.class, name = "article")
})
public abstract class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnoreProperties(ignoreUnknown = true) // If not available, ignore (used to retrieve article by url)
    private long id;

    @Column(name = "title")
    private String title;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "source")
    @JsonIgnore // If not, infinite recursion
    private Set<Quote> quotes;

    public Source(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source = (Source) o;
        return this.title.equals(source.title);
    }

    public abstract String originToString(); // TODO - better name?

    @Override
    public String toString() {
        return this.getTitle() + " (" + this.originToString() + ")";
    }

}
