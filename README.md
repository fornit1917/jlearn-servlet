# jlearn-servlet

My small study-project for practice with core java web technologies. Web application for tracking books which I read.

### Tech details

* Without frameworks: Servlets and plain JDBC
* Build by Maven
* Uses some libraries: Logback, Freemarker
* Uses async servlet and async http client with Java 8 CompletableFuture (see [BookDetailsServlet](https://github.com/fornit1917/jlearn-servlet/blob/master/src/main/java/jlearn/servlet/BookDetailsServlet.java) and [BookDetailsService](https://github.com/fornit1917/jlearn-servlet/blob/master/src/main/java/jlearn/servlet/service/BookDetailsService.java)
* Deployed on [Heroku Cloud](http://heroku.com) (was deployed :))
* Deployed on my own server using Docker