# Powerful Inverted Index

## Idea
Build a well-designed Inverted Index web application for fast text search. Process and display data from APIs, find single/multiple words with conditions, find proximity of 2 words in a text. Maybe also try to detect spam/similar content.

## Design
At the core, all user fed input files are sent to a PubSub model with each Susbcriber end processing a unique InvetredIndex. 
Each of the 3 APIs will be querying data from a static object containing the list of InvertedIndices.
InvertedIndex -> PubSub -> QueryObj -> APIs -> User

## APIs
*  /InvetredIndexAPI
    * Build the core from user chosen files. Show processing time and what is built.
    * Direct to Stats, SimpleQuery, ComplexQuery
*	/InvetredIndexAPI/Stats
    *	Most/least frequent terms
    *	Most/least distributed term from corpus
    *	Can add more…
*	/InvetredIndexAPI/SimpleQuery
    *	Single word, get results with keyword highlighted.
*  /InvetredIndexAPI/ComplexQuery
    *	Multiple words with following:
          *	Regex for whether to split user input into multiple words or match in entirety
          *	Any order
          *	Proximity between them
*	/SPAM
    *	If I have time, use the Inverted Index to pull out spam/similar text
