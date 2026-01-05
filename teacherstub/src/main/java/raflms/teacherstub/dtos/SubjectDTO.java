package raflms.teacherstub.dtos;

public class SubjectDTO {

    private String fullName; // Objektno orijentisano programiranje

    private String shortName; // OOP

    public SubjectDTO() {
    }

    public SubjectDTO(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
