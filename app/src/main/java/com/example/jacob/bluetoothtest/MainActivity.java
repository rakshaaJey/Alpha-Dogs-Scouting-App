package com.example.jacob.bluetoothtest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.jacob.bluetoothtest.forms.ScoutingForm;
import com.example.jacob.bluetoothtest.forms.TimePeriod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ScoutingForm m_currentForm = new ScoutingForm();

    private Button  m_auto, m_teleop, m_endgame, m_offence, m_defence, m_defending, m_idle, m_new, m_load, m_save, m_send, m_matchToggle;

    private Button m_farShotOne,  m_farShotTwo, m_farShotThree, m_farShotFour, m_farShotFive, m_farShotSix, m_farShotSeven, m_farShotEight, m_farShotNine, m_farShotTen, m_tarMacShotOne, m_tarMacShotTwo, m_tarMacShotThree, m_tarMacShotFour, m_closeShot, m_protectedRed, m_protectedBlue;

    private ImageButton m_shelfButton;
    private LinearLayout m_shelfLayout, m_defenceModeLayout, m_defenceTypeLayout;
    private ToggleButton m_autoCross;
    private String m_loadName;

    private TextView m_teamInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        m_loadName = intent.getStringExtra(intent.EXTRA_TEXT); //Gets message from previous activity, stores in message string

        m_protectedBlue = findViewById(R.id.blueClimb);
        m_protectedRed = findViewById(R.id.redClimb);
        m_farShotOne = findViewById(R.id.farShotOne);
        m_farShotTwo   = findViewById(R.id.farShotTwo);
        m_farShotThree = findViewById(R.id.farShotThree);
        m_farShotFour = findViewById(R.id.farShotFour);
        m_farShotFive = findViewById(R.id.farShotFive);
        m_farShotSix = findViewById(R.id.farShotSix);
        m_farShotSeven = findViewById(R.id.farShotSeven);
        m_farShotEight = findViewById(R.id.farShotEight);
        m_farShotNine = findViewById(R.id.farShotNine);
        m_farShotTen = findViewById(R.id.farShotTen);
        m_tarMacShotOne = findViewById(R.id.tarMacShotOne);
        m_tarMacShotTwo = findViewById(R.id.tarMacShotTwo);
        m_tarMacShotThree = findViewById(R.id.tarMacShotThree);
        m_tarMacShotFour = findViewById(R.id.tarMacShotFour);
        m_closeShot = findViewById(R.id.closeShot);

        m_shelfButton = findViewById(R.id.shelfButton);

        m_shelfLayout = findViewById(R.id.modeShelf);

        m_auto = findViewById(R.id.auto);
        m_teleop = findViewById(R.id.teleop);
        m_endgame = findViewById(R.id.endgame);

        m_offence = findViewById(R.id.offence);
        m_defence = findViewById(R.id.defence);

        m_defending = findViewById(R.id.defending);
        m_idle = findViewById(R.id.idle);

        m_defenceModeLayout = findViewById(R.id.defenceMode);
        m_defenceTypeLayout = findViewById(R.id.defenceType);

        m_teamInfo = findViewById(R.id.teamInfo);

        m_matchToggle = findViewById(R.id.toggleMatch);

        m_new = findViewById(R.id.newForm);
        m_save = findViewById(R.id.save);
        m_load = findViewById(R.id.load);
        m_send = findViewById(R.id.send);

        m_autoCross = findViewById(R.id.autoLine);

        updateTeamInfo();
        updateShelf();

        m_matchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_currentForm.matchStarted) {
                    m_currentForm.matchOver = true;
                    m_currentForm.crossedAutoLine = m_autoCross.isChecked();
                    m_currentForm.complete();
                    m_matchToggle.setEnabled(false);
                } else {
                    m_currentForm.matchStarted = true;
                    m_currentForm.currentMode = Constants.GameMode.AUTO;
                }
                updateShelf();
            }
        });

        m_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScoutingForm form = new ScoutingForm();
                form.team = m_currentForm.team;
                form.teamNumber = m_currentForm.teamNumber;
                form.scoutName = m_currentForm.scoutName;
                form.matchNumber = m_currentForm.matchNumber + 1;
                m_currentForm = form;
                m_autoCross.setChecked(false);
                showSetupAlert();
            }
        });



        m_protectedBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    if (m_currentForm.currentMode == Constants.GameMode.ENDGAME && m_currentForm.team == Constants.Team.BLUE ) {
                        showClimbAlert();
                    } else {
                        showProtectedZoneAlert();
                    }
                }
            }
        });

        m_protectedRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    if (m_currentForm.currentMode == Constants.GameMode.ENDGAME && m_currentForm.team == Constants.Team.RED) {
                        showClimbAlert();
                    } else {
                        showProtectedZoneAlert();
                    }
                }
            }
        });

        m_farShotOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                   showFarAlert();
                }
            }
        });

        m_farShotTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showFarAlert();
                }
            }
        });

        m_farShotThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showFarAlert();
                }
            }
        });

        m_farShotFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showFarAlert();
                }
            }
        });

        m_farShotFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showFarAlert();
                }
            }
        });

        m_farShotSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showFarAlert();
                }
            }
        });

        m_farShotSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showFarAlert();
                }
            }
        });

        m_farShotEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showFarAlert();
                }
            }
        });

        m_farShotNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showFarAlert();
                }
            }
        });

        m_farShotTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showFarAlert();
                }
            }
        });

        m_tarMacShotOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showTarMacAlert();
                }
            }
        });

        m_tarMacShotTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showTarMacAlert();
                }
            }
        });

        m_tarMacShotThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showTarMacAlert();
                }
            }
        });

        m_tarMacShotFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showTarMacAlert();
                }
            }
        });

        m_closeShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShelfOut()) {
                    showCloseAlert();
                }
            }
        });

        m_shelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShelfOut()) {
                    if (m_currentForm.matchStarted && !m_currentForm.matchOver) {
                        m_shelfLayout.setVisibility(View.GONE);
                    } else if (m_currentForm.matchStarted && m_currentForm.matchOver) {
                        Utilities.showToast(getBaseContext(), Constants.MATCH_OVER_ERROR, Toast.LENGTH_SHORT);
                    } else {
                        Utilities.showToast(getBaseContext(), Constants.START_MATCH_ERROR, Toast.LENGTH_SHORT);
                    }
                } else {
                    m_shelfLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        m_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.currentMode = Constants.GameMode.AUTO;
                updateShelf();
            }
        });

        m_teleop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.currentMode = Constants.GameMode.TELEOP;
                updateShelf();
            }
        });

        m_endgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.currentMode = Constants.GameMode.ENDGAME;
                updateShelf();
            }
        });

        m_offence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.currentAction = Constants.GameAction.OFFENCE;
                if (m_currentForm.defenceTimes.size() > 0 && m_currentForm.defenceTimes.get(m_currentForm.defenceTimes.size() - 1).started() && !m_currentForm.defenceTimes.get(m_currentForm.defenceTimes.size() - 1).ended()) {
                    m_currentForm.defenceTimes.get(m_currentForm.defenceTimes.size() - 1).tryEnd();
                }
                m_currentForm.currentDefenceType = Constants.DefenceType.IDLE;
                if (m_currentForm.activeDefenceTimes.size() > 0 && m_currentForm.activeDefenceTimes.get(m_currentForm.activeDefenceTimes.size() - 1).started() && !m_currentForm.activeDefenceTimes.get(m_currentForm.activeDefenceTimes.size() - 1).ended()) {
                    m_currentForm.activeDefenceTimes.get(m_currentForm.activeDefenceTimes.size() - 1).tryEnd();
                }
                updateShelf();
            }
        });

        m_defence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.currentAction = Constants.GameAction.DEFENCE;
                m_currentForm.defenceTimes.add(new TimePeriod());
                m_currentForm.defenceTimes.get(m_currentForm.defenceTimes.size() - 1).start();
                updateShelf();
            }
        });

        m_defending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.currentDefenceType = Constants.DefenceType.DEFENDING;
                m_currentForm.activeDefenceTimes.add(new TimePeriod());
                m_currentForm.activeDefenceTimes.get(m_currentForm.activeDefenceTimes.size() - 1).start();
                updateShelf();
            }
        });

        m_idle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.currentDefenceType = Constants.DefenceType.IDLE;
                if (m_currentForm.activeDefenceTimes.size() > 0 && m_currentForm.activeDefenceTimes.get(m_currentForm.activeDefenceTimes.size() - 1).started() && !m_currentForm.activeDefenceTimes.get(m_currentForm.activeDefenceTimes.size() - 1).ended()) {
                    m_currentForm.activeDefenceTimes.get(m_currentForm.activeDefenceTimes.size() - 1).tryEnd();
                }
                updateShelf();
            }
        });

        m_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.WRITE_LOG_REQUEST);
            }
        });

        m_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoadActivity.class);
                startActivity(intent);
            }
        });

        m_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SendMessageActivity.class);
                String message = m_currentForm.toString();
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(intent);
            }
        });

        if (m_loadName != null) {
            Log.i("A", "File recieved, loading" + m_loadName);

            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(m_loadName)));
                String content = reader.readLine(); //Gets the content of the file
                m_currentForm = ScoutingForm.fromString(content);
            } catch (FileNotFoundException e) {
                Log.i("A", "File Not Found");
            } catch (IOException e) {
                Log.i("A", "Error Reading File");
            }
            updateShelf();
            updateTeamInfo();
        } else {
            showSetupAlert();
        }
    }

    boolean isShelfOut() {
        return m_shelfLayout.getVisibility() != View.GONE;
    }

    void updateTeamInfo() {
        m_teamInfo.setText((m_currentForm.scoutName.length() > Constants.SCOUT_NAME_MAX_UI_LENGTH ? m_currentForm.scoutName.substring(0, Constants.SCOUT_NAME_MAX_UI_LENGTH - 3) + "..." : m_currentForm.scoutName) + " Scouting " + m_currentForm.team + " - " + m_currentForm.teamNumber + " For Match " + m_currentForm.matchNumber);
        m_teamInfo.setTextColor((m_currentForm.team == Constants.Team.RED ? getResources().getColor(R.color.redTeam) : getResources().getColor(R.color.blueTeam)));
    }

    void updateShelf() {
        if (m_currentForm.matchStarted && !m_currentForm.matchOver) {
            m_new.setEnabled(false);
            m_send.setEnabled(false);
            m_save.setEnabled(false);
            m_load.setEnabled(false);

            if (m_currentForm.currentMode == Constants.GameMode.AUTO) {
                m_auto.setEnabled(false);
                m_teleop.setEnabled(true);
                m_endgame.setEnabled(true);
                m_autoCross.setEnabled(true);
                m_autoCross.setVisibility(View.VISIBLE);
                m_defenceModeLayout.setVisibility(View.GONE);
                m_defenceTypeLayout.setVisibility(View.GONE);
                m_endgame.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                m_teleop.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                m_auto.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
            } else {

                m_auto.setEnabled(true);
                m_autoCross.setEnabled(false);
                m_autoCross.setVisibility(View.GONE);

                if (m_currentForm.currentMode == Constants.GameMode.TELEOP) {
                    m_teleop.setEnabled(false);
                    m_endgame.setEnabled(true);
                    m_endgame.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                    m_teleop.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
                    m_auto.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                } else {
                    m_teleop.setEnabled(true);
                    m_endgame.setEnabled(false);
                    m_endgame.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
                    m_teleop.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                    m_auto.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                }

                m_defenceModeLayout.setVisibility(View.VISIBLE);

                if (m_currentForm.currentAction == Constants.GameAction.DEFENCE) {

                    m_defence.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
                    m_offence.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                    m_defence.setEnabled(false);
                    m_offence.setEnabled(true);
                    m_defenceTypeLayout.setVisibility(View.VISIBLE);

                    if (m_currentForm.currentDefenceType == Constants.DefenceType.DEFENDING) {
                        m_defending.setEnabled(false);
                        m_idle.setEnabled(true);
                        m_defending.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
                        m_idle.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                    } else {
                        m_defending.setEnabled(true);
                        m_idle.setEnabled(false);
                        m_defending.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                        m_idle.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
                    }
                } else {
                    m_defence.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                    m_offence.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
                    m_defence.setEnabled(true);
                    m_offence.setEnabled(false);
                    m_defenceTypeLayout.setVisibility(View.GONE);
                }
            }
        } else {
            m_new.setEnabled(true);
            m_load.setEnabled(true);

            if (m_currentForm.matchOver) {
                m_send.setEnabled(true);
                m_save.setEnabled(true);
            } else {
                m_send.setEnabled(false);
                m_save.setEnabled(false);
            }

            m_endgame.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
            m_teleop.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
            m_auto.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));

            m_autoCross.setEnabled(false);
            m_auto.setEnabled(false);
            m_teleop.setEnabled(false);
            m_endgame.setEnabled(false);
            m_defenceModeLayout.setVisibility(View.GONE);
            m_defenceTypeLayout.setVisibility(View.GONE);
            m_autoCross.setVisibility(View.GONE);
        }
        if (m_currentForm.matchOver) {
            m_matchToggle.setEnabled(false);
            m_matchToggle.setText("Match Over");
        } else {
            m_matchToggle.setEnabled(true);

            if (m_currentForm.matchStarted) {
                m_matchToggle.setText("End Match");
            } else {
                m_matchToggle.setText("Start Match");
            }
        }
    }


    void showFarAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);

        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_target, null);

        //setup

        ((TextView) layout.findViewById(R.id.targetTitle)).setText(m_currentForm.currentMode == Constants.GameMode.AUTO ? "Auto Shots" : "Far Shots");

        final EditText scored = layout.findViewById(R.id.scored);
        final EditText missed = layout.findViewById(R.id.missed);

        scored.setText("" + (m_currentForm.currentMode == Constants.GameMode.AUTO ? m_currentForm.autoBalls : m_currentForm.farBalls));
        missed.setText("" + (m_currentForm.currentMode == Constants.GameMode.AUTO ? m_currentForm.autoBallsShot - m_currentForm.autoBalls : m_currentForm.farBallsShot - m_currentForm.farBalls));

        final Button addScored = layout.findViewById(R.id.addScored);
        final Button addMissed = layout.findViewById(R.id.addMissed);

        addScored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    scored.setText("" + (Integer.parseInt(scored.getText().toString()) + 1));
                } catch (NumberFormatException e) {

                }
            }
        });

        addMissed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    missed.setText("" + (Integer.parseInt(missed.getText().toString()) + 1));
                } catch (NumberFormatException e) {

                }
            }
        });

        builder.setView(layout);

        builder.setPositiveButton(R.string.affirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText scored = ((AlertDialog) dialogInterface).findViewById(R.id.scored);
                EditText missed = ((AlertDialog) dialogInterface).findViewById(R.id.missed);

                try {
                    if (m_currentForm.currentMode == Constants.GameMode.AUTO) {
                        m_currentForm.autoBalls = Integer.parseInt(scored.getText().toString());
                        m_currentForm.autoBallsShot = Integer.parseInt(scored.getText().toString()) + Integer.parseInt(missed.getText().toString());
                    } else {
                        m_currentForm.farBalls = Integer.parseInt(scored.getText().toString());
                        m_currentForm.farBallsShot = Integer.parseInt(scored.getText().toString()) + Integer.parseInt(missed.getText().toString());
                    }
                } catch (NumberFormatException e) {

                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showSetupAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);

        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_setup, null);

        //setup

        final Button red = layout.findViewById(R.id.red);
        final Button blue = layout.findViewById(R.id.blue);

        final EditText name = layout.findViewById(R.id.scoutName);
        final EditText number = layout.findViewById(R.id.matchNumber);
        final EditText teamNumber = layout.findViewById(R.id.teamNumber);

        name.setText(m_currentForm.scoutName);
        number.setText("" + m_currentForm.matchNumber);
        teamNumber.setText("" + m_currentForm.teamNumber);

        if (m_currentForm.team == Constants.Team.RED) {
            red.setEnabled(false);
            red.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
            blue.setEnabled(true);
            blue.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
        } else {
            red.setEnabled(true);
            red.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
            blue.setEnabled(false);
            blue.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
        }

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.team = Constants.Team.RED;
                red.setEnabled(false);
                red.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
                blue.setEnabled(true);
                blue.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.team = Constants.Team.BLUE;
                red.setEnabled(true);
                red.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonNotSelectedColor)));
                blue.setEnabled(false);
                blue.setBackground(new ColorDrawable(getResources().getColor(R.color.buttonSelectedColor)));
            }
        });

        builder.setView(layout);

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                m_currentForm.scoutName = name.getText().toString();
                m_currentForm.matchNumber = Integer.parseInt(number.getText().toString());
                m_currentForm.teamNumber = Integer.parseInt(teamNumber.getText().toString());
                updateTeamInfo();
                updateShelf();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showProtectedZoneAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);

        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_target, null);

        //setup

        ((TextView) layout.findViewById(R.id.targetTitle)).setText(m_currentForm.currentMode == Constants.GameMode.AUTO ? "Auto Shots" : "Protected Zone Shots");

        final EditText scored = layout.findViewById(R.id.scored);
        final EditText missed = layout.findViewById(R.id.missed);

        scored.setText("" + (m_currentForm.currentMode == Constants.GameMode.AUTO ? m_currentForm.autoBalls : m_currentForm.protectedZoneBalls));
        missed.setText("" + (m_currentForm.currentMode == Constants.GameMode.AUTO ? m_currentForm.autoBallsShot - m_currentForm.autoBalls : m_currentForm.protectedZoneBallsShot - m_currentForm.protectedZoneBalls));

        final Button addScored = layout.findViewById(R.id.addScored);
        final Button addMissed = layout.findViewById(R.id.addMissed);

        addScored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    scored.setText("" + (Integer.parseInt(scored.getText().toString()) + 1));
                } catch (NumberFormatException e) {

                }
            }
        });

        addMissed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    missed.setText("" + (Integer.parseInt(missed.getText().toString()) + 1));
                } catch (NumberFormatException e) {

                }
            }
        });

        builder.setView(layout);

        builder.setPositiveButton(R.string.affirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText scored = ((AlertDialog) dialogInterface).findViewById(R.id.scored);
                EditText missed = ((AlertDialog) dialogInterface).findViewById(R.id.missed);

                try {
                    if (m_currentForm.currentMode == Constants.GameMode.AUTO) {
                        m_currentForm.autoBalls = Integer.parseInt(scored.getText().toString());
                        m_currentForm.autoBallsShot = Integer.parseInt(scored.getText().toString()) + Integer.parseInt(missed.getText().toString());
                    } else {
                        m_currentForm.protectedZoneBalls = Integer.parseInt(scored.getText().toString());
                        m_currentForm.protectedZoneBallsShot = Integer.parseInt(scored.getText().toString()) + Integer.parseInt(missed.getText().toString());
                    }
                } catch (NumberFormatException e) {

                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showTarMacAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);

        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_target, null);

        //setup

        ((TextView) layout.findViewById(R.id.targetTitle)).setText(m_currentForm.currentMode == Constants.GameMode.AUTO ? "Auto Shots" : "Tar Mac Shots");

        final EditText scored = layout.findViewById(R.id.scored);
        final EditText missed = layout.findViewById(R.id.missed);

        scored.setText("" + (m_currentForm.currentMode == Constants.GameMode.AUTO ? m_currentForm.autoBalls : m_currentForm.tarMacBalls));
        missed.setText("" + (m_currentForm.currentMode == Constants.GameMode.AUTO ? m_currentForm.autoBallsShot - m_currentForm.autoBalls : m_currentForm.tarMacBallsShot - m_currentForm.tarMacBalls));

        final Button addScored = layout.findViewById(R.id.addScored);
        final Button addMissed = layout.findViewById(R.id.addMissed);

        addScored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    scored.setText("" + (Integer.parseInt(scored.getText().toString()) + 1));
                } catch (NumberFormatException e) {

                }
            }
        });

        addMissed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    missed.setText("" + (Integer.parseInt(missed.getText().toString()) + 1));
                } catch (NumberFormatException e) {

                }
            }
        });

        builder.setView(layout);

        builder.setPositiveButton(R.string.affirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText scored = ((AlertDialog) dialogInterface).findViewById(R.id.scored);
                EditText missed = ((AlertDialog) dialogInterface).findViewById(R.id.missed);

                try {
                    if (m_currentForm.currentMode == Constants.GameMode.AUTO) {
                        m_currentForm.autoBalls = Integer.parseInt(scored.getText().toString());
                        m_currentForm.autoBallsShot = Integer.parseInt(scored.getText().toString()) + Integer.parseInt(missed.getText().toString());
                    } else {
                        m_currentForm.tarMacBalls = Integer.parseInt(scored.getText().toString());
                        m_currentForm.tarMacBallsShot = Integer.parseInt(scored.getText().toString()) + Integer.parseInt(missed.getText().toString());
                    }
                } catch (NumberFormatException e) {

                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void showCloseAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);

        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_target, null);

        //setup

        ((TextView) layout.findViewById(R.id.targetTitle)).setText(m_currentForm.currentMode == Constants.GameMode.AUTO ? "Auto Shots" : "Close Shots");

        final EditText scored = layout.findViewById(R.id.scored);
        final EditText missed = layout.findViewById(R.id.missed);

        scored.setText("" + (m_currentForm.currentMode == Constants.GameMode.AUTO ? m_currentForm.autoBalls : m_currentForm.closeBalls));
        missed.setText("" + (m_currentForm.currentMode == Constants.GameMode.AUTO ? m_currentForm.autoBallsShot - m_currentForm.autoBalls : m_currentForm.closeBallsShot - m_currentForm.closeBalls));

        final Button addScored = layout.findViewById(R.id.addScored);
        final Button addMissed = layout.findViewById(R.id.addMissed);

        addScored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    scored.setText("" + (Integer.parseInt(scored.getText().toString()) + 1));
                } catch (NumberFormatException e) {

                }
            }
        });

        addMissed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    missed.setText("" + (Integer.parseInt(missed.getText().toString()) + 1));
                } catch (NumberFormatException e) {

                }
            }
        });

        builder.setView(layout);

        builder.setPositiveButton(R.string.affirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText scored = ((AlertDialog) dialogInterface).findViewById(R.id.scored);
                EditText missed = ((AlertDialog) dialogInterface).findViewById(R.id.missed);

                try {
                    if (m_currentForm.currentMode == Constants.GameMode.AUTO) {
                        m_currentForm.autoBalls = Integer.parseInt(scored.getText().toString());
                        m_currentForm.autoBallsShot = Integer.parseInt(scored.getText().toString()) + Integer.parseInt(missed.getText().toString());
                    } else {
                        m_currentForm.closeBalls = Integer.parseInt(scored.getText().toString());
                        m_currentForm.closeBallsShot = Integer.parseInt(scored.getText().toString()) + Integer.parseInt(missed.getText().toString());
                    }
                } catch (NumberFormatException e) {

                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    void showClimbAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);

        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_climb, null);

        //setup

        final Button addRungLevel = layout.findViewById(R.id.addRungLevel);

        final Button start = layout.findViewById(R.id.start);
        final Button end = layout.findViewById(R.id.end);

        final EditText rungLevel = layout.findViewById(R.id.rungLevel);

        addRungLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rungLevel.setText("" + (Integer.parseInt(rungLevel.getText().toString()) + 1));
                } catch (NumberFormatException e) {

                }
            }
        });

        try {
             m_currentForm.rungLevel = Integer.parseInt(rungLevel.getText().toString());
        } catch (NumberFormatException e) {

        }

        if (m_currentForm.climbPeriod.started() && !m_currentForm.climbPeriod.ended()) {
            //Started
            start.setText("Restart Climb");
            start.setEnabled(true);
            end.setEnabled(true);
        } else if (m_currentForm.climbPeriod.ended()) {
            //Over
            start.setText("Restart Climb");
            start.setEnabled(true);
            end.setEnabled(false);
        } else {
            //Not started
            start.setText("Start Climb");
            start.setEnabled(true);
            end.setEnabled(false);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.climbPeriod = new TimePeriod();
                m_currentForm.climbPeriod.start();
                if (m_currentForm.climbPeriod.started() && !m_currentForm.climbPeriod.ended()) {
                    //Started
                    start.setText("Restart Climb");
                    start.setEnabled(true);
                    end.setEnabled(true);
                } else if (m_currentForm.climbPeriod.ended()) {
                    //Over
                    start.setText("Restart Climb");
                    start.setEnabled(true);
                    end.setEnabled(false);
                } else {
                    //Not started
                    start.setText("Start Climb");
                    start.setEnabled(true);
                    end.setEnabled(false);
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.climbPeriod.end();
                if (m_currentForm.climbPeriod.started() && !m_currentForm.climbPeriod.ended()) {
                    //Started
                    start.setText("Restart Climb");
                    start.setEnabled(true);
                    end.setEnabled(true);
                } else if (m_currentForm.climbPeriod.ended()) {
                    //Over
                    start.setText("Restart Climb");
                    start.setEnabled(true);
                    end.setEnabled(false);
                } else {
                    //Not started
                    start.setText("Start Climb");
                    start.setEnabled(true);
                    end.setEnabled(false);
                }
            }
        });

        builder.setView(layout);

        builder.setPositiveButton(R.string.affirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == Constants.WRITE_LOG_REQUEST) {

            Log.i("A", "Received response for write permission request.");

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When write permission is granted

                Log.i("A", "Write permission has now been granted.");

                File home = new File(getApplicationInfo().dataDir + "/Logs"); //application directory, so: /data/data/com.whatever.otherstuff/Logs
                home.mkdirs();
                File csv = new File(home.getAbsolutePath() + "/" + m_currentForm.matchNumber + "-" + m_currentForm.teamNumber);

                try {


                    BufferedWriter writer = new BufferedWriter(new FileWriter(csv));
                    writer.write(m_currentForm.toString());
                    writer.close();

                    Log.i("A", "Finished writing to " + csv.getAbsolutePath() + "");

                    //IO crap for testing
                    //BufferedReader reader = new BufferedReader(new FileReader(csv));
                    //reader.close();
                } catch (IOException e) {
                    //IO didn't work, honestly this shouldn't happen, if it does its probably a device or permission error
                    Log.i("A", "File write to " + csv.getAbsolutePath() + " failed");
                }

            } else {
                Log.i("A", "Write permission was NOT granted.");
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}


