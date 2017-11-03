package com.app_team11.conquest.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;

import com.app_team11.conquest.R;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Continent Model class with variables Continent Name,Score & Continent Owner
 * Created by Vasu on 06-10-2017.
 *
 * @version 1.0.0
 */
public class Continent {

    private String contName;
    private int score;
    private Player contOwner;
    private int contColor;
    private static final AtomicInteger count = new AtomicInteger(0);
    private int continentID;

    public Continent() {

    }

    /**
     * Returns the name od continent
     * @return continent name
     */
    public String getContName() {
        return contName;
    }

    /**
     * Sets the name of the continent
     * @param contName
     */
    public void setContName(String contName) {
        this.contName = contName;
    }

    /**
     * Returns the score
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the Owner of Continent
     * @return continent owner
     */
    public Player getContOwner() {
        return contOwner;
    }

    /**
     * Sets the Owner of the Continent
     * @param contOwner
     */
    public void setContOwner(Player contOwner) {
        this.contOwner = contOwner;
    }

    /**
     * Initialize class members
     *
     * @param contName
     * @param score
     */
    public Continent(String contName, int score, Context context) {
        this.contName = contName;
        this.score = score;
        if (context != null)
            setRandomColorToContinent(context);
    }

    /**
     * Parametrized constructor
     *
     * @param ContName
     */
    public Continent(String ContName, Context context) {
        this.contName = getContName();
        setRandomColorToContinent(context);
    }

    public Continent(String ContName, int score) {
        this.contName = getContName();
        this.score = score;
    }

    public Continent copyContinent() {
        Continent continent = new Continent();
        continent.setContName(this.getContName());
        continent.setScore(this.getScore());
        continent.setContOwner(this.getContOwner());
        continent.setContColor(this.getContColor());
        return continent;
    }

    public void setRandomColorToContinent(Context context) {
        continentID = count.incrementAndGet();
        int[] continentColor = context.getResources().getIntArray(R.array.continentColor);
        this.setContColor(continentColor[(continentID % continentColor.length)]);
    }

    public int getContColor() {
        return contColor;
    }

    public void setContColor(int contColor) {
        this.contColor = contColor;
    }
}
