package com.inenergis.microbot.camel;

import com.inenergis.microbot.camel.beans.FileSorter;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileInformation {

    private Pattern p = Pattern.compile(FileSorter.pattern);

    private Logger log = Logger.getLogger(FileInformation.class);

    public Object readTriggerFile(@Body Object body, @Headers Map headers) {
        log.info("Body " + body);
        log.info("Headers " + headers);
        String fileName = (String) headers.get("CamelFileName");
        String path = (String) headers.get("CamelFileAbsolutePath");
        Matcher m = p.matcher(fileName);
        if (m.find()) {
            String program = m.group(FileSorter.PROGRAM_INDEX);
            String date = m.group(FileSorter.DATE_INDEX);
            String action = m.group(FileSorter.ACTION_TYPE_INDEX);
            File file = new File(path);
            log.info(String.format("Trigger file %s %s %s %s", fileName, program, date, action));

            for (File f : file.getParentFile().listFiles()) {
                log.info("File " + f.getName());
                if (!f.getName().equals(fileName) && !f.getName().contains("camelLock") && !f.getName().contains("trigger")) {
                    Matcher m2 = p.matcher(f.getName());
                    if (m2.find()) {
                        if (program.equals(m2.group(FileSorter.PROGRAM_INDEX)) && date.equals(m2.group(FileSorter.DATE_INDEX)) && action.equals(m2.group(FileSorter.ACTION_TYPE_INDEX))) {
                            log.info("Same event creating trigger file");
                            File newF = new File(file.getParentFile(), f.getName() + ".trigger");
                            try {
                                newF.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        log.debug("Ignoring file");
                    }
                }
            }
        }

        return body;
    }
}