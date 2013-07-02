package com.agrologic.app.model.rxtx;

import com.agrologic.app.gui.rxtx.DataImage;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.ProgramRelay;
import com.agrologic.app.model.ProgramSystemState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataController extends Data implements Serializable {

    private List<DataChangeListener> listeners;
    private List<ProgramRelay> programRelays;
    private List<ProgramSystemState> programSystemStates;

    public DataController(Data copy) {
        super(copy);
        this.listeners = new ArrayList<DataChangeListener>();
    }

    public void addDataChangeListener(DataChangeListener listener) {
        listeners.add(listener);
    }

    public void fireDataChanges(DataChangeEvent event) {
        for (DataChangeListener dcl : listeners) {
            dcl.dataChanged(event);
        }
    }

    public void setProgramRelays(List<ProgramRelay> prs) {
        this.programRelays = new ArrayList<ProgramRelay>();
        for (ProgramRelay pr : prs) {
            if (pr.getDataId().equals(getId())) {
                programRelays.add(pr);
            }
        }
    }

    public void setProgramSystemStates(List<ProgramSystemState> pss) {
        this.programSystemStates = new ArrayList<ProgramSystemState>();
        for (ProgramSystemState pr : pss) {
            programSystemStates.add(pr);
        }
    }

    @Override
    public synchronized void setValue(Long newValue) {
        DataChangeEvent dce = null;
        if (newValue == null) {
            newValue = Long.valueOf(-1);
        }
        switch (getSpecial()) {
            default:
                super.setValue(newValue);
                final String value = getFormattedValue();
                dce = new DataChangeEvent(value);
                fireDataChanges(dce);

                break;

            case RELAY:
                try {
                    super.setValue(newValue);
                    for (ProgramRelay relay : programRelays) {
                        relay.init(newValue);
                        dce = new DataChangeEvent(updateValue(relay), relay.getBitNumber());
                        fireDataChanges(dce);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SYSTEM_STATE:
                try {
                    super.setValue(newValue);
                    String text = "---";
                    if (super.getValue() == null) {
                        super.setValue((long) 0);
                    }
                    for (ProgramSystemState pss : programSystemStates) {
                        long v = super.getValue();
                        long n = pss.getSystemStateNumber();
                        if (v == n) {
                            text = pss.getText();
                            dce = new DataChangeEvent(text);
                            fireDataChanges(dce);
                        }
                    }
                    // 17000-11878
                    // 16000-11378
                    // 15000-10835
                    // 10600-7326(6500)
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private DataImage.Type updateValue(ProgramRelay relay) {
        DataImage.Type returnType = null;

        if (relay.getText().contains("Fan") || relay.getText().contains("Mixer")) {
            if (relay.isOn()) {
                returnType = DataImage.Type.FAN_ON;
            } else {
                returnType = DataImage.Type.FAN_OFF;
            }
        } else if (relay.getText().contains("Light")) {
            if (relay.isOn()) {
                returnType = DataImage.Type.LIGHT_ON;
            } else {
                returnType = DataImage.Type.LIGHT_OFF;
            }
        } else if (relay.getText().contains("Cool")) {
            if (relay.isOn()) {
                returnType = DataImage.Type.COOL_ON;
            } else {
                returnType = DataImage.Type.COOL_OFF;
            }
        } else if (relay.getText().contains("Heater")) {
            if (relay.isOn()) {
                returnType = DataImage.Type.HEATER_ON;
            } else {
                returnType = DataImage.Type.HEATER_OFF;
            }
        } else if (relay.getText().contains("Feed")) {
            if (relay.isOn()) {
                returnType = DataImage.Type.AOUGER_ON;
            } else {
                returnType = DataImage.Type.AOUGER_OFF;
            }
        } else if (relay.getText().contains("Water")) {
            if (relay.isOn()) {
                returnType = DataImage.Type.WATER_ON;
            } else {
                returnType = DataImage.Type.WATER_OFF;
            }
        } else if (relay.getText().contains("Ignition")) {
            if (relay.isOn()) {
                returnType = DataImage.Type.SPARK_ON;
            } else {
                returnType = DataImage.Type.SPARK_OFF;
            }
        } else {
            if (relay.isOn()) {
                returnType = DataImage.Type.RELAY_ON;
            } else {
                returnType = DataImage.Type.RELAY_OFF;
            }
        }

        return returnType;
    }
}
