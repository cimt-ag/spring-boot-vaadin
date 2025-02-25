package de.cimt.springbootvaadin.repository;

import de.cimt.springbootvaadin.model.ModuleData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleDataRepository extends JpaRepository<ModuleData, String> {

    @Query("select m from ModuleData m where lower(m.moduleName) like lower(concat('%', :searchTerm, '%')) ")
    List<ModuleData> search(@Param("searchTerm") String searchTerm);

    ModuleData findByModuleName(String moduleName);
}
