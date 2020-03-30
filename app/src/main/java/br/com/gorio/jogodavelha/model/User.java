package br.com.gorio.jogodavelha.model;

public class User implements Comparable<User> {
    private String name, pushId;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    @Override
    public int compareTo(User another) {
        return name.compareTo(another.name);
    }
}
