package com.agrologic.app.gui.wizard.api.examp;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.gui.wizard.WizardPanelDescriptor;

public class TestPanel1Descriptor extends WizardPanelDescriptor {
    public static final String IDENTIFIER = "FIND_USER_PANEL";

    public TestPanel1Descriptor() {
        super(IDENTIFIER, new TestPanel1());
    }

    @Override
    public Object getNextPanelDescriptor() {
        return TestPanel2Descriptor.IDENTIFIER;
    }

    @Override
    public Object getBackPanelDescriptor() {
        return null;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
