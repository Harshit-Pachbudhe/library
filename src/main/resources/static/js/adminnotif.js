// ---- Save Notification ----
function addNotification(message, studentName) {
  let notifications = JSON.parse(localStorage.getItem("notifications")) || [];
  notifications.push({
    message,
    studentName,
    date: new Date().toLocaleString()
  });
  localStorage.setItem("notifications", JSON.stringify(notifications));
}

// ---- Issued Books ----
let issuedBooks = JSON.parse(localStorage.getItem("issuedBooks")) || [];

// ---- Render Books in Table ----
function renderIssuedBooks() {
  let tbody = document.querySelector("#issuedBooksTable tbody");
  tbody.innerHTML = "";

  issuedBooks.forEach((b, i) => {
    let tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${b.studentName}</td>
      <td>${b.bookTitle}</td>
      <td>${b.pickupDate}</td>
      <td>${b.returnDate}</td>
      <td>${b.status}</td>
      <td>
        ${b.status === "Issued" ? 
          `<button onclick="returnBook(${i})">Mark Returned</button>` : 
          `<span>✅ Returned</span>`}
      </td>
    `;
    tbody.appendChild(tr);
  });
}

// ---- Issue Book ----
document.getElementById("issueForm").addEventListener("submit", e => {
  e.preventDefault();
  let studentName = document.getElementById("studentName").value;
  let bookTitle = document.getElementById("bookTitle").value;
  let pickupDate = document.getElementById("pickupDate").value;
  let returnDate = document.getElementById("returnDate").value;

  issuedBooks.push({
    studentName, bookTitle, pickupDate, returnDate, status: "Issued"
  });
  localStorage.setItem("issuedBooks", JSON.stringify(issuedBooks));

  // ✅ Add notification
  addNotification(`📖 Book "${bookTitle}" issued to you. Return by ${returnDate}.`, studentName);

  renderIssuedBooks();
  e.target.reset();
});

// ---- Return Book ----
function returnBook(index) {
  issuedBooks[index].status = "Returned";
  localStorage.setItem("issuedBooks", JSON.stringify(issuedBooks));

  addNotification(`✅ Book "${issuedBooks[index].bookTitle}" has been marked as returned.`, issuedBooks[index].studentName);

  renderIssuedBooks();
}

// ---- Send Manual Notification ----
document.getElementById("notifyForm").addEventListener("submit", e => {
  e.preventDefault();
  let studentName = document.getElementById("notifyStudent").value;
  let message = document.getElementById("notifyMessage").value;

  addNotification(message, studentName);

  alert("Message sent to " + studentName);
  e.target.reset();
});

// First load
renderIssuedBooks();
addNotification("⚠️ Your book 'Data Structures' is overdue! Please return immediately.", "Rannu");
addNotification("✅ Book 'DBMS' has been successfully returned.", "Rannu");
