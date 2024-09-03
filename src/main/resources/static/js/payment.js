// 테스트 결제
document.querySelector("#pay-btn").addEventListener("click", function (e) {

    let buyer_name = document.getElementById("name").textContent
    let IMP = window.IMP;

    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더함
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    const timestamp = `${year}${month}${day}${hours}${minutes}${seconds}`;
    let uid = timestamp

    let myMoney = document.getElementById("money")
    let amount = 10000
    IMP.init("imp64246530"); // 가맹점 식별코드
    IMP.request_pay({
        pg: 'kakaopay.TC0ONETIME', // PG사 코드표에서 선택
        pay_method: 'card', // 결제 방식
        merchant_uid: uid, // 결제 고유 번호
        name: 'POMA 테스트 결제', // 제품명
        amount: amount, // 가격
        //구매자 정보 ↓
        buyer_name: buyer_name,
        m_redirect_url: `https://dlpoma.store/paymentMobile?url=${window.location.href}`,
    }, async function (response) { // callback
        if (response.success) {
            await charge(response.imp_uid)
        } else {
            //결제 취소할때 뜨길래 주석 처리했어요
            //alert('포트원 서버와 연결 실패!')
        }
    });

    async function charge(imp_uid) {
        let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

        const headers = {
            "Content-Type": "text/plain;charset=UTF-8",
            "x-requested-with": "XMLHttpRequest",
            "X-CSRF-TOKEN" : csrfToken
        };
        const body = imp_uid;
        try {
            const response = await fetch("https://dlpoma.store/payment", {
                method: "POST", headers: headers, body: body
            });
            if (response.status === 201) {
                myMoney.textContent = (parseInt(myMoney.textContent) + amount).toString()
                alert("충전 완료")
            }
            else if(response.status === 401){
                let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
                if(result) window.location.href = "/user/signin"
            }
            else{
                let responseText = await response.text();
                alert(responseText)
            }
        } catch (error) {
            console.error("fetch error")
        }
    }
});


