let currentURL = window.location.href;
if(currentURL.includes("reviews")) {
    // 실행할 코드 작성
    let element = document.getElementById("mentor-mentee")
    element.classList.add("selected"); // "selected" 클래스 추가
}