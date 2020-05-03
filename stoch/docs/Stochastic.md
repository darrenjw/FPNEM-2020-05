---
title: "Part 3: Modelling epidemics in continuous time and using stochastic processes"
author: "Darren Wilkinson"
date: "darrenjw.github.io"
---

# Abstracting model structure from simulation approach

## The structure of an SIR model

We will use my library, [`scala-smfsb`](https://github.com/darrenjw/scala-smfsb), associated with [*my book*](https://github.com/darrenjw/smfsb/blob/master/README.md). SIR and SEIR models are included in the library. The library uses a [*Petri net*](https://en.wikipedia.org/wiki/Petri_net) approach to separate the representation of the structure of the model from the method we use to simulate its dynamics.

```scala mdoc:silent
import smfsb._
import breeze.linalg._
import breeze.numerics._
```
```scala mdoc
val dMod = SpnModels.sir[IntState]()
```

```scala mdoc:invisible
// "Cheating" a bit here, and using EvilPlot rather than breeze-viz for plotting
// as that's easier to hook into mdoc

  import com.cibo.evilplot._
  import com.cibo.evilplot.plot._
  import com.cibo.evilplot.plot.aesthetics.DefaultTheme._
  import com.cibo.evilplot.numeric.Point
  import com.cibo.evilplot.colors.Color

  def plotTs[S: State](ts: Ts[S], title: String = "") = {
    val times = (ts map (_._1)).toArray
    val idx = 0 until ts(0)._2.toDvd.length
    val states = ts map (_._2)
    val stateArrays = idx.map(i => (states map (_.toDvd.apply(i))).toArray)
    val colors = Color.getGradientSeq(idx.length)
    val plts = idx map (i => {
      val pop = stateArrays(i)
      val points = (times zip pop).map{case (t,x) => Point(t, x)}
      LinePlot.series(points, "", colors(i))
    })
    val plt = Overlay(plts:_*)
      .xAxis()
      .yAxis()
      .frame()
      .title(title)
      .xLabel("time")
      .yLabel("Number of individuals")
      .render()
	plt
  }
```

## Simulation

We can feed a model into a simulation algorithm and get back a function (closure) for simulating from the dynamics of the process. We can then feed this function for simulating from the transition kernel of the process into a function that unfolds the dynamics into a time series of system states.

```scala mdoc
val stepSIRds = Step.gillespie(dMod)
```
```scala mdoc:silent
val tsSIRds = Sim.ts(DenseVector(100,5,0), 0.0, 10.0,
	0.05, stepSIRds)
```	
```scala
import Sim.plotTs
plotTs(tsSIRds, "Gillespie simulation of the SIR model")
```

## Plot (exact discrete stochastic dynamics)

```scala mdoc:evilplot:SIRds.png
plotTs(tsSIRds, "Gillespie simulation of the SIR model")
```


# Approximate simulation algorithms

## Approximating the discrete stochastic dynamics

The [*Gillespie algorithm*](https://en.wikipedia.org/wiki/Gillespie_algorithm) simulates every transition event explicitly. This leads to exact simulation of the underlying stochastic process, but can come at a high computational price. If necessary, we can speed up simulation by discretising time, and using the [*Poisson distribution*](https://en.wikipedia.org/wiki/Poisson_distribution) to advance the dynamics.

```scala mdoc:silent
val stepSIRdsa = Step.pts(dMod)
val tsSIRdsa = Sim.ts(DenseVector(100,5,0), 0.0, 10.0,
	0.05, stepSIRdsa)
plotTs(tsSIRdsa, "Poisson simulation of the SIR model")
```

## Plot (approximate discrete stochastic dynamics)

```scala mdoc:evilplot:SIRdsa.png
plotTs(tsSIRdsa, "Poisson simulation of the SIR model")
```


## Continuous state approximations

When dealing with very large populations and numbers of transition events, even the Poisson discretisation can become problematic. In this case, a continuous state approximation can be used which represents the process as a [*stochastic differential equation*](https://en.wikipedia.org/wiki/Stochastic_differential_equation) to be numerically integrated. For this, a continuous state instantiation of the SIR model must be used.
```scala mdoc:silent
val cMod = SpnModels.sir[DoubleState]()
val stepSIRcs = Step.cle(cMod)
val tsSIRcs = Sim.ts(DenseVector(100.0,5.0,0.0), 
    0.0, 10.0, 0.05, stepSIRcs)
plotTs(tsSIRcs, "Langevin simulation of the SIR model")
```

## Plot (continuous stochastic dynamics)

```scala mdoc:evilplot:SIRcs.png
plotTs(tsSIRcs, "Langevin simulation of the SIR model")
```


## Mass-action kinetics

If we aren't interested in stochastic effects, we can ignore the noise to get a representation of the model as a set of [*ordinary differential equations*](https://en.wikipedia.org/wiki/Ordinary_differential_equation) to be numerically integrated.
```scala mdoc:silent
val stepSIRcd = Step.euler(cMod)
val tsSIRcd = Sim.ts(DenseVector(100.0,5.0,0.0),
    0.0, 10.0, 0.05, stepSIRcd)
plotTs(tsSIRcd,
    "Deterministic simulation of the SIR model")
```

## Plot (continuous stochastic dynamics)

```scala mdoc:evilplot:SIRcd.png
plotTs(tsSIRcd, "Deterministic simulation of the SIR model")
```


## Population modelling

Let's now see how to mimic the example we looked at in part 2 using discrete time deterministic kinetics. For this we need a model with appropriate parameters.
```scala mdoc:silent
val p0 = DenseVector(1.0e7, 2.0, 0.0)
val cPop = SpnModels.sir[DoubleState](DenseVector(5.0e-8, 0.1))
val stepPopcd = Step.euler(cPop)
val tsPopcd = Sim.ts(p0, 0.0, 100.0, 0.5, stepPopcd)
plotTs(tsPopcd,
    "Deterministic simulation of the SIR model")
```
Note that there isn't an exact match with the discrete time model we considered earlier, but that they are qualitatively very similar.

## Plot (deterministic population dynamics)

```scala mdoc:evilplot:Popcd.png
plotTs(tsPopcd, "Deterministic simulation of the SIR model")
```


## Stochastic model

We can compare the ODE model with the SDE equivalent.
```scala mdoc:silent
val stepPopcs = Step.cle(cPop)
val tsPopcs = Sim.ts(p0, 0.0, 100.0, 0.5, stepPopcs)
plotTs(tsPopcs,
    "Stochastic simulation of the SIR model")
```
Note that due to the very large number of individuals involved, laws of large numbers render stochastic effects imperceptible here.

## Plot (stochastic population dynamics)

```scala mdoc:evilplot:Popcs.png
plotTs(tsPopcs,
    "Stochastic simulation of the SIR model")
```


# SEIR

## SEIR

```scala mdoc
val stepSEIR = Step.gillespie(SpnModels.seir[IntState]())
val tsSEIR = Sim.ts(DenseVector(100,5,0,0),
    0.0, 20.0, 0.05, stepSEIR)
plotTs(tsSEIR, "Gillespie simulation of the SEIR model")

```

# Spatial effects

## SEIR as a reaction diffusion process



