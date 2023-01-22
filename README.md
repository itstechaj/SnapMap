<img src="https://user-images.githubusercontent.com/74065677/212561505-29fc16c8-8725-47c9-8a81-87593f7f3a73.png" width="300" height="300">
# WELCOME

##### “ Map Your Memories ”

## SNAPMAP

- By Astro Hackers

- **See the demonstration video of SnapMap app** : https://drive.google.com/drive/folders/1JSKN2c0lFVVPW95pSTTcZU4DwzeDUWRl?usp=share_link



### SnapMap

###### "A mobile application which allows a user to post images along with

###### the geo-coordinates to a cloud database"

Welcome to our presentation on SnapMap, a revolutionary
mobile application that allows users to capture and in a
whole new way. With this application, users can click
images using the front or back camera and tag them with
the current latitude and longitude of the location.The
images and their corresponding geo-coordinates are then
stored in a cloud database for easy access and sharing.
Join us as we take you through the key features and
functionalities of this innovative application.


#### Image Capturing and Geo-tagging
This application allows users to easily capture images with
the help of the front or back camera and tag the longitude
and latitude. You need to give location’s permission to click
images if you deny the access then it will open location in
mobile’s setting where you need to allow location.Now,with
just a click, users can take a picture and the application
automatically tags it with the current latitude and longitude
of the location and display it to the user with the tagged
latitude and longitude, it also shows the current location of
the user in the map using **bing map API**.
The application uses the **GPS** of the device to obtain
the real-time coordinates and tags the images accordingly
.This feature makes the application unique and useful for
travelers and photographers.


### Database Structure
<img src="https://user-images.githubusercontent.com/74065677/213907389-9680109c-761f-418b-a6e6-0672a500ec5e.png" width="600" height="400">
**_Firebase_** _:-_ Storing and Organizing Data

SnapMap utilizes **Firebase** as its database solution. Firebase provides a real-time and secure
cloud-based database that allows us to store and organize the images and their corresponding
geo-coordinates.
The database contains the following details:-
**● UserId (unique identifier of the user in the format of Date-Day-Time)
● User Details (Details of user like name,email,password,dateofjoin..)
● All images uploaded by the user
● Latitude and Longitude (geo-coordinates of the location where the image was captured)
● Image URL (URL of the stored image) with geo-coordinates and upload date and time
● Image URL (URL of the stored image)
**
The structure of the storage is as follows:
users(root)->userid->{userId,username,email,password,joindate,uploadedImages->{datetime->{imageUrl,latitude,longitude,timeofupload}}}

### Conclusion

The created app offers a unique and convenient way to capture and share memories.We believe that this
application will change the way people capture and share their memories and we invite you to try it out
for yourself.

**_Now let’s see the pros of the application: -_**
**●** **_Recall Memory:-_** When you geo tag your images, it allows the you to recall memories of a certain
place and time. This can be useful for personal memories, but also for business, such as
remembering the progress of a construction site or real estate project.
**●** **_Discovery:-_** Geo **-** tagging images can help users discover new places and experiences they may not
have otherwise known about. For example, a traveler who sees a geo-tagged image of a beautiful
location may be inspired to visit that place themselves.
**●** **_Historical significance:-_** For photographers, journalists, and researchers, geo-tagging images can be
an important tool for preserving historical information and providing context for the images they
capture.
**●** **_Better data analysis:-_** Geo-tagging images can provide valuable data for different business, for
example, for real estate to track the location of the property, for agriculture to track growth of crops,
for construction to track progress of the site and many more.


### Demo
<img src="https://user-images.githubusercontent.com/74065677/213907604-e0b894ac-f488-40f3-9679-357c197c8a9c.jpg" width="300" height="600">


<img src="https://user-images.githubusercontent.com/74065677/213907551-5ad8d893-d9ba-4673-8188-92a473278244.jpg" width="300" height="600">

<img src="https://user-images.githubusercontent.com/74065677/213907578-e5dcbbfb-d553-43b2-a32e-2ec932841b5a.jpg" width="300" height="600">

<img src="https://user-images.githubusercontent.com/74065677/213907631-4dbd7d9c-2efb-4350-93be-a15c0e7e4335.jpg" width="300" height="600">

<img src="https://user-images.githubusercontent.com/74065677/213907649-d99cbc6b-3e43-4611-862e-3f8f2ae4c3bc.jpg" width="300" height="600">

<img src="https://user-images.githubusercontent.com/74065677/213908146-2ecddade-c22b-47ec-b37c-373a566a1507.jpg" width="300" height="600">

### References & Sources

The following sources were used in the research and development of the Geo-Tagging Images application:

```
● Bing Map API for displaying current location of user in map
```
```
● Firebase realtime database by Google for storing image data
```
```
● StackOverflow
```
```
● Youtube
```
### Any contributions or suggestions are greatly appreciated and welcomed.

### THANK YOU

### Contributors
- [Pratyush Verma](https://github.com/verma321)
- [Manish Kumar Sharma](https://github.com/shubhman20)
- [Utkarsh Anand](https://github.com/UTKARSHANANDX)
- [Abhijit Kumar](https://github.com/itstechaj)

