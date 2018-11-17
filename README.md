# Powerful Inverted Index

## Idea
Build a well-designed generic Inverted Index web application for fast text search with added functionality. Process and display stats for user input, find multiple words with conditions, find proximity of 2 words in a text. Maybe also try to detect spam/similar content.

## Design
For every dataset loaded, maintain three data structures for the Inverted Index. One mapping term to document objects. Other mapping term to (frequency OR distribution) of document objects. This way we can save time and memory for processing requests (by looking at the latter data structure)s and retrieve results faster.
Use a Pub-Sub AsyncUordered model to compute the three tables, as a dataset may contain multiple files to process. 
I will first build this core and then work on APIs. Using Amazon dataset.

## APIs
*	/STATS
    *	Most/least frequent terms (from corpus or a specific review)
    *	Most/least distributed term from corpus
    *	Can add more…
*	/FIND
    *	Single word
    *	Multiple words with following:
          *	In order
          *	Any order
          *	Proximity between them
*	/SPAM
    *	If I have time, use the Inverted Index to pull out spam/similar text

## TIMELINE
*	Inverted Index : 15hrs (starting from scratch to build a generic model)
*	/STATS & /FIND APIs: 10hrs
*	Server, web application, JavaScript : 25hrs
*	Testing on small and big datasets: 5hrs
