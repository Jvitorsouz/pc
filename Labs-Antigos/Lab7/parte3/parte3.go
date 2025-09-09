package main

import (
	"fmt"
	"math/rand"
	"time"
)

func main () {
	rand.Seed(time.Now().UnixNano())
	ch := aux(100)
	ch2 := aux(100)
	totalSum := 0


	for i:=0; i<1000; i++ {
		x := <- ch
		y := <- ch2
		totalSum += x + y
	}

	fmt.Println("Soma total: ", totalSum)
}

func aux (max_sleep_ms int) chan int {
	 
	ch := make(chan int)
	go func () {
		for i := 0; i<1000; i++ {
			ch <- exec(max_sleep_ms)
		}
		close(ch)
	}()

	return ch
}

func exec (tempMax int) int {
	x := rand.Intn(tempMax)
	time.Sleep(time.Duration(x) * time.Millisecond)
	return x

}