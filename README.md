# TMobileTest
This repository contains the complete source code of T-Mobile assessment project for Android.

## Requirements
Create an Android app that uses [Test API](https://private-8ce77c-tmobiletest.apiary-mock.com/test/home) that allows to get list of pre-defined result.
1. Query the rest API for receiving the home page response.
2. Display the data by constructing a list view.
3. The view(s) should dynamically set the height based on the
content.
4. All texts in the view(s) should be displayed (no truncation)
5. The view(s) which have image should set the height dynamically
based on image resolution in the API response.
Ex: Images in the API response can be a rectangle (higher width/
higher height) or square.
6. All image + text models should be rendered with UI hierarchy as
below:
    - Image will take the entire RecyclerView
    - Texts will be displayed on top of the ImageView beginning from bottom of the RecyclerView

## Additional Work (Myself)
1. Supports portrait and landscape modes with light and night mode. In landscape mode, diplay the list with 2 columns
2. Handle offline mode by saving last data from api. So the app works without network once connected to server only once.

## Build system
### Environment
Android Studio Dolphin | 2021.3.1 Patch 1

## Authors
Jason Tonkins([tinysoft120](https://github.com/tinysoft120/TMobileTest)), jason.tonkins120@gmail.com
