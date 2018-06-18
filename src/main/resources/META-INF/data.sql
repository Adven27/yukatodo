DROP TABLE IF EXISTS system_log;
DROP SEQUENCE  IF EXISTS logging_event_id_seq;

CREATE SEQUENCE logging_event_id_seq MINVALUE 1 START 1;
CREATE TABLE system_log
(
  timestmp         BIGINT NOT NULL,
  formatted_message  TEXT NOT NULL,
  logger_name       VARCHAR(254) NOT NULL,
  level_string      VARCHAR(254) NOT NULL,
  thread_name       VARCHAR(254),
  stack_trace       VARCHAR(2000),
  event_id          BIGINT DEFAULT nextval('logging_event_id_seq') PRIMARY KEY
);