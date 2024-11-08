package com.project.goldmedal.controller;

import com.project.goldmedal.model.*;
import com.project.goldmedal.repositories.GoldMedalRepository;
import com.project.goldmedal.repositories.CountryRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {

    private final GoldMedalRepository goldMedalRepository;
    private final CountryRepository countryRepository;

    public GoldMedalController(GoldMedalRepository goldMedalRepository, CountryRepository countryRepository) {
        this.goldMedalRepository = goldMedalRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        System.out.println("The country name is " + country);
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderByYearAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryOrderByYearDesc(countryName);
                }
                break;
            case "season":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderBySeasonAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryOrderBySeasonDesc(countryName);
                }
                break;
            case "city":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderByCityAsc(countryName);
                }
                else {
                    medalsList = this.goldMedalRepository.findByCountryOrderByCityDesc(countryName);
                }
                break;
            case "name":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderByNameAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryOrderByNameDesc(countryName);
                }
                break;
            case "event":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryOrderByEventAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryOrderByEventDesc(countryName);
                }
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        var countryOptional = this.countryRepository.findByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }
        var country = countryOptional.get();
        var goldMedalCount = this.goldMedalRepository.findByCountry(countryName).size();

        var summerWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName, "Summer");
        System.out.println(summerWins.size());
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;

        var totalSummerEvents = this.goldMedalRepository.findBySeason("Summer").size();
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        var winterWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName, "Winter");
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        var totalWinterEvents = this.goldMedalRepository.findBySeason("Winter").size();
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        var numberEventsWonByFemaleAthletes = this.goldMedalRepository.findByCountryAndGender(countryName, "Women").size();
        var numberEventsWonByMaleAthletes = this.goldMedalRepository.findByCountryAndGender(countryName, "Men").size();

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                if (ascendingOrder) {
                    countries = this.countryRepository.findAllByOrderByNameAsc();
                } else {
                    countries = this.countryRepository.findAllByOrderByNameDesc();
                }
                // TODO: list of countries sorted by name in the given order
                break;
            case "gdp":
                if (ascendingOrder) {
                    countries = this.countryRepository.findAllByOrderByGdpAsc();
                } else {
                    countries = this.countryRepository.findAllByOrderByGdpDesc();
                }
                // TODO: list of countries sorted by gdp in the given order
                break;
            case "population":
                if (ascendingOrder) {
                    countries = this.countryRepository.findAllByOrderByPopulationAsc();
                } else {
                    countries = this.countryRepository.findAllByOrderByPopulationDesc();
                }
                // TODO: list of countries sorted by population in the given order
                break;
            case "medals":
            default:
                countries = this.countryRepository.findAll();// TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = this.goldMedalRepository.findByCountry(country.getName()).size();// TODO: get count of medals for the given country
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
