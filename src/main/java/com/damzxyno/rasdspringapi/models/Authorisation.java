package com.damzxyno.rasdspringapi.models;

import java.util.ArrayList;
import java.util.List;

public class Authorisation {
    private SecureModel staticMod = new SecureModel();
    private List<SecureModel> relativeMod = new ArrayList<>();


    public SecureModel getStaticMod() {
        return staticMod;
    }


    public List<SecureModel> getRelativeMod() {
        return relativeMod;
    }

    public void setRelativeMod(List<SecureModel> relativeMod) {
        this.relativeMod = relativeMod;
    }
}
