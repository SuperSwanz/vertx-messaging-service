# vertx-messaging-service
Sample application built in Vertx Reactivex, custom annotations to showcase sending of emails using Vertx.

## Getting Started

Git clone the project on your local machine and import it to your favorite ide.

### Prerequisites

For runnning this, you will need
- Java 1.8
- Gradle support - In Eclipse editor, goto help -> eclipse marketplace -> search for buildship (buildship gradle integration) and install it.

## Brief

This application tests the API that you want to test and pass the response back with the api result.
- **ServiceLauncher**        -> The starting point of the application. It is used to set the app configuration.
- **MainVerticle**            -> Main verticle deploys the HttpServerVerticle.
- **MessageHandler**          -> Message Handler which receives the input, process the input, calls MesagingService to send the email and returns the Json response.


## Running the app

For running the app, (IDE used here is Eclipse)
- Open **config.json** file and set the "http_server_port" as per your choice. Also, update the "mailConfig" settings as per your mail provider.
- Once, changes are done in **config.json**, right click on the project("vertx-messaging-service"), <br />select "Run As" -> "Run Configurations". Set:
  * **Main**: com.messaging.api.ServiceLauncher
  * **Program arguments**: <br />run com.messaging.api.verticle.MainVerticle -conf ../vertx-messaging-service/src/main/resources/config.json
  * **VM arguments**: -Dlogback.configurationFile=file:../vertx-messaging-service/src/main/resources/logback.xml <br /><br /> After setting the variables, click "Run".<br />
- If app starts successfully, goto **http://localhost:8080/messaging/api/message/ping**. Status json {"code":200,"message":"success","hasError":false,"data":{"status":"Ok"}} will be served as response.
- To call the Message handler, do <br />
**Type:** *POST http://localhost:8080/messaging/api/message/send* <br />
**Headers:** *Content-Type: application/json* <br />
**Data to send:** *json data below -> Update emailIds, attachments, content as you want*
```
{
  "from": "xxxxx@yyyyy.com",
  "to": [
    "xxxxx@yyyyy.com",
    "xxxxx@yyyyy.com"
  ],
  "cc": [
    "xxxxx@yyyyy.com"
  ],
  "subject": "subject",
  "content": "content",
  "isContentHTML": true,
  "attachments": [
    {
      "filePath": "../vertx-messaging-service/src/main/resources/JavaFX_1.png",
      "name": "name_inline",
      "contentType": "image/jpeg",
      "disposition": "disposition_1",
      "description": "description_inline",
      "contentId": "contentId_1",
      "isInlineAttachment": false
    }
  ]
}
```
* Response would be: <br />
```
{
  "code": 200,
  "message": "success",
  "hasError": false,
  "data": {
    "messageID": "<msg.1513336315325.vertxmail.3@XX.X.XX.XXXX>",
    "recipients": [
      "xxxxx@yyyyy.com",
      "xxxxx@yyyyy.com",
      "xxxxx@yyyyy.com"
    ]
  }
}
```
## Built With

* [Vertx](http://vertx.io/) - The web framework used
* [Gradle](https://gradle.org/) - Dependency Management

