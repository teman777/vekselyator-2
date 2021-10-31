FROM mysql:5.6

ADD init_veksel.sql /docker-entrypoint-initdb.d/init_veksel.sql
RUN chmod -R 775 /docker-entrypoint-initdb.d

ENV MYSQL_ROOT_PASSWORD vekselbotpassword
