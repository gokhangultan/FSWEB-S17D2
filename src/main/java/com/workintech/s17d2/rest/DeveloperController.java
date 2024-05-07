package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    public Map<Integer, Developer> developers;
    private Taxable taxable;
    @PostConstruct
    public void init(){
        developers = new HashMap<>();
    }

    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @GetMapping("")
    public List<Developer> allDevelopers(){
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer findDeveloper(@PathVariable int id){
        return developers.get(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void addDeveloper(@RequestBody Developer developer) {
        Developer newDeveloper;
        switch(developer.getExperience()) {
            case JUNIOR:
                newDeveloper = new JuniorDeveloper(developer.getId(), developer.getName(), developer.getSalary() - developer.getSalary() * taxable.getSimpleTaxRate());
                break;
            case MID:
                newDeveloper = new MidDeveloper(developer.getId(), developer.getName(), developer.getSalary() - developer.getSalary() * taxable.getMiddleTaxRate());
                break;
            case SENIOR:
                newDeveloper = new SeniorDeveloper(developer.getId(), developer.getName(), developer.getSalary() - developer.getSalary() * taxable.getUpperTaxRate());
                break;
            default:
                throw new IllegalArgumentException("Invalid experience level");
        }
        developers.put(newDeveloper.getId(), newDeveloper);
    }

    @PutMapping("/{id}")
    public Developer update(@PathVariable int id, @RequestBody Developer developer){
        if(developers.containsKey(id)){
            return null;
        }
        developers.put(id, developer);
        return developers.get(developer.getId());
    }

    @DeleteMapping("/{id}")
    public Developer delete(@PathVariable int id) {
        if (developers.containsKey(id)) {
            return null;
        }
        Developer dev = developers.get(id);
        developers.remove(id, developers.get(id));
        return dev;
    }
}
