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
* TestFrame 0.9 (this one, in the `testframe.api` namespace, will be phased out)
* TestFrame 0.95 or later (`org.testframe.api` namespace)

## Miscellaneous notes

In the Java currency information file, the S&atilde;o Tom&eacute; and Príncipe 
dobra (STD) is listed as a historical currency, in effect from 1977 to 2017, and 
the dobra (STN) is not identified as historical. However, Manny's currency 
converter API responds with "\{\}" for conversions involving STN, but on June 
24, 2024, that same API responded "\{"USD_STD":20697.981008\}" and 
"\{"EUR_STD":22213.258432\}" for queries involving the U.&nbsp;S. dollar and 
euro, respectively.

----

This project is closed off to Hacktoberfest.
