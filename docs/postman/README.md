# API Testing Documentation

## Postman Requests
- Collection file:SpotABirdApp.postman_collection.json
- Notes: PostmanRequests.txt (human-readable request notes)
- Environment file: SpotABirdApp.postman_environment.json

## Using These Requests
1. Install Postman
2. Create new collection
**Recommended**: Use Import → File and select PostmanRequests.postman_collection.json.
**Alternative**: Open the JSON file in a text editor, copy its contents, then use Import → Raw Text in Postman.

## Required Environment Variables
- `base_url`: Your API base URL
- `token`: JWT auth token (get from /auth/login)

## Notes
- Authentication: Use the login endpoint to get a JWT token, then include it in headers for protected routes.
- Pagination: Defaults to page=0 and size=10 if not specified.