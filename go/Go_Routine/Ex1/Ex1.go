package main

import  (
	"fmt"
	"time"
)

func fale (pessoa string) {
	for i := 0; i < 3; i++ {
		fmt.Println(pessoa, ":", i)
		time.Sleep(time.Millisecond * 100)
	}
}

func main() {
	nomes := []string{"Ana", "Bia", "Carlos", "Daniel", "Joao"}
	for i:= 0; i<5; i++{
		go fale(nomes[i])
	}

	/*
	go func(msg string) {
		fmt.Println(msg)
	}("Indo...")*/

	time.Sleep(time.Second)
	fmt.Println("Fim do main")
}
