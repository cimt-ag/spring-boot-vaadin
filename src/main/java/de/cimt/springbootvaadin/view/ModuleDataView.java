package de.cimt.springbootvaadin.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import de.cimt.springbootvaadin.config.AdminConstants;
import de.cimt.springbootvaadin.model.ModuleData;
import de.cimt.springbootvaadin.services.ModuleService;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

@Slf4j
@SpringComponent
@Scope("prototype")
@RolesAllowed("ROLE_ADMIN")
@Route(value = "admin/module", layout = MainLayout.class)
@PageTitle("Module Data | Admin | Spring Boot Vaadin")
public class ModuleDataView extends VerticalLayout {

    Grid<ModuleData> grid = new Grid<>(ModuleData.class, false);
    Grid.Column<ModuleData> idColumn = grid.addColumn(ModuleData::getId)
            .setHeader("ID").setResizable(true).setSortable(true);
    Grid.Column<ModuleData> createdColumn = grid.addColumn(ModuleData::getCreatedDate)
            .setHeader("Erstellt").setResizable(true).setSortable(true);
    Grid.Column<ModuleData> modifiedColumn = grid.addColumn(ModuleData::getLastModifiedDate)
            .setHeader("Geändert").setResizable(true).setSortable(true);
    Grid.Column<ModuleData> nameColumn = grid.addColumn(ModuleData::getModuleName)
            .setHeader("Name").setResizable(true).setSortable(true);
    Grid.Column<ModuleData> reviewColumn = grid.addComponentColumn(moduleData -> createStatusIcon(moduleData.getImportData())).
            setHeader(AdminConstants.DATA_IMPORT).setWidth("6rem").setSortable(true);
    TextField searchField = new TextField();
    ModuleDataForm form;
    ModuleService service;

    private static class ColumnToggleContextMenu extends ContextMenu {
        public ColumnToggleContextMenu(Component target) {
            super(target);
            setOpenOnClick(true);
        }

        void addColumnToggleItem(String label, Grid.Column<ModuleData> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
            menuItem.setKeepOpen(true);
        }
    }

    public ModuleDataView(ModuleService service) {
        this.service = service;
        addClassName("moduleData-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("moduleData-grid");
        grid.setSizeFull();
        grid.getColumns().forEach(col -> {
            col.setAutoWidth(true);
            log.info("Column Class Name, Header: {} {}", grid.getClassName(), col.getHeaderText());
        });
        grid.asSingleSelect().addValueChangeListener(event -> editModuleData(event.getValue()));
    }

    private void configureForm() {
        form = new ModuleDataForm();
        form.setWidth("25em");
        form.setVisible(false);
        form.addSaveListener(this::saveModuleData);
        form.addDeleteListener(this::deleteModuleData);
        form.addCloseListener(e -> closeEditor());
    }

    private Icon createStatusIcon(boolean status) {
        Icon icon;
        if (status) {
            icon = VaadinIcon.CHECK.create();
            icon.getElement().getThemeList().add("badge success");
        } else {
            icon = VaadinIcon.CLOSE_SMALL.create();
            icon.getElement().getThemeList().add("badge error");
        }
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }

    private void saveModuleData(ModuleDataForm.SaveEvent event) {
        ModuleData moduleData = event.getModuleData();
        service.saveModuleData(moduleData);
        updateList();
        closeEditor();
    }

    private void deleteModuleData(ModuleDataForm.DeleteEvent event) {
        service.deleteModuleData(event.getModuleData());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        searchField.setPlaceholder("Filter by module name...");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> updateList());

        Button addModuleDataButton = new Button("Add module", click -> addModuleData());
        Button menuButton = new Button("Show/Hide Columns");
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(menuButton);
        columnToggleContextMenu.addColumnToggleItem("ID", idColumn);
        columnToggleContextMenu.addColumnToggleItem("Erstellt", createdColumn);
        columnToggleContextMenu.addColumnToggleItem("Geändert", modifiedColumn);
        columnToggleContextMenu.addColumnToggleItem("Name", nameColumn);
        columnToggleContextMenu.addColumnToggleItem(AdminConstants.DATA_IMPORT, reviewColumn);
        HorizontalLayout toolbar = new HorizontalLayout(searchField, addModuleDataButton, menuButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    public void editModuleData(ModuleData moduleData) {
        if (moduleData == null) {
            closeEditor();
        } else {
            form.setModuleData(moduleData);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    public void closeEditor() {
        form.setModuleData(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addModuleData() {
        grid.asSingleSelect().clear();
        editModuleData(new ModuleData());
    }

    private void updateList() {
        grid.setItems(service.findAll(searchField.getValue()));
    }
}
