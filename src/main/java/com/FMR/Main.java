package com.FMR;

//JAVA
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

//OkHTTP
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//RSSReader
import com.apptasticsoftware.rssreader.RssReader;
import com.apptasticsoftware.rssreader.Item;


public class Main
{
    public static final String NRC_CURRENT = "https://www.nrc.gov/rss/reactorstatus.xml";

	public static OkHttpClient _NRC_CLIENT;
	public static RssReader _NRC_RSSREADER;

    private static void updateCurrent()
    {
		//Contact NRC and get data
		Request NRCHandle = new Request.Builder().url(NRC_CURRENT).build();
		Response NRCResponse;
		try
		{
			NRCResponse = _NRC_CLIENT.newCall(NRCHandle).execute();
		} catch (IOException e)
		{
			System.out.println(e + "\n[updateCurrent] Unable to contact NRC, check internet or interference");
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
				System.out.println(e + "\n[updateCurrent] Unable to fetch content from NRC, check internet or interference");
				return;
			}
		}
		else
		{
			System.out.println("NRC HTTP RESPONSE: [" + NRCResponse.code() + "]\n[updateCurrent] Non optimal NRC call." +
					" Check URL, Internet, or contact developer");
			return;
		}
		NRCResponse.close();

		//Parse data and build something out of it
		InputStream NRC_RSS = new ByteArrayInputStream(NRCContent.getBytes(StandardCharsets.UTF_8));
		List<Item> ReactorData = _NRC_RSSREADER.read(NRC_RSS).toList();
		System.out.println("=== REACTOR DATA ===");
		ReactorData.forEach(Reactor ->
				System.out.println(Reactor.getTitle().orElse("NRC Data Handling Error")));
	}


    public static void main(String[] args)
    {
		_NRC_CLIENT = new OkHttpClient();
		_NRC_RSSREADER = new RssReader();
		updateCurrent();
    }
}