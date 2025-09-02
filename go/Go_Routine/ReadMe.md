# Explicação

- Go foi projetada com a concorrência em seu núcleo
- A principal ferramenta para isso é GoRoutine

# Go Routine

-  É uma thread leve gerenciada pelo runtime do Go.
- Consome pouca memória

- time.Sleep(time.Second) => É necessário colocar no programas pois é responsável por não fazer o programa terminar antes da função main execultar as gorotuines.
- A função main é executada em sua propria Goroutine 







## Escopo do Projeto

- **Objetivo:**  
  Explorar e exemplificar o funcionamento das goroutines em Go.

- **Funcionalidades:**
  - Execução concorrente de funções usando goroutines.
  - Comunicação entre goroutines via channels.
  - Exemplos de sincronização e controle de concorrência.

- **Pré-requisitos:**
  - Go instalado (versão 1.18 ou superior)
  - Ambiente Linux

- **Como executar:**
  ```bash
  go run main.go
  ```

- **Estrutura dos arquivos:**
  - `main.go`: Código principal com exemplos de goroutines.
  - `ReadMe.md`: Documentação do projeto.

## Referências

-