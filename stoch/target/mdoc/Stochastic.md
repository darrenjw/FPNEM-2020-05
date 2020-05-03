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
//   smfsb.SpnModels$$$Lambda$6775/2093194212@44a0b56e
// )
```


## Simulation

We can feed a model into a simulation algorithm and get back a function for simulating from the dynamics of the process. We can then feed this function for simulating from the transition kernel of the process into a function that unfolds the dynamics into a time series of values.

```scala
val stepSIRds = Step.gillespie(dMod)
// stepSIRds: (IntState, Time, Time) => IntState = smfsb.Step$$$Lambda$6776/466719227@222cf2ee
val tsSIRds = Sim.ts(DenseVector(100,5,0), 0.0, 10.0,
	0.05, stepSIRds)
// tsSIRds: Ts[IntState] = List(
//   (0.0, DenseVector(100, 5, 0)),
//   (0.05, DenseVector(97, 8, 0)),
//   (0.1, DenseVector(92, 13, 0)),
//   (0.15000000000000002, DenseVector(80, 24, 1)),
//   (0.2, DenseVector(66, 37, 2)),
//   (0.25, DenseVector(49, 53, 3)),
//   (0.3, DenseVector(41, 59, 5)),
//   (0.35, DenseVector(30, 67, 8)),
//   (0.39999999999999997, DenseVector(21, 73, 11)),
//   (0.44999999999999996, DenseVector(13, 77, 15)),
//   (0.49999999999999994, DenseVector(10, 77, 18)),
//   (0.5499999999999999, DenseVector(6, 79, 20)),
//   (0.6, DenseVector(4, 78, 23)),
//   (0.65, DenseVector(3, 78, 24)),
//   (0.7000000000000001, DenseVector(2, 78, 25)),
//   (0.7500000000000001, DenseVector(2, 76, 27)),
//   (0.8000000000000002, DenseVector(2, 74, 29)),
//   (0.8500000000000002, DenseVector(1, 74, 30)),
//   (0.9000000000000002, DenseVector(1, 71, 33)),
//   (0.9500000000000003, DenseVector(0, 71, 34)),
//   (1.0000000000000002, DenseVector(0, 68, 37)),
//   (1.0500000000000003, DenseVector(0, 66, 39)),
//   (1.1000000000000003, DenseVector(0, 64, 41)),
//   (1.1500000000000004, DenseVector(0, 63, 42)),
//   (1.2000000000000004, DenseVector(0, 61, 44)),
//   (1.2500000000000004, DenseVector(0, 60, 45)),
//   (1.3000000000000005, DenseVector(0, 58, 47)),
//   (1.3500000000000005, DenseVector(0, 58, 47)),
//   (1.4000000000000006, DenseVector(0, 58, 47)),
//   (1.4500000000000006, DenseVector(0, 58, 47)),
//   (1.5000000000000007, DenseVector(0, 58, 47)),
//   (1.5500000000000007, DenseVector(0, 55, 50)),
//   (1.6000000000000008, DenseVector(0, 54, 51)),
//   (1.6500000000000008, DenseVector(0, 53, 52)),
//   (1.7000000000000008, DenseVector(0, 50, 55)),
//   (1.7500000000000009, DenseVector(0, 49, 56)),
//   (1.800000000000001, DenseVector(0, 48, 57)),
//   (1.850000000000001, DenseVector(0, 47, 58)),
//   (1.900000000000001, DenseVector(0, 46, 59)),
//   (1.950000000000001, DenseVector(0, 46, 59)),
//   (2.000000000000001, DenseVector(0, 45, 60)),
//   (2.0500000000000007, DenseVector(0, 45, 60)),
//   (2.1000000000000005, DenseVector(0, 45, 60)),
//   (2.1500000000000004, DenseVector(0, 43, 62)),
//   (2.2, DenseVector(0, 42, 63)),
//   (2.25, DenseVector(0, 42, 63)),
//   (2.3, DenseVector(0, 41, 64)),
//   (2.3499999999999996, DenseVector(0, 41, 64)),
// ...
plotTs(tsSIRds, "Gillespie simulation of the SIR model")
// res0: geometry.Drawable = Group(
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
//                       Point(3.067826334635417, 12.900000000000034),
//                       Point(6.135652669270834, 34.400000000000034),
//                       Point(9.203479003906251, 86.0),
//                       Point(12.271305338541667, 146.2),
//                       Point(15.339131673177084, 219.3),
//                       Point(18.4069580078125, 253.70000000000005),
//                       Point(21.474784342447915, 301.0),
//                       Point(24.54261067708333, 339.7),
//                       Point(27.610437011718748, 374.1),
//                       Point(30.678263346354164, 387.0),
//                       Point(33.74608968098958, 404.2),
//                       Point(36.813916015625, 412.8),
//                       Point(39.88174235026042, 417.1),
//                       Point(42.94956868489584, 421.4),
//                       Point(46.01739501953126, 421.4),
//                       Point(49.08522135416668, 421.4),
//                       Point(52.1530476888021, 425.7),
//                       Point(55.22087402343752, 425.7),
//                       Point(58.28870035807294, 430.0),
//                       Point(61.35652669270835, 430.0),
//                       Point(64.42435302734377, 430.0),
//                       Point(67.49217936197918, 430.0),
//                       Point(70.56000569661461, 430.0),
//                       Point(73.62783203125002, 430.0),
// ...
```

## Plot

![](SIRds.png)

## Approximating the discrete stochastic dynamics

The [*Gillespie algorithm*](https://en.wikipedia.org/wiki/Gillespie_algorithm) simulates every transition event explicitly. This leads to exact simulation of the underlying stochastic process, but can come at a high computational price. 

```scala
val cMod = SpnModels.sir[DoubleState]()
// cMod: Spn[DoubleState] = UnmarkedSpn(
//   List("S", "I", "R"),
//   1  1  0  
// 0  1  0  ,
//   0  2  0  
// 0  0  1  ,
//   smfsb.SpnModels$$$Lambda$6775/2093194212@2c8f1e77
// )
val stepSIRcd = Step.euler(cMod)
// stepSIRcd: (DoubleState, Time, Time) => DoubleState = smfsb.Step$$$Lambda$6989/503544831@77363dd5
```

# SEIR

## SEIR

```scala
val stepSEIR = Step.gillespie(SpnModels.seir[IntState]())
// stepSEIR: (IntState, Time, Time) => IntState = smfsb.Step$$$Lambda$6776/466719227@49070cc6
val tsSEIR = Sim.ts(DenseVector(100,5,0,0), 0.0, 20.0, 0.05, stepSEIR)
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
//   (0.9500000000000003, DenseVector(100, 4, 1, 0)),
//   (1.0000000000000002, DenseVector(100, 4, 1, 0)),
//   (1.0500000000000003, DenseVector(100, 4, 1, 0)),
//   (1.1000000000000003, DenseVector(99, 5, 1, 0)),
//   (1.1500000000000004, DenseVector(99, 5, 1, 0)),
//   (1.2000000000000004, DenseVector(98, 6, 1, 0)),
//   (1.2500000000000004, DenseVector(97, 7, 1, 0)),
//   (1.3000000000000005, DenseVector(96, 8, 1, 0)),
//   (1.3500000000000005, DenseVector(96, 8, 1, 0)),
//   (1.4000000000000006, DenseVector(96, 8, 1, 0)),
//   (1.4500000000000006, DenseVector(96, 8, 1, 0)),
//   (1.5000000000000007, DenseVector(95, 9, 1, 0)),
//   (1.5500000000000007, DenseVector(94, 10, 1, 0)),
//   (1.6000000000000008, DenseVector(94, 10, 1, 0)),
//   (1.6500000000000008, DenseVector(93, 11, 1, 0)),
//   (1.7000000000000008, DenseVector(90, 14, 1, 0)),
//   (1.7500000000000009, DenseVector(90, 14, 1, 0)),
//   (1.800000000000001, DenseVector(90, 14, 1, 0)),
//   (1.850000000000001, DenseVector(89, 15, 1, 0)),
//   (1.900000000000001, DenseVector(88, 15, 2, 0)),
//   (1.950000000000001, DenseVector(84, 19, 2, 0)),
//   (2.000000000000001, DenseVector(83, 20, 2, 0)),
//   (2.0500000000000007, DenseVector(83, 20, 2, 0)),
//   (2.1000000000000005, DenseVector(83, 20, 2, 0)),
//   (2.1500000000000004, DenseVector(81, 22, 2, 0)),
//   (2.2, DenseVector(81, 21, 3, 0)),
//   (2.25, DenseVector(78, 24, 3, 0)),
//   (2.3, DenseVector(77, 25, 3, 0)),
//   (2.3499999999999996, DenseVector(74, 28, 3, 0)),
// ...
```

# Spatial effects

## SEIR as a reaction diffusion process



