crystal-game
============

A multiplayer game where placers are allowed to discovera location for crystals and other items and collect them.

# Project Breakdown

## Crystal Game

The client side application

## Crystal Game Server

The server side application

## Crystal Game Library

The library project for the client and the server

## Crystal Game Test

The android test application for Crystal game

## Google Play Services Library

The librar project containing dependencies such as Google Maps


# Installation

To set up the project in eclipse import the 5 projects to the enditor using the 'File > Import' menu.
In order to do so, you will have to import `Crystal Game Library` and `Crystal Game Server` as a Java project; and `Crystal Game`, `Crystal Game test`, and `Google Play Services` as an Android project.

The Android projects require you to have the Android SDK installed on your machine and have the Android plugin installed in eclipse.

When you have imported the projects, you may build the `CrystalGame` project as an Android application and push it to your phone. To run the server, run the `Server.java` file in the default package of the Server project.

By default, the server uses port `3000`. You may change this in the `Server.java` file.

# How to play
Install the application on the Android device and launch it. Make sure that you are on the same network as the server, or the server has a public IP. Make sure that the firewall allows TCP connections on the selected ports (port 3000 by default).

To set the server address and port in the client application, launch it and click the settings icon on the main screen.

In order to launch a game you are required to join or create a group. Once you are in a group you may request the start of a game.



