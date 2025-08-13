# API Testing Documentation

## Postman Requests
- Requests available in PostmanRequests.txt
- Import instructions below...

## Using These Requests
1. Install Postman
2. Create new collection
3. Use "Import" > "Raw Text"

## Required Environment Variables
- `base_url`: Your API base URL
- `token`: JWT auth token (get from /auth/login)

## Notes
- Authentication: Use the login endpoint to get a JWT token, then include it in headers for protected routes.
- Pagination: Defaults to page=0 and size=10 if not specified.