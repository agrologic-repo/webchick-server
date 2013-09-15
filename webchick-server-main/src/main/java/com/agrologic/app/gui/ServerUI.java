package com.agrologic.app.gui;

import com.agrologic.app.model.Cellink;

import java.util.Iterator;

public interface ServerUI {

    CellinkTable getCellinkTable();

    Iterator<Cellink> iterator();
}



