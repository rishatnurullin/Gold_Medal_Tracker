package com.project.goldmedal.repositories;
import org.springframework.data.repository.CrudRepository;
import com.project.goldmedal.model.GoldMedal;
import java.util.List;
public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer>{
    //finds GoldMedals won by a country
    public List<GoldMedal> findByCountry(String country);
    public List<GoldMedal> findByCountryOrderByYearAsc(String country);
    public List<GoldMedal> findByCountryOrderByYearDesc(String country);
    public List<GoldMedal> findByCountryOrderByCityAsc(String country);
    public List<GoldMedal> findByCountryOrderByCityDesc(String country);
    public List<GoldMedal> findByCountryOrderByNameAsc(String name);
    public List<GoldMedal> findByCountryOrderByNameDesc(String name);
    public List<GoldMedal> findByCountryOrderByEventAsc(String name);
    public List<GoldMedal> findByCountryOrderByEventDesc(String name);
    public List<GoldMedal> findByCountryOrderBySeasonAsc(String name);
    public List<GoldMedal> findByCountryOrderBySeasonDesc(String name);
    public List<GoldMedal> findByCountryAndSeasonOrderByYearAsc(String country, String season);
    public List<GoldMedal> findByCountryAndSeasonOrderByYearDesc(String country, String season);
    public List<GoldMedal> findBySeason(String season);
    public List<GoldMedal> findByCountryAndGender(String country, String gender);

}
