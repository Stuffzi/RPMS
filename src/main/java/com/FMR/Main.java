package com.FMR;

//JAVA
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


//OkHTTP
import com.opencsv.exceptions.CsvValidationException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//RSSReader
import com.apptasticsoftware.rssreader.RssReader;
import com.apptasticsoftware.rssreader.Item;


//OpenCSV
import com.opencsv.CSVReader;
import com.opencsv.CSVIterator;

/**
 * Everything is fully accessable because this is meant to be a temprorary messanger object.
 * Returned by the method which scans the IAEA reference document. Gives all of the static info you need to know.
 * Not meant to be modified after it leaves getReactorData().
 * @author Makayla B
 */
@SuppressWarnings("unused")
class IAEAReferencePass
{
	public String name;

	public int MWt; //current data
	public int MWe; //current data
	public String model; //this will be like BWR-5 or WH 4LP, generic will be overwritten
	public String operator; //not to be confused with owner
	public String conStart; //date of first construction start
	public String firstCritical; //date of first critcality
	public String firstSync; //date of first synchronization/power output
	public String comOperation; //date of which the unit is commissioned

	public IAEAReferencePass(String name)
	{
		this.name = name;
	}

	public String getComOperation()
	{
		return comOperation;
	}
}


public class Main
{
    public static final String NRC_CURRENT = "https://www.nrc.gov/public-involve/rss?feed=plant-status";
	public static final String REFERENCE_LOCATION = "src/main/resources/IAEA_REFERENCE.csv";

	public static OkHttpClient _NRC_Client;
	public static RssReader _NRC_RSSReader;
	public static CSVReader _IAEA_CSVReader;

	public static ArrayList<Reactor> _roster;
	private static IAEAReferencePass _ERROR_REACTOR;


	private static IAEAReferencePass getReactorData(String rName)
	{
		//step 1: format the name so that it matches what's in the IAEA database
		rName = rName.toUpperCase();


		//step 2: find the record
		String[] row = new String[24];
		IAEAReferencePass returner = new IAEAReferencePass(rName);
		while (true)
		{
			try
			{
				if ((row = _IAEA_CSVReader.readNext()) == null)
				{
					System.out.println("[getReactorData] Could not find requested reactor '" + rName + "', ensure" +
							"IAEA_REFERENCE is up to date and not corrupted.");
					return _ERROR_REACTOR;
				}
				else
				{
					break;
				}
			} catch (IOException | CsvValidationException e)
			{
				System.out.println(e + "\n[getReactorData] Possible data corruption in the IAEA_REFERENCE. Consider " +
						"re-downloading it.");
			}

		}

		return returner;
	}


    private static void updateCurrent()
    {
		//Contact NRC and get data
		Request NRCHandle = new Request.Builder().url(NRC_CURRENT).build();
		Response NRCResponse;
		try
		{
			NRCResponse = _NRC_Client.newCall(NRCHandle).execute();
		} catch (IOException e)
		{
			System.out.println(e + "\n[updateCurrent] Unable to contact NRC, check internet or interference.");
			return;
		}

		String NRCContent;
		if (NRCResponse.isSuccessful())
		{
			try
			{
				NRCContent = NRCResponse.body().string();
			}
			catch (IOException e)
			{
				System.out.println(e + "\n[updateCurrent] Unable to fetch content from NRC, check internet or interference.");
				return;
			}
		}
		else
		{
			System.out.println("NRC HTTP RESPONSE: [" + NRCResponse.code() + "]\n[updateCurrent] Non optimal NRC call." +
					" Check URL, Internet, or contact developer.");
			return;
		}
		NRCResponse.close();

		//Parse data and build something out of it
		InputStream NRC_RSS = new ByteArrayInputStream(NRCContent.getBytes(StandardCharsets.UTF_8));
		List<Item> ReactorData = _NRC_RSSReader.read(NRC_RSS).toList();
		System.out.println("=== REACTOR DATA ===");
		ReactorData.forEach(reactorD ->
		{
			String reactorNRC = reactorD.getTitle().orElse("NRC Data Handling Error - xxx%");
			int dividerIndex = reactorNRC.indexOf('-');
			_roster.add(new Reactor(
					reactorNRC.substring(0, dividerIndex - 1),
					reactorNRC.substring(dividerIndex + 2, reactorNRC.indexOf('%')),
					0,
					0));
		});

		_roster.forEach(item ->
		{
			String rName = item.get_name();
			String rPow = item.get_model();
			System.out.println("Reactor: [" + rName + "]" +
					"\n|- At power: [" + rPow + "]\n");
		});
	}


    public static void main(String[] args)
    {
		_NRC_Client = new OkHttpClient();
		_NRC_RSSReader = new RssReader();
		try
		{
			_IAEA_CSVReader = new CSVReader(new FileReader(REFERENCE_LOCATION));
		} catch (FileNotFoundException e)
		{
			System.out.println(e + "\n[main] Could not find reference data. It should be in /resources, named" +
					" 'IAEA_REFERENCE.CSV'. Please check your installation or manually install it.");
			return;
		}
		_roster = new ArrayList<>();

		_ERROR_REACTOR = new IAEAReferencePass("NULL");

		updateCurrent();
    }
}