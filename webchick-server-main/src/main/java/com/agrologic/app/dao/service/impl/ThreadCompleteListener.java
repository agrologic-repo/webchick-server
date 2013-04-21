/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service.impl;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public interface ThreadCompleteListener {

    void notifyOfThreadComplete(final Thread thread);
}
