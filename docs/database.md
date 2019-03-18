# Database Instructions   

## Initializing database and tables

Beware that this will drop the database and recreate a new, empty one. In bash,
from the `euphoria/euphoria-service/src/main/resources/` directory:

```bash
cat initialize-database.sql | mysql -u root -p
```

## Dump/load database to/from a dump file

Make sure you have a database called `exampledb` to dump from/to. In bash:

```bash
mysqldump -u root -p exampledb > exampledb-dump.sql;  # Dump to a dump file
mysql -u root -p exampledb < exampledb-dump.sql;      # Load from a dump file
```

## Additional handy commands in MySQL

In MySQL:

```sql
mysql -u root -p             -- Start MySQL
show databases;              -- List all databases
show tables;                 -- List all tables
describe tablename;          -- Show table details
select * from tablename;     -- Show all table contents
drop table tablename;        -- Delete a table
drop database databasename;  -- Delete a database
```
