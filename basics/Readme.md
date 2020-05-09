# Modelling basics

A quick introduction to modelling growth and epidemic processes without any calculus or probability theory.

This is a Scala `sbt` project. Doing `sbt run` will run a demo, and `sbt mdoc` will compile the [mdoc](https://scalameta.org/mdoc/) markdown files in [docs](docs/). To follow along interactively in the REPL, just run `sbt console`.

There's also a `Makefile` in here, so just typing `make` should build all of the presentation material.

* Part 1: Growth modelling - [mdoc](docs/Growth.md), [md](target/mdoc/Growth.md), [PDF slides](target/mdoc/Growth.pdf)
* Part 2: Compartmental models - [mdoc](docs/Compartmental.md), [md](target/mdoc/Compartmental.md), [PDF slides](target/mdoc/Compartmental.pdf)

### Exercises

1. Implement the logistic growth model in the functional language of your choice. Check that you can reproduce some of the examples described in Part 1.
2. Read about the [logistic map](https://en.wikipedia.org/wiki/Logistic_map), and investigate its chaotic properties.
3. [for Scala people] Implement an SEIR model, and compare the dynamics of SEIR and SIR models for comparable parameter choices.
4. [for non-Scala people] Implement the SIR model in the language of your choice, and make sure you can replicate the examples from Part 2.
5. Adapt the SIR simulation code to allow step changes to the model parameters during the course of a simulation run. Use this to simulate the effect of a three month lockdown (which brings the reproduction rate down to 0.7, say). How long before the number of infected people shoots up when the lockdown is lifted?



#### eof
