---
title: "Part 3: Modelling epidemics in continuous time and using stochastic processes"
author: "Darren Wilkinson"
date: "darrenjw.github.io"
---

# Abstracting model structure from simulation approach

## The structure of an SIR model

We will use my library, [`scala-smfsb`](https://github.com/darrenjw/scala-smfsb), associated with [*my book*](https://github.com/darrenjw/smfsb/blob/master/README.md). SIR and SEIR models are included in the library. The library uses a [*Petri net*](https://en.wikipedia.org/wiki/Petri_net) approach to separate the representation of the structure of the model from the method we use to simulate its dynamics.

```scala
import smfsb._
import breeze.linalg._
import breeze.numerics._
```
```scala
val dMod = SpnModels.sir[IntState]()
// dMod: Spn[IntState] = UnmarkedSpn(
//   List("S", "I", "R"),
//   1  1  0  
// 0  1  0  ,
//   0  2  0  
// 0  0  1  ,
//   smfsb.SpnModels$$$Lambda$5355/612269312@44e1dbbf
// )
```


## Simulation

We can feed a model into a simulation algorithm and get back a function (closure) for simulating from the dynamics of the process. We can then feed this function for simulating from the transition kernel of the process into a function that unfolds the dynamics into a time series of system states.

```scala
val stepSIRds = Step.gillespie(dMod)
// stepSIRds: (IntState, Time, Time) => IntState = smfsb.Step$$$Lambda$5356/772652843@1086762
```
```scala
val tsSIRds = Sim.ts(DenseVector(100,5,0), 0.0, 10.0,
	0.05, stepSIRds)
```	
```scala
import Sim.plotTs
plotTs(tsSIRds, "Gillespie simulation of the SIR model")
```

## Plot (exact discrete stochastic dynamics)

![](SIRds.png)

# Approximate simulation algorithms

## Approximating the discrete stochastic dynamics

The [*Gillespie algorithm*](https://en.wikipedia.org/wiki/Gillespie_algorithm) simulates every transition event explicitly. This leads to exact simulation of the underlying stochastic process, but can come at a high computational price. If necessary, we can speed up simulation by discretising time, and using the [*Poisson distribution*](https://en.wikipedia.org/wiki/Poisson_distribution) to advance the dynamics.

```scala
val stepSIRdsa = Step.pts(dMod)
val tsSIRdsa = Sim.ts(DenseVector(100,5,0), 0.0, 10.0,
	0.05, stepSIRdsa)
plotTs(tsSIRdsa, "Poisson simulation of the SIR model")
```

## Plot (approximate discrete stochastic dynamics)

![](SIRdsa.png)

## Continuous state approximations

When dealing with very large populations and numbers of transition events, even the Poisson discretisation can become problematic. In this case, a continuous state approximation can be used which represents the process as a [*stochastic differential equation*](https://en.wikipedia.org/wiki/Stochastic_differential_equation) to be numerically integrated. For this, a continuous state instantiation of the SIR model must be used.
```scala
val cMod = SpnModels.sir[DoubleState]()
val stepSIRcs = Step.cle(cMod)
val tsSIRcs = Sim.ts(DenseVector(100.0,5.0,0.0), 
    0.0, 10.0, 0.05, stepSIRcs)
plotTs(tsSIRcs, "Langevin simulation of the SIR model")
```

## Plot (continuous stochastic dynamics)

![](SIRcs.png)

## Mass-action kinetics

If we aren't interested in stochastic effects, we can ignore the noise to get a representation of the model as a set of [*ordinary differential equations*](https://en.wikipedia.org/wiki/Ordinary_differential_equation) to be numerically integrated.
```scala
val stepSIRcd = Step.euler(cMod)
val tsSIRcd = Sim.ts(DenseVector(100.0,5.0,0.0),
    0.0, 10.0, 0.05, stepSIRcd)
plotTs(tsSIRcd,
    "Deterministic simulation of the SIR model")
```

## Plot (continuous stochastic dynamics)

![](SIRcd.png)

## Population modelling

Let's now see how to mimic the example we looked at in part 2 using discrete time deterministic kinetics. For this we need a model with appropriate parameters.
```scala
val p0 = DenseVector(1.0e7, 2.0, 0.0)
val cPop = SpnModels.sir[DoubleState](DenseVector(5.0e-8, 0.1))
val stepPopcd = Step.euler(cPop)
val tsPopcd = Sim.ts(p0, 0.0, 100.0, 0.5, stepPopcd)
plotTs(tsPopcd,
    "Deterministic simulation of the SIR model")
```
Note that there isn't an exact match with the discrete time model we considered earlier, but that they are qualitatively very similar.

## Plot (deterministic population dynamics)

![](Popcd.png)

## Stochastic model

We can compare the ODE model with the SDE equivalent.
```scala
val stepPopcs = Step.cle(cPop)
val tsPopcs = Sim.ts(p0, 0.0, 100.0, 0.5, stepPopcs)
plotTs(tsPopcs,
    "Stochastic simulation of the SIR model")
```
Note that due to the very large number of individuals involved, laws of large numbers render stochastic effects imperceptible here.

## Plot (stochastic population dynamics)

![](Popcs.png)

# SEIR

## SEIR

```scala
val stepSEIR = Step.gillespie(SpnModels.seir[IntState]())
// stepSEIR: (IntState, Time, Time) => IntState = smfsb.Step$$$Lambda$5356/772652843@62a422cb
val tsSEIR = Sim.ts(DenseVector(100,5,0,0),
    0.0, 20.0, 0.05, stepSEIR)
// tsSEIR: Ts[IntState] = List(
//   (0.0, DenseVector(100, 5, 0, 0)),
//   (0.05, DenseVector(100, 5, 0, 0)),
//   (0.1, DenseVector(100, 5, 0, 0)),
//   (0.15000000000000002, DenseVector(100, 5, 0, 0)),
//   (0.2, DenseVector(100, 5, 0, 0)),
//   (0.25, DenseVector(100, 5, 0, 0)),
//   (0.3, DenseVector(100, 5, 0, 0)),
//   (0.35, DenseVector(100, 5, 0, 0)),
//   (0.39999999999999997, DenseVector(100, 5, 0, 0)),
//   (0.44999999999999996, DenseVector(100, 5, 0, 0)),
//   (0.49999999999999994, DenseVector(100, 5, 0, 0)),
//   (0.5499999999999999, DenseVector(100, 5, 0, 0)),
//   (0.6, DenseVector(100, 5, 0, 0)),
//   (0.65, DenseVector(100, 5, 0, 0)),
//   (0.7000000000000001, DenseVector(100, 5, 0, 0)),
//   (0.7500000000000001, DenseVector(100, 5, 0, 0)),
//   (0.8000000000000002, DenseVector(100, 5, 0, 0)),
//   (0.8500000000000002, DenseVector(100, 5, 0, 0)),
//   (0.9000000000000002, DenseVector(100, 5, 0, 0)),
//   (0.9500000000000003, DenseVector(100, 5, 0, 0)),
//   (1.0000000000000002, DenseVector(100, 5, 0, 0)),
//   (1.0500000000000003, DenseVector(100, 5, 0, 0)),
//   (1.1000000000000003, DenseVector(100, 5, 0, 0)),
//   (1.1500000000000004, DenseVector(100, 5, 0, 0)),
//   (1.2000000000000004, DenseVector(100, 5, 0, 0)),
//   (1.2500000000000004, DenseVector(100, 5, 0, 0)),
//   (1.3000000000000005, DenseVector(100, 5, 0, 0)),
//   (1.3500000000000005, DenseVector(100, 5, 0, 0)),
//   (1.4000000000000006, DenseVector(100, 5, 0, 0)),
//   (1.4500000000000006, DenseVector(100, 5, 0, 0)),
//   (1.5000000000000007, DenseVector(100, 5, 0, 0)),
//   (1.5500000000000007, DenseVector(100, 4, 1, 0)),
//   (1.6000000000000008, DenseVector(99, 5, 1, 0)),
//   (1.6500000000000008, DenseVector(98, 6, 1, 0)),
//   (1.7000000000000008, DenseVector(97, 7, 1, 0)),
//   (1.7500000000000009, DenseVector(97, 7, 1, 0)),
//   (1.800000000000001, DenseVector(96, 8, 1, 0)),
//   (1.850000000000001, DenseVector(96, 8, 1, 0)),
//   (1.900000000000001, DenseVector(95, 9, 1, 0)),
//   (1.950000000000001, DenseVector(95, 9, 1, 0)),
//   (2.000000000000001, DenseVector(94, 10, 1, 0)),
//   (2.0500000000000007, DenseVector(94, 10, 1, 0)),
//   (2.1000000000000005, DenseVector(93, 11, 1, 0)),
//   (2.1500000000000004, DenseVector(93, 11, 1, 0)),
//   (2.2, DenseVector(93, 11, 1, 0)),
//   (2.25, DenseVector(93, 11, 1, 0)),
//   (2.3, DenseVector(92, 12, 1, 0)),
//   (2.3499999999999996, DenseVector(91, 13, 1, 0)),
// ...
plotTs(tsSEIR, "Gillespie simulation of the SEIR model")
// res11: geometry.Drawable = Group(
//   Vector(
//     Translate(
//       StrokeStyle(
//         Group(
//           Vector(
//             Translate(Rotate(Line(516.0, 1.0), 90.0), -0.5000000000000158, 0.0),
//             Translate(Line(736.2783203125, 1.0), 0.0, 515.5)
//           )
//         ),
//         HSLA(0.0, 0.0, 12.0, 1.0)
//       ),
//       63.72167968750004,
//       33.0
//     ),
//     Translate(
//       Group(
//         Vector(
//           Resize(
//             LineDash(
//               StrokeStyle(
//                 Translate(
//                   Path(
//                     Vector(
//                       Point(0.0, 0.0),
//                       Point(1.4725566406250001, 0.0),
//                       Point(2.9451132812500003, 0.0),
//                       Point(4.417669921875, 0.0),
//                       Point(5.8902265625000005, 0.0),
//                       Point(7.362783203125, 0.0),
//                       Point(8.835339843749999, 0.0),
//                       Point(10.307896484374998, 0.0),
//                       Point(11.780453125, 0.0),
//                       Point(13.253009765624999, 0.0),
//                       Point(14.725566406249998, 0.0),
//                       Point(16.198123046874997, 0.0),
//                       Point(17.670679687499998, 0.0),
//                       Point(19.143236328125, 0.0),
//                       Point(20.61579296875, 0.0),
//                       Point(22.088349609375, 0.0),
//                       Point(23.560906250000002, 0.0),
//                       Point(25.033462890625007, 0.0),
//                       Point(26.506019531250008, 0.0),
//                       Point(27.97857617187501, 0.0),
//                       Point(29.451132812500006, 0.0),
//                       Point(30.923689453125007, 0.0),
//                       Point(32.39624609375001, 0.0),
//                       Point(33.86880273437501, 0.0),
//                       Point(35.34135937500001, 0.0),
// ...
```

# Spatial effects

## SEIR as a reaction diffusion process



