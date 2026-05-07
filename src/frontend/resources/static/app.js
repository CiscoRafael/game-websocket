const connectionStatus = document.getElementById("connectionStatus");
const playerStatus = document.getElementById("playerStatus");
const nameInput = document.getElementById("nameInput");
const classSelect = document.getElementById("classSelect");
const connectButton = document.getElementById("connectButton");
const resetButton = document.getElementById("resetButton");
const clearMessagesButton = document.getElementById("clearMessages");
const messages = document.getElementById("messages");
const actionButtons = Array.from(document.querySelectorAll(".battle-action"));

let socket = null;
let jogadorAtual = localStorage.getItem("miniRpgJogador") || "";
let classeAtual = localStorage.getItem("miniRpgClasse") || "MAGO";

nameInput.value = jogadorAtual;
classSelect.value = classeAtual;

function getSocketUrl() {
  const protocol = window.location.protocol === "https:" ? "wss:" : "ws:";
  return `${protocol}//${window.location.host}/ws`;
}

function timestamp() {
  return new Date().toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit", second: "2-digit" });
}

function addMessage(text, kind = "system") {
  const item = document.createElement("div");
  item.className = `message ${kind}`;
  item.textContent = `[${timestamp()}] ${text}`;
  messages.appendChild(item);
  messages.scrollTop = messages.scrollHeight;
}

function setConnectionState(text, kind = "system") {
  connectionStatus.textContent = text;
  connectionStatus.style.color = kind === "success" ? "var(--success)" : kind === "error" ? "var(--danger)" : "var(--text)";
}

function setBattleEnabled(enabled) {
  actionButtons.forEach((button) => {
    button.disabled = !enabled;
  });
}

function sendJson(payload) {
  if (!socket || socket.readyState !== WebSocket.OPEN) {
    addMessage("Conecte-se antes de enviar ações.", "warning");
    return;
  }

  socket.send(JSON.stringify(payload));
}

function connect() {
  const nome = nameInput.value.trim();
  const tipo = classSelect.value;

  if (!nome) {
    addMessage("Digite um nome para entrar na arena.", "warning");
    nameInput.focus();
    return;
  }

  jogadorAtual = nome;
  classeAtual = tipo;
  localStorage.setItem("miniRpgJogador", jogadorAtual);
  localStorage.setItem("miniRpgClasse", classeAtual);

  if (socket) {
    socket.close();
  }

  socket = new WebSocket(getSocketUrl());
  setConnectionState("Conectando...", "system");
  addMessage(`Conectando como ${jogadorAtual}, classe ${classeAtual}.`, "system");

  socket.addEventListener("open", () => {
    setConnectionState("Conectado", "success");
    playerStatus.textContent = `${jogadorAtual} · ${classeAtual}`;
    setBattleEnabled(true);
    sendJson({ nome: jogadorAtual, tipo: classeAtual });
    addMessage("Cadastro enviado. Aguarde o oponente.", "success");
  });

  socket.addEventListener("message", (event) => {
    addMessage(event.data, "system");
  });

  socket.addEventListener("close", () => {
    setConnectionState("Desconectado", "error");
    setBattleEnabled(false);
    addMessage("Conexão encerrada.", "warning");
  });

  socket.addEventListener("error", () => {
    setConnectionState("Erro na conexão", "error");
    addMessage("Não foi possível conectar ao WebSocket.", "error");
  });
}

connectButton.addEventListener("click", connect);

nameInput.addEventListener("keydown", (event) => {
  if (event.key === "Enter") {
    connect();
  }
});

classSelect.addEventListener("change", () => {
  classeAtual = classSelect.value;
  localStorage.setItem("miniRpgClasse", classeAtual);
});

actionButtons.forEach((button) => {
  button.addEventListener("click", () => {
    if (!jogadorAtual) {
      addMessage("Registre seu nome antes de agir.", "warning");
      return;
    }

    sendJson({ jogador: jogadorAtual, acao: button.dataset.action });
    addMessage(`Você escolheu ${button.textContent.toLowerCase()}.`, "system");
  });
});

resetButton.addEventListener("click", () => {
  if (socket) {
    socket.close();
    socket = null;
  }

  jogadorAtual = "";
  classeAtual = "MAGO";
  localStorage.removeItem("miniRpgJogador");
  localStorage.removeItem("miniRpgClasse");
  nameInput.value = "";
  classSelect.value = "MAGO";
  playerStatus.textContent = "Nenhum jogador registrado";
  setConnectionState("Desconectado", "error");
  setBattleEnabled(false);
  addMessage("Jogador redefinido. Você pode entrar com outro nome.", "warning");
});

clearMessagesButton.addEventListener("click", () => {
  messages.innerHTML = "";
});

setBattleEnabled(false);
setConnectionState("Desconectado", "error");

if (jogadorAtual) {
  addMessage(`Nome salvo encontrado: ${jogadorAtual}. Clique em conectar para entrar novamente.`, "system");
}

let socket = null;
let meuNome = "";

const connectButton = document.getElementById('connectButton');
const panelRegistro = document.getElementById('panel-registro');
const panelCombate = document.getElementById('panel-combate');
const messagesDiv = document.getElementById('messages');

connectButton.addEventListener('click', () => {
    const nome = document.getElementById('nameInput').value;
    const classe = document.getElementById('classSelect').value;

    if (!nome) return alert("Digite seu nome!");

    meuNome = nome;
    socket = new WebSocket('ws://10.34.126.101:8080/ws');

    socket.onopen = () => {
        const payload = { nome: nome, tipo: classe };
        socket.send(JSON.stringify(payload));
        document.getElementById('connectionStatus').innerText = "Conectado";
        document.getElementById('connectionStatus').style.color = "#10b981";
    };

    socket.onmessage = (event) => {
        const msg = event.data;

        // ESTADO: Aguardando Oponente
        if (msg.includes("Aguardando oponente")) {
            panelRegistro.innerHTML = `
                <div class="text-center py-10">
                    <h2 class="text-amber-500 font-black text-2xl animate-pulse">BUSCANDO OPONENTE...</h2>
                    <p class="text-zinc-400 mt-2">Você entrou na fila como ${meuNome}.</p>
                </div>
            `;
            return;
        }

        // ESTADO: Início da Batalha (O Redirecionamento Visual)
        if (msg === "COMMAND_MOVE_TO_ARENA" || msg.includes("Batalha iniciada")) {
            // Remove o painel de registro
            panelRegistro.classList.add('hidden'); 
            
            // Ativa o painel de combate (remove opacidade e bloqueio)
            panelCombate.classList.remove('opacity-50', 'pointer-events-none');
            
            document.getElementById('playerStatus').innerText = `Guerreiro: ${meuNome}`;
            adicionarMensagem("🔥 A arena foi selada! O combate começou.");
            return;
        }

        // Logs normais de dano/ações
        adicionarMensagem(msg);
    };
});

// Envio de Ações
document.querySelectorAll('.battle-action').forEach(button => {
    button.addEventListener('click', () => {
        const acao = button.getAttribute('data-action');
        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.send(JSON.stringify({ acao: acao }));
            adicionarMensagem(`Você escolheu: ${acao}. Aguardando oponente...`);
        }
    });
});

function adicionarMensagem(texto) {
    const p = document.createElement('p');
    p.textContent = `> ${texto}`;
    messagesDiv.appendChild(p);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}