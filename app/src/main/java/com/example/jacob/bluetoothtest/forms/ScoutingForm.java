package com.example.jacob.bluetoothtest.forms;

import com.example.jacob.bluetoothtest.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ScoutingForm {
    public boolean crossedAutoLine = false;

    public Constants.Team team = Constants.Team.RED;

    public int teamNumber = 1;

    public int matchNumber = 0;

    public String scoutName = "";

    public Constants.GameMode currentMode = Constants.GameMode.AUTO;
    public Constants.GameAction currentAction = Constants.GameAction.OFFENCE;
    public Constants.DefenceType currentDefenceType = Constants.DefenceType.IDLE;

    public boolean matchOver = false;
    public boolean matchStarted = false;

    public TimePeriod climbPeriod = new TimePeriod();
    public ArrayList<TimePeriod> defenceTimes = new ArrayList<TimePeriod>();
    public ArrayList<TimePeriod> activeDefenceTimes = new ArrayList<TimePeriod>();

    public String defendedTeams = "";

    public double defenceTime = 0;
    public double activeDefenceTime = 0;
    public double climbTime = 0;

    public int farBalls = 0;
    public int farBallsShot = 0;
    public int tarMacBalls = 0;
    public int tarMacBallsShot = 0;
    public int lowCloseBalls = 0;
    public int lowCloseBallsShot = 0;
    public int highCloseBalls = 0;
    public int highCloseBallsShot = 0;
    public int protectedZoneBalls= 0;
    public int protectedZoneBallsShot = 0;

    public int autoBalls = 0;
    public int autoBallsShot = 0;

    public int rungLevel = 0;

    public int foulCounter;

    public Constants.Climb climb = Constants.Climb.NONE;

    private boolean m_finalized = false;

    public ScoutingForm() {

    }

    public void complete() {
        if (defenceTimes.size() > 0) {
            defenceTimes.get(defenceTimes.size() - 1).tryEnd();
        }
        if (activeDefenceTimes.size() > 0) {
            activeDefenceTimes.get(activeDefenceTimes.size() - 1).tryEnd();
        }
        defenceTime = TimePeriod.millisToSeconds(getTimeListSum(defenceTimes));
        activeDefenceTime = TimePeriod.millisToSeconds(getTimeListSum(activeDefenceTimes));
        if (!climbPeriod.ended()) {
            climbPeriod = new TimePeriod();
        }
        climbTime = climbPeriod.getDurationSeconds();
        m_finalized = true;
    }

    public boolean getCompleted() {
        return m_finalized;
    }

    @Override
    public String toString() {
        return teamNumber + ","
                + team + ","
                + matchNumber + ","
                + scoutName + ","
                + (crossedAutoLine ? "True" : "False") + ","
                + autoBalls + ","
                + autoBallsShot + ","
                + farBalls + ","
                + farBallsShot + ","
                + tarMacBalls + ","
                + tarMacBallsShot + ","
                + lowCloseBalls + ","
                + lowCloseBallsShot + ","
                + protectedZoneBalls + ","
                + protectedZoneBallsShot + ","
                + activeDefenceTime + ","
                + defenceTime + ","
                + defendedTeams + ","
                + climbTime + ","
                + rungLevel + ","
                + foulCounter + ","
                + highCloseBalls + ","
                + highCloseBallsShot;
    }

    public static ScoutingForm fromString(String s) {

        String[] arr = s.split(",");

        ScoutingForm ret = new ScoutingForm();

        ret.teamNumber = Integer.parseInt(arr[0]);
        ret.team = Constants.Team.fromString(arr[1]);
        ret.matchNumber = Integer.parseInt(arr[2]);
        ret.scoutName = arr[3];
        ret.crossedAutoLine = "True".equals(arr[4]);
        ret.autoBalls = Integer.parseInt(arr[5]);
        ret.autoBallsShot = Integer.parseInt(arr[6]);
        ret.farBalls = Integer.parseInt(arr[7]);
        ret.farBallsShot = Integer.parseInt(arr[8]);
        ret.tarMacBalls = Integer.parseInt(arr[9]);
        ret.tarMacBallsShot = Integer.parseInt(arr[10]);
        ret.lowCloseBalls = Integer.parseInt(arr[11]);
        ret.lowCloseBallsShot = Integer.parseInt(arr[12]);
        ret.protectedZoneBalls = Integer.parseInt(arr[13]);
        ret.protectedZoneBallsShot = Integer.parseInt(arr[14]);
        ret.activeDefenceTime = Double.parseDouble(arr[15]);
        ret.defenceTime = Double.parseDouble(arr[16]);
        ret.defendedTeams = arr[17];
        ret.climbTime = Double.parseDouble(arr[18]);
        ret.climb = Constants.Climb.fromString(arr[19]);
        ret.foulCounter =  Integer.parseInt(arr[20]);
        ret.highCloseBalls = Integer.parseInt(arr[21]);
        ret.highCloseBallsShot = Integer.parseInt(arr[22]);

        ret.matchStarted = true;
        ret.matchOver = true;

        return ret;
    }

    private long getTimeListSum(ArrayList<TimePeriod> list) {
        long sum = 0;
        for (TimePeriod p : list) {
            sum += p.getDuration();
        }
        return sum;
    }
}
