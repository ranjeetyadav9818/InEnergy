package com.inenergis.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by egamas on 20/10/2017.
 */
public class CsvGenerator {


    private static String DEFAULT_PATH ;
    private static String PATH ;
    private static String PATH_OUT ;
    private static String PATH_OUT_SA ;
    private static String TABLE ;

    public static void main(String... args) {

        retrieveParameters(args);

        System.out.println("Parameters :");
        System.out.println("\tInitial file: " + PATH);
        System.out.println("\tFile to export to sheet " + PATH_OUT_SA);
        System.out.println("\tFile to send to redshift: " + PATH_OUT);
        System.out.println("\tTarget table : " + TABLE);

        try {

            final FileProcessor fileProcessor = FileProcesorFactory.getFileProcessor(TABLE);

            System.out.println("Generating sa file...");
            fileProcessor.generateCsvSaFile(PATH,PATH_OUT_SA);
            System.out.println("SA file generated successfully");

            System.out.println("Generating redshift file...");
            fileProcessor.generateCsvRedshiftFile(PATH_OUT,PATH_OUT_SA);
            System.out.println("redshift file generated successfully!!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void retrieveParameters(String[] args) {

        File directory = new File("./");
        DEFAULT_PATH = directory.getAbsolutePath();
        DEFAULT_PATH = DEFAULT_PATH.substring(0,DEFAULT_PATH.length()-2)  //remove point
               + "/InenergisUtils/src/main/resources/"; // Project folder
        System.out.println("Absolute Path: "+DEFAULT_PATH);

        if (args != null && args.length > 0) {
            PATH = args[0].trim();
        } else {
            PATH = DEFAULT_PATH + "MeterDataIni.txt";
        }
        if (args != null && args.length > 1) {
            PATH_OUT = args[1].trim();
        } else {
            PATH_OUT = PATH + ".csv";
        }
        if (args != null && args.length > 2) {
            PATH_OUT_SA = args[2].trim();
        } else {
            PATH_OUT_SA = PATH + "_sa.csv";
        }
        if (args != null && args.length > 3) {
            TABLE = args[3].trim();
        } else {
            TABLE = "interval_data";
//            TABLE = "PEAK_DEMAND_INTERVAL_DATA";
        }
    }





}
