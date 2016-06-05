# Environment toy model programming test

## Overview

This is a simple programming test where the participant creates a toy model of the environment.

## Getting Started

Create a toy model of the environment (taking into account things like atmosphere, topography,
geography, oceanography, or similar) that evolves over time. Then take measurements at various
locations (ie weather stations), and then have your program emit that data, as in the following:

Station Local Time Conditions Temperature Pressure Humidity
Sydney 2015-12-23 16:02:12 Rain +12.5 1010.3 97
Melbourne 2015-12-25 02:30:55 Snow -5.3 998.4 55
Adelaide 2016-01-04 23:05:37 Sunny +39.4 1114.1 12

where temperature is in °C, pressure in hPa, and relative humidity as a %. Obviously you can't give
it to us as a table (ok, yes, you could feed us markdown, but let's not do that?) so instead submit
your data to us in the following format

SYD|-33.86,151.21,39|2015-12-23T05:02:12Z|Rain|+12.5|1004.3|97
MEL|-37.83,144.98,7|2015-12-24T15:30:55Z|Snow|-5.3|998.4|55
ADL|-34.92,138.62,48|2016-01-03T12:35:37Z|Sunny|+39.4|1114.1|12
with a three letter IATA code used as a station label.


## For Test Editors

The test is designed to be relatively realistic in a variety of ways.  
The specification is not as concise as it could be and not all of the requirements are covered directly in the requirements/description comment.  
This is deliberate - we want the participant to browse and understand the related code in order to be able to proceed effectively.


