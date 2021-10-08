# neaapi

## Requirements
- Generate api key in [nasa nea](https://api.nasa.gov/)
- Alter environment variable API_KEY with api key generated in `docker-compose.yml` or `./src/resources/application.yml`

## Resources availability
- http://localhost:8080/nea/actuator/health
- http://localhost:8080/nea/feed/2020-12-12
- http://localhost:8080/nea/feed?startDate=2020-12-12&endDate=2020-12-19