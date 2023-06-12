// *** Board 게시판 관련 Script *** //
// Board 게시판 추가하기
function boardAdd(){
    let boardInputForm = new FormData(document.getElementById("boardInputForm"));
    const boardState = document.getElementById("boardState").value;

    $.ajax({
        type:"POST",
        url: "/api/board",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: boardInputForm,
        success: function(result){
            alert(boardState + " 게시글이 등록되었습니다.");
            boardFilesAdd(result.num);
            window.location.href="/host/boardList?name=" + boardState;
        },
        err: function(err){
            console.log("err:", err);
            alert("실패");
        }
    });
}

// Board 게시판 수정하기
function boardUpdate(){
    let boardInputForm = new FormData(document.getElementById("boardInputForm"));
    const boardState = document.getElementById("boardState").value;
    const boardNum = document.getElementById("boardNum");
    const baseBoardFileYN = document.getElementById("baseBoardFileYN");

    // api/board에서 추가와 수정 둘다 가능함(save함수)
    $.ajax({
        type:"POST",
        url: "/api/board",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: boardInputForm,
        success: function(result){
            if(baseBoardFileYN.checked === false){
                boardFilesDel();
                boardFilesAdd(boardNum.value);
            }
            alert("상품이 수정되었습니다.");
            window.location.href="/host/boardList?name=" + boardState;
        },
        err: function(err){
            console.log("err:", err);
            alert("실패");
            return false;
        }
    });
}

// Cloth 상품 삭제하기
function boardDel() {
    const clothIds = document.getElementsByName("clothIds"); // 화면에 있는 모든 Checkbox(clothIds)
    let clothDelList = []; // 삭제된 clothIds

    // checkBox에 체크되었는지 확인 후 삭제
    for (let i = 0; i < clothIds.length; i++) {
        if(clothIds[i].checked === true){
            clothDelAjax(clothIds[i].value);
            clothDelList.push(clothIds[i].value);
        }
    }

    // 삭제되었음을 alert 띄워줌
    alert(clothDelList + "번 상품이 삭제되었습니다.");

    // 화면에서 해당 요소 삭제해줌
    for (let i = 0; i < clothDelList.length; i++) {
        document.getElementById(clothDelList[i]).remove();
    }

    // cloth 1개 삭제하는 Ajax 함수
    function clothDelAjax(clothId){
        $.ajax({
            type: 'DELETE',
            url: '/api/cloths/' + clothId,
            success: (result) => {
                //AJAX 성공시 실행 코드
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    }
}
// 상품 checkbox 한번에 체크 함수
function allBoardCheck() {
    const clothCheckAll = document.getElementById("clothCheckAll");
    const clothIds = document.getElementsByName("clothIds");
    // 해당 페이지의 모든 stock CheckBox에 체크
    if(clothCheckAll.checked === true){
        for (let i = 0; i < clothIds.length; i++) {
            clothIds[i].checked = true;
        }
    } else {
        for (let i = 0; i < clothIds.length; i++) {
            clothIds[i].checked = false;
        }
    }
}