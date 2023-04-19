INSERT INTO scheduled_task (name, description, expression, active, is_system)
VALUES ('Synchronize table structure', 'Synchronize the table structure of the data source library at 1 am every day', '0 20 * * * ?', TRUE, TRUE);
