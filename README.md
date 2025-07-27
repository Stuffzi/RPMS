# RPMS
## About
Realtime monitor of commercial power reactors in the United States. Shows reactor power, history, and incident reports.
Meant to be a cross-platform Java graphical application. I am making every effort for it to be portable on most devices.
Can be operated in a static or interactive mode, either appearing as a dashboard or browser for your technical needs.
Updates power daily at 5 am EST, but incident reports will be added when they are published.
Make sure to get an updated IAEA file from this github every few months! Reactor status can change every so often.

Under MIT license, feel free to modify or use this project however you wish, given that credit is respected.
Written by Makayla.

## Project progress
Not much right now, just command line. I'm building!

## Installation
Currently no JAR available. Feel free to compile with gradle though.

## Tools used
-OkHTTPClient 5.1.0 || https://square.github.io/okhttp/#releases
-RSSReader 3.9.3 || https://github.com/w3stling/rssreader
-OpenCSV 5.11.2 || https://github.com/w3stling/rssreader
-(For generating IAEA_REFERENCE.csv) AtomCrawler || https://github.com/SpinBath/AtomCrawler/tree/main
-Gradle, Java 24