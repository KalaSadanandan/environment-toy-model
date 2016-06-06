# A toy model of the environment

## Overview

This is a toy model of the environment.
The source code could be run and test in both sbt console and eclipse-scala.

## Getting Started

Create a toy model of the environment (taking into account things like atmosphere, topography,
geography, oceanography, or similar) that evolves over time. Then take measurements at various
locations (ie weather stations), and then have your program emit that data, as in the following:

Station     Local      Time    Conditions Temperature Pressure Humidity

Sydney     2015-12-23 16:02:12 Rain       +12.5       1010.3    97

where temperature is in °C, pressure in hPa, and relative humidity as a %. Obviously you can't give
it to us as a table (ok, yes, you could feed us markdown, but let's not do that?) so instead submit
your data to us in the following format

SYD|-33.86,151.21,39|2015-12-23T05:02:12Z|Rain|+12.5|1004.3|97

with a three letter IATA code used as a station label.


## For Test Editors

The test is designed to be relatively realistic in a variety of ways.  
You could run the test in 2 ways:

1. sbt test
2. Eclipse-Scala


