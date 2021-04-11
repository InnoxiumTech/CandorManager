package uk.co.innoxium.candor.window.modscene;

import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.store.ModStore;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.Date;
import java.time.Instant;


public class ModsTableModel extends AbstractTableModel implements TableModelListener {

    String[] headers = { "Name", "State", "Installed Date" };

    public ModsTableModel() {

        this.addTableModelListener(this);
    }

    @Override
    public int getRowCount() {

        return ModStore.MODS.size();
    }

    @Override
    public int getColumnCount() {

        return headers.length;
    }

    @Override
    public String getColumnName(int column) {

        return headers[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Mod mod = ModStore.MODS.getAtIndex(rowIndex);

        switch(columnIndex) {

            case 0 -> {

                return mod.getReadableName();
            }

            case 1 -> {

                return mod.getState().name();
            }

            case 2 -> {

                return Date.from(Instant.now());
            }

            default -> throw new IllegalArgumentException("Bad Cell (" + rowIndex + ", " + columnIndex + ")");
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return false;
        // To enable double click mod toggle we have disabled editing name for the time being
//        return columnIndex == 0;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {

        Mod mod = ModStore.MODS.getAtIndex(rowIndex);

        switch(columnIndex) {

            case 0 -> {

                mod.setReadableName((String)value);
                ModStore.updateModState(mod, mod.getState());
            }
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public void tableChanged(TableModelEvent e) {

//        int row = e.getFirstRow();
//        int column = e.getColumn();
//
//        Mod mod = ModStore.MODS.getAtIndex(row);
//
//        Object obj = this.getValueAt(row, column);
//
//        switch(column) {
//
//            case 0 -> mod.setReadableName(String.valueOf(obj));
//        }
    }
}
