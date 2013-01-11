package javax.money;

import java.io.IOException;
import java.util.Locale;

import javax.money.format.AmountFormatter;
import javax.money.format.AmountParser;
import javax.money.format.LocalizableAmountFormatter;
import javax.money.format.LocalizableAmountParser;
import javax.money.format.ParseException;

public class Samples {

	public static void main(String[] args) {
		// Creating one
		CurrencyUnit currency = Monetary.getCurrencyUnitProvider().get(
				"ISO4217", "CHF");
		MonetaryAmount amount = Monetary.getMonetaryAmountFactory().get(
				currency, 1.0d);

		// Using parsers
		try {
			AmountParser parser = Monetary.getAmountParserFactory()
					.getAmountParser(Locale.GERMANY);
			MonetaryAmount amount1 = parser.parse("CFH 123.45");

			LocalizableAmountParser locParser = Monetary
					.getAmountParserFactory().getLocalizableAmountParser();

			MonetaryAmount amount2 = locParser
					.parse("CFH 123.45", Locale.CHINA);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Using formatters
		try {
			AmountFormatter formatter = Monetary.getAmountFormatterFactory()
					.getAmountFormatter(Locale.GERMANY);
			String formatted = formatter.print(amount);
			LocalizableAmountFormatter locFormatter = Monetary
					.getAmountFormatterFactory()
					.getLocalizableAmountFormatter();

			String formatted2 = locFormatter.print(amount, Locale.CHINA);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
