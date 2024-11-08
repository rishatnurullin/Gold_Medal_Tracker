package com.project.goldmedal.repositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.goldmedal.model.Country;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer>{
    public Optional<Country> findByName(String name);
    public List<Country> findAllByOrderByNameAsc();
    public List<Country> findAllByOrderByNameDesc();
    public List<Country> findAllByOrderByGdpAsc();
    public List<Country> findAllByOrderByGdpDesc();
    public List<Country> findAllByOrderByPopulationDesc();
    public List<Country> findAllByOrderByPopulationAsc();
}
