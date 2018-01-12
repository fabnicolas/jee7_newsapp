package ejb;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Finalgalaxy
 * 
 * Example code.
 * Using jdbc/dbsrc_newsapp as DB source. \m/
 */
@Entity             // Let's mark this class as an Entity.
public class NewsEntity implements Serializable {
    private static final long serialVersionUID = 1L;    // Java ID for the object
    
    // Business data: id, title, body (of a news entity).
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;    // ID auto generated for the object inside the DB
    private String title;
    private String body;

    // Setters.
    public void setId(Long id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setBody(String body) {this.body = body;}
    
    // Getters.
    public Long getId()        {return id;}
    public String getTitle() {return title;}
    public String getBody() {return body;}
    
    // Object hashCode, equals, toString
    @Override public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    @Override public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewsEntity)) {
            return false;
        }
        NewsEntity other = (NewsEntity) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
    @Override public String toString() {
        return "ejb.NewsEntity[ id=" + id + " ]";
    }
    
}
