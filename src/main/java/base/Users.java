package base;


import javax.persistence.*;

@Entity
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;
    @Column(unique = true, nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String fullname;

    @Enumerated(EnumType.ORDINAL)
    private UserRole userRole;


    public Users() {
    }

    public Users(String login, String password, String fullname, UserRole userRole) {
        this.login = login;
        this.password = password;
        this.fullname = fullname;
        this.userRole = userRole;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public UserRole getUserRole() {return userRole;}

    public void setUserRole(UserRole userRole) {this.userRole = userRole;}

    @Override
    public String toString() {
        return "id = " + userID +
                "\t login = " + login +
                "\t fullname = " + fullname+
                "\t role = " + userRole;
    }
}
