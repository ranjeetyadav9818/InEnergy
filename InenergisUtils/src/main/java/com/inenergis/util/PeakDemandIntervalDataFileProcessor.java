package com.inenergis.util;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by egamas on 01/12/2017.
 */
public class PeakDemandIntervalDataFileProcessor implements FileProcessor {

    @Override
    public void generateCsvSaFile(String path, String pathOutSa) throws IOException {
        DateTimeFormatter ymdFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter ymdhmissFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        final List<LocalDateTime> measures = generate30DaysLastMonth();
        Stream<String> stream = Files.lines(Paths.get(path));
        final List<String> lines = stream.map(line -> {
            final String[] split = line.split(",");
            final String sa = split[0];
            final String sp_id = split[1];
            return new ImmutablePair(sp_id, sa);
        }).map(pair
                -> {
            final List<String> spXdates = measures.stream().map(measure ->
                    pair.left + "," + pair.left + "," + generateValue() + "," + measure.format(ymdFormatter) + "," +
                            measure.format(ymdhmissFormatter)
                            + ",KW,A,PDT,ELECTRICITY," + pair.right
            ).collect(Collectors.toList());
            return spXdates;
        }).flatMap(Collection::stream).collect(Collectors.toList());

        Files.write(Paths.get(pathOutSa), lines);

    }

    private String generateValue() {
        final DecimalFormat f = new DecimalFormat("###.#######");
        return f.format((Math.random() * 9.0) + 0.4);
    }

    private static List<LocalDateTime> generate30DaysLastMonth() {
        List<LocalDateTime> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            for (int day = 1; day <= 28; day++) {
                for (int hour = 0; hour < 24; hour++) {
                    result.add(LocalDateTime.of(2018, month, day, hour, 10));
                }
            }
        }
        return result;
    }

    @Override
    public void generateCsvRedshiftFile(String pathOut, String pathOutSa) throws IOException {
        Stream<String> stream = Files.lines(Paths.get(pathOutSa));
        final List<String> lines = stream.map(line -> {
            final String[] split = line.split(",");
            return split[0] + "," +
                    split[1] + "," +
                    split[2] + "," +
                    split[3] + "," +
                    split[4] + "," +
                    split[5] + "," +
                    split[6] + "," +
                    split[7];
        }).collect(Collectors.toList());

        Files.write(Paths.get(pathOut), lines);
    }
}
