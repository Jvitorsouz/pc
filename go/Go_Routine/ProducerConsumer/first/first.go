package main

import (
	"fmt"
	"math/rand"
)


func main() {

	j := make(chan int)

	buffer := make(chan int, 1)
	go producer(buffer)
	go consumer(buffer, j)
	<- j

	//time.Sleep(2 * time.Millisecond) 
}


func producer(inch chan int){
	for i := 0; i < 10; i++ {
		x := rand.Intn(10)
		fmt.Println("Produced:", x)
		inch <- x
	}
	close(inch)
}

func consumer(outch chan int, donech chan int) {
	for x := range outch {
		fmt.Println("Consumed:", x)
	}
	donech <- 0
}