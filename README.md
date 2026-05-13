<!-- Este template foi criado para servir como referência e pode ser facilmente adaptado para diferentes projetos de desenvolvimento -->

<!-- [![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=99999999&assignment_repo_type=AssignmentRepo) [![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=99999999)
-->

<a href="https://classroom.github.com/online_ide?assignment_repo_id=99999999&assignment_repo_type=AssignmentRepo"><img src="https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg" width="200"/></a> <a href="https://classroom.github.com/open-in-codespaces?assignment_repo_id=99999999"><img src="https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg" width="250"/></a>

---

# 👾 Mini-RPG 👾

---

## 📝 Sobre o Projeto

Este projeto consiste em um Mini RPG em Tempo Real desenvolvido para a disciplina de Redes de Computadores da PUC Minas. A solução implementa um sistema cliente-servidor que permite a comunicação bidirecional instantânea entre dois jogadores em sistemas distintos, utilizando o protocolo WebSocket para garantir atualizações em tempo real.
---

## Objetivo

O objetivo central é a aplicação prática de comunicação entre processos (IPC) situados em sistemas distintos. A solução utiliza o mecanismo de Sockets (via protocolo WebSocket) para garantir que as ações de um jogador sejam refletidas instantaneamente no estado de jogo do adversário.

---

## ✨ Funcionalidades Principais

- 🛡️ **Sistema de Classes:** Escolha entre 5 classes balanceadas (Mago, Guerreiro, Tank, Clérigo e Ladino), cada uma com atributos únicos de HP, Mana e Velocidade.
- ⏳ **Combate por Turnos Dinâmico:** A ordem de ataque é definida pela velocidade dos personagens.
- ⚡ **Gestão de Vigor:** Mecânica que limita ações consecutivas, exigindo estratégia no uso de habilidades.
- 🔥 **Sistema de Status:** Implementação de efeitos como Queimadura, Parry (aparar), Esquiva e Defesa.
- 🌐 **Sincronização em Tempo Real:** Atualizações instantâneas de status e log de combate para ambos os jogadores via WebSocket.

---

## 🛠 Tecnologias Utilizadas

As seguintes ferramentas, frameworks e bibliotecas foram utilizados na construção deste projeto. Recomenda-se o uso das versões listadas (ou superiores) para garantir a compatibilidade.

### 💻 Front-end

* Linguagem: JavaScript (Vanilla ES6+)
* Interface: HTML5 e CSS3
* Comunicação: WebSocket API nativa do navegador

### 🖥️ Back-end

*Linguagem/Runtime: Java 21 (JDK)
*Framework: Spring Boot 3.x
*Protocolo: WebSocket com suporte a STOMP
*Processamento de Dados: Jackson JSON (Serialização)
*Utilitários: Project Lombok

---

## 🏗 Arquitetura

O sistema segue o modelo Cliente-Servidor:

Você pode incluir:

- Servidor (Spring Boot): Centraliza a lógica de jogo, gerencia as sessões de WebSocket e valida os turnos. Ele garante a integridade dos dados e resolve os conflitos de velocidade.
- Clientes (Web): Interfaces leves que enviam as ações do jogador e renderizam o estado atualizado recebido do servidor.
- Protocolo: A troca de mensagens ocorre via JSON, permitindo uma estrutura de dados clara para HP, Mana e efeitos de status.

---

## 🔧 Instalação e Execução

### Pré-requisitos
* Java JDK 21 ou superior.
* Maven (incluído via Wrapper ./mvnw).
* Navegador moderno (Chrome, Firefox ou Edge).

---

## 📦 Passo a Passo

1. Clone o Repositório:

```
git clone https://github.com/CiscoRafael/game-websocket
cd game-websocket
```

2. Inicie o Servidor (Back-end):

```
cd backend
./mvnw spring-boot:run
```

> [!NOTE]
> 🚀 O servidor estará rodando em http://localhost:8080.


3. Acesse o Jogo (Front-end):
  Abra o arquivo `index.html` localizado na pasta do frontend em dois navegadores diferentes para simular os dois jogadores.

---

## 📂 Estrutura de Pastas

A organização do repositório separa claramente as responsabilidades do servidor e do cliente.

```
.
├── /backend                 # ☕ Aplicação Spring Boot (API & WebSocket)
│   ├── /src/main/java       # 📂 Código-fonte Java
│   │   └── /com/CiscoRafael/game
│   │       ├── /config      # 🔧 Configurações de WebSocket (STOMP/Message Broker)
│   │       ├── /controller  # 🎮 Handlers de mensagens e rotas
│   │       ├── /model       # 🧬 Classes de personagens (Mago, Guerreiro, etc.)
│   │       └── /service     # ⚙️ Lógica de batalha, turnos e cálculo de dano
│   ├── /src/main/resources  # 📂 Recursos do Spring
│   │   └── application.yml  # ⚙️ Configurações de porta e servidor
│   ├── mvnw                 # 🛠️ Maven Wrapper (Execução sem instalação local)
│   └── pom.xml              # 📦 Gerenciamento de dependências (Lombok, Jackson)
│
├── /frontend                # 📁 Interface do Jogador (Web)
│   ├── index.html           # 📄 Interface da arena de batalha e logs
│   ├── style.css            # 🎨 Estilização, animações e layout responsivo
│   └── script.js            # 🔌 Lógica de conexão WebSocket e interação de UI
│
├── /docs                    # 📚 Documentação complementar e PDF do trabalho
├── .gitignore               # 🧹 Arquivos ignorados (target, .settings, etc.)
└── README.md                # 📘 Documentação principal do projeto
```

---
### 👥 Autores

| 👤 Nome | :octocat: GitHub |
| :--- | :--- |
| **Felipe Augusto Mendes Pereira** | [Acesse o Perfil](https://github.com/Felp64) |
| **Francisco Rafael Pereira Rodrigues** | [CiscoRafael](https://github.com/CiscoRafael) |


## 🙏 Agradecimentos
Em ambiente acadêmico, citar fontes e inspirações é crucial (integridade acadêmica). Em ambiente profissional, mostra humildade e conexão com a comunidade.

Gostaria de agradecer aos seguintes canais e pessoas que foram fundamentais para o desenvolvimento deste projeto:

* [**Engenharia de Software PUC Minas**](https://www.instagram.com/engsoftwarepucminas/) - Pelo apoio institucional, estrutura acadêmica e fomento à inovação e boas práticas de engenharia.
* [**Prof. Diego Rocha**] - Pelas orientações na disciplina de Redes de Computadores.

---

## 📄 Licença

Este projeto é distribuído sob a **[Licença MIT](https://github.com/joaopauloaramuni/laboratorio-de-desenvolvimento-de-software/blob/main/LICENSE)**.

---
