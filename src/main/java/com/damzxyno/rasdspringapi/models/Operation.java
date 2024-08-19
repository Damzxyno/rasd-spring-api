package com.damzxyno.rasdspringapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Operation {
    private boolean isAuthenticated;

    @JsonIgnore
    private boolean readOnly;

    public void setAuthorisationMod(Authorisation authorisationMod) {
        this.authorisationMod = authorisationMod;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly() {
        this.readOnly = true;
    }
    private Authorisation authorisationMod = new Authorisation();

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public Authorisation getAuthorisationMod() {
        return authorisationMod;
    }
}
