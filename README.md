# quarkus-crud-reactive-mongodb project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

This project demonstrates the usage of MongoDB with Panache on top with reactive endpoints by
 creating entities and managing relations between and their corresponding operations in a blog crud
like fashion.

This project is presented in the following article.

[Creating a Reactive CRUD blog app with MongoDB, Quarkus and Panache](https://dvddhln.medium.com/creating-a-reactive-crud-blog-app-with-mongodb-quarkus-and-panache-54d659cf8dcb)


## Run mongodb in Docker container

    docker run -ti --rm -p 27017:27017 mongo:4.0

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

### Example Response

    {
    "id": "60dc9301971c0d514f62792e",
    "author": "John Doe",
    "comments": [
        {
            "id": "60dc930f971c0d514f62792f",
            "content": "22323232",
            "creationDate": "2021-06-30T17:51:43.781"
        },
        {
            "id": "60dc9310971c0d514f627930",
            "content": "22323232",
            "creationDate": "2021-06-30T17:51:44.815"
        },
        {
            "id": "60dc9311971c0d514f627931",
            "content": "1337",
            "creationDate": "2021-06-30T17:51:45.659"
        }
    ],
    "content": "This is some sample content",
    "creationDate": "2021-06-30T17:51:29.812",
    "title": "A new title"
    }

## GET All Posts 

    $ curl "localhost:8080/posts"

## GET All Posts by author and title

    $ curl "localhost:8080/posts/search?author=David&title=My Post"

## GET All Posts by date

    $ curl "localhost:8080/posts/search?dateFrom=2021-06-17T00:00:00.000Z&dateTo=2022-06-17T00:00:00.000Z"

## GET All Posts by multiple authors

    $ curl "localhost:8080/posts/search2?authors=John Doe&authors=Grace Kelly"


## Create POST

    $ curl -X POST "localhost:8080/posts"
    
    {
    "title":"My Post",
    "author":"John Doe"
    }

## Add comment to post

    $ curl -X PUT "localhost:8080/posts/60db336deb401c61ad7c559c"
    
    {
        "content":"This is a comment"
    }
    
## Delete post by id

    $ curl -X DELETE "localhost:8080/posts/60db4b085d2d613300cc136b"

## GET All Comments

    $ curl GET "localhost:8080/comments"

## Update comment

    $ curl -X PUT "localhost:8080/comments/60dc741f971c0d514f627904"

    {
        "content":"This is an update comment"
    }

## Delete comment 
    
    $ curl -X DELETE "localhost:8080/comments/60dc741f971c0d514f627904"