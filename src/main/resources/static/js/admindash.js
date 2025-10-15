// Local Storage Database
const db = {
  get(k){ try{return JSON.parse(localStorage.getItem(k))}catch(e){return null} },
  set(k,v){ localStorage.setItem(k,JSON.stringify(v)) },
  init(){
    if(!this.get('books')) this.set('books', []);
    if(!this.get('students')) this.set('students', []);
  }
};

// Render Books
function renderBooks(){
  const booksTableBody = document.querySelector('#booksTable tbody');
  if(!booksTableBody) return; // not on this page
  const books = db.get('books') || [];
  booksTableBody.innerHTML = '';
  books.forEach((b,i)=>{
    const tr = document.createElement('tr');
    tr.innerHTML = `<td>${b.title}</td><td>${b.author}</td><td>${b.copies}</td>
    <td>
      <button class='btn success' onclick='editBook(${i})'>Edit</button>
      <button class='btn danger' onclick='deleteBook(${i})'>Delete</button>
    </td>`;
    booksTableBody.appendChild(tr);
  });
}

// Render Students
function renderStudents(){
  const studentsTableBody = document.querySelector('#studentsTable tbody');
  if(!studentsTableBody) return;
  const students = db.get('students') || [];
  studentsTableBody.innerHTML = '';
  students.forEach((s,i)=>{
    const tr = document.createElement('tr');
    tr.innerHTML = `<td>${s.name}</td><td>${s.email}</td><td>${s.department}</td><td>${s.year}</td>
    <td>
      <button class='btn success' onclick='editStudent(${i})'>Edit</button>
      <button class='btn danger' onclick='deleteStudent(${i})'>Delete</button>
    </td>`;
    studentsTableBody.appendChild(tr);
  });
}

// Add Book
document.getElementById('addBook')?.addEventListener('click',()=>{
  const title = document.getElementById('bookTitle').value.trim();
  const author = document.getElementById('bookAuthor').value.trim();
  const copies = parseInt(document.getElementById('bookCopies').value);
  if(!title || !author || isNaN(copies)) return alert('Fill all book fields');
  const books = db.get('books'); books.push({title,author,copies}); db.set('books',books);
  renderBooks();
  document.getElementById('bookTitle').value='';
  document.getElementById('bookAuthor').value='';
  document.getElementById('bookCopies').value='';
});

// Add Student
document.getElementById('addStudent')?.addEventListener('click',()=>{
  const name = document.getElementById('studentName').value.trim();
  const email = document.getElementById('studentEmail').value.trim();
  const department = document.getElementById('studentDepartment').value.trim();
  const year = document.getElementById('studentYear').value.trim();
  if(!name || !email || !department ||!year) return alert('Fill all student fields');
  const students = db.get('students');students.push({name,email,department,year}); db.set('students', students);

  renderStudents();
  document.getElementById('studentName').value='';
  document.getElementById('studentEmail').value='';
  document.getElementById('studentDepartment').value='';
  document.getElementById('studentYear').value='';
});

// Delete & Edit Books
window.deleteBook = (i)=>{
  const books = db.get('books'); books.splice(i,1); db.set('books',books); renderBooks();
}
window.editBook = (i)=>{
  const books = db.get('books'); const b = books[i];
  const title = prompt('Title:',b.title); if(!title) return;
  const author = prompt('Author:',b.author); if(!author) return;
  const copies = parseInt(prompt('Copies:',b.copies)); if(isNaN(copies)) return;
  books[i] = {title,author,copies}; db.set('books',books); renderBooks();
}

// Delete & Edit Students
window.deleteStudent = (i)=>{
  const students = db.get('students'); students.splice(i,1); db.set('students',students); renderStudents();
}
window.editStudent = (i)=>{
  const students = db.get('students'); const s = students[i];
  const name = prompt('Name:',s.name); if(!name) return;
  const email = prompt('Email:',s.email); if(!email) return;
  const department = prompt('Department:',s.department); if(!department) return;
  const year = prompt('Year:',s.year); if(!year) return;
  students[i] = {name,email,department,year}; db.set('students',students); renderStudents();
}

// Init
db.init();
renderBooks();
renderStudents();
