DROP TABLE IF EXISTS peak_demand_interval_data;
CREATE TABLE peak_demand_interval_data (

  id               BIGINT DEFAULT "identity"(108448, 0, '0,1' :: TEXT),
  meter_reading_id VARCHAR(10),
  service_point_id VARCHAR(10),
  secondary_sp_id  VARCHAR(10),
  usage_value      NUMERIC(10, 7),
  date             VARCHAR(12),
  usage_time       VARCHAR(10),
  units            VARCHAR(10),
  is_estimate      VARCHAR(10),
  daylight_savings VARCHAR(10),
  commodity_type   VARCHAR(20)
);

ALTER TABLE interval_data
  ADD COLUMN
  commodity_type VARCHAR(20) DEFAULT 'ELECTRICITY';

