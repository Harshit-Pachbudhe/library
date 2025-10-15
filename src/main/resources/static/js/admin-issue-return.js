const db = {
  get(k){ try{return JSON.parse(localStorage.getItem(k))}catch(e){return null} },
  set(k,v){ localStorage.setItem(k,JSON.stringify(v)) },
  init(){ if(!this.get('loans')) this.set('loans', []); }
};

function renderLoans(){
  const loansTableBody = document.querySelector('#loansTable tbody');
  const loans = db.get('loans') || [];
  loansTableBody.innerHTML = '';
  loans.forEach((l,i)=>{
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${l.student}</td>
      <td>${l.book}</td>
      <td>${l.status}</td>
      <td>
        <button class='btn success' onclick='returnLoan(${i})'>Return</button>
        <button class='btn danger' onclick='deleteLoan(${i})'>Delete</button>
      </td>`;
    loansTableBody.appendChild(tr);
  });
}

document.getElementById('addLoan')?.addEventListener('click',()=>{
  const student = document.getElementById('loanStudent').value.trim();
  const book = document.getElementById('loanBook').value.trim();
  if(!student || !book) return alert("Fill both fields");
  const loans = db.get('loans'); loans.push({student,book,status:"Issued"});
  db.set('loans',loans); renderLoans();
  document.getElementById('loanStudent').value='';
  document.getElementById('loanBook').value='';
});

window.returnLoan = (i)=>{
  const loans = db.get('loans'); loans[i].status="Returned"; db.set('loans',loans); renderLoans();
}
window.deleteLoan = (i)=>{
  const loans = db.get('loans'); loans.splice(i,1); db.set('loans',loans); renderLoans();
}

db.init();
renderLoans();
