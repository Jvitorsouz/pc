package main

import (
	"fmt"
	"math/rand"
	"sync"
)

func main() {
	buffer := make(chan int, 3) // Canal com buffer de tamanho 3
	var wg sync.WaitGroup

	wg.Add(2) // vamos esperar o produtor e o consumidor

	go producer(buffer, &wg)
	go consumer(buffer, &wg)

	wg.Wait() // Espera os dois terminarem
	fmt.Println("Programa finalizado")
}

func producer(ch chan int, wg *sync.WaitGroup) {
	defer wg.Done() // sinaliza que terminou
	for i := 0; i < 10; i++ {
		x := rand.Intn(100)
		fmt.Println("Produced:", x)
		ch <- x
	}
	close(ch) // fecha o canal ao terminar
}

func consumer(ch chan int, wg *sync.WaitGroup) {
	defer wg.Done() // sinaliza que terminou
	for x := range ch { // lê até o canal fechar
		fmt.Println("Consumed:", x)
	}
}

