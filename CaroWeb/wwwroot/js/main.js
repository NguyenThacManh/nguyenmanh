const BOARD_SIZE = 15;
const WIN_LENGTH = 5;

const app = document.getElementById('app');

app.innerHTML = `
  <main class="app-shell">
    <section class="hero card">
      <div>
        <p class="eyebrow">Dự án web mini</p>
        <h1>Cờ caro chơi trực tiếp trên trình duyệt</h1>
        <p class="hero-copy">
          Chơi 2 người trên cùng máy hoặc đấu với máy ngay trong trình duyệt.
          Mục tiêu là xếp được 5 quân liên tiếp theo hàng ngang, dọc hoặc chéo.
        </p>
      </div>
      <div class="hero-actions">
        <button id="reset-game" class="primary-btn">Chơi ván mới</button>
        <button id="undo-move" class="secondary-btn">Hoàn tác</button>
      </div>
    </section>

    <section class="layout-grid">
      <aside class="sidebar card">
        <div class="panel">
          <h2>Chế độ chơi</h2>
          <div class="mode-switch" role="radiogroup" aria-label="Chế độ chơi">
            <button class="mode-btn active" data-mode="pvp" aria-pressed="true">
              2 người chơi
            </button>
            <button class="mode-btn" data-mode="bot" aria-pressed="false">
              Đấu với máy
            </button>
          </div>
        </div>

        <div class="panel">
          <h2>Trạng thái</h2>
          <p id="status-text" class="status-text">Lượt của X</p>
          <div class="player-tags">
            <span class="player-tag x-tag">X đi trước</span>
            <span class="player-tag o-tag">O theo sau</span>
          </div>
        </div>

        <div class="panel">
          <h2>Bảng điểm</h2>
          <div class="scoreboard">
            <article>
              <span class="score-label">X thắng</span>
              <strong id="score-x">0</strong>
            </article>
            <article>
              <span class="score-label">O thắng</span>
              <strong id="score-o">0</strong>
            </article>
            <article>
              <span class="score-label">Hòa</span>
              <strong id="score-draw">0</strong>
            </article>
          </div>
        </div>

        <div class="panel">
          <h2>Luật nhanh</h2>
          <ul class="rules-list">
            <li>Bàn cờ 15x15 ô.</li>
            <li>Người thắng là người nối được 5 quân liên tiếp.</li>
            <li>Bạn có thể hoàn tác nước vừa đi.</li>
            <li>Đổi chế độ chơi bất kỳ lúc nào để bắt đầu ván mới.</li>
          </ul>
        </div>
      </aside>

      <section class="game-area card">
        <div class="project-meta panel compact-panel">
          <h2>Mở bằng Visual Studio / VS Code</h2>
          <ul class="rules-list compact-list">
            <li>Mở thư mục dự án này trực tiếp trong editor.</li>
            <li>Không cần cài thêm package ngoài để chạy local.</li>
            <li>Dùng <code>npm run dev</code> để chạy local server.</li>
            <li>Dùng <code>npm run build</code> để tạo thư mục <code>dist/</code>.</li>
          </ul>
        </div>

        <div class="board-header">
          <h2>Bàn cờ</h2>
          <p id="move-counter">Số nước đã đi: 0</p>
        </div>
        <div class="board-wrapper">
          <div
            id="board"
            class="board"
            role="grid"
            aria-label="Bàn cờ caro 15 nhân 15"
          ></div>
        </div>
      </section>
    </section>
  </main>
`;

const boardElement = document.getElementById('board');
const statusText = document.getElementById('status-text');
const moveCounter = document.getElementById('move-counter');
const resetButton = document.getElementById('reset-game');
const undoButton = document.getElementById('undo-move');
const modeButtons = document.querySelectorAll('.mode-btn');

const scoreElements = {
  X: document.getElementById('score-x'),
  O: document.getElementById('score-o'),
  draw: document.getElementById('score-draw'),
};

const scores = { X: 0, O: 0, draw: 0 };

let gameMode = 'pvp';
let board = [];
let currentPlayer = 'X';
let moves = [];
let gameOver = false;
let winningCells = [];

