package com.inenergis.util;

import java.io.IOException;

/**
 * Created by egamas on 01/12/2017.
 */
public interface FileProcessor {

    void generateCsvSaFile(String path, String pathOut) throws IOException;

    void generateCsvRedshiftFile(String pathOut,String pathOutSa) throws IOException;

}
