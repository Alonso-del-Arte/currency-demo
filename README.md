# Currency Demo

For a presentation about JSR-354.

Money is unfortunately a big part of our lives. And yet, Java does not provide 
an out-of-the-box data type to represent money amounts. Our options in Java 8 
are, from very bad to very good but still short of ideal:

* Use `float` or `double` &mdash; not good to use floating point for fixed point 
quantities
* Use 'int` or `long` &mdash; no information about where to put the decimal 
point
* Use `java.math.BigDecimal` &mdash; no information about the currency
* Create our own class &mdash; we might duplicate third-party library efforts
* Use a third-party library &mdash; we might need to integrate our project with 
a project that uses a different third-party library

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
* ~~TestFrame 0.95~~ (`org.testframe.api` namespace but `assertPrintOut()` was a 
stub, this has also been phased out)
* TestFrame 1.0 or later (`org.testframe.api` namespace, `assertPrintOut()`)

## Notes about internationalization

Although I have not made a conscious effort to internationalize this repository, 
there is some internationalization in this program. Java gives us some 
internationalization for "free." In particular, the names and symbols of 
currencies will vary depending on your locale.

Unicode provides a fairly complete set of currency symbols, many of which are 
accessible through some `java.util.Currency`'s `getSymbol()` function depending 
on the default or specified locale. The following table lists some symbols that 
are generally used for one specific currency (though the ones marked with an 
asterisk are also used for some other currency in combination with letters).

| Unicode | Symbol   | ISO-4217 | Name                     |
|---------|----------|----------|--------------------------|
| U+0024  | $        | USD*     | United States dollar     |
| U+00A3  | &pound;  | GBP      | British pound            |
| U+00A5  | &yen;    | JPY*     | Japanese yen             |
| U+20A1  | &#x20A1; | CRC      | Costa Rican col&oacute;n |
| U+20A6  | &#x20A6; | NGN      | Nigerian naira           |
| U+20A9  | &#x20A9; | KRW      | South Korean won         |
| U+20AA  | &#x20AA; | ILS      | Israeli new shekel       |
| U+20AB  | &#x20AB; | VND      | Vietnamese dong          |
| U+20AC  | &euro;   | EUR      | Euro                     |
| U+20B9  | &#x20B9; | INR      | Indian rupee             |

The symbol &#x20A0; was intended for the euro but it's hardly used nowadays. The 
symbol &#x20A8; for the Indian rupee was used much more frequently.

The cruzeiro symbol &#x20A2; is of greater historical interest, even though now 
the official currency of Brazil is the Brazilian real (BRL), for which the 
symbols R$ and BR$ are used to varying degree.

Some euro-replaced currencies still have their own symbols in Unicode.

| Unicode | Symbol   | ISO-4217 | Name           |
|---------|----------|----------|----------------|
| U+20A3  | &#x20A3; | FRF      | French franc   |
| U+20A4  | &#x20A4; | ITL      | Italian lira   |
| U+20A7  | &#x20A7; | ESP      | Spanish peseta |

Note that Bitcoin has the symbol &#x20BF; and the 3-letter code XBT has been 
proposed for ISO-4217.

FINISH WRITING

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
