package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;

public class ColumnToggleContextMenu<T> extends ContextMenu {
    public ColumnToggleContextMenu(Component target) {
        super(target);
        setOpenOnClick(true);
    }

    public void addColumnToggleItem(Grid.Column<T> column) {
        String label = column.getHeaderText();
        MenuItem menuItem = this.addItem(label, e -> {
            column.setVisible(e.getSource().isChecked());
        });
        menuItem.setCheckable(true);
        menuItem.setChecked(column.isVisible());
        menuItem.setKeepOpen(true);
    }

    public void addColumnToggleItem(String label, Grid.Column<T> column) {
        MenuItem menuItem = this.addItem(label, e -> {
            column.setVisible(e.getSource().isChecked());
        });
        menuItem.setCheckable(true);
        menuItem.setChecked(column.isVisible());
        menuItem.setKeepOpen(true);
    }
}
