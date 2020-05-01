# FPNEM-2020-05

### Materials for the (online) May 2020 [FP North East Meetup](https://www.meetup.com/fpnortheast/)

## An introduction to epidemic modelling for functional programmers

*I will give a basic introduction to epidemics and how to write functional code for simulating their behaviour. Most of the examples will require only minimal maths. There will be some simple coding exercises for audience participation, and informal chat over a beer (BYO!).*

### Important disclaimer - please read!

**I am not an epidemiologist! These materials are for illustrative purposes only. They are intended to give an introduction to the techniques used by epidemiologists and epidemic modellers from a functional programmer perspective. The parameters that I use in the models are NOT chosen to accurately reflect the current pandemic, and therefore have NO predictive value in that context.**

## Outline

* The directory [basics](basics/) contains a Scala `sbt` project containing everything needed to build and run the examples and presentation. All you need is a recent JDK and [sbt](https://www.scala-sbt.org/) in order to build and run everything.
* The directory [stoch](stoch/) contains another Scala `sbt` project, but this one includes a dependency on my [scala-smfsb](https://github.com/darrenjw/scala-smfsb) library for stochastic simulation of SEIR models in continuous time.


Copyright (C) 2020 [Darren J Wilkinson](https://darrenjw.github.io/)

#### eof
