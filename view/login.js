const API_URL = "http://localhost:8080";
let token = localStorage.getItem("token");
let currentPage = 0;
let totalPages = 0;


const loginDiv = document.getElementById("loginDiv");
const orcamentosDiv = document.getElementById("orcamentosDiv");
const loginError = document.getElementById("loginError");
const loading = document.getElementById("loading");
const orcamentosContainer = document.getElementById("orcamentos");

// ===== LOGIN =====
async function login() {
  const login = document.getElementById("loginInput").value;
  const senha = document.getElementById("senhaInput").value;

  try {
    const res = await fetch(`${API_URL}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ login, senha })
    });

    if (!res.ok) throw new Error("Login inválido");

    const data = await res.json();
    localStorage.setItem("token", data.tokenJwt);
    token = data.tokenJwt;

    loginDiv.classList.add("d-none");
    orcamentosDiv.classList.remove("d-none");
    getOrcamentos();
  } catch (err) {
    loginError.textContent = err.message;
  }
}

document.getElementById("btnLogin").addEventListener("click", login);

// ===== LOGOUT =====
document.getElementById("btnLogout").addEventListener("click", () => {
  localStorage.removeItem("token");
  token = null;
  orcamentosDiv.classList.add("d-none");
  loginDiv.classList.remove("d-none");
});

// ===== BUSCAR ORÇAMENTOS =====
async function getOrcamentos(page = 0) {
  if (!token) return;

  loading.classList.remove("d-none");
  orcamentosContainer.innerHTML = "";

  try {
    const url = `${API_URL}/costs${totalPages ? `?page=${page}` : ""}`;
    const res = await fetch(url, {
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (!res.ok) throw new Error("Erro ao buscar orçamentos");

    const data = await res.json();

    // Caso o backend não envie paginação (sem totalPages)
    if (data.content) {
      totalPages = data.totalPages ?? 1;
      currentPage = data.number ?? 0;
      renderOrcamentos(data.content);
    } else if (Array.isArray(data)) {
      totalPages = 1;
      currentPage = 0;
      renderOrcamentos(data);
    }

    updatePaginationControls();
  } catch (err) {
    orcamentosContainer.innerHTML = `<p class="text-danger">Falha ao carregar orçamentos</p>`;
  } finally {
    loading.classList.add("d-none");
  }
}


// ===== RENDERIZAR ORÇAMENTOS =====
function renderOrcamentos(orcamentos) {
  orcamentosContainer.innerHTML = "";

  orcamentos.forEach(o => {
    const col = document.createElement("div");
    col.className = "col-md-4";

    col.innerHTML = `
      <div class="card">
        <div class="card-buttons">
          <button class="btn btn-sm btn-outline-primary" onclick="abrirModalEditar(${o.id}, '${o.nome}', ${o.valor}, '${o.categoria}', '${o.status}')">
            <i class="bi bi-pencil"></i>
          </button>
          <button class="btn btn-sm btn-outline-danger" onclick="confirmarExcluir(${o.id})">
            <i class="bi bi-trash"></i>
          </button>
        </div>
        <h5 class="card-title">${o.nome}</h5>
        <p class="card-text"><strong>Valor:</strong> ${o.valor ?? "—"}</p>
        <p class="card-text"><strong>Categoria:</strong> ${o.categoria ?? "—"}</p>
        <p class="card-text"><strong>Status:</strong> ${o.status ?? "—"}</p>
      </div>
    `;

    orcamentosContainer.appendChild(col);
  });
}

// ===== MODAL - CRIAR =====
function abrirModalCriar() {
  document.getElementById("orcamentoModalLabel").innerText = "Novo Orçamento";
  document.getElementById("orcamentoForm").reset();
  document.getElementById("orcamentoId").value = "";
  document.getElementById("salvarBtn").onclick = criarOrcamento;
  new bootstrap.Modal(document.getElementById("orcamentoModal")).show();
}

async function criarOrcamento() {
  const body = getFormData();
  await fetch(`${API_URL}/costs`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: JSON.stringify(body)
  });
  bootstrap.Modal.getInstance(document.getElementById("orcamentoModal")).hide();
  getOrcamentos();
}

// ===== MODAL - EDITAR =====
function abrirModalEditar(id, nome, valor, categoria, status) {
  document.getElementById("orcamentoModalLabel").innerText = "Editar Orçamento";
  document.getElementById("orcamentoId").value = id;
  document.getElementById("nome").value = nome;
  document.getElementById("valor").value = valor;
  document.getElementById("categoria").value = categoria;
  document.getElementById("status").value = status;
  document.getElementById("salvarBtn").onclick = editarOrcamento;
  new bootstrap.Modal(document.getElementById("orcamentoModal")).show();
}

async function editarOrcamento() {
  const id = document.getElementById("orcamentoId").value;
  const body = getFormData();
  body.id = id;
  await fetch(`${API_URL}/costs`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: JSON.stringify(body)
  });
  bootstrap.Modal.getInstance(document.getElementById("orcamentoModal")).hide();
  getOrcamentos();
}

// ===== EXCLUIR =====
async function confirmarExcluir(id) {
  if (confirm("Tem certeza que deseja excluir este orçamento?")) {
    await fetch(`${API_URL}/costs/${id}`, {
      method: "DELETE",
      headers: { "Authorization": "Bearer " + token }
    });
    getOrcamentos();
  }
}

// ===== FORM DATA =====
function getFormData() {
  return {
    nome: document.getElementById("nome").value,
    valor: parseFloat(document.getElementById("valor").value),
    categoria: document.getElementById("categoria").value,
    status: document.getElementById("status").value
  };
}

// ===== AUTO LOGIN SE JÁ TIVER TOKEN =====
if (token) {
  loginDiv.classList.add("d-none");
  orcamentosDiv.classList.remove("d-none");
  getOrcamentos();
}


function updatePaginationControls() {
  document.getElementById("pageInfo").innerText = `Página ${currentPage + 1} de ${totalPages}`;
  document.getElementById("prevPage").disabled = currentPage === 0;
  document.getElementById("nextPage").disabled = currentPage + 1 >= totalPages;
}

document.getElementById("prevPage").addEventListener("click", () => {
  if (currentPage > 0) getOrcamentos(currentPage - 1);
});

document.getElementById("nextPage").addEventListener("click", () => {
  if (currentPage + 1 < totalPages) getOrcamentos(currentPage + 1);
});
