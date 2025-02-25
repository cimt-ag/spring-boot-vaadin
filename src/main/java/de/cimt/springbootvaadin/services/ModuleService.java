package de.cimt.springbootvaadin.services;

import de.cimt.springbootvaadin.model.ModuleData;
import de.cimt.springbootvaadin.repository.ModuleDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ModuleService {

    private final ModuleDataRepository moduleDataRepository;

    public ModuleService(ModuleDataRepository moduleDataRepository) {
        this.moduleDataRepository = moduleDataRepository;
    }

    public List<ModuleData> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return moduleDataRepository.findAll();
        } else {
            return moduleDataRepository.search(stringFilter);
        }
    }

    public ModuleData findByName(String moduleName) {
        if (moduleName == null || moduleName.isEmpty()) {
            return null;
        } else {
            return moduleDataRepository.findByModuleName(moduleName);
        }
    }

    public boolean importData(String moduleName) {
        ModuleData module = moduleDataRepository.findByModuleName(moduleName);
        if (module != null) {
            return module.getImportData();
        } else {
            log.info("Module {} not found, should import data", moduleName);
            return true;
        }
    }

    public void setDataImported(String moduleName) {
        ModuleData module = moduleDataRepository.findByModuleName(moduleName);
        if (module == null) {
            log.info("Module {} not found, will create it", moduleName);
            module = new ModuleData();
            module.setModuleName(moduleName);
            module.setImportData(false);
            moduleDataRepository.save(module);
        } else {
            log.info("Module {} found, change import data", module);
            module.setImportData(false);
            moduleDataRepository.save(module);
        }
    }

    public void saveModuleData(ModuleData moduleData) {
        if (moduleData == null) {
            log.warn("ModuleData is null, can't save it.");
        } else {
            moduleDataRepository.save(moduleData);
        }
    }

    public void deleteModuleData(ModuleData moduleData) {
        if (moduleData == null) {
            log.warn("ModuleData is null, can't delete it.");
        } else {
            moduleDataRepository.delete(moduleData);
        }
    }
}
