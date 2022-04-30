import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nicehash.connect.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.UUID;

/**
 * exchange example
 */
public class Exch
{
	private Log log = LogFactory.getLog(Exch.class);

	private static final String URL_ROOT   = "https://api2.nicehash.com/"; //use https://api2.nicehash.com for production
	private static final String ORG_ID     = ""; //get it here: https://test.nicehash.com/my/settings/keys or https://new.nicehash.com/my/settings/keys
	private static final String API_KEY    = "";
	private static final String API_SECRET = "";

	private static final String CURRENCY_SELL = "TBTC"; //user BTC for production
	private static final String CURRENCY_BUY  = "TLTC"; //use LTC for production

    public static void main( String[] args ) {
		new Exch();
    }

	private Exch() {
	    Api api = new Api(URL_ROOT, ORG_ID, API_KEY, API_SECRET);

	    //get server time
	    String timeResponse = api.get("api/v2/time");
		JsonObject timeObject = new Gson().fromJson(timeResponse, JsonObject.class);
		String time = timeObject.get("serverTime").getAsString();
	    log.info("server time: " + time);

		//get exchange settings
		String exchResponse = api.get("exchange/api/v2/info/status");
		JsonObject exchObject = new Gson().fromJson(exchResponse, JsonObject.class);
		//log.info("exchanges: " + exchObject.toString());

		final JsonObject[] exchanges = new JsonObject[1];
		exchObject.get("symbols").getAsJsonArray().forEach(exch-> {
			JsonObject e = exch.getAsJsonObject();
			if (e.get("baseAsset").getAsString().equals(CURRENCY_BUY)) {
				exchanges[0] = e;
			}
		});

		JsonObject mySettings = exchanges[0];
		log.info("exchange settings: " + mySettings);

		//get balance
		String activityResponse = api.get("main/api/v2/accounting/accounts2", true, time);

		JsonObject accountsObject = new Gson().fromJson(activityResponse, JsonObject.class);
		log.info("Accounts Object: " + accountsObject.toString());
		//JsonArray accountsArray = new Gson().fromJson(activityResponse, JsonArray.class);
		//log.info("accounts: " + accountsArray.toString());

		//log.info("accounts: " + accountsArray.toString());

		// accountsObject.get("currencies").getAsJsonArray().forEach(currency-> {
		// 	JsonObject e = currency.getAsJsonObject();
		// 	if (e.get("currency").getAsString().equals("BTC")) {
		// 		System.out.println("Currency: " + e.get("currency").getAsString());
		// 	}
		// });

		// final double[] balance = new double[1];
		// accountsArray.forEach(acc-> {
		// 	JsonObject a = acc.getAsJsonObject();
		// 	if (a.get("currency").getAsString().equals(CURRENCY_SELL)) {
		// 		balance[0] = a.get("balance").getAsDouble();
		// 	}
		// });



    }
}
