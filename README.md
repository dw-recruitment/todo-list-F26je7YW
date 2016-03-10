# todo.app

`clj + cljs <3`

## Prerequisites

- You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

- You will need to setup a Postgres database
as follows:

	```
	CREATE DATABASE TODOS OWNER admin;
	CREATE USER admin WITH PASSWORD 'admin';
	```

## Running the ring server

To start a web server for the application, run:

    lein run

## Running the figwheel server

    lein figwheel

then visit: http://localhost:3000

## License

Copyright © 2016 FIXME
