# Currency Demo

For a presentation about JSR-354.

Money is unfortunately a big part of our lives. And yet, Java does not provide 
an out-of-the-box data type to represent money amounts. Our options in Java 8 
are, from very bad to very good:

* Use `float` or `double`
* Use 'int` or `long`
* Use `java.math.BigDecimal`
* Create our own class
* Use a third-party library

The JSR-354 proposal was approved for consideration to add to Java 9. However, 
it wasn't actually added to Java 9, and as of Java 21 it is still not an 
official part of the Java Development Kit.

## Dependencies

* Joda Money 1.03, `org.joda.money.{CurrencyMismatchException, Money}`

TODO: Add Moneta dependency

### Dependencies for test classes

* TestNG

TODO: Add dependency for TestFrame 0.8, 0.9 or 1.0, 
`static testframe.api.Asserters.assertThrows` or 
`static org.testframe.api.Asserters.assertThrows`, I've been having trouble with 
`assertThrows()` from TestNG...

----

This project is closed off to Hacktoberfest.
