// Carousel Logic
const books = [
  { title: "Digital Library Systems", img: "/images/p2.jpg" },
  { title: "Advanced Algorithms", img: "/images/p3.jpg" },
  { title: "Data Structures", img: "/images/p4.jpg" },
  { title: "Computer Networks", img: "/images/p5.jpg" }
];

let current = 0;
const imgEl = document.getElementById("carousel-img");
const titleEl = document.getElementById("carousel-title");

setInterval(() => {
  current = (current + 1) % books.length;
  imgEl.src = books[current].img;
  titleEl.textContent = books[current].title;
}, 3000);