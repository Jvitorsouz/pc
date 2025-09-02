package main

import (
	"fmt"
	"time"
)

func main() {
	go alert()
	x := fibonaci(42222)
	fmt.Println(x)
}

func fibonaci(x int) int {
	if x < 2 {
		return x
	}
	return fibonaci(x-1) + fibonaci(x-2)
}

func alert() {
	for {
		fmt.Println("Calculando fibonaci")
		time.Sleep(1 * time.Second)
	}
}
