package main

import (
	"fmt"
	"io"
	"log"
	"math/rand"
	"net"
	"time"
	"unicode"
)

// Charset para gerar strings aleatórias
const charset = "abcdefghijklmnopqrstuvwxyz1234567890"

var seededRand = rand.New(rand.NewSource(time.Now().UnixNano()))

// Gera uma string aleatória usando charset
func StringWithCharset(length int, charset string) string {
	b := make([]byte, length)
	for i := range b {
		b[i] = charset[seededRand.Intn(len(charset))]
	}
	return string(b)
}

// Função prática para gerar string aleatória
func RandString(length int) string {
	return StringWithCharset(length, charset)
}

// Checa se a string contém apenas letras
func isLetter(s string) bool {
	for _, r := range s {
		if !unicode.IsLetter(r) {
			return false
		}
	}
	return true
}

func main() {
	listener, err := net.Listen("tcp", "localhost:8000")
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("Servidor rodando em localhost:8000 ...")

	for {
		conn, err := listener.Accept()
		if err != nil {
			log.Println("Erro ao aceitar conexão:", err)
			continue
		}
		// Cada cliente é atendido concorrente em uma goroutine
		go handleConnection(conn)
	}
}

// handleConnection envia strings aleatórias para o cliente
func handleConnection(c net.Conn) {
	defer c.Close()
	for i := 0; i < 10; i++ { // envia 10 mensagens
		msg := fmt.Sprintf("Mensagem %d: %s\n", i+1, RandString(8))
		_, err := io.WriteString(c, msg)
		if err != nil {
			log.Println("Erro ao enviar mensagem:", err)
			return
		}
		time.Sleep(1 * time.Second)
	}
}