function createEmptyBoard() {
  return Array.from({ length: BOARD_SIZE }, () => Array(BOARD_SIZE).fill(''));
}

function renderBoard() {
  boardElement.innerHTML = '';

  for (let row = 0; row < BOARD_SIZE; row += 1) {
    for (let col = 0; col < BOARD_SIZE; col += 1) {
      const cell = document.createElement('button');
      const value = board[row][col];
      const isWinningCell = winningCells.some(([winRow, winCol]) => winRow === row && winCol === col);

      cell.className = 'cell';
      cell.type = 'button';
      cell.dataset.row = String(row);
      cell.dataset.col = String(col);
      cell.dataset.value = value;
      cell.setAttribute('role', 'gridcell');
      cell.setAttribute('aria-label', `Ô ${row + 1}, ${col + 1}${value ? ` chứa ${value}` : ''}`);

      if (value) {
        cell.classList.add('filled', value === 'X' ? 'filled-x' : 'filled-o');
      }

      if (isWinningCell) {
        cell.classList.add('winning');
      }

      cell.addEventListener('click', () => handleMove(row, col));
      boardElement.appendChild(cell);
    }
  }

  moveCounter.textContent = `Số nước đã đi: ${moves.length}`;
}

function updateStatus(message) {
  statusText.textContent = message;
}

function updateScores() {
  scoreElements.X.textContent = String(scores.X);
  scoreElements.O.textContent = String(scores.O);
  scoreElements.draw.textContent = String(scores.draw);
}

function startNewGame({ preserveScores = true } = {}) {
  board = createEmptyBoard();
  currentPlayer = 'X';
  moves = [];
  gameOver = false;
  winningCells = [];

  if (!preserveScores) {
    scores.X = 0;
    scores.O = 0;
    scores.draw = 0;
    updateScores();
  }

  renderBoard();
  updateStatus(gameMode === 'bot' ? 'Lượt của X - bạn đi trước.' : 'Lượt của X');
}

function getDirectionCount(row, col, rowStep, colStep, player) {
  const cells = [[row, col]];
  let count = 1;

  for (const direction of [-1, 1]) {
    let nextRow = row + rowStep * direction;
    let nextCol = col + colStep * direction;

    while (
      nextRow >= 0 &&
      nextRow < BOARD_SIZE &&
      nextCol >= 0 &&
      nextCol < BOARD_SIZE &&
      board[nextRow][nextCol] === player
    ) {
      count += 1;
      cells.push([nextRow, nextCol]);
      nextRow += rowStep * direction;
      nextCol += colStep * direction;
    }
  }

  return { count, cells };
}

function checkWinner(row, col, player) {
  const directions = [
    [0, 1],
    [1, 0],
    [1, 1],
    [1, -1],
  ];

  for (const [rowStep, colStep] of directions) {
    const { count, cells } = getDirectionCount(row, col, rowStep, colStep, player);
    if (count >= WIN_LENGTH) {
      return cells;
    }
  }

  return null;
}

function endGame(winner, cells = []) {
  gameOver = true;
  winningCells = cells;
  renderBoard();

  if (winner === 'draw') {
    scores.draw += 1;
    updateStatus('Ván cờ hòa! Hãy thử lại.');
  } else {
    scores[winner] += 1;
    updateStatus(`${winner} đã thắng ván này!`);
  }

  updateScores();
}

function applyMove(row, col, player) {
  board[row][col] = player;
  moves.push({ row, col, player });

  const winnerCells = checkWinner(row, col, player);
  if (winnerCells) {
    endGame(player, winnerCells);
    return true;
  }

  if (moves.length === BOARD_SIZE * BOARD_SIZE) {
    endGame('draw');
    return true;
  }

  currentPlayer = currentPlayer === 'X' ? 'O' : 'X';
  renderBoard();
  return false;
}

