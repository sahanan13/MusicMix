MusicMix
===

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Users can fill out a questionnaire about their mood/feelings that day and app will generate a music playlist for them based on artists that they listen to or might like and the genres as well.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Music
- **Mobile:** App will be mobile first - more convenient to answer questions and generate playlists from phone. User can also play music from generated playlists on laptop as well (through Spotify?).
- **Story:** Allows users to discover new music and listen to new playlists according to their mood or what they feel like listening to. Users can generate playlists as well as generate playlists with their friends to listen to music together.
- **Market:** Anyone who listens to music. Users who like to listen to new music regularly will enjoy this app, since the app will generate playlists with new songs they might like as well for the user.
- **Habit:** Users can fill out the questionnaires several times a day to generate new playlists. This is habit forming, since users can create playlists and will go back to listen to them often.
- **Scope:** The app can start out as a way to create playlists for users based on their mood, but can expand to creating playlists based on genre or allowing users to create playlists with friends as well.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [x] User can log in
* [x] User is able to answer questions in questionnaire
* [x] Using the results of the questionnaire, a playlist can be generated for the user
* [x] The playlist has similar music according to the user's tastes
* [x] The user can like the playlist (heart button)
* [x] User can save playlist to their Spotify
* [x] User can logout
* [x] User can sign up for an account if they don't have one

**Optional Nice-to-have Stories**

* [x] User can see playlist images in home/profile screen
* [x] User can see profile screen with their username, picture, and playlists
* [x] User can view a detail view of playlists and the list of songs in the playlist
* [ ] User can "refresh" to generate a new playlist if they don't like the playlist that was generated
* [ ] User can open Spotify from app
* [ ] User can share playlist with friends
* [ ] User can add friends on the app
* [ ] User can also generate separate playlists based on genres
* [ ] Users can create playlists together - using results of multiple people's questionnaires or playlists of multiple users
* [ ] Users can write comments on playlists to friends
* [ ] Users can see graphs about their common genres, songs, and questionnaire answers.

### 2. Screen Archetypes

* Login screen
   * User can login
* Registration screen
   * User can create a new account
* Stream screen
    * User can view all their playlists
* Detail screen
    * User can click on a playlists to view details about the playlists - ex. duration, songs list, people it is shared with
* Creation screen
    * User can fill out a new questionnaire to have a playlist generated
* Profile screen
    * User can view their profile information
* Settings screen
    * User can change settings in the app

### 3. Navigation

**Tab Navigation** (Tab to Screen)
* Home (Feed with playlists)
* Questionnaire/Generate Playlist
* Profile
* Logout

**Flow Navigation** (Screen to Screen)

* Login screen
   * Home
* Registration Screen
   * Home
* Stream Screen
    * Details of a playlists
* Creation Screen
    * Home screen (with new playlist showing at the top)

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="https://imgur.com/a/I9ywNXO" width=600>
**[Wireframe Image](https://imgur.com/a/I9ywNXO)**

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
### Models

#### Song/Track:
| Property       | Type         | Description  |
| :------------- | :----------: | -----------: |
| spotifyId       | String   | Spotify id for song    |
| title           | String |         Song title |
| artist          | String | Song artist |
| album | String | album that song is in |
| timeAdded | DateTime | time added to playlist |
| playlistName | String | what playlist the song is in |
| username | String | what user added the song |
| duration | int | how long the song is (in minutes) |
| liked | boolean | true if user liked the song |

#### Playlist:
| Property       | Type         | Description  |
| :------------- | :----------: | -----------: |
| playlistId       | String   | Spotify id for playlist    |
| title           | String |          Title of playlist |
| artists          | String array | artists in that playlist |
| songTitles | String array| titles of song in the playlist |
| createdBy | String | user who created the playlist|
| createdAt | DateTime | when the playlist was created |
| duration | int | how long the playlist is (in minutes) |
| liked | boolean | true if user liked the playlist |
| tags | String array | tags associated with the playlist |



### Networking
[Add list of network requests by screen]
- Home Feed Screen
    - (Read/GET) Query all playlists where current user is the createdBy user
    - (Create/POST) Create a new like on a playlist if user likes it
    - (Delete) Delete like if user unlikes the playlist
- Create Playlist Screen
    - (Create/POST) Create a new playlist object that has song objects in it
- Profile Screen
    - (Read/GET) Query information about user, like following count, followers count, and list of playlists
- Playlist Detail screen
    - (Read/GET) Query information about playlist object
    - (Create/POST) Create a new like if user likes playlist
    - (Delete) Delete the like if user unlikes the playlist

[Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
