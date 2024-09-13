/*
 * Copyright (C) 2024 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package currency;

import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Currency;
import java.util.Scanner;

/**
 * Provides access to Manny's Free Currency Converter API. To use this, you need 
 * an API key, and to place that API key in an environment variable called 
 * "FOREX_API_KEY". Go to the website <a 
 * href="https://free.currencyconverterapi.com">currencyconverterapi.com</a> for 
 * more information.
 * @author Alonso del Arte
 */
public class MannysCurrencyConverterAPIAccess implements ExchangeRateProvider {
    
    private static final String API_KEY = System.getenv("FOREX_API_KEY");
    
    private static final String USER_AGENT_ID = "Java/"
            + System.getProperty("java.version");
    
    // TODO: Write tests for this
    @Override
    public double getRate(Currency source, Currency target) {
        return -1.0;
    }
    
}
