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
* ~~TestFrame 0.9~~ (this has been phased out)
* TestFrame 0.95 or later (`org.testframe.api` namespace)

## Miscellaneous notes

In the Java currency information file, the S&atilde;o Tom&eacute; and 
Pr&iacute;ncipe dobra (STD) is listed as a historical currency, in effect from 
1977 to 2017, and the dobra (STN) is not identified as historical. However, 
Manny's currency converter API responds with "\{\}" for conversions involving 
STN, but on June 24, 2024, that same API responded "\{"USD_STD":20697.981008\}" 
and "\{"EUR_STD":22213.258432\}" for queries involving the U.&nbsp;S. dollar and 
euro, respectively.

Not sure what's going on with the Mauritanian ouguiya. The currency code "MRO" 
is identified as "Mauritanian Ouguiya (1973–2017)" in the Java currency 
information file. But Manny's currency converter, as of July 9, 2024, gives an 
exchange rate of 356.999828 for U.&nbsp;S. dollars to MRO. But that same 
converter gives an exchange rate of 35.6999828 for USD to MRU, which the Java 
currency information file suggests is not a historical currency.

----

This project is closed off to Hacktoberfest.
