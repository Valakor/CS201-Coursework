SimCity201 README
======

Project repository for the SimCity201 project completed by Matthew Pohlmann, Brandon Barber, Ben Doherty, and Skyler Lloyd for the final project of CSCI 201 - Principles of Software Development. The total line count is 40,000+ lines of code completed over the course of 7 weeks.

##Final Submission

###Compiling and Running the Code
####Instructions
To compile and run SimCity201:
  + Clone the repository into a folder
  + In Eclipse, File -> New -> Java Project
  + Uncheck `Use default location`
  + Click `Browse` and select the folder you downloaded the repository to
  + Ensure that `Java SE 7` is selected under JRE
  + Click `Finish`
  + Eclipse will give some errors from the JUnit testing section because JUnit4 is not included in the build path
  + With the current project open, Project -> Properties -> Libraries -> Add Library...
  + Select `JUnit`
  + Click `Next`
  + Under `JUnit library version` select `JUnit 4`
  + Click `Finish`
  + Open `src.cs201.Main.java`
  + Click `Run` to build and run SimCity201
  + Click `Show Scenario Panel` and choose a scenario to run

###Work Load
 + **Matthew Pohlmann:**
   + PersonAgent
   + Role Base Class
   + Structure Base Class
   + CityTime Helper Class
   + CityDirectory Helper Class
   + StructurePanel Base Class
   + RestaurantConfigPanel
   + Person Creation/Info Panel
   + Trace Panel Integration
   + Restaurant
     + Restaurant Structure Base Class
     + Restaurant Cashier Role Base Class
     + Restaurant Cook Role Base Class
     + Restaurant Customer Role Base Class
     + Restaurant Host Role Base Class
     + Restaurant Waiter Role Base Class
   + Personal Restaurant Integration
   + Teammates' Restaurant Integration Assistance
   + Initial Wiki setup
   + Initial Package/Directory setup
 + **Brandon Barber:**
   + Team Leader
   + Transit System
     + Vehicle Agent (and Gui)
     + Car (and Gui)
     + Bus (and Gui)
     + TransitRole (and Gui)
     + Bus Stops (and Gui)
     + Collision and movement of all Walking people/cars/busses
     + Transit Config Panel
     + Unit Testing of Above Code
   + GUI
     + CityPanel (the animation of SimCity201 as a whole)
     + Settings Panel (Holds Config Panels)
       + Config Panel Base Class (Used to display information and configure buildings)
 + **Ben Doherty:**
   + Market
     + Market Structure (and Gui)
     + Market Employee Role (and Gui)
     + Market Manager Role (and Gui)
     + Market Consumer Role (and Gui)
     + Market Config Panel
     + A* Implementation in MarketGUI
     + Unit Testing of Above Code
     + Time Panel (increasing/decreasing how fast time passes in SimCity201)
 + **Skyler Lloyd:**
   + Housing
     + Residence Structure (and Gui)
     + Apartment Complex Structure (and Gui) (Actually just an office for the Landlord Role, but contains a list of Residence that are the "apartments")
     + Landlord Role (and Gui)
     + Renter Role (and Gui) (unused?)
     + Residence Role (and Gui)
     + Residence Config Panel (Doesn't exist)
     + Apartment Complex Config Panel (Doesn't exist)
     + Unit Testing of Above Code

###Things That Don't Work
 + No File I/O for configuration
 + No GUI for dynamic configuration of buildings
 + No working Bank
 + Slightly buggy Pedestrian/Vehicle collisions
 + Animation in the CityView is extremely slow and buggy because Java can't handle so many draw calls
 + Some large scenarios don't work because we simply did not have the time or resources to integrate everything with only 4 people
 + Skyler's restaurant is incomplete (shifts don't quite work, no rotating stand, no cook animation)
