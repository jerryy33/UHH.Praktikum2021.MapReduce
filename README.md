# Hashtag Analysis
  Using Map-Reduce this Project is analysing Hashtags from Twitter.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Status](#status)
* [Inspiration](#inspiration)
* [Contact](#contact)

## General info
### English:
Since hashtags have been introduced they are a key faktor for social media plattforms such as Twitter. Hashtags are used as keywords, many users use hashtags to describe their tweet in one short description. This categorizes tweets and tweets can be found faster becuase of that.
User than can search for hashtags to find alle tweets which are using the specified hashtag. Every person with a socialmedia profile can create a hashtag, over time a lot of hashtags have been created, some more popular than others. Many of them dont have a clear meaning at first look, the reason for this is that hashtags are mostly used as abbreviations (e.g. blm stands for BlackLivesMatter).

This Projekt handles this problem and tries to visualize the meaning and context of different hashtags. Based on tweets of the year 2020 we show all words which are mostly used with a specific hashtag.

Visit http://basecamp-demos.informatik.uni-hamburg.de:8080/hashtag-analyse-1/dashboard to try it yourself.

### German:
Seit Ihrer Einführung sind Hashtags zu einem wichtigen Bestandteil, der weltweiten Social-Media-Plattform geworden und befinden sich heutzutage in fast allen Beiträgen
des Mikroblogging-Dienstes Twitter, genauso wie auch in anderen sozialen Netzwerken. Dort dienen sie nämlich als Schlagwörter. Wer zu einem bestimmten Thema etwas postet,
kann dafür sorgen, dass sein Beitrag schneller gefunden werden kann, indem er zusätzlich ein passendes Hashtag zu seinem Beitrag hinzufügt, welches diesen inhaltlich beschreibt.
Sucht man dann nach den Hashtags, für die man sich interessiert, findet man alle Beiträge, die zusammen mit diesem Schlagwort verfasst wurden.
Jede Person, die auf einem sozialen Netzwerk wie Twitter oder Instagram angemeldet ist, kann ein Hashtag erstellen und ihn so benennen, wie Sie es möchte.
So kann es passieren, dass mit der Zeit Hashtags entstehen, deren Bedeutung nicht wirklich eindeutig sind.

Genau mit dieser Problematik setzt sich dieses Projekt auseinander. Basierend auf Tweets aus dem Jahr
2020 liefern wir Ihnen alle Wörter, die in Verbindung mit den meist eingesetzten 100 Hashtags aus diesem Zeitraum aufgetaucht sind. 
Dadurch soll die Bedeutung von den Hashtags, anhand der Wörtern zu identifiziert werden, mit denen sie am öftesten erschienen sind. 
Somit können Sie auf einen der Hashtags klicken und es werden Ihnen die Wörter aufgelistet, die in dem Zeitraum prozentual am häufigsten in Verbindung mit dem Hashtag aufgetaucht sind.
Auf unser Website kann dies auprobiert werden http://basecamp-demos.informatik.uni-hamburg.de:8080/hashtag-analyse-1/dashboard.

## Technologies
* Java 
* Maven
* Hadoop
* MYSQL
* CSS
* Javascript
* HTML

## Setup
First you need to scrape data from Twitter so you can execute map-reduce on this data.
Using Hadoop(http://hadoop.apache.org/) this Projekt can be executed to get the necassary data.


## Status
Project is: finished

## Inspiration
  https://github.com/basecamp-uhh/Java-MapReduce
