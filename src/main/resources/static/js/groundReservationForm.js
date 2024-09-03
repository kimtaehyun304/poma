//document.querySelector(".form-row").style.display = 'none'

let div2 = document.createElement("div")
div2.style.display = "none"
document.body.append(div2)

// 매치 신청 버튼 클릭 이벤트
document.querySelector(".submitBtn").addEventListener("click", function (e) {
    reservation();
    async function reservation() {
        // 현재 URL을 가져옵니다.
        let currentUrl = window.location.pathname;
        // 정규 표현식을 사용하여 '/reservation/sports' 부분을 추출합니다.
        let regex = /\/reservation\/(\w+)/;
        let sports = currentUrl.match(regex)[1];

        let groundId = document.getElementsByName("groundId")[0].value;
        let date = document.getElementsByName("date")[0].value;
        let time = document.getElementsByName("time")[0].value;
        let gender = document.getElementsByName("gender")[0].value;
        let maxSize = document.getElementsByName("maxSize")[0].value;
        let match = document.getElementsByName("match")[0].value;
        //let mercenarySize = document.getElementsByName("mercenarySize")[0].value;
        let userIdList = Array.from(document.querySelectorAll('input[name="userIdList"]:checked'))
            .map(input => input.value);

        let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

        const headers = {
            "Content-Type": "application/json;charset=UTF-8",
            "x-requested-with": "XMLHttpRequest",
            "X-CSRF-TOKEN" : csrfToken
        };
        const body = {
            groundId: groundId,
            date: date,
            time: time,
            gender: gender,
            maxSize: maxSize,
            match: match,
            userIdList : userIdList
        };

        try {
            const response = await fetch(`https://dlpoma.store/reservation/${sports}`, {
                method: "POST", headers: headers, body: JSON.stringify(body)
            });
            let data = await response.text();
            if (response.status === 201) {
                window.location.href = `/${match}Match/${sports}?date=${date}&location=all&gender=${gender}`;
            }
            else if(response.status === 401){
                let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
                if(result) window.location.href = "/user/signin"
            }else if(response.status === 409){
                if (data === '구입할 돈이 부족합니다'){
                    let result = confirm('구입할 돈이 부족합니다! 충전할까요?')
                    if(result) document.querySelector("#pay-btn").click()
                }else
                    alert(data)
            }else {
                alert(data)
            }
        } catch (error) {
            alert("에러가 발생했습니다")
        }
    }
});

document.querySelector("#matchCategory").addEventListener("change", (e) => {
    let selectedValue = e.target.value;
    let memberList = document.getElementById("memberList")
    let mercenarySize = document.getElementById("mercenarySize")

    if(selectedValue === "social"){
        //memberList.style.display = "none";
        //mercenarySize.style.display = "none";
        memberList.className = "d-none";
        mercenarySize.className = "d-none";
    }

    if(selectedValue === "team"){
        //memberList.style.display = "";
        //mercenarySize.style.display = "none";
        memberList.className = "";
        mercenarySize.className = "d-none";
        getTeamMembers()
    }

    if(selectedValue === "mercenary"){
        //memberList.style.display = "";
        //mercenarySize.style.display = "";
        memberList.className = "";
        mercenarySize.className = "";
        getTeamMembers()
    }

    async function getTeamMembers() {
        let gender =   document.querySelector("#gender").value

        try {
            const response = await fetch(`https://dlpoma.store/api/team/users?gender=${gender}`, {
                method: "GET"
            });

            if (response.status === 200) {
                document.querySelector("#memberList").innerHTML = "<h1>멤버 목록</h1>"
                let json = await response.json()
                json.forEach(j => {
                    let label = document.createElement('label');
                    label.className ="pr-2"
                    let checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.name = 'userIdList';
                    checkbox.value = j.id;
                    checkbox.className = 'Wh-20px'
                    let span = document.createElement('span');
                    span.className = 'Font-size-20px';
                    span.textContent = j.name
                    label.appendChild(checkbox);
                    label.appendChild(span);
                    document.querySelector("#memberList").appendChild(label)
                })
            }
            else if(response.status === 401){
                let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
                if(result) window.location.href = "/user/signin"
            }
            else {
                let responseText = await response.text()
                alert(responseText)
            }
        } catch (error) {
            console.error("에러가 발생했습니다")
        }

    }
})

//카카오 지도 api & 라이브러리
let mapElement = document.querySelector("#map")
let messageElement =  document.querySelector('.noneResult')
let selectElement = document.querySelector("#sel1")

let ps = new kakao.maps.services.Places()
let container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
let options
let map

//keywordSearch 두번째 파라미터는 콜백함수입니다.
ps.keywordSearch(selectElement.children[0].textContent, function(data, status, pagination){
    if (status === kakao.maps.services.Status.OK) {
        options = {
            center: new kakao.maps.LatLng(data[0].y, data[0].x), //첫번째 option 태그 데이터
            level: 3 //지도의 레벨(확대, 축소 정도)
        };
    }else if(data.length === 0) {
        mapElement.style.display = 'none'
        messageElement.className = "noneResult"
    }else
        alert("에러가 발생했습니다")
    map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
    //displayMarker(data[0]);
},{size:1});

selectElement.addEventListener("change", function (e){
    ps.keywordSearch(e.target[e.target.selectedIndex].textContent, function (data, status, pagination){
        if (status === kakao.maps.services.Status.OK) {
            mapElement.style.display = ''
            messageElement.className = "noneResult d-none"
            let coords = new kakao.maps.LatLng(data[0].y, data[0].x);
            map.setCenter(coords)
            displayMarker(data[0]);
        }else if(data.length === 0) {
            mapElement.style.display = 'none'
            //messageElement.style.display = ''
            messageElement.className = "noneResult"
        }else
            alert("에러가 발생했습니다")
    },{size:1});
})

// 지도에 마커를 표시하는 함수입니다
function displayMarker(place) {
    // 마커를 생성하고 지도에 표시합니다
    var marker = new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(place.y, place.x)
    });
}

let currentURL = window.location.href;
if(currentURL.includes("reservation")) {
    // 실행할 코드 작성
    let element = document.getElementById("ground-reservation")
    element.classList.add("selected"); // "selected" 클래스 추가
}