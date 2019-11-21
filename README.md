# CattleLog
This repository holds the complete code for the CattleLog Android app, albeit with the redaction of all API keys, credentials, and private cattle records. The primary goals of CattleLog are to (1) provide employees of the VanWagner Family Farm with an efficient method of cattle record lookup via an intuitive user interface and (2) display requested cattle records in an aesthetically pleasing, organized manner.

If the code in this repository is compiled and executed, it will display data from a representative, toy database, which is available at https://github.com/Camoen/CattleLog-File-Processor-Public/blob/master/cattlelog_database.db.

## Table of Contents
* [CattleLog Video Demonstration](#cattleLog-video-demonstration)
* [CattleLog Features](#cattlelog-features)
    * [Main Screen](#main-screen)
    * [Heats List](#heats-list)
        * [Next Expected Heats Query](#next-expected-heats-query)
    * [Animal Details](#animal-details)
        * [Overview Tab](#overview-tab)
        * [Health Tab](#health-tab)
        * [Treatment Tab](#treatment-tab)
    

## CattleLog Video Demonstration
A video demonstration of the app is available on YouTube: https://youtu.be/n9yGcRzhoyQ

## CattleLog Features
### Main Screen
The main screen after startup displays a scrollable list of all cattle on the farm, ordered by tag number (denoted “Number”). Specific animals can be searched for by tag number, metal tag number, or barn name.  Search values may be provided by the user either manually or vocally.  The list of cattle is filtered in real time based on the input search value.  Tapping on any row will result in the display of a unique [animal's details](#animal-details).

| <img src="https://user-images.githubusercontent.com/16565961/69368027-4e1e6100-0c67-11ea-873b-532f674f1732.png" alt="CattleLog - Main Screen" width="66%" height=""> | <img src="https://user-images.githubusercontent.com/16565961/69368103-74dc9780-0c67-11ea-9333-ef59708487c6.png" alt="CattleLog - Main Screen Search" width="66%" height=""> |
|:---:|:---:|
| Main Screen | Main Screen Search |

### Heats List
Another list view is available under the Heats tab.  This scrollable list displays all cattle with a “next expected heat” date in a given range, as defined by the [Next Expected Heats Query](#next-expected-heats-query).  In this view, results are ordered by next expected heat date, denoted “Heat Date”, and animals can be searched for by tag number or service sire.  The list of cattle is filtered in real time based on the input search value.  Tapping on any row will result in the display of a unique [animal's details](#animal-details).

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


### Animal Details
#### Overview Tab
This tab displays an animal's identification details, genetic/heritage data, and reproduction information.  The tab's layout was based on end user specifications, with the most important data near the top.  Note that the four most important attributes ("Next Expected Heat Date", "Times Bred / Date", "Service Sire", and "Due Date") are larger, for improved readability.  This tab also contains 10 user-defined fields; these fields may be modified in the future, as decided by the farm's record-keeper.  Due to the fluctuating nature of the user-defined fields, a description for each is provided at the bottom of the tab (the descriptions are also easily updated by the farm's record-keeper).  Data for this tab is provided by a combination of three input files (`Report 1.csv`, `Report 2.csv`, and `Treatments.xls`, examples of which are available here: https://github.com/Camoen/CattleLog-File-Processor-Public/tree/master/Project%20Files)

| <img src="https://user-images.githubusercontent.com/16565961/69368107-77d78800-0c67-11ea-80f8-21d7ea098e50.png" alt="CattleLog - Overview Tab Top"> | <img src="https://user-images.githubusercontent.com/16565961/69368116-7b6b0f00-0c67-11ea-9b2b-7c3a6ecf67b8.png" alt="CattleLog - Overview Tab Middle"> | <img src="https://user-images.githubusercontent.com/16565961/69368124-7e65ff80-0c67-11ea-9633-017c90049b5b.png" alt="CattleLog - Overview Tab Bottom"> |
|:---:|:---:|:---:|
| Overview Tab (Top) | Overview Tab (Middle) | Overview Tab (Bottom) |

#### Health Tab
This tab contains a scrollable list of an animal's health events, such as medications, births, weanings, etc.  More detail for some events may be provided in the [Treatments Tab](#treatments-tab).  Data for this tab is provided by an automatically generated CSV report (example provided at https://github.com/Camoen/CattleLog-File-Processor-Public/blob/master/Project%20Files/Report%203.csv).

| <img src="https://user-images.githubusercontent.com/16565961/69368130-82921d00-0c67-11ea-938b-eff86b4d530b.png" alt="CattleLog - Animal Details Health Tab " width="66%" height=""> | <img src="https://user-images.githubusercontent.com/16565961/69368135-858d0d80-0c67-11ea-8d3a-2a2ceaf605f1.png" alt="CattleLog - Animal Details Health Tab (cont.)" width="66%" height=""> |
|:---:|:---:|
| Animal Details Health Tab | Animal Details Health Tab (cont.) |

#### Treatment Tab
This tab contains a scrollable list of an animal's treatments.  This tab may provide a more detailed overview for specific events in the [Health Tab](#health-tab).  Data for this tab is provided by a manually generated XLS workbook (example provided at https://github.com/Camoen/CattleLog-File-Processor-Public/blob/master/Project%20Files/Treatments.xls).
<p align="center"><img src="https://user-images.githubusercontent.com/16565961/69368138-8756d100-0c67-11ea-8ac7-63f8ebb459ce.png" alt="CattleLog - Animal Details Treatment Tab" width="35%" height=""><br>Animal Details Treatment Tab</p>
