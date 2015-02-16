# JsonApi-Android

JsonApi-Android is a parser library that convert the json api file (JSONAPI.org formatted) into a usable class. What you need to do is define the classes and then call a JSONAPI function.

How to use
=====

**Gradle**
````gradle
not ready yet.
````

Assume the JSON object which will be used in this example is :
````text
{
  "links": {
    "posts.author": {
      "href": "",
      "type": "people"
    },
    "posts.comments": {
      "href": "",
      "type": "comments"
    }
  },
  "data": [{
    "id": "1",
    "title": "Rails is Omakase",
    "links": {
      "author": "9",
      "comments": [ "1", "2", "3" ]
    }}, {
    "id": "2",
    "title": "The Parley Letter",
    "links": {
      "author": "9",
      "comments": [ "4", "5" ]
   }}, {
    "id": "3",
    "title": "Dependency Injection is Not a Virtue",
    "links": {
      "author": "9",
      "comments": [ "6" ]
    }
  }],
  "linked": {
    "people": [{
      "id": "9",
      "name": "@d2h"
    },
    {
      "id": "10",
      "name": "Andiny gehol"
    }],
    "comments": [{
      "id": "1",
      "body": "Mmmmmakase"
    }, {
      "id": "2",
      "body": "I prefer unagi"
    }, {
      "id": "3",
      "body": "What's Omakase?"
    }, {
      "id": "4",
      "body": "Parley is a discussion, especially one between enemies"
    }, {
      "id": "5",
      "body": "The parsley letter"
    }, {
      "id": "6",
      "body": "Dependency Injection is Not a Vice"
    }]
  }
}

````

Since there are several object from the JSON data, we have to pre-defined the class for the data response. In this case `Post.java`, `People.java`, `Comment.java`. It's almost similar manner when you are trying to convert a json data using GSON Library.

**Post.java**
````java
public class Post {
    private String id;
    private String title;
    private People author;
    private Comment[] comments;
}
````

**People.java**
````java
public class People {
    private String id;
    private String name;
}
````

**Comment.java**
````java
public class Comment {
    private String id;
    private String body;
}
````

Since we want to get all resource objects from `data` field in JSON file, we have to define the response/target class. If the data is a only JSON Object, instead of JSON Array, you only need to remove the array type on the class above.

**DataResponse.java**
````java
public class DataResponse {
    @SerializeName("data")
    private Post[] posts;
}
````

Then you have to create a new instance of JSONAPI class and catch the exception as shown below.

**Call function**
````java
        String jsonString = "" //Define your jsonstring here.

        try {
            DataResponse postData = (DataResponse) new JSONAPI().fromJson(jsonString , DataResponse.class);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
````

Finally, the object returned by this library is a usable object where all linked resource has been already mapped into the pre-defined classes.

License
=====

JSONAPI is available under the MIT license. See the LICENSE file for more info.
