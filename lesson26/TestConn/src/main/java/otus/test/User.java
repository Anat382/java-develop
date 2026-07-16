package otus.test;
import java.util.ArrayList;
import java.util.List;


class User {
    private int id;
    private String email;
    private String password;
    private List<Role> roles = new ArrayList<>();

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }
}