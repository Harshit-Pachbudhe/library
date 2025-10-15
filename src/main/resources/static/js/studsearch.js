// ✅ Load books saved by Admin in localStorage
let books = JSON.parse(localStorage.getItem("libraryBooks")) || [];

// Elements
const searchInput = document.getElementById("searchInput");
const streamFilter = document.getElementById("streamFilter");
const categoryFilter = document.getElementById("categoryFilter");
const bookList = document.getElementById("bookList");

// Render Books Function
function renderBooks() {
  let search = searchInput.value.toLowerCase();
  let stream = streamFilter.value;
  let category = categoryFilter.value;

  bookList.innerHTML = "";

  let filtered = books.filter(b => {
    let matchesSearch = b.title.toLowerCase().includes(search) || b.author.toLowerCase().includes(search);
    let matchesStream = stream === "" || b.stream === stream;
    let matchesCategory = category === "" || b.type === category;
    return matchesSearch && matchesStream && matchesCategory;
  });

  if (filtered.length === 0) {
    bookList.innerHTML = `<tr><td colspan="5" style="text-align:center;">❌ No books found</td></tr>`;
  }
  
  

  filtered.forEach(book => {
    let row = `<tr>
      <td>${book.title}</td>
      <td>${book.author}</td>
      <td>${book.stream}</td>
      <td>${book.type}</td>
      <td><a href="${book.link}" target="_blank">Download</a></td>
    </tr>`;
    bookList.innerHTML += row;
  });
}

// Event Listeners
searchInput.addEventListener("input", renderBooks);
streamFilter.addEventListener("change", renderBooks);
categoryFilter.addEventListener("change", renderBooks);

// Initial Load
renderBooks();
