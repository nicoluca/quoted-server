package org.nico.quotedserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    @Column(name = "last_edited")
    private Timestamp lastEdited;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "source_id")
    @JsonIncludeProperties({"id"})
    private Source source;

    public Quote(String text, Source source) {
        this.text = text;
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return text.equals(quote.text) &&
                source.equals(quote.source);
    }

}
