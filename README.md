# DocConverter
DocConverter - simple web-api document converter based on LibreOffice

## Requirements
[LibreOffice](https://www.libreoffice.org/) should be installed

## Defaults
Listening port - 8080. To change, use `--server.port=80`

Listening address - localhost. To change, use `--server.address=192.168.1.1`

Timeout to wait LibreOffice process - 120 seconds. To change, use `FILE_CONVERT_TIMEOUT` environment variable

## Usage
- `/status/` GET request returns OK if service is up and running
- `/convert/` POST (with DOC \ XLS file content in body) request returns PDF in response body.

## Usage in Docker
Dockerfile, prebuilt jar file, and docker-compose.yml example included