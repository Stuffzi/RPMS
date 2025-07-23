package com.FMR;

import java.io.IOException;
import java.io.InputStream;
import com.apptasticsoftware.rssreader.RssReader;
import com.apptasticsoftware.rssreader.Item;


import java.net.*;
import java.util.List;
import java.util.stream.Stream;

public class Main
{
    public static final String NRC_CURRENT = "https://www.nrc.gov/rss/reactorstatus.xml";

	public static RssReader NRC_RSS = null;

    private static boolean _automaticMode = false;


    private static void updateCurrent()
    {
		Stream<Item> reactorPowers;
		try
		{
			reactorPowers = NRC_RSS.read(NRC_CURRENT);
		} catch (IOException e)
		{
			System.out.println(e + "\n[updateCurrent] Unable to get the NRC RSS feed. Check internet and interference.");
			return;
		}

		reactorPowers.forEach(item -> {
			System.out.println(item.getTitle().orElse("---"));
		});
	}



    public static void main(String[] args)
    {
		NRC_RSS = new RssReader();
		updateCurrent();
    }
}