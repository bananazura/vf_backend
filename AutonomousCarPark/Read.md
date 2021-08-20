This project is about Spring Boot Rest API's. Project aim is creating simple backend for autonomous car park.

You can use API for use APIs. 

API repositories are at package com.byelken.auto.carpark.repository

API controllers are at package com.byelken.auto.carpark.controller

API models are at package com.byelken.auto.carpark.models

API Security configurations are at package com.byelken.auto.carpark.filter

API system has custom exception handling. You can look at package com.byelken.auto.carpark.exception

The backend has swagger-ui for listing services. The swagger-ui url: http://localhost:8080/vfapp/swagger-ui

Also you can use Test's for testing APIs. **/api/auth/** services can be tested at UserControllerTest, **/api/rest/**  serviices can be tested at GarageControllerTest.

/api/auth/login API used post and need Authorization Header. This header can create with this approach -> Base64(email+":"+md5(email + : + password))
/api/auth/signup API used post and need User json

**/api/rest/** APIs require auth-token header field. You can get auth-token with using login API

/api/rest/garage API get all cars at garage
/api/rest/garage/park API park your car with Garage Body json
/api/rest/garage/leave API leave your car at carpark with plaque path param.