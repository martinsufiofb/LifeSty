# LifeStyle

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
LifeStyle - Helps users start their fitness journey/goals, see other users who have the same journey/goals and lets them connect.


### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Fitness & Social
- **Mobile:** Mobile App
- **Story:** Allows users to track their fitness journey
- **Market:** People who would prefer home workout and would love a virtual home workout partner
- **Habit:** Would be used at the convenience of the users
- **Scope:** Connecting users with similar home work taste across the neighborhood.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users can Sign Up
* Users can Login and Logout
* Users can view a 4 workout exercises
* Users can see other uses with the same fitness goals within a 5 mile radius
* Users can View their profiles along with that of their friends 
* Users can search for username to view users profile
* Users sees a splash screen when app is launched
* App uses camera to record the count of an exercises that is done (not supported with all exercises)


**Optional Nice-to-have Stories**

* App has a very polished UI
* Users can upload picture after they reach a milestone
* Users can like and comment on others post
* Users can see thier friends milestones post
* App uses camera to record the count of an exercises that is done(supported with all 4 exercises)


### 2. Screen Archetypes

* Login Screen
  * User can Log in
  * User can click sign up button

* Sign Up Screen
  * User can Sign up for an account
  
* Home Screen / Timeline
  * User can View workouts exercises and select one.
  * User can click on workout exercises to see more details
  
* Profile Screen
  * User can view thier profile and their Workout description
  * User can view thier milestone Posts
  * User can View total number of likes

* Detail Screen
  * User can view details of a certain cell's information
  * User can like & Comment

* Splash Screen
  * User sees app icon once the app is launched

* Community screen
  * User can see people doing the same exercise within a 5 mile radius  


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home Screen / TimeLine
* Profile Screen
* Community Screen

**Flow Navigation** (Screen to Screen)

* Login Screen
  * Sign Up Screen
  * Home / Timeline Screen

* Sign Up Screen
  * Login Screen
  * Home / Timeline Screen
  
* Home / Timeline Screen
  * Detail Screen
  * User Profile Screen
  * Community Screen
 
* Profile Screen
  * Detail Screen

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="https://github.com/martinsufiofb/LifeStyle/blob/main/IMG_4487.JPG" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models

#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user|
   | Username      | String   | image author |
   | profile pic   | File     | profile pic of this |
   | Goal          | Pointer to the Goals | points to the goals section of the user |

#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post|
   | author        | Pointer to User| points to the author made the post |
   | image         | File     | image that user posts |
   | caption       | String   | image caption by author |
   | commentsCount | Number   | number of comments that has been posted to an image |
   | likesCount    | Number   | number of likes for the post |
   | createdAt     | DateTime | date when post is created (default field) |
   
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
