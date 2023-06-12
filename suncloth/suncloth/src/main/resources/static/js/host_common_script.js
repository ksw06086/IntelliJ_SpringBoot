// *** 검색 관련 Script *** //
// --- 날짜 관련 함수들 --- //
/* 관련 함수 설명
// 날짜
var date = new Date();  현재
var nowYear = date.getFullYear();
var nowMonth = date.getMonth()+1;
var nowDay = date.getDate();

// 어제날짜
var yesterDate = date.getTime() - (1*24*60*60*1000);
date.setTime(yesterDate); // 이걸로 date가 하루 전으로 초기화 되어서 가져오기만 하면됨
*/
const todayDate = new Date(); /* 현재 날짜 */
function dateToday(){ // 오늘
    document.getElementById('lastDay').value = todayDate.toISOString().substring(0, 10);
    document.getElementById('firstDay').value = todayDate.toISOString().substring(0, 10);
    document.searchForm.dayNum.value = 0; // ?? 이거 모르겠음
}
function dateWeek(){ // 1주일
    const date = new Date(); date.setDate(todayDate.getDate() - 7);
    document.getElementById('lastDay').value = todayDate.toISOString().substring(0, 10);
    document.getElementById('firstDay').value = date.toISOString().substring(0, 10);
    document.searchForm.dayNum.value = 1; // ?? 이거 모르겠음
}
function dateMonth1(){ // 1개월
    const date = new Date(); date.setDate(todayDate.getDate() - 30);
    document.getElementById('lastDay').value = todayDate.toISOString().substring(0, 10);
    document.getElementById('firstDay').value = date.toISOString().substring(0, 10);
    document.searchForm.dayNum.value = 2; // ?? 이거 모르겠음
}
function dateMonth3(){ // 3개월
    const date = new Date(); date.setDate(todayDate.getDate() - 90);
    document.getElementById('lastDay').value = todayDate.toISOString().substring(0, 10);
    document.getElementById('firstDay').value = date.toISOString().substring(0, 10);
    document.searchForm.dayNum.value = 3;
}
function dateMonth6(){ // 6개월
    const date = new Date(); date.setDate(todayDate.getDate() - 180);
    document.getElementById('lastDay').value = todayDate.toISOString().substring(0, 10);
    document.getElementById('firstDay').value = date.toISOString().substring(0, 10);
    document.searchForm.dayNum.value = 4;
}

