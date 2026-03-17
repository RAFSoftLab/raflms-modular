package raflms.studentstub.api.datamodel;

public class AssignmentData {

    private String term;
    private String group;

    public AssignmentData(String term, String group) {
        this.term = term;
        this.group = group;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "AssignmentData{" +
                "term='" + term + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
