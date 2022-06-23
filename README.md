# Book-Social


## 1. User Stories (Required and Optional)

**Required Must-have Stories**


 * User can sign in using FB SDK
 * User can search for books
 * User can add books to library
 * User can delete books from library
 * User can categorize books into read, unread or now reading
 * User can view  book details
 * User can give a book review with rating and text
 * User can see reviews of books
 * User can make a review through the app via FB


**Optional Nice-to-have Stories**

 * User comment on a review
 * User can like a review, comment 
 * User can view other FB friends who is on the App
 * User can sign in using Email
 * User can see all their friends reviews
 * User can see all their friends library
 * User can add a photo when signing up using email
 * User can search for other users on the app
 * User can send friend request to people on the app

## 2. Screen Archetypes

 **LOGIN**
* On show
A button that allows users to sign in with their facebook account

* On click
The user details will be stored on the User table in parse
New user (Name, FB ID)

**MAIN PAGE**
* On show
From user table 
fetch now reading
fetch reading book

From open library API
Fetch book title
Fetch book cover

* On click 
On book opens the details page.

**LIBRARY**
* On show
From the user table
Fetches the now reading, read and unread
Fetches book cover from open API

* On click
On books, show the book details.

**USER PAGE**
* On show
From User table
Fetch the friends 

From post table
Fetch the book, like, timestamp, user ID of owner

	From Comment table
Get the post ID, User ID, body, like., timestamp 


* On click
From post table
Fetch the number of likes on a post.
Add or remove like for user

**BOOK DETAILS PAGE**
* On show
From the open library API
Fetches the book description, cover, date of publication

**Search Page**
* On show
From Open Library API
Search from books by name

* On Click
From user table
Add book to either read, unread or now reading




## 3. Navigation

**Tab Navigation** (Tab to Screen)

 * Home
 * Search
 * User Page
 * Details Page

**Flow Navigation** (Screen to Screen)

 ![](https://i.imgur.com/wjNwcUS.png)
 
 ## Milestones
 **Week 1**
  * Build the login screen
  * Allow users to login through Facebook 
  * Store user information (name, email) on Parse server

**Week 2**
  * Work on the search screen
  * Integrate the open library API
  * Users can now search for books

**Week 3**
  * Build the library screen
  * User can now add new books to library
  * User can see the number of books in library
 
**Week 4**
  * Build the home screen
  * User can see current reading books
  * User can set reading goals
 
**Week 5**
  * Build the user page
  * User can post a review
  * User can rate a review
  * User can like a review
 
**Week 6**
  * Build the post details page
  * Fetch book description, book cover and title from the API
  * User can see brief description on about a book

**Week 7**
  * Start working on some stretch goals
  
   
