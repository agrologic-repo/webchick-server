package com.agrologic.app.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String company;
    private String email;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private String phone;
    private UserRole role;
    private Integer state;
    private Boolean toValidate;
    private Collection<Cellink> cellinks;

    public User() {
        cellinks = new ArrayList<Cellink>();
        toValidate = false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUserRole(Integer role) {
        this.role = UserRole.get(role);
    }

    public void setUserRole(UserRole userRole) {
        this.role = userRole;
    }

    public UserRole getUserRole() {
        return role;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Integer getState() {
        return state == null ? 0 : state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public boolean getValidate() {
        return toValidate;
    }

    public void setValidate(boolean toValidate) {
        this.toValidate = toValidate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Collection<Cellink> getCellinks() {
        return Collections.unmodifiableCollection(cellinks);
    }

    public void setCellinks(Collection<Cellink> list) {
        if (cellinks == null) {
            cellinks = new ArrayList<Cellink>();
        }

        for (Cellink c : list) {
            addCellink(c);
        }
    }

    public void addCellink(Cellink cellink) {
        cellinks.add(cellink);
    }

    public Collection<Cellink> getOnlineCellinks() {
        List<Cellink> onlineCellinks = new ArrayList<Cellink>();

        for (Cellink c : cellinks) {
            if (c.getState() == CellinkState.STATE_ONLINE) {
                onlineCellinks.add(c);
            }
        }

        return onlineCellinks;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;

        return new EqualsBuilder()
                .append(this.login, user.login)
                .append(this.password, user.password).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.login)
                .append(this.password).toHashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(" ID : ").append(id).append(" LOGIN :").append(login).append(
                " PASSWORD : ").append(password).append(" FIRST : ").append(firstName).append(" LAST : ").append(
                lastName).append(" PHONE : ").append(phone).append(" E-MAIL : ").append(email).toString();
    }
}


