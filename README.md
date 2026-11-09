# Lost & Found App

An Android app for reporting lost and found documents (IDs, cards, certificates and similar) so they can find their way back to their owners.

## Features

- Register and log in.
- Report a lost document or a found one, with category and photo.
- Browse all lost and all found reports.
- "My documents": manage and delete your own reports.
- Admin approval of lost-document reports.

## Stack

- Android (Java), AndroidX, Navigation drawer with fragments
- Retrofit 2 + Gson for the REST API, Volley
- Picasso and CircleImageView for images

## API

The app talks to a PHP REST backend. Main calls (`interfaces/RESTApiInterface.java`):

| Method | Path |
|---|---|
| POST | `user/register/`, `user/login/` |
| GET | `mobile/getCategory`, `mobile/getAllLost/`, `mobile/getAllFound` |
| POST | `mobile/store/lost`, `mobile/store/found` |
| POST | `mobile/getUserLost`, `mobile/getUserFound` |
| POST | `mobile/deleteLost`, `mobile/deleteFound`, `mobile/approve/lost` |

To point the app at your own backend, change `BASE_URL` in `singleton/RESTApiClient.java`.

## Running

Open in Android Studio and run on a device or emulator (minSdk 21).
