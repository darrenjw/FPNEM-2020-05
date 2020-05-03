# Continuous time and stochastic modelling of epidemic processes

Some more advanced modelling of epidemics, but skipping over all of the maths.

Again, it's a Scala `sbt` project. `sbt run` will run a demo, `sbt mdoc` will compile the docs, `sbt console` will give a REPL to follow along interactively.

There's a `Makefile`, and `make` will build all of the presentation material.

* Part 3: Modelling epidemics in continuous time and using stochastic processes - [mdoc](docs/Stochastic.md), [md](target/mdoc/Stochastic.md), [PDF slides](target/mdoc/Stochastic.pdf)

For anyone who is interested in the underpinning maths, [read my book!](https://github.com/darrenjw/smfsb/blob/master/README.md)

### Exercises

1. Read my blog post on [stochastic reaction-diffusion modelling](https://darrenjw.wordpress.com/2019/01/22/stochastic-reaction-diffusion-modelling/) and run the spatial SIR example.
2. Simulate a spatial SEIR model, using both stochastic and deterministic algorithms.
3. A 100x100 grid contains 10^4 cells, so if each contains 10^3 susceptibles, there will be 10^7 susceptibles in total. Run a spatial version of the SIR model from Part 3 with comparable parameters and relatively slow diffusion. Monitor the total counts over the grid, and compare the overall behaviour of the spatial simulation with the well-mixed case.


#### eof
