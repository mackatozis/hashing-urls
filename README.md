<p align="center">
    <a href="https://travis-ci.org/mackatozis/hashing-urls">
    <img src="https://travis-ci.org/mackatozis/hashing-urls.svg?branch=master" /></a>
    <a href="https://codecov.io/gh/mackatozis/hashing-urls">
    <img src="https://codecov.io/gh/mackatozis/hashing-urls/branch/master/graph/badge.svg" /></a>
    <a href="https://sonarcloud.io/dashboard?id=mackatozis_hashing-urls">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=mackatozis_hashing-urls&metric=sqale_rating" /></a>
    <a href="https://adoptopenjdk.net/">
    <img src="https://img.shields.io/badge/java-11-orange" /></a>
    <a href="https://start.spring.io/">
    <img src="https://img.shields.io/badge/springboot-2.2.6-brightgreen" /></a>
    <a href="https://maven.apache.org/download.cgi">
    <img src="https://img.shields.io/badge/maven-3.6.3-blue" /></a>
    <a href="https://github.com/mackatozis/hashing-urls/blob/master/LICENSE">
    <img src="https://img.shields.io/badge/license-MIT-green" /></a>    
</p>

# About

In a nutshell, Hashing URLs is the Java implementation to compute the hash prefixes of a URL per [Google's standards].

# What to Use this microservice for and When to Use It

Use this microservice to compute the URL hash prefixes of a URL to use against the Google's Web Risk [Update API].

# Requirements

* **Java** : 11+ 
* **SpringBoot** : 2.2.6+
* **Maven** : 3.6.3+

# Usage

To run the application, run the following command in a terminal window (in the `hashing-urls`) directory:

    mvn spring-boot:run

Now to compute the hash prefixes of a URL, run the service with curl (in a separate terminal window), by running the following command (pass the URL as query param):

    curl "http://localhost:8180/hashing-urls/hash-prefixes?url=http://www.example.com"

which will output the following:

```
{
    "url": "http://www.example.com",
    "expressionHashes": [
        {
            "expression": "example.com/",
            "fullHash": "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af2653f8a938cd801",
            "hashPrefixes": [
                "73d986e0",
                "73d986e009",
                     .
                     .
                     .
                "73d986e009065f182c10bcb6a45db3d6eda9498f8930654af2653f8a938cd8"
            ]
        },
        {
            "expression": "www.example.com/",
            "fullHash": "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a5070fe145d87977",
            "hashPrefixes": [
                "d59cc9d3",
                "d59cc9d3fe",
                     .
                     .
                     .
                "d59cc9d3fecd8cf920eadd03012f0be497fb8c0e3c3e7ee8a5070fe145d879"
            ]
        }
    ]
}
```

# Contributing
Follow the [contributing guidelines](CONTRIBUTING.md) if you want to propose a change.

# License
URL Hash Prefixes is **licensed** under the **[MIT License]**.

[Google's standards]: https://cloud.google.com/web-risk/docs/urls-hashing
[Update API]: https://cloud.google.com/web-risk/docs/update-api
[MIT License]: https://github.com/mackatozis/hashing-urls/blob/master/LICENSE
