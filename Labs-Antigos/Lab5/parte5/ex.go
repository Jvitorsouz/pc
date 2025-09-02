package main

import (
	"fmt"
	"math/rand"
	"time"
)

// Producer gera números aleatórios e envia para o canal
func producer(ch chan int, count int) {
	for i := 0; i < count; i++ {
		num := rand.Intn(100) // número aleatório entre 0 e 99
		fmt.Println("Produzido:", num)
		ch <- num // envia para o canal
		time.Sleep(100 * time.Millisecond)
	}
	close(ch) // fecha o canal quando terminar
}

// Consumer lê do canal e imprime apenas números pares
func consumer(ch chan int) {
	for num := range ch {
		if num%2 == 0 {
			fmt.Println("Par:", num)
		}
	}
}

func main() {
	rand.Seed(time.Now().UnixNano())

	ch := make(chan int, 5) // canal com buffer de 5 elementos

	go producer(ch, 20) // produtor gera 20 números
	consumer(ch)        // consumidor lê do canal
}
