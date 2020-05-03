---
title: "Part 3: Modelling epidemics in continuous time and using stochastic processes"
author: "Darren Wilkinson"
date: "darrenjw.github.io"
---

# Abstracting model structure from simulation approach

## The structure on an SIR model

Blah

```scala mdoc:silent
import smfsb._
import breeze.linalg._
import breeze.numerics._
```

```scala mdoc:invisible
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

```scala mdoc
val dMod = SpnModels.sir[IntState]()
val stepSIRds = Step.gillespie(dMod)
val tsSIRds = Sim.ts(DenseVector(100,5,0), 0.0, 10.0, 0.05, stepSIRds)
plotTs(tsSIRds, "Gillespie simulation of the SIR model")
```

## Plot

```scala mdoc:evilplot:SIRds.png
plotTs(tsSIRds, "Gillespie simulation of the SIR model")
```

```scala mdoc
val cMod = SpnModels.sir[DoubleState]()
val stepSIRcd = Step.euler(cMod)

```

## SEIR

```scala mdoc
val stepSEIR = Step.gillespie(SpnModels.seir[IntState]())
val tsSEIR = Sim.ts(DenseVector(100,5,0,0), 0.0, 20.0, 0.05, stepSEIR)
//Sim.plotTs(tsSEIR, "Gillespie simulation of the SEIR model")

```


