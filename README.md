<div align="center"><img src="https://user-images.githubusercontent.com/74065677/212561505-29fc16c8-8725-47c9-8a81-87593f7f3a73.png" width="300" height="300"></div>

## SNAPMAP <sub>(By Astro Hackers)
</sub>

- A mobile application which allows a user to post images along with the geo-coordinates to a real time cloud database

- **See the demonstration video of SnapMap app** : https://drive.google.com/drive/folders/1JSKN2c0lFVVPW95pSTTcZU4DwzeDUWRl?usp=share_link


SnapMap is a revolutionary
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
<img src="https://user-images.githubusercontent.com/74065677/224414808-1737dcfc-1fc1-4894-a39a-2f99a974d22b.png" width="600" height="400">

#### Storing and Organizing Data
SnapMap utilizes **Firebase** as its database solution. Firebase provides a real-time and secure
cloud-based database that allows us to store and organize the images and their corresponding
geo-coordinates.
The database contains the following details:-
* UserId (unique identifier of the user in the format of Date-Day-Time)
* User Details (Details of user like name,email,password,dateofjoin..)
* All images uploaded by the user
* Latitude and Longitude (geo-coordinates of the location where the image was captured)
* Image URL (URL of the stored image) with geo-coordinates and upload date and time

#### The structure of the storage is as follows:
```json
{
    "users": {
        "userid": {
            "userId": "",
            "username": "",
            "email": "",
            "password": "",
            "joindate": "",
            "uploadedImages": {
                "datetime": {
                    "imageUrl": "",
                    "latitude": "",
                    "longitude": "",
                    "timeofupload": ""
                }
            }
        }
    }
}

```
### Conclusion

The created app offers a unique and convenient way to capture and share memories.We believe that this
application will change the way people capture and share their memories and we invite you to try it out
for yourself.

### Now let’s see the pros of the application: -
* Recall Memory:- When you geo tag your images, it allows the you to recall memories of a certain
place and time. This can be useful for personal memories, but also for business, such as
remembering the progress of a construction site or real estate project.
* Discovery:- Geo tagging images can help users discover new places and experiences they may not
have otherwise known about. For example, a traveler who sees a geo-tagged image of a beautiful
location may be inspired to visit that place themselves.
* Historical significance:- For photographers, journalists, and researchers, geo-tagging images can be
an important tool for preserving historical information and providing context for the images they
capture.
* Better data analysis:- Geo-tagging images can provide valuable data for different business, for
example, for real estate to track the location of the property, for agriculture to track growth of crops,
for construction to track progress of the site and many more.


### Sample Images

<div align="center">
    <a href="#" style="padding:25px;"><img src="https://user-images.githubusercontent.com/74065677/224420770-48ab0085-caf2-41fc-a613-84163342abaf.jpeg" width="200" height="400"></a>
    <a href="#" style="padding:25px;"><img src="https://user-images.githubusercontent.com/74065677/224420884-37697934-36b6-4eda-b245-fc2258cf71b4.jpeg" width="200" height="400"></a>
    <a href="#" style="padding:25px;"><img src="https://user-images.githubusercontent.com/74065677/224420886-ccfe7528-9fae-456d-b2e7-9e56fff11179.jpeg" width="200" height="400"></a>
</div>
<div align="center" >
    <a href="#" style="padding:25px;"><img src="https://user-images.githubusercontent.com/74065677/224420892-aacf800b-5236-4441-aac6-3802498487ba.jpeg" width="200" height="400"></a>
    <a href="#" style="padding:25px;"><img src="https://user-images.githubusercontent.com/74065677/224420895-3435a922-f70b-49d1-9832-476064560633.jpeg" width="200" height="400"></a>
    <a href="#" style="padding:25px;"><img src="https://user-images.githubusercontent.com/74065677/224420896-b07cc6e6-37f4-4011-91f2-3deaa6d6f132.jpeg" width="200" height="400"></a>
</div>
<div align="center" >
    <a href="#" style="padding:25px;"><img src="https://user-images.githubusercontent.com/74065677/224420868-becc7658-c8cb-4bab-95d9-ed4f60279ee2.jpeg" width="200" height="400"></a>
    <a href="#" style="padding:25px;"><img src="https://user-images.githubusercontent.com/74065677/224420872-d99814b8-f06a-4051-91ca-eab8448ca0b4.jpeg" width="200" height="400"></a>
    <a href="#" style="padding:25px;"><img src="https://user-images.githubusercontent.com/74065677/224420878-3b0b993f-03fc-42a6-a95d-367ad316dcd6.png" width="200" height="400"></a>
</div>

### References & Sources

The following sources were used in the research and development of the Geo-Tagging Images application:

```
● Bing Map API for displaying current location of user in map and generating heatmap
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
