package com.mycompany.employees;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Employee {
    private int id;
    private Map<Integer, Period> projectHistory = new HashMap<>();
    //colleague and days worked with him
    private TreeMap<Long, Integer> colleagueHistory = new TreeMap<>();

    public Employee(int id) {
        this.id = id;
    }

    public void addToProjectHistory(int projectId, LocalDate startDate, LocalDate endDate){
        projectHistory.put(projectId, new Period(startDate, endDate));
    }

    public void addToColleagueHistory(long days, int colleagueId){
        colleagueHistory.put(days, colleagueId);
    }

    public Map<Integer, Period> getProjectHistory() {
        return Collections.unmodifiableMap(projectHistory);
    }

    public Map.Entry<Long, Integer> getClosestColleague() {
        return colleagueHistory.firstEntry();
    }

    public Map<Integer, Period> getProjectHistory(Set<Integer> ids) {
        Map<Integer, Period> matchingProjects = new HashMap<>();
        for (Integer integer : projectHistory.keySet()) {
            if (ids.contains(integer)){
                matchingProjects.put(integer, projectHistory.get(integer));
            }
        }
        return matchingProjects;
    }

    public int getId() {
        return id;
    }
}
