package com.peltashield.inferno.model.tournament;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier on 8/13/15.
 */
public class PlayerLite {

    protected String name;
    protected int score = 0;
    protected int matchScore = 0;
    protected int won = 0;
    protected int drew = 0;
    protected int lost = 0;
    protected int wins = 0;
    protected int looses = 0;
    protected boolean freeOnAMatch = false;
    protected List<String> playedWith = new ArrayList<>();
    protected List<String> results = new ArrayList<>();

    public PlayerLite(String name) {
        this.name = name;
    }

    public void updateDraw(int matchWins, boolean add) {
        drew += add ? 1 : -1;
        updateScore(1, matchWins, matchWins, add);
    }

    public void updateWin(int matchWins, int matchLooses, boolean add) {
        won += add ? 1 : -1;
        updateScore(3, matchWins, matchLooses, add);
    }

    public void addBye() {
        updateWin(2, 0, true);
    }

    public void updateLoose(int matchWins, int matchLooses, boolean add) {
        lost += add ? 1 : -1;
        updateScore(0, matchWins, matchLooses, add);
    }

    protected void updateScore(int matchScore, int matchWins, int matchLooses, boolean add) {
        score +=    add ? matchScore   : -matchScore;
        wins +=     add ? matchWins    : -matchWins;
        looses +=   add ? matchLooses  : -matchLooses;
        updateMatchScore(matchScore, matchWins, matchLooses, add);
    }

    protected void updateMatchScore(int score, int matchWins, int matchLooses, boolean add) {
        int updateScore = score * 10000 + matchWins * 100 - matchLooses;
        matchScore += add ? updateScore : -updateScore;
    }

    public boolean isEqualTo(PlayerLite player) {
        return name.equals(player.name);
    }

    protected String matchWith(PlayerLite player) {
        int i = playedWith.indexOf(player.getName());
        if (i >= 0) {
            return results.get(i);
        } else {
            return null;
        }
    }

    public boolean playedWith(PlayerLite player) {
        return matchWith(player) != null;
    }

    public boolean wasFreeOnAMatch() {
        return freeOnAMatch;
    }
    public void setFreeOnAMatch() {
        this.freeOnAMatch = true;
    }

    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
    public int getWon() {
        return won;
    }
    public int getDrew() {
        return drew;
    }
    public int getLost() {
        return lost;
    }
    public int getWins() {
        return wins;
    }
    public int getLooses() {
        return looses;
    }

    public int getMatchScore() {
        return matchScore;
    }

}
