# Proj_3

This repository contains two separate Android applications developed for the same course assignment:

- [UnipiCityVibe](UnipiCityVibe)
- [UnipiAudioStories](UnipiAudioStories)

Both projects are native Android apps written in Java and built with Gradle. Each project has its own source code, documentation, and Firebase configuration

## Shared Project Details

- Android Studio: 2025.3.4.7-1
- Language: Java 11
- Minimum SDK: 33
- Target SDK: 36
- Build system: Gradle 8.13

## UnipiCityVibe

UnipiCityVibe is an Android app for discovering cultural events near the user, receiving notifications about nearby events, and reserving tickets

### Main Features

- Firebase Authentication (Email - Password) for user sign-in, sign-up, and logout
- Firebase Realtime Database for events, users, and tickets
- Nearby events shown as a list and on Google Maps
- Notifications for nearby events while the user is connected
- Ticket booking and viewing of already reserved tickets
- User settings stored with SharedPreferences
- Permission management for location and notifications
- UI customization such as font size and dark mode
- Multilingual interface in English, Greek, and Russian

### Setup Notes

- A Google Maps API key is required and must be added to `local.properties` under `MAPS_API_KEY`
- A Firebase project is required for Authentication and Realtime Database
- The Firebase rules used for this app are in [UnipiCityVibe/doc/rules_firebase.json](UnipiCityVibe/doc/rules_firebase.json)
- When testing location features, use an emulator location near one of the stored events. Dummy events [UnipiCityVibe/doc/unipicityvibe-36204-default-rtdb-export.json](UnipiCityVibe/doc/unipicityvibe-36204-default-rtdb-export.json)

### Documentation

- Documentation: [UnipiCityVibe/doc/documentation.pdf](UnipiCityVibe/doc/documentation.pdf)
- Class diagram: [UnipiCityVibe/doc/classDiagram.drawio](UnipiCityVibe/doc/classDiagram.drawio)
- Class diagram image: [UnipiCityVibe/doc/classDiagram.png](UnipiCityVibe/doc/classDiagram.png)
- UI class diagram: [UnipiCityVibe/doc/classdiagramUI.drawio](UnipiCityVibe/doc/classdiagramUI.drawio)
- UI class diagram image: [UnipiCityVibe/doc/classdiagramUI.png](UnipiCityVibe/doc/classdiagramUI.png)

## UnipiAudioStories

UnipiAudioStories is an Android app for browsing children's stories, listening to them with text-to-speech, and reviewing listening statistics

### Main Features

- Firebase Realtime Database for the story catalogue
- Story list and story detail screens
- Text-to-speech playback for reading stories aloud
- Story images loaded with Glide
- Local statistics storage for listen counts and favorite stories (SharedPreferences)
- A statistics screen that shows total listens and the top three stories
- UI language switching stored locally
- Multilingual interface in English, Greek, and Russian

### Setup Notes

- A Firebase project is required for the story database. Dummy Stories [UnipiAudioStories/doc/unipiaudiostories-dc02f-default-rtdb-export.json](UnipiAudioStories/doc/unipiaudiostories-dc02f-default-rtdb-export.json)
- The Firebase rules used for this app are in [UnipiAudioStories/doc/rules_firebase.json](UnipiAudioStories/doc/rules_firebase.json)

### Documentation

- Documentation: [UnipiAudioStories/doc/documentation.pdf](UnipiAudioStories/doc/documentation.pdf)
- Class diagram: [UnipiAudioStories/doc/classDiagram.drawio](UnipiAudioStories/doc/classDiagram.drawio)
- Class diagram image: [UnipiAudioStories/doc/classDiagram.png](UnipiAudioStories/doc/classDiagram.png)

## Assignment Brief

The main course brief for both projects is available here:

- [Proj_3/doc/teliki_ergasia_omadiki_2025-2026.pdf](doc/teliki_ergasia_omadiki_2025-2026.pdf)

## Repository Layout

- `UnipiCityVibe/`: event-discovery and ticket-booking Android app
- `UnipiAudioStories/`: story-reading Android app
- `doc/`: overall assignment brief