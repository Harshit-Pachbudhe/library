const db = {
  get(k) { 
    try { return JSON.parse(localStorage.getItem(k)) } 
    catch(e) { return [] } 
  }
};

document.getElementById("viewBooksBtn").addEventListener("click", loadMyBooks);

function loadMyBooks() {
  const name = document.getElementById("studentName").value.trim();
  if (!name) { 
    alert("Please enter your name"); 
    return; 
  }

  const allBooks = db.get("issuedBooks") || [];
  const myBooks = allBooks.filter(b => b.studentName.toLowerCase() === name.toLowerCase());

  // Separate Issued & Returned
  const issuedBooks = myBooks.filter(b => b.status === "Issued");
  const returnedBooks = myBooks.filter(b => b.status === "Returned");

  populateTable("issuedTable", issuedBooks);
  populateTable("returnedTable", returnedBooks);
}

function populateTable(tableId, books) {
  const tbody = document.getElementById(tableId).querySelector("tbody");
  tbody.innerHTML = "";

  if (books.length > 0) {
    books.forEach(book => {
      tbody.innerHTML += `<tr>
        <td>${book.bookTitle}</td>
        <td>${book.pickupDate}</td>
        <td>${book.returnDate}</td>
      </tr>`;
    });
  } else {
    tbody.innerHTML = `<tr><td colspan="3">No records found.</td></tr>`;
  }
}

document.querySelector('.refresh-btn').addEventListener('click', () => {
    alert("Page refreshed!");
});

document.querySelector('.notif-btn').addEventListener('click', () => {
    alert("No new notifications.");
});

