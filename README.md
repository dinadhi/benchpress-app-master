# benchpress-app
A simple benchmarking system using Java, Play Framework, Akka and EBean.

This is a classic Java, Play Framework, Akka and EBean application, backed by a H2 database. It demonstrates:
- Handling asynchronous results, Handling time-outs
- Achieving, Futures to use more idiomatic error handling.
- Accessing a JDBC database, using EBean.
- Asynchronous web scraping using Akka Actors

-----------------------------------------------------------------------
### Instructions
-----------------------------------------------------------------------
* Clone the project into local system
* To run the Play framework 2.6.x, you need JDK 8
* Install Scala SBT if you do not have it already. You can get it from here: [download](http://www.scala-sbt.org/download.html)
* Execute `sbt clean compile` to build the project
* Execute `sbt run` to execute the project
* benchpress-app should now be accessible at [localhost:9000](http://localhost:9000/)

### Requests

##### Start Benchmarking
```
http://localhost:9000/benchmark?path=http://localhost:9000&amp;parallelism=10
```

##### Stats By Benchpress Id
```
http://localhost:9000/stats?id=1512066779376
```

-----------------------------------------------------------------------
### Documentations
-----------------------------------------------------------------------

### Play

Play documentation is here:

[https://playframework.com/documentation/latest/Home](https://playframework.com/documentation/latest/Home)

### EBean

EBean is a Java ORM library that uses SQL:

[https://www.playframework.com/documentation/latest/JavaEbean](https://www.playframework.com/documentation/latest/JavaEbean)

and the documentation can be found here:

[https://ebean-orm.github.io/](https://ebean-orm.github.io/)
