# TravelClassify

An NLP engine developed for [THack Dublin](http://www.tnooz.com/event/thack-dublin-2015/) as part of a [travel
recommendation/inspiration application](https://github.com/KlavierCat/THack).

## Workflow

![Travel classificaton workflow](images/workflow.png)

We've manually compiled a list of 50+ known and lesser known travel activities. We created keywords and hashtags
which are indicative of these activities and extracted 15k+ tweets for each activity. We trained a Naive Bayes
classifier on these tweets using [Mallet](http://mallet.cs.umass.edu/) (MAchine Learning for LanguagE Toolkit).
In our application, we require the user to log in using his social media accounts. We are then able to extract
his social media posts, classify them, and suggest the top 5 activities that best fit the user's profile based
on the aggregated scores.

## Next steps

![Activities to places](images/activities_places.png)

We are then able to suggest places to the user that suit a user's favourite activities by using the following API features:
- [TripAdvisor Content API](https://developer-tripadvisor.com/content-api/)
- Keyword-based filtering of [Hotelbeds API](http://www.programmableweb.com/api/hotelbeds)

Similarly, we could leverage the Semantic Web or compile our own activities / cities database from unstructured data.

Expanding the activities can involve manual curation, crowd-sourcing, as well as automation (semantic similarity,
word vectors).

## Evaluation

Due to time constraints and API restrictions, we were only able to extract 350k tweets for 30 activities.
The classifier trained on these tweets has an accuracy of 85% that could be considerably improved
given more training data.

This is the confusion matrix for the extracted 30 activities:

label   0   1   2   3   4   5   6   7   8   9  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27  28  29  |total
  0         Alpine_skiing 1976   5   .   6   .   .   3   .   1   1   4   .   .   .  11   .   .   .  14  57  13   1  12   3   3   1   .   4   6  15  |2136
  1               Archery  12 1831   .   .   .   .   2   1   .   .  10   4   2   .   7   .   .   .  21  96  22   .  11   9   .   .   .   3   3  17  |2051
  2               Birding   8   2 954   .   .   .   1   .   .   .   9   .   1   .   2   .   .   .  30  57   8   .   4   5   .   1   .   2   .  16  |1100
  3               Camping  17  19   1 1276   .   .  26   3   .   1   5   .   .   .  12   .   .   .   9  81  10   .   8  45   .   1   .   1  17  17  |1549
  4              Canoeing   9  13   .  11 225   .   7   .   .   .   7   .   .   . 185   .   .   .  10  24   3   4   9   5   .   .   .   1   1   5  |519
  5             Canyoning   1   1   1   .   .   7   .   1   .   .   .   .   .   .   5   .   .   .   1   2   1   .   .   .   .   1   .   .   .   3  |24
  6               Fishing   4  10   4  54   .   . 1284   .   .   .   6   .   .   .  13   .   .   1  13  67   8   1  11  21   .   .   .   6  14  23  |1540
  7                  Food   3   5   .   1   .   .   3 759   .   1   6   .   1   .   .   .   .   .  18  62   6   .  20  36   .   .   .   .   2 112  |1035
  8            Geocaching   8   7   .   2   .   .   2   . 215   .   9   .   .   .   3   .   .   .  14  20  14   1   5  11   .   .   .   1   2  14  |328
  9                  Golf  10  12   1   .   .   .   2   .   . 589   4   .   .   .   .   .   .   .  22  34  13   .  15   4   .   .   .   .   1  17  |724
 10            Historical   5   6   .   1   .   .   .   1   .   . 2269   .   .   .   1   .   .   .   9 119  17   .  11   5   1   .   .  21   1  10  |2477
 11      Horseback_riding  35  20   1   .   1   .   .   .   .   .   . 258   .   .   3   .   .   .   5   7   4   1   4   2   1  21   .   .   1  10  |374
 12      Hot_air_balloons   3   2   2   .   .   .   1   2   .   .   3   . 677   .   1   .   .   .   4  14   .   .   2  11   2   .   .   1   .   5  |730
 13            Jet_skiing  16   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   1   1   .   .   .   .   .   .   .   .   .  |18
 14              Kayaking  22  10   2   8   2   .   2   1   .   .   6   .   .   . 1512   .   .   .   9  37  12   1   8   8   1   2   .   1   1  18  |1663
 15          Martial_arts   4  14   .   1   .   .   .   .   .   .  11   .   .   .   . 148   .   .   9  13   9   .  18   4   .   .   .   .   1   8  |240
 16          Orienteering   1  11   .   1   .   .   .   .   .   .   .   .   .   .   .   .   6   .   7   7   2   .   6   3   .   .   .   .   .   1  |45
 17           Parasailing  40   2   .   4   .   .   3   .   .   1   .   .   .   .   5   .   .  53   7   5   .   .   2   2   3  25   .   8   1   3  |164
 18              Partying   2   4   1   1   .   .   1   4   .   .  20   .   .   .   2   .   .   . 1931  69  15   .   8   6   .   1   .   .   .  24  |2089
 19           Photography   9   7   7   1   .   .   3   .   .   2  19   .   .   .   1   .   .   .  28 3105  38   .  21  15   1   2   .   9   1  19  |3288
 20 Recreational_shooting   6  14   .   1   .   .   2   1   .   2   7   1   1   .   1   .   .   .  18 806 2365   .   8   8   .   .   .   .   1  14  |3256
 21         Rock_climbing  40  15   3   2   1   .   1   .   .   .   3   .   1   .  28   .   .   .   9  11  14 219  15   2   3   .   .   1   3  14  |385
 22               Running   5   6   1   .   .   .   .   .   .   .   6   1   .   .   2   .   .   .  14  67   9   . 2486   3   1   .   .   .   4   6  |2611
 23              Shopping   7   8   2   .   .   .   5  11   .   .   4   .   .   .   1   .   .   .  18 140  10   .  14 1931  31   .   .   1   3  23  |2209
 24             Skydiving  16   6   .   3   .   .   1   .   .   .   3   1   3   .   3   .   .   .   9  17   7   1   4   4 607   .   .   8   .   4  |697
 25            Snorkeling  30   2   .   .   .   .   3   .   .   .   2   .   1   .  34   .   .   .   7  32   9   .   2   7  12 371   .   6   4   6  |528
 26          Snowmobiling   3   .   1   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   4   .   .   .   2   .   .   3   .   1   4  |18
 27               Surfing  14   9   1   .   .   .   5   2   .   1   9   .   .   .  12   .   .   .  10 106  24   2  17   8   8   .   . 1123   7  15  |1373
 28              Swimming  13  18   1  17   .   .  18   .   .   .   6   1   .   .   6   .   .   .  19  42  22   .  28  18   .   .   .   6 1110  21  |1346
 29          Wine_Tourism   7   9   .   4   .   .   2   7   .   3  13   .   .   .   2   .   .   .  33 120  20   .  13  34   3   .   .   1   2 1852  |2125

For some activities, e.g. Canyoning, Jet skiing, Orienteering, etc. we extracted too few tweets due to API constraints.
