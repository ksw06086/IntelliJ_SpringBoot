// *** 휴대폰 번호 정규식 ***
let regPhone = /^01([0|1|2|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;

// *** 브랜드 관련 Script *** //
// 브랜드 추가하기
function brandAdd() {
    const brandNameInput = document.getElementById("brandNameInput").value;
    const brandHPInput = document.getElementById("brandHPInput").value;
    const brandParam = { brandName : brandNameInput, hp : brandHPInput };

    if(brandNameInput === ""){
        alert("브랜드 명을 입력해주세요.");
        return false;
    }

    $.ajax({
        type: 'post',
        contentType:'application/json',
        data: JSON.stringify(brandParam),
        url: '/api/brand',
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(result.brandName + "브랜드가 추가되었습니다.");
            window.location.href="/host/brandList";
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// 브랜드 수정하기
function brandUpdate(){
    const brandId = document.getElementById("brandId").textContent;
    const brandName = document.getElementById("brandNameInput").value;
    const brandHP = document.getElementById("brandHPInput").value;
    const brandParam = { brandName : brandName, hp : brandHP };

    if(brandName === ""){
        alert("브랜드 명을 입력해주세요.");
        return false;
    }

    $.ajax({
        type: 'PUT',
        contentType:'application/json',
        data: JSON.stringify(brandParam),
        url: '/api/brands/' + brandId,
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(result.brandName + " 브랜드가 수정되었습니다.");
            window.location.href="/host/brandList";
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// 브랜드 삭제하기
function brandDel() {
    const brandIds = document.getElementsByName("brandIds"); // 화면에 있는 모든 Checkbox(brandIds)
    let brandDelList = []; // 삭제된 brandIds

    // checkBox에 체크되었는지 확인 후 삭제
    for (let i = 0; i < brandIds.length; i++) {
        if(brandIds[i].checked === true){
            brandDelAjax(brandIds[i].value);
            brandDelList.push(brandIds[i].value);
        }
    }

    // 삭제되었음을 alert 띄워줌
    alert(brandDelList + "번 브랜드가 삭제되었습니다.");

    // 화면에서 해당 요소 삭제해줌
    for (let i = 0; i < brandDelList.length; i++) {
        document.getElementById(brandDelList[i]).remove();
    }

    // brand 1개 삭제하는 Ajax 함수
    function brandDelAjax(brandId){
        $.ajax({
            type: 'DELETE',
            url: '/api/brands/' + brandId,
            success: (result) => {
                //AJAX 성공시 실행 코드
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    }
}

// 브랜드 checkbox 한번에 체크 함수
function allBrandCheck() {
    const brandCheckAll = document.getElementById("brandCheckAll");
    const brandIds = document.getElementsByName("brandIds");
    // 해당 페이지의 모든 brand CheckBox에 체크
    if(brandCheckAll.checked === true){
        for (let i = 0; i < brandIds.length; i++) {
            brandIds[i].checked = true;
        }
    } else {
        for (let i = 0; i < brandIds.length; i++) {
            brandIds[i].checked = false;
        }
    }
}

// 브랜드 HP 입력 확인(쓰지 않고 표시만 해주는 방향으로 생각 바뀜 혹시 몰라 남겨둠)
function brandHPInput(hp) {
    const brandHPDiv = document.getElementById("brandHPDiv");

    if(!regPhone.test(hp)){
        brandHPDiv.style.color = "red";
        brandHPDiv.textContent = "ex) 010-1234-5678";
    }
}

// *** 메인 카테고리 관련 Script *** //
// MainCategory 추가하기
function mainCategoryAdd(){
    const mainCategoryInput = document.getElementById("mainCategoryInput").value;
    const mainCategoryParam = { mainName : mainCategoryInput };

    $.ajax({
        type: 'post',
        contentType:'application/json',
        data: JSON.stringify(mainCategoryParam),
        url: '/api/mainCategory',
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(result.mainName + " 카테고리가 추가되었습니다.");
            mainCategoriesGet();
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// MainCategory 삭제하기
function mainCategoryDel(){
    const deleteMainCategoryForm = document.getElementById("mainCategoryDelSelector");
    const mainCode = deleteMainCategoryForm.value;
    const mainName = deleteMainCategoryForm.options[deleteMainCategoryForm.selectedIndex].text;

    $.ajax({
        type: 'DELETE',
        url: '/api/mainCategories/' + mainCode,
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(mainName + " 카테고리가 삭제되었습니다.");
            mainCategoriesGet();
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// MainCategory 목록 화면의 mainCategoriesSeletor에 추가해주기
function mainCategoriesGet(){
    $.ajax({
        type: 'get',
        url: '/api/mainCategories',
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const allMainCategoryByClass = document.getElementsByClassName("mainCategoriesSelector");

            // 1. mainCategoriesSelector Clear
            for (let i = 0; i < allMainCategoryByClass.length; i++) {
                allMainCategoryByClass[i].innerHTML = `<option value = "">1차 카테고리</option>`;
            }
            // 2. mainCategoriesSelector Option 추가
            result.forEach(data => {
                for (let i = 0; i < allMainCategoryByClass.length; i++) {
                    const option = document.createElement('option');
                    option.innerHTML = data.mainName;
                    option.value = data.mainCode;
                    allMainCategoryByClass[i].append(option);
                }
            })
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

/// *** 서브 카테고리 관련 Script *** //
// SubCategory 추가하기
function subCategoryAdd(){
    const subCategoryMainCode = document.getElementById("subCategoryMainCode").value;
    const subCategoryInput = document.getElementById("subCategoryInput").value;
    const subCategoryParam = { subName : subCategoryInput };

    $.ajax({
        type: 'PUT',
        contentType:'application/json',
        data: JSON.stringify(subCategoryParam),
        url: '/api/subCategory/' + subCategoryMainCode,
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(result.subName + " 카테고리가 추가되었습니다.");
            subCategoriesGet(subCategoryMainCode);
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// SubCategory 삭제하기
function subCategoryDel(){
    const subCategoryMainCodeForm = document.getElementById("subCategoryMainCode");
    const mainCode = subCategoryMainCodeForm.value;
    const mainName = subCategoryMainCodeForm.options[subCategoryMainCodeForm.selectedIndex].text;
    const deleteSubCategoryForm = document.getElementById("subCategoryDelSelector");
    const subCode = deleteSubCategoryForm.value;
    const subName = deleteSubCategoryForm.options[deleteSubCategoryForm.selectedIndex].text;

    $.ajax({
        type: 'DELETE',
        url: '/api/subCategories/' + subCode,
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(mainName + "의 " + subName + " 카테고리가 삭제되었습니다.");
            subCategoriesGet(mainCode);
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// SubCategory 목록 화면의 subCategoriesSeletor에 추가해주기
function subCategoriesGet(mainCode){
    $.ajax({
        type: 'GET',
        url: '/api/subCategories/' + mainCode,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const allSubCategoryByClass = document.getElementsByClassName("subCategoriesSelector");

            // 1. mainCategoriesSelector Clear
            for (let i = 0; i < allSubCategoryByClass.length; i++) {
                allSubCategoryByClass[i].innerHTML = `<option value = "">2차 카테고리</option>`;
            }
            // 2. mainCategoriesSelector Option 추가
            result.forEach(data => {
                for (let i = 0; i < allSubCategoryByClass.length; i++) {
                    const option = document.createElement('option');
                    option.innerHTML = data.subName;
                    option.value = data.subCode;
                    allSubCategoryByClass[i].append(option);
                }
            })
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// *** Cloth 상품 관련 Script *** //
// Cloth 상품 추가하기
function clothAdd(){
    let clothInputForm = new FormData(document.getElementById("clothInputForm"));

    $.ajax({
        type:"POST",
        url: "/api/cloth",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: clothInputForm,
        success: function(result){
            mainFileAdd(result.clothId);
            subFilesAdd(result.clothId);
            alert("상품이 등록되었습니다.");
            window.location.href="/host/productList";
        },
        err: function(err){
            console.log("err:", err);
            alert("실패");
        }
    });
}

// Cloth 상품 수정하기
function clothUpdate(){
    let clothInputForm = new FormData(document.getElementById("clothInputForm"));
    const clothId = document.getElementById("clothId");
    const baseMainFileYN = document.getElementById("baseMainFileYN");
    const baseSubFileYN = document.getElementById("baseSubFileYN");

    // api/cloth에서 추가와 수정 둘다 가능함(save함수)
    $.ajax({
        type:"POST",
        url: "/api/cloth",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: clothInputForm,
        success: function(result){
            if(baseMainFileYN.checked === false) {
                mainFileUpdate(clothId.value);
            }
            if(baseSubFileYN.checked === false){
                subFilesDel();
                subFilesAdd(clothId.value);
            }
            alert("상품이 수정되었습니다.");
            window.location.href="/host/productList";
        },
        err: function(err){
            console.log("err:", err);
            alert("실패");
        }
    });
}

// Cloth 상품 삭제하기
function clothDel() {
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
function allClothCheck() {
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
    if(subImageInput.files.length === 0){ return ; } // 서브 이미지 파일 선택 안했으면 그냥 넘기기
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

    // 삭제되었음을 alert 띄워줌
    alert(subFileIds.values() + "번 서브 이미지가 삭제되었습니다.");

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

// *** 색상 관련 Script *** //
// Color 추가하기
function colorAdd(){
    const colorInput = document.getElementById("colorInput").value;
    const colorParam = { colorName : colorInput };

    $.ajax({
        type: 'post',
        contentType:'application/json',
        data: JSON.stringify(colorParam),
        url: '/api/color',
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(result.colorName + " 색상이 추가되었습니다.");
            colorsGet();
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// Color 삭제하기
function colorDel(){
    const deleteColorForm = document.getElementById("colorDelSelector");
    const colorCode = deleteColorForm.value;
    const colorName = deleteColorForm.options[deleteColorForm.selectedIndex].text;

    $.ajax({
        type: 'DELETE',
        url: '/api/colors/' + colorCode,
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(colorName + " 색상이 삭제되었습니다.");
            colorsGet();
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// Color 목록 화면의 colorsSeletor에 추가해주기
function colorsGet(){
    $.ajax({
        type: 'get',
        url: '/api/colors',
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const allColorByClass = document.getElementsByClassName("colorsSelector");

            // 1. mainCategoriesSelector Clear
            for (let i = 0; i < allColorByClass.length; i++) {
                allColorByClass[i].innerHTML = `<option value = "">컬러 선택</option>`;
            }
            // 2. mainCategoriesSelector Option 추가
            result.forEach(data => {
                for (let i = 0; i < allColorByClass.length; i++) {
                    const option = document.createElement('option');
                    option.innerHTML = data.colorName;
                    option.value = data.colorCode;
                    allColorByClass[i].append(option);
                }
            })
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// *** 사이즈 관련 Script *** //
// Size 추가하기
function sizeAdd(){
    const sizeInput = document.getElementById("sizeInput").value;
    const sizeParam = { sizeName : sizeInput };

    $.ajax({
        type: 'post',
        contentType:'application/json',
        data: JSON.stringify(sizeParam),
        url: '/api/size',
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(result.sizeName + " 사이즈가 추가되었습니다.");
            sizesGet();
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// Size 삭제하기
function sizeDel(){
    const deleteSizeForm = document.getElementById("sizeDelSelector");
    const sizeCode = deleteSizeForm.value;
    const sizeName = deleteSizeForm.options[deleteSizeForm.selectedIndex].text;

    $.ajax({
        type: 'DELETE',
        url: '/api/sizes/' + sizeCode,
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(sizeName + " 사이즈가 삭제되었습니다.");
            sizesGet();
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// Size 목록 화면의 colorsSeletor에 추가해주기
function sizesGet(){
    $.ajax({
        type: 'get',
        url: '/api/sizes',
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const allSizeByClass = document.getElementsByClassName("sizesSelector");

            // 1. mainCategoriesSelector Clear
            for (let i = 0; i < allSizeByClass.length; i++) {
                allSizeByClass[i].innerHTML = `<option value = "">사이즈 선택</option>`;
            }
            // 2. mainCategoriesSelector Option 추가
            result.forEach(data => {
                for (let i = 0; i < allSizeByClass.length; i++) {
                    const option = document.createElement('option');
                    option.innerHTML = data.sizeName;
                    option.value = data.sizeCode;
                    allSizeByClass[i].append(option);
                }
            })
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}

// *** Stock 상품 관련 Script *** //
// Stock 재고 추가하기
function stockAdd(){
    if(document.getElementById("basePay").checked === true) {
        document.getElementById("deliPrice").value = 2500;
    } else if(document.getElementById("free").checked === true) {
        document.getElementById("deliPrice").value = 0;
    }
    const clothId = document.getElementById("clothId").value;
    let stockInputForm = new FormData(document.getElementById("stockInputForm"));

    $.ajax({
        type:"POST",
        url: "/api/stock",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: stockInputForm,
        success: function(result){
            alert("재고가 등록되었습니다.");
            window.location.href="/host/stockList?clothId=" + clothId;
        },
        err: function(err){
            console.log("err:", err);
            alert("실패");
        }
    });
}

// Stock 재고 수정하기
function stockUpdate(){
    if(document.getElementById("basePay").checked === true) {
        document.getElementById("deliPrice").value = 2500;
    } else if(document.getElementById("free").checked === true) {
        document.getElementById("deliPrice").value = 0;
    }
    const clothId = document.getElementById("clothId").value;
    let stockInputForm = new FormData(document.getElementById("stockInputForm"));

    $.ajax({
        type:"POST",
        url: "/api/newStock",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: stockInputForm,
        success: function(result){
            alert("재고가 수정되었습니다.");
            window.location.href="/host/stockList?clothId=" + clothId;
        },
        err: function(err){
            console.log("err:", err);
            alert("실패");
        }
    });
}

// Stock 재고 삭제하기
function stockDel() {
    const stockIds = document.getElementsByName("stockIds"); // 화면에 있는 모든 Checkbox(stockIds)
    let stockDelList = []; // 삭제된 stockIds

    // checkBox에 체크되었는지 확인 후 삭제
    for (let i = 0; i < stockIds.length; i++) {
        if(stockIds[i].checked === true){
            stockDelAjax(stockIds[i].value);
            stockDelList.push(stockIds[i].value);
        }
    }

    // 삭제되었음을 alert 띄워줌
    alert(stockDelList + "번 재고가 삭제되었습니다.");

    // 화면에서 해당 요소 삭제해줌
    for (let i = 0; i < stockDelList.length; i++) {
        document.getElementById(stockDelList[i]).remove();
    }

    // stock 1개 삭제하는 Ajax 함수
    function stockDelAjax(stockId){
        $.ajax({
            type: 'DELETE',
            url: '/api/stocks/' + stockId,
            success: (result) => {
                //AJAX 성공시 실행 코드
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    }
}
// 재고 checkbox 한번에 체크 함수
function allStockCheck() {
    const stockCheckAll = document.getElementById("stockCheckAll");
    const stockIds = document.getElementsByName("stockIds");
    // 해당 페이지의 모든 stock CheckBox에 체크
    if(stockCheckAll.checked === true){
        for (let i = 0; i < stockIds.length; i++) {
            stockIds[i].checked = true;
        }
    } else {
        for (let i = 0; i < stockIds.length; i++) {
            stockIds[i].checked = false;
        }
    }
}

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


