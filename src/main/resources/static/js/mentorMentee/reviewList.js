let currentURL = window.location.href;
if(currentURL.includes("reviews")) {
    // 실행할 코드 작성
    let element = document.getElementById("mentor-mentee")
    element.classList.add("selected"); // "selected" 클래스 추가
}

let sports = document.querySelector(".Sports").textContent
let myTr = document.querySelectorAll(".myTr")
myTr.forEach((target) => target.addEventListener("click", function (e) {
    window.location.href=`/reviews/users/${e.target.parentElement.getAttribute('data-mentorId')}?sports=${sports}`
}))
