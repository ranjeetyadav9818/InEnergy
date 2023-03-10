package com.inenergis.dao.aws;

import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.DownloadableData;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.meterData.PeakDemandMeterData;
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
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

@Stateless
@Getter
public class PeakDemandIntervalDataDao extends DownloadableData {

    @Resource(name = "RedShift", lookup = "java:jboss/datasources/RedShift")
    private DataSource redShiftDataSource;

    private String header = "meterReadingId\tservice_point_id\tsecondary_sp_id\tdemand_value\tdate\ttime\tunits\tis_estimate\tdaylight_savings\tcommodity";
    private Logger logger = LoggerFactory.getLogger(PeakDemandIntervalDataDao.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private List<PeakDemandMeterData> getDataTemplate(String sql) throws SQLException, IOException {
        JdbcTemplate template = new JdbcTemplate(redShiftDataSource);
        return template.query(sql, new RowMapper<PeakDemandMeterData>() {
            @Override
            public PeakDemandMeterData mapRow(ResultSet rs, int i) throws SQLException {
                return PeakDemandMeterData.builder().
                        meterReadingId(rs.getString("meter_reading_id")).
                        servicePointId(rs.getString("service_point_id")).
                        secondarySpId(rs.getString("secondary_sp_id")).
                        value(rs.getBigDecimal("usage_value")).
                        date(rs.getString("date")).
                        time(rs.getString("usage_time")).
                        units(rs.getString("units")).
                        isEstimate(rs.getString("is_estimate")).
                        daylightSavings(rs.getString("daylight_savings")).
                        commodityType(CommodityType.valueOf(rs.getString("commodity_type"))).build();
            }
        }, null);
    }

    public ByteArrayInputStream getByServicePointId(String servicePointId) throws SQLException, IOException {
        final String query = "select " +
                "meter_reading_id," +
                " service_point_id, " +
                "secondary_sp_id, " +
                "usage_value, " +
                "date, " +
                "usage_time, " +
                "units, " +
                "is_estimate, " +
                "daylight_savings, " +
                "commodity_type "
                + "from PEAK_DEMAND_INTERVAL_DATA where service_point_id = " + servicePointId;

        return dataToByteArrayInputStream(getDataTemplate(query));
    }

    public List<PeakDemandMeterData> getByServicePointAndDate(String servicePointId, LocalDateTime dateFrom, LocalDateTime dateTo) throws SQLException, IOException {
        String sql = "select " +
                "meter_reading_id," +
                "service_point_id, " +
                "secondary_sp_id, " +
                "usage_value, " +
                "date, " +
                "usage_time, " +
                "units, " +
                "is_estimate, " +
                "daylight_savings, " +
                "commodity_type "
                + "from PEAK_DEMAND_INTERVAL_DATA where service_point_id = " + servicePointId + " " +
                "AND usage_time >= " + dateFrom.format(formatter) + " AND usage_time <= " + dateTo.format(formatter) + " ORDER BY (time) ASC";

        return getDataTemplate(sql);
    }

    //todo filter by commodity
    public List<DateGroupedIntervalData> getIntervalDataGroupByMonth(List<AgreementPointMap> agreementPointMapList, LocalDateTime dateFrom, LocalDateTime dateTo) {

        StringJoiner listSr = new StringJoiner(",");
        agreementPointMapList.stream().forEach(apm -> listSr.add(apm.getServicePoint().getServicePointId()));

        JdbcTemplate template = new JdbcTemplate(redShiftDataSource);
        final String query =
                "select service_point_id, substring(date, 5,2) \"month\"  , sum(usage_value) \"sum\"\n , units " +
                        "from peak_demand_interval_data\n" +
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

    //todo filter by commodity
    public List<DateGroupedIntervalData> getIntervalDataGroupByDay(List<AgreementPointMap> agreementPointMapList, LocalDateTime dateFrom, LocalDateTime dateTo) {
        JdbcTemplate template = new JdbcTemplate(redShiftDataSource);

        StringJoiner listSr = new StringJoiner(",");
        agreementPointMapList.stream().forEach(apm -> listSr.add(apm.getServicePoint().getServicePointId()));

        final String query =
                "select service_point_id, substring(date, 5,4) \"day\"  , sum(usage_value) \"sum\"\n , units " +
                        "from PEAK_DEMAND_INTERVAL_DATA\n" +
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
                final String service_point_id = resultSet.getString("service_point_id");
                final BigDecimal sum = resultSet.getBigDecimal("sum");
                final String units = resultSet.getString("units");
                return DateGroupedIntervalData.builder()
                        .day(day)
                        .month(month)
                        .servicePointId(service_point_id)
                        .usageValue(sum)
                        .units(units).build();
            }
        }, null);
        return result;
    }

    @Override
    protected String serialize(Serializable objectToSerialize) {
        return objectToSerialize.toString();
    }
}
