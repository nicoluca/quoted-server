package org.nico.quotedserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor // Needed for JPA
@Getter @Setter

@Entity
@DiscriminatorValue("article")
public class Article extends Source {

    @Column(name = "url")
    private String url;

    @CreationTimestamp
    @Column(name = "last_visited", nullable = false)
    private Timestamp lastVisited = new Timestamp(System.currentTimeMillis());

    @Builder
    public Article(String title, String url) {
        super(title);

        if (title == null || title.isBlank())
            super.setTitle(url);

        this.url = url;
    }

    @Override
    public String toString() {
        if (this.getTitle().equals(this.url))
            return this.getTitle();
        else
            return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        String thisUrl = removeLastSlash(this.url);
        String otherUrl = removeLastSlash(article.url);
        return thisUrl.equalsIgnoreCase(otherUrl);
    }

    private static String removeLastSlash(String url) {
        return url.replaceAll("/$", "");
    }

    @Override
    public String originToString() {
        return this.url;
    }
}
