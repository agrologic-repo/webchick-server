package com.agrologic.app.model;



import com.agrologic.app.web.CellinkState;
import com.agrologic.app.web.UserRole;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Title: Login - represents all properties of a Login in DBManager application
 * Description:
 * Copyright:    Copyright (c) 2008
 * @author Valery Manakhimov
 * @version 1.0
 */
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<CellinkDto>  cellinks;
    private String            company;
    private String            email;
    private String            firstName;
    private Long              id;
    private String            lastName;
    private String            login;
    private String            password;
    private String            phone;
    private Integer           role;
    private Boolean           toValidate;

    public UserDto() {
        cellinks   = new ArrayList<CellinkDto>();
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
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

    public List<CellinkDto> getCellinks() {
        return cellinks;
    }

    public void setCellinks(List<CellinkDto> cellinks) {
        this.cellinks = cellinks;
    }

    public void addCellink(CellinkDto cellink) {
        cellinks.add(cellink);
    }

    public List<CellinkDto> getOnlineCellinks() {
        List<CellinkDto> onlineCellinks = new ArrayList<CellinkDto>();

        for (CellinkDto c : cellinks) {
            if (c.getState() == CellinkState.STATE_ONLINE) {
                onlineCellinks.add(c);
            }
        }

        return onlineCellinks;
    }

    public List<CellinkDto> getOfflineCellinks() {
        List<CellinkDto> offlineCellinks = new ArrayList<CellinkDto>();

        for (CellinkDto c : cellinks) {
            if (c.getState() == CellinkState.STATE_OFFLINE) {
                offlineCellinks.add(c);
            }
        }

        return offlineCellinks;
    }

    public String roleText() {
        String roleText = "";

        switch (getRole()) {
        case UserRole.ADMINISTRATOR :
            roleText = "Administrator";

            break;

        case UserRole.ADVANCED :
            roleText = "Advanced";

            break;

        case UserRole.REGULAR :
            roleText = "Regular";

            break;

        default :
            break;
        }

        return roleText;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof UserDto)) {
            return false;
        }

        UserDto user = (UserDto) o;

        return new EqualsBuilder().append(this.login, user.login).append(this.password,
                                          user.password).append(this.role, user.role).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.login).append(this.password).append(this.role).toHashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.firstName).append(" ").append(this.lastName).toString();
    }

    public static void main(String args[]) {
        String    sdate = new String("1/11/2011");
        Date      date  = new Date(sdate);
        Timestamp tdate = new Timestamp(date.getTime());

        System.out.println(tdate);

        String s = new SimpleDateFormat("dd/MM/yyyy").format(date);

        System.out.println(s);
    }
}



