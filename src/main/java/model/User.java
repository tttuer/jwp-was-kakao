package model;

import com.github.jknack.handlebars.internal.lang3.StringUtils;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;

        checkIsValidUser();
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    private void checkIsValidUser() {
        if (StringUtils.isBlank(userId)) {
            throw new IllegalArgumentException("Please check userId");
        }

        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Please check password");
        }

        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Please check name");
        }

        if (isInvalidEmail()) {
            throw new IllegalArgumentException("Please check email");
        }
    }

    private boolean isInvalidEmail() {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return !m.matches();
    }

    public boolean same(String password) {
        return this.password.equals(password);
    }
}
