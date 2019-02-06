# Powerful Text Search Application 
![Image of Yaktocat](https://github.com/ksonar/Text-Search-Application/blob/master/files/SideProject.jpg)

## Idea
Well-designed Inverted Index web application for fast text search of Amazon Reviews & QA datasets. Process and display data from APIs, find single/multiple words with simple/complex queries.

## Design
At the core, all user fed input files are sent to a PubSub model with each Susbcriber end processing a unique InvetredIndex. 
Each of the 3 APIs will be querying data from a static object containing the list of InvertedIndices.
PubSub -> InvertedIndex -> QueryObj -> APIs -> User

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
    *	Multiple words query. ',' for OR and ';' for AND.
## Future Work
* Find records with a word-to-word proximity parameter.
* Use the Inverted Index to pull out spam/similar text