function candidateCells() {
  if (moves.length === 0) {
    const center = Math.floor(BOARD_SIZE / 2);
    return [[center, center]];
  }

  const set = new Set();

  moves.forEach(({ row, col }) => {
    for (let rowOffset = -1; rowOffset <= 1; rowOffset += 1) {
      for (let colOffset = -1; colOffset <= 1; colOffset += 1) {
        const nextRow = row + rowOffset;
        const nextCol = col + colOffset;
        if (
          nextRow >= 0 &&
          nextRow < BOARD_SIZE &&
          nextCol >= 0 &&
          nextCol < BOARD_SIZE &&
          !board[nextRow][nextCol]
        ) {
          set.add(`${nextRow},${nextCol}`);
        }
      }
    }
  });

  return [...set].map((item) => item.split(',').map(Number));
}

function scorePosition(row, col, player) {
  const directions = [
    [0, 1],
    [1, 0],
    [1, 1],
    [1, -1],
  ];

  let total = 0;

  for (const [rowStep, colStep] of directions) {
    const { count } = getDirectionCount(row, col, rowStep, colStep, player);
    total += count ** 2;
  }

  return total;
}

function chooseBotMove() {
  const options = candidateCells();
  const opponent = 'X';
  let bestMove = options[0];
  let bestScore = -Infinity;

  for (const [row, col] of options) {
    board[row][col] = 'O';
    if (checkWinner(row, col, 'O')) {
      board[row][col] = '';
      return [row, col];
    }
    board[row][col] = '';
  }

  for (const [row, col] of options) {
    board[row][col] = opponent;
    if (checkWinner(row, col, opponent)) {
      board[row][col] = '';
      return [row, col];
    }
    board[row][col] = '';
  }

  for (const [row, col] of options) {
    board[row][col] = 'O';
    const attackScore = scorePosition(row, col, 'O');
    board[row][col] = '';

    board[row][col] = opponent;
    const defenseScore = scorePosition(row, col, opponent);
    board[row][col] = '';

    const distanceToCenter = Math.abs(row - Math.floor(BOARD_SIZE / 2)) + Math.abs(col - Math.floor(BOARD_SIZE / 2));
    const moveScore = attackScore * 1.2 + defenseScore - distanceToCenter * 0.15;

    if (moveScore > bestScore) {
      bestScore = moveScore;
      bestMove = [row, col];
    }
  }

  return bestMove;
}

function runBotTurn() {
  if (gameOver || gameMode !== 'bot' || currentPlayer !== 'O') {
    return;
  }

  updateStatus('Máy đang tính nước đi...');

  window.setTimeout(() => {
    const [row, col] = chooseBotMove();
    const finished = applyMove(row, col, 'O');

    if (!finished) {
      updateStatus('Lượt của X - đến bạn đi.');
    }
  }, 280);
}

function handleMove(row, col) {
  if (gameOver || board[row][col]) {
    return;
  }

  if (gameMode === 'bot' && currentPlayer === 'O') {
    return;
  }

  const player = currentPlayer;
  const finished = applyMove(row, col, player);

  if (finished) {
    return;
  }

  if (gameMode === 'bot') {
    updateStatus('Máy sắp đi...');
    runBotTurn();
  } else {
    updateStatus(`Lượt của ${currentPlayer}`);
  }
}

function undoMove() {
  if (moves.length === 0) {
    return;
  }

  const steps = gameMode === 'bot' && moves.length >= 2 ? 2 : 1;
  winningCells = [];
  gameOver = false;

  for (let index = 0; index < steps; index += 1) {
    const lastMove = moves.pop();
    if (!lastMove) {
      break;
    }
    board[lastMove.row][lastMove.col] = '';
    currentPlayer = lastMove.player;
  }

  renderBoard();

  if (gameMode === 'bot') {
    updateStatus('Đã hoàn tác. Lượt của bạn (X).');
  } else {
    updateStatus(`Đã hoàn tác. Lượt của ${currentPlayer}`);
  }
}

modeButtons.forEach((button) => {
  button.addEventListener('click', () => {
    const selectedMode = button.dataset.mode;
    if (selectedMode === gameMode) {
      return;
    }

    gameMode = selectedMode;
    modeButtons.forEach((modeButton) => {
      const active = modeButton === button;
      modeButton.classList.toggle('active', active);
      modeButton.setAttribute('aria-pressed', String(active));
    });
    startNewGame();
  });
});

resetButton.addEventListener('click', () => startNewGame());
undoButton.addEventListener('click', undoMove);

updateScores();
startNewGame();
