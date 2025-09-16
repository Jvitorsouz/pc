package main

import (
	"fmt"
	"math/rand"
	"time"
)

const (
	numProdutores  = 3
	numConsumidores = 2
	bufferSize     = 5
	numItens       = 10
)

// Produtor gera números aleatórios e envia para o canal
func produtor(id int, ch chan<- int) {
	for i := 0; i < numItens; i++ {
		item := rand.Intn(100)
		select {
		case ch <- item:
			fmt.Printf("Produtor %d produziu: %d\n", id, item)
		case <-time.After(time.Second): // evita travamento se canal cheio
			fmt.Printf("Produtor %d esperou, canal cheio\n", id)
		}
		time.Sleep(time.Millisecond * time.Duration(rand.Intn(500)))
	}
	fmt.Printf("Produtor %d terminou\n", id)
}

// Consumidor lê itens do canal
func consumidor(id int, ch <-chan int, done chan<- bool) {
	for {
		select {
		case item, ok := <-ch:
			if !ok {
				fmt.Printf("Consumidor %d: canal fechado, encerrando\n", id)
				done <- true
				return
			}
			fmt.Printf("Consumidor %d consumiu: %d\n", id, item)
			time.Sleep(time.Millisecond * time.Duration(rand.Intn(800)))
		case <-time.After(time.Second * 2): // evita bloqueio indefinido
			fmt.Printf("Consumidor %d: timeout, nada para consumir\n", id)
		}
	}
}

func main() {
	rand.Seed(time.Now().UnixNano())

	canal := make(chan int, bufferSize)
	done := make(chan bool)

	// Inicia produtores
	for i := 1; i <= numProdutores; i++ {
		go produtor(i, canal)
	}

	// Inicia consumidores
	for i := 1; i <= numConsumidores; i++ {
		go consumidor(i, canal, done)
	}

	// Espera produtores terminarem (simples aproximação)
	time.Sleep(time.Second * 6)
	close(canal) // fecha canal quando não houver mais produção

	// Espera consumidores terminarem
	for i := 0; i < numConsumidores; i++ {
		<-done
	}

	fmt.Println("Programa encerrado")
}
