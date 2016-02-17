package com.peltashield.inferno.model.tournament;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by javier on 8/13/15.
 */
public class TournamentLite {

    protected List<PlayerLite> players = new ArrayList<>();
    protected String[][][] rounds;

    //######################//
    //### INITIALIZATION ###//
    //######################//
    public void startTournament() {
        if (rounds == null) {
            rounds = new String[getQtyRounds()][][];
        }
    }

    protected int getQtyRounds() {
        int qtyPlayers = players.size();
        int rounds = 0;
        if (qtyPlayers < 9) {
            rounds = 3;
        } else if (qtyPlayers < 17) {
            rounds = 4;
        } else if (qtyPlayers < 33) {
            rounds = 5;
        } else if (qtyPlayers < 65) {
            rounds = 6;
        } else if (qtyPlayers < 129) {
            rounds = 7;
        }
        return rounds;
    }

    public void sortByMatchScore() {
        final Random random = new Random();
        Collections.shuffle(players, random);
        Collections.sort(players, new Comparator<PlayerLite>() {
            @Override
            public int compare(PlayerLite lhs, PlayerLite rhs) {
                return rhs.matchScore - lhs.matchScore;
            }
        });
        List<PlayerLite> newList = new ArrayList<>(players.size());
        for (int i = 0; i < players.size(); i++) {
            PlayerLite playerI = players.get(i);
            for (int j = i + 1; j < players.size() &&
                    playerI.getMatchScore() == players.get(j).getMatchScore(); j++) {
                PlayerLite playerJ = players.get(j);
                String winner = playerI.matchWith(playerJ);
                if (playerI.getName().equals(winner) || playerJ.getName().equals(winner)) {
                    boolean win = playerI.getName().equals(winner);
                    int posI = newList.indexOf(playerI);
                    int posJ = newList.indexOf(playerJ);
                    boolean containsI = posI != -1;
                    boolean containsJ = posJ != -1;
                    if (containsI) {
                        if (containsJ) {
                            if (win ? posI > posJ : posJ > posI) {
                                if (win) {
                                    newList.remove(posI);
                                    newList.add(posJ, playerI);
                                } else {
                                    newList.remove(posJ);
                                    newList.add(posI, playerJ);
                                }
                            }
                        } else {
                            newList.add(win ? posI + 1 : posI, playerJ);
                        }
                    } else {
                        if (containsJ) {
                            newList.add(win ? posI : posI + 1, playerI);
                        } else {
                            newList.add(win ? playerI : playerJ);
                            newList.add(win ? playerJ : playerI);
                        }
                    }
                }
            }
            if (!newList.contains(playerI)) {
                newList.add(random.nextInt(newList.size() - i + 1) + i, playerI);
            }
        }
        players = newList;
    }

