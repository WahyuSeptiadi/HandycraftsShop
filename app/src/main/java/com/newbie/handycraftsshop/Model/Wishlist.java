package com.newbie.handycraftsshop.Model;

import java.util.Dictionary;
import java.util.Map;

public class Wishlist {

    private Map<String, Object> foto;

    public Map<String, Object> getFoto() {
        return foto;
    }

    public void setFoto(Map<String, Object> foto) {
        this.foto = foto;
    }

    public Wishlist(Map<String, Object> foto) {
        this.foto= foto;
    }

    public Wishlist() {

    }
}
