# CattleLog
This repository holds the complete code for the CattleLog Android app, albeit with the redaction of all API keys, credentials, and private cattle records. The primary goals of CattleLog are to (1) provide employees of the VanWagner Family Farm with an efficient method of cattle record lookup via an intuitive user interface and (2) display requested cattle records in an aesthetically pleasing, organized manner.

If the code in this repository is compiled and executed, it will display data from a representative, toy database, which is available at https://github.com/Camoen/CattleLog-File-Processor-Public/blob/master/cattlelog_database.db.

## Table of Contents
* [CattleLog Video Demonstration](#cattleLog-video-demonstration)
* [CattleLog Features](#cattlelog-features)
    * [Main Screen](#main-screen)
    * [Heats List](#heats-list)
        * [Next Expected Heats Query](#next-expected-heats-query)
    

## CattleLog Video Demonstration
A video demonstration of the app is available on YouTube: https://youtu.be/n9yGcRzhoyQ

## CattleLog Features
### Main Screen
The main screen after startup displays a list of all cattle on the farm, ordered by tag number (denoted “Number”). Specific animals can be searched for by tag number, metal tag number, or barn name.  Search values may be provided by the user either manually or vocally.  The list of cattle is filtered in real time based on the input search value.

| <img src="https://user-images.githubusercontent.com/16565961/69368027-4e1e6100-0c67-11ea-873b-532f674f1732.png" alt="CattleLog - Main Screen" width="66%" height=""> | <img src="https://user-images.githubusercontent.com/16565961/69368103-74dc9780-0c67-11ea-9333-ef59708487c6.png" alt="CattleLog - Main Screen Search" width="66%" height=""> |
|:---:|:---:|
| Main Screen | Main Screen Search |

### Heats List
Another list view is available under the Heats tab.  This list displays all cattle with a “next expected heat” date in a given range, as defined by the [Next Expected Heats Query](#next-expected-heats-query).  In this view, results are ordered by next expected heat date, denoted “Heat Date”, and animals can be searched for by tag number or service sire.

<p align="center"><img src="https://user-images.githubusercontent.com/16565961/69368038-58405f80-0c67-11ea-91fa-47cabb0b156d.png" alt="CattleLog - Heats List" width="35%" height=""><br>Heats List (screenshot taken on Nov. 15th)</p>

#### Next Expected Heats Query
Since heat cycles are not exact, it’s helpful to show all expected heats within a few days’ range of the current day (before and after).  The date range specified by the app’s end users was “3 days before and after”.  However, due to the unique way in which data is generated for CattleLog's backend, whenever the backend database is updated, all Next Expected Heat (NEH) dates prior to the current day are lost.  For example, if the database is updated on January 4th, all NEH dates up to and including January 3rd are lost.

To account for this, CattleLog's `getNextExpectedHeatsPreset` query returns all NEH dates between 3 days before and after the current day, but also includes all NEH dates between 18 and 21 days from the current day (an offset of one heat cycle, to account for any “before” dates that were lost in the database update).  See the table below for an illustrative example of this query’s logic.

<p align="center"><img src="https://user-images.githubusercontent.com/16565961/69370972-f4209a00-0c6c-11ea-94d4-e7ebd5fd45bf.png" alt="CattleLog - Example of Next Expected Heats Query Results"></p>

In this example, the "Heats" list is accessed on the 4th.  The `getNextExpectedHeatsPreset` query returns all cattle with a NEH date on the 1st through the 7th, plus all cattle with a NEH date on the 22nd through the 25th (between 18 and 21 days from the current day).  These cattle are then used to populate the "Heats" list.  To fully understand the logic behind this query, assume that the backend database was updated on the 3rd, resulting in the removal of all NEH dates on the 1st and 2nd.  In the backend, these dates have simply been pushed forward one heat cycle, or 21 days.  To account for these "lost" NEH dates, the query also returns NEH dates on the 22nd and 23rd (the 1st + 21 days and the 2nd + 21 days, respectively).

The code for the `getNextExpectedHeatsPreset` query:
```
select * from (
    select * from cattle
    where DATE(NextExpHeat)
      BETWEEN DATE(datetime('now','localtime', '-3 day'))
      AND DATE(datetime('now','localtime', '3 day'))
  UNION
    select * from cattle
    where DATE(NextExpHeat)
      BETWEEN DATE(datetime('now','localtime', '18 day'))
      AND DATE(datetime('now','localtime', '21 day'))
) ORDER BY DATE(NextExpHeat) ASC;
```