    public boolean containsPlayerName(String name) {
        boolean contains = false;
        for (PlayerLite player : players) {
            if (player.getName().equals(name)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public void addPlayer(PlayerLite player) {
        players.add(player);
    }

    public void removePlayer(int playerPosition) {
        players.remove(playerPosition);
    }

    public void clear() {
        players.clear();
        rounds = null;
    }

    public List<PlayerLite> getPlayers() {
        return this.players;
    }

    public String[][][] getRounds() {
        return this.rounds;
    }

    public void setRounds(String[][][] rounds) {
        this.rounds = rounds;
    }

    public List<PlayerLite> getSortedPlayers() {
        sortByMatchScore();
        return this.players;
    }

    //######################//
    //### ROUND          ###//
    //######################//
    public String[][] getLastRound() {
        return getRounds()[getLastRoundPosition()];
    }

    public String[][] generateRound() {
        String[][][] rounds = getRounds();
        int pos = getLastRoundPosition();
        rounds[pos] = generateMatches();
        return rounds[pos];
    }

    protected int getLastRoundPosition() {
        String[][][] rounds = getRounds();
        int i = rounds.length - 1;
        while (rounds[i] == null) {
            i--;
        }
        return i;
    }

    //######################//
    //### MATCH          ###//
    //######################//
    protected String[][] generateMatches() {
        sortByMatchScore();
        List<PlayerLite> players = getPlayers();
        int freePlayer = -1;
        boolean freePlayerPresent = players.size() % 2 == 1;
        if (freePlayerPresent) {
            freePlayer = players.size() - 1;
            while (players.get(freePlayer).wasFreeOnAMatch()) {
                freePlayer--;
            }
            players.get(freePlayer).setFreeOnAMatch();
            players.get(freePlayer).addBye();
            sortByMatchScore();
        }

        List<Integer> onRound = new ArrayList<>(players.size() - (freePlayerPresent ? 1: 0));
        List<Point> matchesDisolved = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (i == freePlayer || onRound.contains(i)) {
                continue;
            }
            PlayerLite player1 = players.get(i);
            boolean match = false;
            for (int j = i+1; !match && j < players.size(); j++) {
                if (j == freePlayer || onRound.contains(j)) {
                    continue;
                }
                PlayerLite player2 = players.get(j);
                if (!player1.playedWith(player2)) {
                    onRound.add(i);
                    onRound.add(j);
                    match = true;
                }
            }
            if (!match) {
                for (int j = players.size() - 1; !match && j >= 0; j--) {
                    if (j == freePlayer) {
                        continue;
                    }
                    PlayerLite player2 = players.get(j);
                    if (!player1.playedWith(player2) && j != i &&
                            !contains(matchesDisolved, i, j)) {
                        int posJ = onRound.indexOf(j);
                        int posI = posJ % 2 == 1 ? posJ-1 : posJ+1; //match i previously set for j
                        int iToRemove = onRound.get(posI);
                        onRound.set(posI, i);
                        match = true;
                        i = iToRemove - 1;
                        matchesDisolved.add(new Point(iToRemove, j));
                    }
                }
            }
            if (!match) {
                throw new RuntimeException("There are no new matchs to generate");
            }
        }

        String[][] round = new String[players.size() / 2][];
        for (int i = 1; i < onRound.size(); i+=2) {
            String[] match = new String[5];
            setPlayer0Name(match, players.get(onRound.get(i - 1)).getName());
            setPlayer1Name(match, players.get(onRound.get(i)).getName());
            setScore0(match, "0");
            setScore1(match, "0");
            round[i/2] = match;
        }
        return round;
    }

    protected boolean contains(List<Point> ps, int i, int j) {
        for (Point p : ps) {
            if (p.x == i && p.y == j || p.x == j && p.y == i) {
                return true;
            }
        }
        return false;
    }

    public PlayerLite getPlayer0(String[] match) {
        PlayerLite player = null;
        String name = getPlayer0Name(match);
        for (PlayerLite p : players) {
            if (p.getName().equals(name)) {
                player = p;
                break;
            }
        }
        return player;
    }

    public PlayerLite getPlayer1(String[] match) {
        PlayerLite player = null;
        String name = getPlayer1Name(match);
        for (PlayerLite p : players) {
            if (p.getName().equals(name)) {
                player = p;
                break;
            }
        }
        return player;
    }

    public void setResult(String[] match, String score0, String score1) {
        setScore0(match, score0);
        setScore1(match, score1);
        updateResult(match, true);
        setEnded(match, true);
    }

    public void unsetResult(String[] match) {
        updateResult(match, false);
        setScore0(match, "0");
        setScore1(match, "0");
        setEnded(match, false);
    }

    protected void updateResult(String[] match, boolean add) {
        String score0 = getScore0(match);
        String score1 = getScore1(match);
        int s0 = Integer.parseInt(score0);
        int s1 = Integer.parseInt(score1);
        switch (score0.compareTo(score1)) {
            case 0:
                getPlayer0(match).updateDraw(s0, add);
                getPlayer1(match).updateDraw(s1, add);
                break;
            case 1:
                getPlayer0(match).updateWin(s0, s1, add);
                getPlayer1(match).updateLoose(s1, s0, add);
                break;
            case -1:
                getPlayer0(match).updateLoose(s0, s1, add);
                getPlayer1(match).updateWin(s1, s0, add);
                break;
        }
    }

    public static boolean hasEnded(String[][] round) {
        boolean ended = true;
        for (String[] match : round) {
            if (!hasEnded(match)) {
                ended = false;
                break;
            }
        }
        return ended;
    }

    //######################//
    //### ACCESSORS      ###//
    //######################//
    protected static void setPlayer0Name(String[] match, String player) {
        match[0] = player;
    }

    protected static void setPlayer1Name(String[] match, String player) {
        match[1] = player;
    }

    public static String getPlayer0Name(String[] match) {
        return match[0];
    }

    public static String getPlayer1Name(String[] match) {
        return match[1];
    }

    public static void setEnded(String[] match, boolean end) {
        match[4] = end ? "y" : null;
    }

    public static boolean hasEnded(String[] match) {
        return "y".equals(match[4]);
    }

    public static void setScore0(String[] match, String score) {
        match[2] = score;
    }

    public static void setScore1(String[] match, String score) {
        match[3] = score;
    }

    public static String getScore0(String[] match) {
        return match[2];
    }

    public static String getScore1(String[] match) {
        return match[3];
    }

}
