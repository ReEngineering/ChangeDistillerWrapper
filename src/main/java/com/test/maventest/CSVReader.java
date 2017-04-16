package com.test.maventest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

public class CSVReader extends SourceCodeExtraction {
	static SourceCodeExtraction test1obj=new SourceCodeExtraction();
	static int column=0;
    public static void csvReader(String projName, Repository repo, Git git) {
    	String a=test1obj.repoName;
    	String a1 = a.replaceAll("/.git", "");
    	a1= a1.replaceAll("/","\\\\");
        String csvFile = a1+"\\batch.csv";
        System.out.println("csv"+csvFile);
        String line = "";
        String cvsSplitBy = ",";
        StringBuffer result = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
            	
                // use comma as separator
                String[] country = line.split(cvsSplitBy);
              // result.append(country[1]);
              String SHA=country[0];
              column++;
              System.out.println("Number of columns read: "+column );
                System.out.println(country[0]);
                SourceCodeExtraction.getFileDetails(SHA,repo,git,projName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

	
}