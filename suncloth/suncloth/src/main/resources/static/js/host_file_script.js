// *** File 관련 Script *** //
// 메인이미지 선택시 미리보기
function imageFileSelect(mainImage) {
    const preview = document.getElementById('mainImage');
    const reader = new FileReader();
    reader.readAsDataURL(mainImage);
    reader.onload = (e) => {
        preview.src = e.target.result;
        preview.className = "wh-100px";
    };
}

// File Checkbox 선택시 동작 함수
function baseMainFileUpdateYN(){
    const baseMainFileYN = document.getElementById("baseMainFileYN");
    const mainFile = document.getElementById("mainFile");
    const mainImage = document.getElementById("mainImage");
    if(baseMainFileYN.checked === true){
        mainFile.className = 'd-none';
        mainFile.required = false;
        mainImage.className = 'wh-100px';
    } else {
        mainFile.className = '';
        mainFile.required = true;
        mainImage.className = 'wh-100px d-none';
    }
}
function baseSubFileUpdateYN(){
    const baseSubFileYN = document.getElementById("baseSubFileYN");
    const subFile = document.getElementById("subFile");
    const subImagesDiv = document.getElementById("subImagesDiv");
    if(baseSubFileYN.checked === true){
        subFile.className = 'd-none';
        subImagesDiv.className = '';
    } else {
        subFile.className = '';
        subImagesDiv.className = 'd-none';
    }
}
function baseBoardFileUpdateYN(){
    const baseBoardFileYN = document.getElementById("baseBoardFileYN");
    const boardFile = document.getElementById("boardFile");
    const boardImagesDiv = document.getElementById("boardImagesDiv");
    if(baseBoardFileYN.checked === true){
        boardFile.className = 'd-none';
        boardImagesDiv.className = '';
    } else {
        boardFile.className = '';
        boardImagesDiv.className = 'd-none';
    }
}


// 메인 이미지 파일 삽입
function mainFileAdd(clothId) {
    const mainImageInput = document.getElementById("mainFile");
    const formData = new FormData();
    formData.append("mainImage", mainImageInput.files[0]);
    formData.append("clothId", clothId);

    // 메인 이미지 update 하기
    $.ajax({
        type:"POST",
        url: "/api/mainFile",
        processData: false,
        contentType: false,
        data: formData,
        success: function(result){
            alert("메인이미지 삽입 성공");
        },
        err: function(err){
            alert("메인이미지 삽입 실패");
        }
    })
}
// 메인 이미지 파일 수정
function mainFileUpdate(clothId) {
    const mainImageInput = document.getElementById("mainFile");
    const mainFileId = document.getElementById("mainFileId");
    const formData = new FormData();
    formData.append("mainImage", mainImageInput.files[0]);
    formData.append("mainFileId", mainFileId.value);
    formData.append("clothId", clothId);

    // 메인 이미지 update 하기
    $.ajax({
        type:"POST",
        url: "/api/replaceMainFile",
        processData: false,
        contentType: false,
        data: formData,
        success: function(result){
            alert("메인이미지 수정 성공");
        },
        err: function(err){
            alert("메인이미지 수정 실패");
        }
    })
}

// 서브 이미지 파일들 삽입
function subFilesAdd(clothId) {
    const subImageInput = document.getElementById("subFile");
    const formData = new FormData();
    if(subImageInput.files.length === 0){ return false; } // 서브 이미지 파일 선택 안했으면 그냥 넘기기
    for (let i = 0; i < subImageInput.files.length; i++) {
        // formData 에 'subImages' 이라는 키값으로 subImageFile 값을 append 시킨다.
        formData.append('subImages', subImageInput.files[i]);
    }
    formData.append("clothId", clothId);

    // 서브 이미지 재삽입하기
    $.ajax({
        type:"POST",
        url: "/api/subFile",
        processData: false,
        contentType: false,
        data: formData,
        success: function(result){
            alert("서브이미지 삽입 성공");
        },
        err: function(err){
            alert("서브이미지 삽입 실패");
        }
    })
}
// 서브 이미지 파일들 삭제
function subFilesDel() {
    const subFileIds = document.getElementsByName("subFileIds"); // subFileIds 서브이미지들

    // 서브 이미지 삭제
    for (let i = 0; i < subFileIds.length; i++) {
        subFileDelAjax(subFileIds[i].value);
    }

    // subFile 1개 삭제하는 Ajax 함수
    function subFileDelAjax(subFileId){
        $.ajax({
            type: 'DELETE',
            url: '/api/subFiles/' + subFileId,
            success: (result) => {
                //AJAX 성공시 실행 코드
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    }
}