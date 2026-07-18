# UnipiFireChat

UnipiFireChat is a simple Android messenger built with Firebase Authentication and Firebase Realtime Database

The app supports:

- user registration and sign-in with Firebase Authentication 
- a home screen that lists existing chats
- sending chat invites to other users by username
- opening a conversation and sending messages in real time
- logging out and returning to the entry screen

## Project Details

- Android Studio: 2025.3.4.7-1
- Language: Java 11
- Minimum SDK: 33
- Target SDK: 36
- Build system: Gradle

## Firebase Setup

To run the app on your own machine, create your own Firebase project and enable:

- Firebase Authentication (Email - Password)
- Firebase Realtime Database

The database rules used for this project are in [doc/rules_firebase.json](doc/rules_firebase.json)

Make sure to add your own `google-services.json` file under `app/`

## Main Flow

1. Open the app and choose sign in or sign up
2. Create an account or log in with an existing one
3. From the home screen, start or open a chat
4. Send messages inside the selected conversation

## Documentation

- Assignment brief: [doc/AndroidPLH2526_ergasia2.pdf](doc/AndroidPLH2526_ergasia2.pdf)
- Project documentation: [doc/Documentation.pdf](doc/Documentation.pdf)
- Class diagram: [doc/ClassDiagram.drawio](doc/ClassDiagram.drawio)
- Class diagram image: [doc/ClassDiagram.png](doc/ClassDiagram.png)

## Source Structure

- `app/src/main/java`: application code
- `app/src/main/res`: layouts, strings, and resources
- `doc`: documentation, rules, and diagrams


