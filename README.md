# Baseflix Android

Rough V1 Android client for the Baseflix home media server.

## V1 currently includes

- Enter a Baseflix server address
- Connect to `GET /api/videos`
- Group videos by folder
- Display video names
- Open a Media3 video player
- Basic play/pause/seek controls from Media3
- LAN HTTP support for a local Baseflix server

## Expected server response

`GET /api/videos` should return a JSON array. Each item should contain at least:

```json
{
  "id": 1,
  "name": "Movie.mp4",
  "folder": "Movies",
  "stream_url": "/video/Movie.mp4"
}
```

The existing Baseflix server can be adapted to this contract once its current playback route is confirmed.

## Next V1 steps

1. Connect the exact existing Baseflix stream route.
2. Add poster thumbnails.
3. Improve the movie grid UI.
4. Add Continue Watching.
5. Add Android TV navigation.
6. Add profiles.
7. Add Photos and Music later.
