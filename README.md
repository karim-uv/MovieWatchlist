# Movie Watchlist

<img src="https://github.com/user-attachments/assets/48c3c737-c2d0-4cb4-9e01-98f0fd05d33f" width="260"> <img src="https://github.com/user-attachments/assets/bb7e3747-e1dd-4366-bd11-d6bf78727c16" width="260">

---

A native Android app for tracking films you want to watch and films you've already seen, with your own ratings, notes and reviews. Films can be added manually, or pulled straight from **The Movie Database (TMDB)** so the title, genre, synopsis and poster art fill in automatically.

Built solo in Java as a university project.

---




## Features

- Add films manually or import them from TMDB with a single search
- Mark films as **Watched** or **To Watch**, with a colour-coded status bar on each card
- Five-star rating plus free-text notes and reviews
- Poster artwork on both the list and the detail screen, with a fallback for manually added films
- Full edit and delete support, with a confirmation dialog before deleting
- Data persists locally, so everything except TMDB search works offline

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java |
| Architecture | MVVM (ViewModel + LiveData + Repository) |
| Local storage | Room over SQLite |
| Networking | Retrofit 2 + Gson |
| Image loading | Glide |
| UI | Material 3, RecyclerView, multiple Activities |
| Min / Target SDK | 33 / 36 |

## Architecture

```
UI (Activities + RecyclerView Adapter)
        |  observes LiveData
        v
    ViewModel
        |
        v
    Repository ---------------+
        |                     |
        v                     v
  Room DAO / SQLite    Retrofit -> TMDB API
```

The repository sits between the ViewModel and both data sources, keeping every database write and network call off the main thread. The UI never touches Room or Retrofit directly. It observes `LiveData` and redraws when the underlying data changes.

## TMDB integration

The search flow deliberately uses two endpoints rather than one:

1. `GET /3/search/movie` returns matching films, but supplies only **genre IDs** such as `[28, 12]`.
2. When the user selects a result, `GET /3/movie/{id}` resolves those IDs into readable genre names.

Two further details:

- **Debouncing** &mdash; the search fires 500 ms after the user stops typing rather than on every keystroke. Typing "Inception" would otherwise trigger nine separate API calls.
- **URLs, not blobs** &mdash; only the poster URL is stored in the database. Glide downloads and caches the image on disk, which keeps the SQLite database small and fast.

Network failures are caught in Retrofit's `onFailure` callback and surfaced to the user. The rest of the app continues working offline from the local database.

## Setup

You will need a free TMDB API key from [themoviedb.org](https://www.themoviedb.org/settings/api).

1. Clone the repository and open it in Android Studio.
2. Add your key to `local.properties` in the project root:

   ```properties
   TMDB_API_KEY=your_key_here
   ```

3. Sync Gradle and run.

`local.properties` is git-ignored, so the API key never enters version control. It is injected at build time through `buildConfigField` and read in code as `BuildConfig.TMDB_API_KEY`. The build is written to succeed even when the key is missing, so cloning and building never fails outright. TMDB search simply returns nothing until a key is supplied.

## Project structure

```
app/src/main/java/com/karim/moviewatchlist/
├── api/          Retrofit client, TMDB endpoint interface, response models
├── data/         Movie entity, DAO, Room database, repository
└── ui/           Activities, RecyclerView adapter, ViewModel
```
