# Masterani
A good(?) android app to watch anime.

## Credits
This project could not be done without [Masterani.me](https://www.masterani.me). At the end of the day, all the information in the app is from them.  
  
Only thing I've done is create a simple Node.js server that takes episode links and properly formats them/scrapes the direct link if applicable.  
  
Example JSON response from the server:
 
```json
[
    {
        "id": 1,
        "name": "MP4Upload",
        "quality": 480,
        "link": "https://www10.mp4upload.com:282/d/rox3rc27z3b4quuolguuqokmcsejrewt27d6w56j3ngfnexajamlnaaz/Naruto - 001 - Enter Naruto Uzumaki! [DarkDream].mp4"
    }
]
```

## Screenshots
<img align="left" src="https://i.imgur.com/r2KiSSJ.jpg" width="270" height="480"/>
<img align="left" src="https://i.imgur.com/j1rKvfP.jpg" width="270" height="480"/>
<img align="left" src="https://i.imgur.com/WKmRmu0.jpg" width="270" height="480"/>
<img align="center" src="https://i.imgur.com/v7IjgiP.jpg" width="270" height="480"/>



## TODO
This is a work in progress, and this release only contains the most basic of features. Some **future possible implementations** include:
* MyAnimeList (MAL) Integration
* Kitsu Integration
* Chromecast Support
* Android 4.4.2 Support
* Optimizations and Memory Management Improvements
* Cache objects
* Check if no internet
* Reformating the Video Player
* Allow Setting for Users to pick a Source
* Add Setting for Quality Preference (1080, 720, 420)
* Finish the Algorithm for Finding the Best Link (Currently justs chooses the first one supported)
* Finish Sorting
* Fix Timing/Date issues for Localization

## About
I Made this because there aren't any great anime apps avaliable to the public. Also, this was the perfect opportunity to try out Kotlin so I had a lot of fun with this.

###### Criticism is highly encouraged, as said, this is my first time with Kotlin, so many things are probably not done as well as it should've been.
