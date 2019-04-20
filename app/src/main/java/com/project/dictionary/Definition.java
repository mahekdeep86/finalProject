package com.project.dictionary;

/**
 * This class models a a single definition of a search term
 */
public class Definition {
    private final String hw;
    private final String pr;
    private final String fl;
    private final String def;
    private final int id;
    private final boolean online;

    /**
     * Class constructor which creates a new Definition object
     * @param hw
     * @param pr
     * @param fl
     * @param def
     */
    public Definition(String hw, String pr, String fl, String def) {
        this.hw = hw;
        this.pr = pr;
        this.fl = fl;
        this.def = def;
        this.id = 0;
        this.online = true;
    }
    public Definition(int id, String hw, String pr, String fl, String def) {
        this.id = id;
        this.hw = hw;
        this.pr = pr;
        this.fl = fl;
        this.def = def;
        this.online = false;
    }

    /**
     * Returns the head word of the definition
     * @return
     */
    public String getHw() {
        return hw;
    }

    /**
     * Returns the pronunciation of the word
     * @return
     */
    public String getPr() {
        return pr;
    }

    public String getFl() {
        return fl;
    }

    /**
     * Returns the definitions of the term
     * @return
     */
    public String getDef() {
        return def;
    }

    /**
     * Returns the id of the word
     * @return
     */
    public int getId() {
        return id;
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        return "Hw = " + hw + " Pr = " + pr + " fl = " + fl + " def: " + def.length();
    }
}
