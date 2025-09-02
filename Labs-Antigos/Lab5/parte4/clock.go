package main

import (
	"fmt"
	"io"
	"log"
	"math/rand"
	"net"
	"time"
)

// Função para gerar strings aleatórias
const charset = "abcdefghijklmnopqrstuvwxyz1234567890"

var seededRand = rand.New(rand.NewSource(time.Now().UnixNano()))

func RandString(length int) string {
	b := make([]byte, length)
	for i := range b {
		b[i] = charset[seededRand.Intn(len(charset))]
	}
	return string(b)
}

func main() {
	listener, err := net.Listen("tcp", "localhost:8000")
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("Servidor rodando em localhost:8000 ...")

	for {
		// Aceita a conexão de um cliente
		conn, err := listener.Accept()
		if err != nil {
			log.Println("Erro ao aceitar conexão:", err)
			continue
		}

		// Cada conexão é tratada em uma goroutine separada
		go handleConnection(conn)
	}
}

// handleConnection atende a conexão de um cliente
func handleConnection(c net.Conn) {
	defer c.Close()
	for i := 0; i < 5; i++ { // envia 5 mensagens por cliente
		message := fmt.Sprintf("Mensagem %d: %s\n", i+1, RandString(10))
		_, err := io.WriteString(c, message)
		if err != nil {
			log.Println("Erro ao enviar mensagem:", err)
			return
		}
		time.Sleep(1 * time.Second)
	}
}
