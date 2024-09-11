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

At first, the baseline for this project was Java 8, but I've decided to move it 
up to Java 17. I'm actually using Java 21, though. Compiling with Java 17 or 
earlier might still be possible, but then be aware that some null pointer 
exceptions might have null exception messages.

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

Venezuela apparently has two active official currencies, the Venezuelan 
bol&iacute;var (VES) and the bol&iacute;var digital (VED), which has been 
described as no more a digital currency than any other official currency. As the 
API I'm using in this project recognizes VES but not VED, I'm excluding VED from 
this project.

The situation with the Zimbabwean dollar (ZWD, ZWL, ZWN, ZWR) is even more 
confusing. The currency chooser in this project simply omits them all, even 
though at least one currency conversion API acknowledges one of the four 
ISO-4217 codes as valid. Specifically, Manny's currency converter API 
acknowledges ZWL as a valid current currency code, with an exchange rate from 
U.&nbsp;S. dollar of 321.999592 as of September 10, 2024.

----

This project is closed off to Hacktoberfest.
