package com.mycompany.employees;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EmployeeManager {
    private static Map<Integer, Employee> employees = new HashMap<>();

    public static void main(String[] args) {
        initialize();
        for (Employee employee : employees.values()) {
            for (Employee colleague : employees.values()) {
                if (employee.getId() == colleague.getId()) {
                    continue;
                }
                Map<Integer, Period> employeeProjects = employee.getProjectHistory();
                Map<Integer, Period> colleagueProjects = colleague.getProjectHistory(employeeProjects.keySet());

                long daysWorkingTogether = daysWorkingTogether(employeeProjects, colleagueProjects);

                employee.addToColleagueHistory(daysWorkingTogether, colleague.getId());
            }
        }

        int employeeOutput = 0;
        int colleagueOutput = 0;
        long maxDaysWorkedTogether = 0;
        for (Employee employee : employees.values()) {
            if (employee.getClosestColleague().getKey()>maxDaysWorkedTogether){
                maxDaysWorkedTogether = employee.getClosestColleague().getKey();
                employeeOutput = employee.getId();
                colleagueOutput = employee.getClosestColleague().getValue();
            }
        }
        if (maxDaysWorkedTogether == 0){
            System.out.println("The employees didn't work on one project at the same time.");
        }else {
            System.out.println(employeeOutput + ", " + colleagueOutput + ", " + maxDaysWorkedTogether);
        }
    }

    public static void initialize(){
        try {
            File myObj = new File("C:\\Users\\Asus\\Desktop\\New folder (2)\\Victor-Mladenov-employees\\resources\\filename.txt");
            Scanner fileReader = new Scanner(myObj);
            fileReader.nextLine();
            while (fileReader.hasNextLine()) {
                String employeeLine = fileReader.nextLine();
                String[] employeeInfo = employeeLine.split(",");
                Integer employeeId = Integer.parseInt(employeeInfo[0]);
                Employee employee;
                if (employees.containsKey(employeeId)){
                    employee = employees.get(employeeId);
                }else {
                    employee = new Employee(employeeId);
                }
                LocalDate endTime = null;
                if (!employeeInfo[3].trim().equalsIgnoreCase("null")){
                    endTime = LocalDate.parse(employeeInfo[3].trim());
                }
                employee.addToProjectHistory(Integer.parseInt(employeeInfo[1].trim()),LocalDate.parse(employeeInfo[2].trim())
                        , endTime == null ? LocalDate.now() : endTime);
                employees.put(employeeId, employee);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
            e.printStackTrace();
        }
    }

    public static long daysWorkingTogether(Map<Integer, Period> employeeProjects, Map<Integer, Period> colleagueProjects){
        long daysWorkingTogether = 0;
        for (Integer colleagueProject : colleagueProjects.keySet()) {
            for (Integer employeeProject : employeeProjects.keySet()) {
                if (colleagueProject.equals(employeeProject)){
                    Period colleaguePeriod = colleagueProjects.get(colleagueProject);
                    Period employeePeriod = employeeProjects.get(employeeProject);
                    if (colleaguePeriod.getStartDate().atStartOfDay().isAfter(employeePeriod.getStartDate().atStartOfDay()) && colleaguePeriod.getStartDate().atStartOfDay().isBefore(employeePeriod.getEndDate().atStartOfDay())){
                        daysWorkingTogether = Duration.between(colleaguePeriod.getStartDate().atStartOfDay(), employeePeriod.getEndDate().atStartOfDay()).toDays();
                    }
                    if (colleaguePeriod.getEndDate().atStartOfDay().isAfter(employeePeriod.getStartDate().atStartOfDay()) && colleaguePeriod.getEndDate().atStartOfDay().isBefore(employeePeriod.getEndDate().atStartOfDay())){
                        daysWorkingTogether = Duration.between(employeePeriod.getStartDate().atStartOfDay(), colleaguePeriod.getEndDate().atStartOfDay()).toDays();
                    }
                }
            }
        }
        return daysWorkingTogether;
    }
}
