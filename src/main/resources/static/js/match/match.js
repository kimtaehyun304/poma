document.addEventListener('DOMContentLoaded', function() {
    // flatpickr를 이용해 datepicker 활성화
    flatpickr("#datepicker", {
        dateFormat: "Y-m-d", // 날짜 형식 지정
        locale: "ko", // 한국어로 설정
    });
});

//페이지 이동시 검색정보 유지
let dateElement = document.getElementsByName("date")[0]
let locationElement = document.getElementsByName("location")[0]
let genderElement = document.getElementsByName("gender")[0]

window.addEventListener("load", initMatchLink)

dateElement.addEventListener("change", changeMatchLink)
locationElement.addEventListener("change", changeMatchLink)
genderElement.addEventListener("change", changeMatchLink)

function initMatchLink() {
    let queryParam = window.location.search

    if(queryParam !== '') {
        let socialMatch = document.getElementById("social-match")
        socialMatch.href = socialMatch.getAttribute("href").split('?')[0] + queryParam

        let teamMatch = document.getElementById("team-match")
        teamMatch.href = teamMatch.getAttribute("href").split('?')[0] + queryParam

        let mercenaryMatch = document.getElementById("mercenary-match")
        mercenaryMatch.href = mercenaryMatch.getAttribute("href").split('?')[0] + queryParam
    }
}

function changeMatchLink() {
    let queryParam = `?date=${dateElement.value}&location=${locationElement.value}&gender=${genderElement.value}`

    let socialMatch = document.getElementById("social-match")
    socialMatch.href = socialMatch.getAttribute("href").split('?')[0] + queryParam

    let teamMatch = document.getElementById("team-match")
    teamMatch.href = teamMatch.getAttribute("href").split('?')[0] + queryParam

    let mercenaryMatch = document.getElementById("mercenary-match")
    mercenaryMatch.href = mercenaryMatch.getAttribute("href").split('?')[0] + queryParam
}

// 마감 가리기 & 보기
let userSizeElements = document.getElementsByClassName("UserSize")
let removeBtn = document.getElementById("removeBtn")

removeBtn.addEventListener("click", () => {
    for(let u of userSizeElements){
        if(u.textContent.includes("신청 마감")){
            let card = u.parentElement.parentElement.parentElement.parentElement
            if(card.style.display === "") {
                card.style.display = "none";
                removeBtn.textContent = "마감 보기"
            }
            else {
                card.style.display = "";
                removeBtn.textContent = "마감 가리기"
            }
        }
    }

})