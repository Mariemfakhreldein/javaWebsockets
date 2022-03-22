package gov.iti.jets;

public class MessageDto {
    String userName;
    String content;
    String gender;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
