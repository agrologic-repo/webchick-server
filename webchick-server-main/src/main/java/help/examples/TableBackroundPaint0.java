
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package help.examples;



import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JTable;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
class TableBackroundPaint0 extends JTable {
    private static final long serialVersionUID = 1L;

    TableBackroundPaint0(Object[][] data, Object[] head) {
        super(data, head);
        setOpaque(false);
        ((JComponent) getDefaultRenderer(Object.class)).setOpaque(false);
    }

//  @Override
//  public void paintComponent(Graphics g) {
//      Color background = new Color(168, 210, 241);
//      Color controlColor = new Color(230, 240, 230);
//      int width = getWidth();
//      int height = getHeight();
//      Graphics2D g2 = (Graphics2D) g;
//      Paint oldPaint = g2.getPaint();
//      g2.setPaint(new GradientPaint(0, 0, background, width, 0, controlColor));
//      g2.fillRect(0, 0, width, height);
//      g2.setPaint(oldPaint);
//      for (int row : getSelectedRows()) {
//          Rectangle start = getCellRect(row, 0, true);
//          Rectangle end = getCellRect(row, getColumnCount() - 1, true);
//          g2.setPaint(new GradientPaint(start.x, 0, controlColor, (int) ((end.x + end.width - start.x) * 1.25), 0, Color.orange));
//          g2.fillRect(start.x, start.y, end.x + end.width - start.x, start.height);
//      }
//      super.paintComponent(g);
//  }
}



