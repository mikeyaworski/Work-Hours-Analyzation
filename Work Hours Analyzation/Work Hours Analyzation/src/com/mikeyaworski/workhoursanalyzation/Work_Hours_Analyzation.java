package com.mikeyaworski.workhoursanalyzation;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Work_Hours_Analyzation {
    public static void main(String[] args) {
        
        // total hours all time
        // total hours for each job
        // total hours for each year
        // total hours for each different job each year
        // average hours per week all time
        // average hours per week for each job
        // average hours per week each year
        // average hours per week each year for each job
        // first and last date worked
        // first and last date worked for each year
        // first and last date worked for each job
        // average hours per shift, all time
        // average hours per shift, for each job
        // average hours per shift, for each year
        // average hours per shift, for each job each year
        
        System.out.println();
        
        String fileInput = "work_hours.txt"; // default location for input file
        String fileOutput = null; // if specified in argument, the analysis will write it on this file (in addition to writing to the console). otherwise, it will remain null and the analysis will only go to the console.
        
        if (args.length > 0) {
            fileInput = args[0];
        }
        if (args.length > 1) {
            fileOutput = args[1];
        }
        
        Scanner scanner = null; // to read from the input file
        PrintWriter pw = null; // to write to the output file
        
        try {
            File file = new File(fileInput);
            scanner = new Scanner(file);
            
            // attempt to make a file output stream
            // if it fails, just make it null and continue as usual
            if (fileOutput != null) {
                try {
                    pw = new PrintWriter(fileOutput);
                } catch (FileNotFoundException fnf) {
                    System.out.println("The output file was not found: " + fileOutput);
                } catch (Exception e) {
                    System.out.println("Problem reading output file: " + fileOutput);
                    e.printStackTrace();
                }
            }
            
            double totalHours = 0;
            int totalNumberOfShifts = 0;
            String firstDateEver = "", lastDateEver = "";
            
            List<Job> jobs = new ArrayList<Job>();
            List<Year> years = new ArrayList<Year>();
            
            // index of the job and year being focused on in the iteration, and year in the previous iteration
            int jobIndex = -1, yearIndex = -1, previousYearIndex = -1;
            
            // Each iteration is a shift in a job. the shift has a year.
            // currentYearJob is the job being focused on in a specific year.
            // If the job changes, the currentYearJob updates. If the year changes, the currentYearJob changes.
            // When the currentYearJob changes, it needs to be added to the years list. It is edited completely before being added to the list
            // Note: this does not need to be added to the jobs list. The jobs list is updated at the same time this is.
            Job currentYearJob = null;
            
            // keeps track of whether or not currentYearJob is a reference to a year already in the list or not
            // so if it's a new job all together, it needs to be added to the year when it updates
            // but if it's a reference to a job already in the list, it doesn't need to be added to the list again
            boolean yearAlreadyAdded = false;
            
            // each iteration focuses on a shift or job name
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                // doesn't start with hyphen and ends with colon
                // this is a job name
                if (line.matches("[^-].*:$")) {
                    
                    String jobName = line.substring(0, line.length() - 1);
                    Job job = new Job(jobName);
                    jobs.add(job);
                    
                    if (jobIndex > -1) {
                        if (!yearAlreadyAdded) { // don't add the job if it's just a reference to a job already in the year
                            years.get(previousYearIndex).addJob(currentYearJob);
                        }
                        // update the dates
                        years.get(previousYearIndex).setFirstDate(Dates.compareDatesFirst(currentYearJob.getFirstDate(), years.get(previousYearIndex).getFirstDate()));
                        years.get(previousYearIndex).setLastDate(Dates.compareDatesLast(currentYearJob.getLastDate(), years.get(previousYearIndex).getLastDate()));
                        previousYearIndex = -1; // new job, so restart the year index because this is only for yearly jobs, but it's a new job now
                    }
                    // new job: reset yearly job, reset the reference flag, increment the job index for the new job
                    currentYearJob = new Job(jobName);
                    yearAlreadyAdded = false;
                    jobIndex++;
                    
                } else if (line.matches("^-.*")) { // starts with hyphen (this is a job shift)
                    String[] daySplices = line.substring(1, line.length()).split(";");
                    
                    // extract the date from the input line
                    String date = daySplices[0];
                    
                    // extract the hours from the input line
                    String hours = daySplices[daySplices.length - 1].trim();
                    if (hours.endsWith("h")) hours = hours.substring(0, hours.length() - 1); // get rid of h at the end
                    
                    String year = "";
                    
                    // extract the year from the input line
                    String[] dateSplices = date.split("[\\/,\\s]+");
                    if (dateSplices.length >= 2) { // year was split by slash
                        year = dateSplices[dateSplices.length - 1];
                    }
                    if (year.length() == 2) {
                        year = "20" + year;
                    }
                    
                    yearIndex = -1;
                    
                    // see if the year already exists in the years list
                    for (int i = 0; i < years.size(); i++) {
                        if (years.get(i).getYear().equals(year)) {
                            yearIndex = i;
                        }
                    }
                    
                    // this year was already used, but is not currently the one focused on (year changed during job)
                    // or, the year didn't already exist (-1)
                    if (yearIndex != previousYearIndex) {
                        if (previousYearIndex != -1) {
                            // save the previous job and update to the new focus
                            if (!yearAlreadyAdded) { // don't add the job if it's just a reference to a job already in the year
                                years.get(previousYearIndex).addJob(currentYearJob);
                            }
                            // update first and last dates
                            years.get(previousYearIndex).setFirstDate(Dates.compareDatesFirst(currentYearJob.getFirstDate(), years.get(previousYearIndex).getFirstDate()));
                            years.get(previousYearIndex).setLastDate(Dates.compareDatesLast(currentYearJob.getLastDate(), years.get(previousYearIndex).getLastDate()));
                            
                            // if the year exists already, check if the job already exists for this year as well
                            // if it does, create a reference to it; otherwise, create a brand new job for this year
                            if (yearIndex != -1) {
                                for (Job yearJob : years.get(yearIndex).getJobs()) {
                                    if (yearJob.getName().equals(currentYearJob.getName())) {
                                        currentYearJob = yearJob;
                                        yearAlreadyAdded = true;
                                    } else {
                                        currentYearJob = new Job(currentYearJob.getName());
                                        yearAlreadyAdded = false;
                                    }
                                }
                            } else {
                                currentYearJob = new Job(currentYearJob.getName());
                                yearAlreadyAdded = false;
                            }
                            
                        }
                        previousYearIndex = yearIndex; // previousYearIndex is no longer needed this iteration, so update it for the next one
                    }
                    
                    // if year didn't already exist, create a new one
                    if (yearIndex == -1) {
                        Year y = new Year(year);
                        years.add(y);
                        yearIndex = previousYearIndex = years.size() - 1;
                    }
                    
                    // update the total hours
                    jobs.get(jobIndex).setHours(jobs.get(jobIndex).getHours() + Double.parseDouble(hours));
                    currentYearJob.setHours(currentYearJob.getHours() + Double.parseDouble(hours));
                    // this means the current year's total hours will never get updated since this job won't be added again. so update manually
                    if (yearAlreadyAdded) {
                        years.get(yearIndex).addHours(Double.parseDouble(hours));
                    }
                    
                    // update the shift count
                    jobs.get(jobIndex).addShift();
                    currentYearJob.addShift();
                    
                    // update the first last dates for the job in focus (yearly and overall)
                    jobs.get(jobIndex).setLastDate(Dates.compareDatesLast(date, jobs.get(jobIndex).getLastDate()));
                    jobs.get(jobIndex).setFirstDate(Dates.compareDatesFirst(date, jobs.get(jobIndex).getFirstDate()));
                    currentYearJob.setLastDate(Dates.compareDatesLast(date, currentYearJob.getLastDate()));
                    currentYearJob.setFirstDate(Dates.compareDatesFirst(date, currentYearJob.getFirstDate()));
                }
            }
            // last job being worked on needs to be added
            if (!yearAlreadyAdded) { // don't add the job if it's just a reference to a job already in the year
                years.get(previousYearIndex).addJob(currentYearJob);
            }
            // set first and last dates as a sanity check
            years.get(previousYearIndex).setFirstDate(Dates.compareDatesFirst(currentYearJob.getFirstDate(), years.get(previousYearIndex).getFirstDate()));
            years.get(previousYearIndex).setLastDate(Dates.compareDatesLast(currentYearJob.getLastDate(), years.get(previousYearIndex).getLastDate()));
            
            // sort the years by descending years order
            Collections.sort(years, new Year.YearComparator());
            
            // these are the all-time possible first and last dates (will be the first and last dates from each job)
            List<String> possibleFirstDates = new ArrayList<String>(), possibleLastDates = new ArrayList<String>();
            
            // iterate through the jobs and collect some aggregate data
            for (Job job : jobs) {
                totalHours += job.getHours();
                totalNumberOfShifts += job.getNumberOfShifts();
                possibleFirstDates.add(job.getFirstDate());
                possibleLastDates.add(job.getLastDate());
            }
            
            firstDateEver = Dates.compareDatesFirst(possibleFirstDates);
            lastDateEver = Dates.compareDatesLast(possibleLastDates);
            
            System.out.println("All time hours: " + convertHoursToString(totalHours));
            System.out.println("First date ever: " + firstDateEver + "\nLast date ever: " + lastDateEver);
            
            String averageHoursPerWeek = getAverageHoursPerWeek(firstDateEver, lastDateEver, totalHours);
            System.out.println("Average Hours Per Week: " + averageHoursPerWeek);
            
            double avgHoursPerShift = totalHours / totalNumberOfShifts;
            System.out.println("Average Hours Per Shift: " + convertHoursToString(avgHoursPerShift) + "\n\n--------------------------------------------\n");
            
            // print to the output file if one was initialized
            if (pw != null) {
                pw.write("All time hours: " + convertHoursToString(totalHours) + "\n");
                pw.append("First date ever: " + firstDateEver + "\nLast date ever: " + lastDateEver + "\n");
                pw.append("Average Hours Per Week: " + averageHoursPerWeek + "\n");
                pw.append("Average Hours Per Shift: " + convertHoursToString(avgHoursPerShift) + "\n\n--------------------------------------------\n" + "\n");
            }
            
            for (Job job : jobs) {
                System.out.println(job + "\n");
                
                // print to the output file if one was initialized
                if (pw != null) {
                    pw.append(job + "\n" + "\n");
                }
            }
            
            // print yearly data
            for (Year year : years) {
                
                System.out.println("--------------------------------------------\nYear: " + year.getYear() + "\n");
                
                System.out.println("Total hours this year: " + year.getHours());
                System.out.println("First Date of the Year: " + year.getFirstDate());
                System.out.println("Last Date of the Year: " + year.getLastDate());
                System.out.println("Average Hours Per Week: " + getAverageHoursPerWeek(year.getFirstDate(), year.getLastDate(), year.getRawHours()));
                System.out.println("Average Hours Per Shift: " + convertHoursToString(year.getAverageHoursPerShift()) + "\n");
                
                // print to the output file if one was initialized
                if (pw != null) {
                    pw.append("--------------------------------------------\nYear: " + year.getYear() + "\n" + "\n");
                    
                    pw.append("Total hours this year: " + year.getHours() + "\n");
                    pw.append("First Date of the Year: " + year.getFirstDate() + "\n");
                    pw.append("Last Date of the Year: " + year.getLastDate() + "\n");
                    pw.append("Average Hours Per Week: " + getAverageHoursPerWeek(year.getFirstDate(), year.getLastDate(), year.getRawHours()) + "\n");
                    pw.append("Average Hours Per Shift: " + convertHoursToString(year.getAverageHoursPerShift()) + "\n" + "\n");
                }
                
                List<Job> jobsForYear = year.getJobs();
                
                for (Job job : jobsForYear) {
                    System.out.println(job + "\n");
                    
                    // print to the output file if one was initialized
                    if (pw != null) {
                        pw.append(job + "\n" + "\n");
                    }
                }
            }
            
            scanner.close();
            if (pw != null) pw.close();
            
        } catch (FileNotFoundException fnf) {
            System.out.println("The input file cannot be found.\nIf no location was specified, the default location would in the root directory of this file under the name \"work_hours.txt\".\nThe location used was: " + fileInput);
        } catch (Exception e) {
            System.out.println("Problem reading input file: " + fileInput);
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (pw != null) {
                pw.close();
            }
        }
    }
    
    public static String getAverageHoursPerWeek(String firstDate, String lastDate, double totalHours) {
        double numberOfWeeks = Dates.weekDifference(firstDate, lastDate);
        if (numberOfWeeks < 1) numberOfWeeks = 1;
        double avgHours = totalHours / numberOfWeeks;
        
        return convertHoursToString(avgHours);
    }
    
    // e.g. 7.25 hours returns "7 hours 15 minutes"
    // and 7.1 hours returns "7 hours 6 minutes"
    public static String convertHoursToString(double hours) {
        String hoursStr = "";
        
        int mins = (int)Math.round(((hours % 1) * 60)); // number of extra minutes on the hour
        int ihours = (int)hours; // integer hours (truncate the minutes out)
        
        if (ihours > 0) hoursStr = String.valueOf(ihours) + " hours ";
        if (mins > 0) hoursStr += String.valueOf(mins) + " minutes";
        
        return hoursStr;
    }
}
