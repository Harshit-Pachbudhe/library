let studentName = "Rannu"; // ⚡ Example: current logged-in student

// ---- Load Books ----
function loadStudentBooks() {
  let books = JSON.parse(localStorage.getItem("issuedBooks")) || [];
  let tbody = document.querySelector("#studentBooks tbody");
  tbody.innerHTML = "";

  books
    .filter(b => b.studentName === studentName)
    .forEach(b => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${b.bookTitle}</td>
        <td>${b.pickupDate}</td>
        <td>${b.returnDate}</td>
        <td>${b.status}</td>
      `;
      tbody.appendChild(tr);
    });
}

// ---- Load Messages (Notifications) ----
function loadMessages() {
  let notifications = JSON.parse(localStorage.getItem("notifications")) || [];
  let container = document.getElementById("messages");
  container.innerHTML = "";

  notifications
    .filter(n => n.studentName === studentName)
    .forEach(n => {
      let div = document.createElement("div");

      // Agar message me "overdue" word hai → red card
      let type = n.message.toLowerCase().includes("overdue") ? "overdue" : "info";

      div.className = `message ${type}`;
      div.textContent = `${n.message}  •  ${n.date}`;
      container.appendChild(div);
    });
}

// First load
loadStudentBooks();
loadMessages();
