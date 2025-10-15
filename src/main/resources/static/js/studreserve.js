const form = document.getElementById("reserveForm");
const studentTable = document.querySelector("#studentTable tbody");

let reservations = JSON.parse(localStorage.getItem("studentReservations")) || [];
const availableBooks = [
  "C Programming",
  "Data Structures",
  "Algorithms",
  "Database Systems",
  "Operating Systems",
  "Machine Learning",
  "Artificial Intelligence"
];

// Load books in dropdown
const bookSelect = document.getElementById("bookTitle");
availableBooks.forEach(book => {
  let option = document.createElement("option");
  option.value = book;
  option.textContent = book;
  bookSelect.appendChild(option);
});

// Render reservations
function renderReservations() {
  studentTable.innerHTML = "";
  reservations.forEach((res, index) => {
    let today = new Date().toISOString().split("T")[0];
    let status = (today > res.returnDate) ? "Completed" : "Active";

    let row = `
      <tr>
        <td>${index + 1}</td>
        <td>${res.book}</td>
        <td>${res.pickupDate}</td>
        <td>${res.pickupTime}</td>
        <td>${res.returnDate}</td>
        <td><span class="status ${status.toLowerCase()}">${status}</span></td>
      </tr>
    `;
    studentTable.innerHTML += row;
  });
}

// Add reservation
form.addEventListener("submit", function(e) {
  e.preventDefault();

  const student = document.getElementById("studentName").value;
  const book = document.getElementById("bookTitle").value;
  const pickupDate = document.getElementById("pickupDate").value;
  const pickupTime = document.getElementById("pickupTime").value;
  const timelineDays = parseInt(document.getElementById("timelineDays").value);

  // Calculate return date
  let returnDate = new Date(pickupDate);
  returnDate.setDate(returnDate.getDate() + timelineDays);
  let returnDateStr = returnDate.toISOString().split("T")[0];

  reservations.push({ student, book, pickupDate, pickupTime, returnDate: returnDateStr });
  localStorage.setItem("studentReservations", JSON.stringify(reservations));

  form.reset();
  renderReservations();
});

renderReservations();
