document.addEventListener('DOMContentLoaded', function() {
    // flatpickr를 이용해 datepicker 활성화
    flatpickr("#datepicker", {
        dateFormat: "Y-m-d", // 날짜 형식 지정
        locale: "ko", // 한국어로 설정
    });
});