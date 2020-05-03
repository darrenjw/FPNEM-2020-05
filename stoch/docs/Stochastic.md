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

We can feed a model into a simulation algorithm and get back a function for simulating from the dynamics of the process. We can then feed this function for simulating from the transition kernel of the process into a function that unfolds the dynamics into a time series of values.

```scala mdoc
val stepSIRds = Step.gillespie(dMod)
val tsSIRds = Sim.ts(DenseVector(100,5,0), 0.0, 10.0,
	0.05, stepSIRds)
plotTs(tsSIRds, "Gillespie simulation of the SIR model")
```

## Plot

```scala mdoc:evilplot:SIRds.png
plotTs(tsSIRds, "Gillespie simulation of the SIR model")
```


## Approximating the discrete stochastic dynamics

The [*Gillespie algorithm*](https://en.wikipedia.org/wiki/Gillespie_algorithm) simulates every transition event explicitly. This leads to exact simulation of the underlying stochastic process, but can come at a high computational price. 

```scala mdoc
val cMod = SpnModels.sir[DoubleState]()
val stepSIRcd = Step.euler(cMod)

```

# SEIR

## SEIR

```scala mdoc
val stepSEIR = Step.gillespie(SpnModels.seir[IntState]())
val tsSEIR = Sim.ts(DenseVector(100,5,0,0), 0.0, 20.0, 0.05, stepSEIR)
//Sim.plotTs(tsSEIR, "Gillespie simulation of the SEIR model")

```

# Spatial effects

## SEIR as a reaction diffusion process



