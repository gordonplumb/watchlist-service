# Watchlist Service

Service for https://github.com/gordonplumb/thewatchlist.

Requires:
- MySQL
- TMDB API token (https://developer.themoviedb.org/docs/getting-started)

Configure the following environment variables:
- SECRET_KEY: signing key for generating JWTs
- TMDB_TOKEN: bearer token for the TMDB API
