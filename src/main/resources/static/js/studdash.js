document.querySelector('.refresh-btn').addEventListener('click', () => {
    alert("Page refreshed!");
});

document.querySelector('.notif-btn').addEventListener('click', () => {
    alert("No new notifications.");
});

function previewImage(event) {
    document.getElementById("profileImage").src =
        URL.createObjectURL(event.target.files[0]);
}
