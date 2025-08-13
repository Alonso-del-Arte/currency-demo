# Currency Demo

For a presentation about JSR-354.

Money is unfortunately a big part of our lives. And yet, Java does not provide 
an out-of-the-box data type to represent money amounts. Our options in Java 8 
are, from very bad to very good but still short of ideal:

* Use `float` or `double` &mdash; not good to use floating point for fixed point 
quantities
* Use `int` or `long` &mdash; no information about where to put the decimal 
point
* Use `java.math.BigDecimal` &mdash; no information about the currency
* Create our own class &mdash; we might duplicate third-party library efforts
* Use a third-party library &mdash; we might need to integrate our project with 
a project that uses a different third-party library

The JSR-354 proposal was approved for consideration to add to Java 9. However, 
it wasn't actually added to Java 9, and as of Java 21 it is still not an 
official part of the Java Development Kit (JDK). The next long-term support 
(LTS) release is Java 25, due out September 2025, and it's not expected to 
incorporate JSR-354 either.

If JSR-354 had been added in Java 9, there would have been a `java.money` 
package added to the JDK, which would have been roughly analogoous to the 
`java.time` package that was added in Java 8.

However, Java does provide the `Currency` type in the `java.util` package, which 
spares us the effort of having to define a type to represent the currencies. An 
instance of `Currency` can represent euros, for example. The `Currency` class 
comes with a lot of information for internationalization, such as what a 
currency is called in several different locales.

The International Organization for Standardization (ISO) provides 3-letter codes 
and 3-digit zero-padded numeric codes for each currency recognized by the 
organization. These are listed in ISO-4217. For example, the euro has the 
3-letter code EUR and the 3-digit 978, and the United States dollar has the 
codes USD and 840.

From what I've seen, the 3-letter codes are far more popular than the 3-digit 
codes. Both codes are available for a `Currency` instance through the functions 
`getCurrencyCode()` and `getNumericCode()` (the latter was added in Java 7).

As a very small consolation for not adding JSR-354, Java 9 at least added the 
function `getNumericCodeAsString()`. For example, for the Bahraini dinar (BHD), 
`getNumericCode()` returns 48 and `getNumericCodeAsString()` returns "048".

But an instance of `Currency` still needs an instance of another type to 
represent a specific amount of that currency, such as, for example, 20&euro;. 
For general purpose uses, which could involve interest rate calculations, the 
numeric part of the money amount class would probably be `BigDecimal`. But in 
this demonstration, I'm using `long`.

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
* TestFrame 1.0 or later (`org.testframe.api` namespace, `assertPrintOut()`). Go 
to [testframe.org](https://testframe.org) and click on "Download Version 1.0".
* TestFrame 1.01. Testing `HardCodedRateProvider` in the `currency.conversions` 
package, I realized that I neglected the customized messages for 
`assertInRange()` with parameters of type `double`. This bothered me enough that 
I had to do a patch release of TestFrame. If you're not bothered by that 
shortcoming like I was, you can continue using 1.0.

## Online currency conversion APIs

There are many currency conversion APIs online that can be used from a Java 
program like the one in this project. I came across Manny's Currency Converter 
API through Stack Overflow many years ago and found it to be quite easy and 
straightforward to use. The free tier allows up to one hundred HTTP requests in 
an hour. The request syntax is very simple and so is the JSON response. Almost 
every currency with an ISO-4217 code is supported. So that's the one I chose for 
this project.

However, in November 2024, if I recall correctly, the free tier went down, as 
had happened a few times before. But then more than a week passed by without it 
coming back online.

Finally, in February 2025, the server was reconfigured to respond that the free 
tier servers "are down indefinitely," and encouraging users to sign up for 
either a prepaid plan starting at $6 for presumably some fixed number of request 
credits, or a paid plan of $10 or $60 a month.

Manny's free tier was almost too good to be true. I will have to look for some 
other free API, which will probably be very limited in the number of currencies 
it supports and how many requests can be made per hour.

As of March 2025, I've decided to go with ExchangeRate-API from AYR Tech. It 
seems to be better documented than Manny's API, but the JSON responses require 
more work parsing and caching. Go to 
[ExchangeRate-API.com](https://www.exchangerate-api.com) for information on the 
price tiers and documentation on how to use the API. Also note that the API key 
does not expire monthly like Manny's does.

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
| U+058F  | &#x58F;  | AMD      | Armenian dram            |
| U+060B  | &#x60B;  | AFN      | Afghan afghani           |
| U+0E3F  | &#xE3F;  | THB      | Thai bhat                |
| U+20A1  | &#x20A1; | CRC      | Costa Rican col&oacute;n |
| U+20A6  | &#x20A6; | NGN      | Nigerian naira           |
| U+20A9  | &#x20A9; | KRW      | South Korean won         |
| U+20AA  | &#x20AA; | ILS      | Israeli new shekel       |
| U+20AB  | &#x20AB; | VND      | Vietnamese dong          |
| U+20AC  | &euro;   | EUR      | Euro                     |
| U+20B9  | &#x20B9; | INR      | Indian rupee             |
| U+20BA  | &#x20BA; | TRY      | Turkish lira             |
| U+20BC  | &#x20BC; | AZN      | Azerbaijani manat        |
| U+20BD  | &#x20BD; | RUB      | Russian ruble            |
| U+20BE  | &#x20BE; | GEL      | Georgian lari            |

The symbol &#x20A0; was intended for the euro but it's hardly used nowadays. The 
symbol &#x20A8; for the Indian rupee was used much more frequently.

As far as I can tell, only three of these have word HTML entities: the British 
pound (&amp;pound;), the Japanese yen (&amp;yen;) and the euro (&amp;euro;). 
They all of course have numerical HTML entities.

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
proposed for ISO-4217. That symbol should not be confused with the symbol for 
the Thai bhat, &#xE3F;.

## Notes about divisions of a unit of currency

I am aware of the cent symbol, &cent; (Unicode U+00A2, HTML entity &amp;cent;). 
I briefly thought about using it in this project, for amounts from &minus;0.99 
to 0.99. The most obvious reason that dissuaded me from that idea is that not 
all units of currencies divide into 100 cents.

Currencies like the Iraqi dinar (IQD) and the Omani rial (OMR) divide into 1,000 
milles, or darahim. Some currencies, like the Japanese yen (JPY) or the Ugandan 
shilling (UGX) don't subdivide at all.

To my knowledge, there are no currencies that divide only into ten parts. And 
I'm only aware of one currency that divides into 10,000 parts, the Chilean unit 
of account (CLF). But from what I can gather, that's not really used for 
everyday purchases like groceries, with the main currency in Chile being the 
Chilean peso (CLP), which interestingly enough doesn't subdivide at all.

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

Note to self, 13 December 2024: Check if Trinidad &amp; Tobago dollar is brittle 
for other display names test

----

This project is closed off to Hacktoberfest.
