package main

import (
	"math/rand"
	"time"
)

func main () {
	x := exec(10)
	println("Dormiu por ", x, "ms")

}


func exec (tempMax int) int {
	x := rand.Intn(tempMax)
	time.Sleep(time.Duration(x) * time.Millisecond)
	return x

}