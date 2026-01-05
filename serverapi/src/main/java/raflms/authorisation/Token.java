package raflms.authorisation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Token {
    @Id
    private String id;
    private String value;

    public Token() {
    }

    public Token(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
