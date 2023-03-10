package com.inenergis.dao.aws;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.DownloadableData;
import com.inenergis.entity.meterData.IntervalData;
import com.inenergis.service.aws.DateGroupedIntervalData;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

@Stateless
@Getter
public class IntervalDataDao extends DownloadableData {

    @Resource(name = "RedShift", lookup = "java:jboss/datasources/RedShift")
    private DataSource redShiftDataSource;

    private String header = "service_point_id\tsecondary_sp_id\tusage_value\tdate\ttime\tunits\tis_estimate\tdaylight_savings\tdirection";
    private Logger logger = LoggerFactory.getLogger(IntervalDataDao.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private List<IntervalData> getDataTemplate(String sql) throws SQLException, IOException {
        JdbcTemplate template = new JdbcTemplate(redShiftDataSource);
        return template.query(sql, new RowMapper<IntervalData>() {
            @Override
            public IntervalData mapRow(ResultSet rs, int i) throws SQLException {
                return IntervalData.builder().servicePointId(rs.getString("service_point_id")).
                        secondarySpId(rs.getString("secondary_sp_id")).
                        value(rs.getBigDecimal("usage_value")).
                        date(rs.getString("date")).
                        time(rs.getString("usage_time")).
                        units(rs.getString("units")).
                        isEstimate(rs.getString("is_estimate")).
                        daylightSavings(rs.getString("daylight_savings")).
                        direction(rs.getString("direction")).build();
            }
        }, null);
    }

    public ByteArrayInputStream getByServicePointId(String servicePointId) throws SQLException, IOException {
        String sql = "select service_point_id, " +
                "secondary_sp_id, " +
                "usage_value, " +
                "date, " +
                "usage_time, " +
                "units, " +
                "is_estimate, " +
                "daylight_savings, " +
                "direction "
                + "from COMMODITY_INTERVAL_DATA where service_point_id = " + servicePointId;

        return dataToByteArrayInputStream(getDataTemplate(sql));
    }

    public List<IntervalData> getIntervalData(String servicePointId, LocalDateTime dateFrom, LocalDateTime dateTo) throws SQLException, IOException {
        String sql = "select service_point_id, " +
                "secondary_sp_id, " +
                "usage_value, " +
                "date, " +
                "usage_time, " +
                "units, " +
                "is_estimate, " +
                "daylight_savings, " +
                "direction "
                + "from COMMODITY_INTERVAL_DATA where service_point_id = " + servicePointId + " " +
                "AND usage_time >= " + dateFrom.format(formatter) + " AND usage_time <= " + dateTo.format(formatter) + " ORDER BY usage_time ASC";

        return getDataTemplate(sql);
    }

    public List<DateGroupedIntervalData> getIntervalDataGroupByMonth(List<AgreementPointMap> agreementPointMapList, LocalDateTime dateFrom, LocalDateTime dateTo) {

        StringJoiner listSr = new StringJoiner(",");
        agreementPointMapList.stream().forEach(apm -> listSr.add(apm.getServicePoint().getServicePointId()));

        JdbcTemplate template = new JdbcTemplate(redShiftDataSource);
        final String query =
                "select service_point_id, substring(date, 5,2) \"month\"  , sum(usage_value) \"sum\"\n , units " +
                        "from commodity_interval_data\n" +
                        "  where service_point_id in (" + listSr.toString() + ")" +
                        " AND usage_time >= " + dateFrom.format(formatter) + " AND usage_time <= " + dateTo.format(formatter) +
                        " group by service_point_id, substring(date, 5,2), units "
                        + "ORDER BY substring(date, 5,2) ASC ";

        final List<DateGroupedIntervalData> result = template.query(query, new RowMapper<DateGroupedIntervalData>() {
            @Override
            public DateGroupedIntervalData mapRow(ResultSet resultSet, int i) throws SQLException {
                return DateGroupedIntervalData.builder()
                        .month(resultSet.getString("month"))
                        .servicePointId(resultSet.getString("service_point_id"))
                        .usageValue(resultSet.getBigDecimal("sum"))
                        .units(resultSet.getString("units")).build();
            }
        }, null);
        return result;
    }

    public List<DateGroupedIntervalData> getIntervalDataGroupByDay(List<AgreementPointMap> agreementPointMapList, LocalDateTime dateFrom, LocalDateTime dateTo) {
        JdbcTemplate template = new JdbcTemplate(redShiftDataSource);

        StringJoiner listSr = new StringJoiner(",");
        agreementPointMapList.stream().forEach(apm -> listSr.add(apm.getServicePoint().getServicePointId()));

        final String query =
                "select service_point_id, substring(date, 5,4) \"day\"  , sum(usage_value) \"sum\"\n , units " +
                        "from COMMODITY_INTERVAL_DATA\n" +
                        "  where service_point_id in (" + listSr.toString() + ")" +
                        " AND usage_time >= " + dateFrom.format(formatter) + " AND usage_time <= " + dateTo.format(formatter) +
                        " group by service_point_id, substring(date, 5,4), units "
                        + "ORDER BY substring(date, 5,4) ASC ";


        final List<DateGroupedIntervalData> result = template.query(query, new RowMapper<DateGroupedIntervalData>() {
            @Override
            public DateGroupedIntervalData mapRow(ResultSet resultSet, int i) throws SQLException {

                final String dayMonth = resultSet.getString("day");
                final String month = dayMonth.substring(0, 2);
                final String day = dayMonth.substring(2, 4);
                return DateGroupedIntervalData.builder()
                        .day(day)
                        .month(month)
                        .servicePointId(resultSet.getString("service_point_id"))
                        .usageValue(resultSet.getBigDecimal("sum"))
                        .units(resultSet.getString("units")).build();
            }
        }, null);
        return result;
    }

    @Override
    protected String serialize(Serializable objectToSerialize) {
        return objectToSerialize.toString();
    }
}
