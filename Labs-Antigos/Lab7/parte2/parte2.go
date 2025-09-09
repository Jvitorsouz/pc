package main

import (
	"fmt"
	"math/rand"
	"time"
)

func main () {
	ch := aux(100)

	for x := range ch {
		fmt.Println("Dormiu por", x, "ms")
	}

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