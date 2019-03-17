# Euphoria

A simple recruiting platform web application, written for ECE366 at The Cooper Union.

Contributors: [George Ho](https://github.com/eigenfoo), [Wendy Ide](https://github.com/wside), [Luka Lipovac](https://github.com/lipovac) and [Ostap Voynarovskiy](https://github.com/ostapstephan).

## Database Instructions   

### Initializing database and tables

Beware that this will drop the database and recreate a new, empty one. In bash:

```bash
cat initialize-database.sql | mysql -u root -p
```

### Dumping database to file

Make sure you have a database called `exampledb` to dump from. In bash:

```bash
mysqldump -u root -p exampledb > exampledb-dump.sql;
```

### Loading database from dump file

Make sure you have a database called `exampledb` to dump to. In bash:

```sql
mysql -u root -p exampledb < exampledb-dump.sql;
```

### Additional handy commands in MySQL

In MySQL:

```sql
mysql -u root -p             -- Start MySQL
show databases;              -- List all databases
show tables;                 -- List all tables
describe tablename;          -- Show table details
drop table tablename;        -- Delete a table
drop database databasename;  -- Delete a table
```
