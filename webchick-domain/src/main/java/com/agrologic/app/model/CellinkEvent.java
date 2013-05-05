package com.agrologic.app.model;

public class CellinkEvent {
    private long         cellinkId;
    private CellinkState newCellinkState;
    private boolean      newWithLogging;

    /**
     * Full description of cellink event was specified.
     * @param newCellinkState the cellink state was changed , so new value of
     *          <br>cellink state was specified.
     */
    public CellinkEvent(long cellinkId, CellinkState newCellinkState) {
        this.cellinkId       = cellinkId;
        this.newCellinkState = newCellinkState;
    }

    public long getCellinkId() {
        return cellinkId;
    }

    /**
     *  Gets the new state if was changed to.
     * @return the new state if was changed to
     */
    public CellinkState getNewCellinkState() {
        return newCellinkState;
    }

    /**
     * Sets the new state if state was changed to.
     * @param newCellinkState the new state if was changed to
     */
    public void setNewCellinkState(CellinkState newCellinkState) {
        this.newCellinkState = newCellinkState;
    }

    /**
     *  Gets the new state if state was changed to.
     * @return the new state if was changed to
     */
    public boolean isNewChecked() {
        return newWithLogging;
    }

    /**
     *  Sets the new checked checked was changed to.
     *  @param newChecked the new checked if was changed to
     */
    public void setNewChecked(boolean newChecked) {
        this.newWithLogging = newChecked;
    }
}


