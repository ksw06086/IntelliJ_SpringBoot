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
                allMainCategoryByClass[i].innerHTML = `<option value = "0">메인 카테고리</option>`;
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
                allSubCategoryByClass[i].innerHTML = `<option value = "0">서브 카테고리</option>`;
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
    if(document.getElementById("basePay").checked === true) {
        document.getElementById("deliPrice").value = 2500;
    } else if(document.getElementById("free").checked === true) {
        document.getElementById("deliPrice").value = 0;
    }
    let clothInputForm = new FormData(document.getElementById("clothInputForm"));

    $.ajax({
        type:"POST",
        url: "/api/cloth",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: clothInputForm,
        success: function(result){
            alert("상품이 등록되었습니다.");
            mainFileAdd(result.clothId);
            subFilesAdd(result.clothId);
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
    if(document.getElementById("basePay").checked === true) {
        document.getElementById("deliPrice").value = 2500;
    } else if(document.getElementById("free").checked === true) {
        document.getElementById("deliPrice").value = 0;
    }
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
            return false;
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

// *** 상품 검색 관련 Script *** //
let searchType = '상품명';                                      // 상품 검색 타입 (상품명, 상품번호)
let searchInput = '';                                          // 상품 검색 텍스트(Text)
let iconList = [];                                             // 아이콘 checkBox 리스트
let brandId = 0, mainCategoryId = 0, subCategoryId = 0;        // 브랜드, 메인 카테고리, 서브 카테고리 ID
let firstDay = '', lastDay = '';                               // 시작, 끝 날짜
// --- 상품 검색 버튼 클릭 --- //
function clothsGet(){
    // 검색 요소 Parameter 변수
    searchType = document.getElementById("searchType").value;
    searchInput = document.getElementById("searchInput").value;
    iconList = []; document.getElementsByName("icon").forEach(icon => {
        if(icon.checked){ iconList.push(icon.value); }
    });
    brandId = document.getElementById("brandId").value;
    mainCategoryId = document.getElementById("mainCategorySelector").value;
    subCategoryId = document.getElementById("subCategorySelector").value;
    firstDay = document.getElementById("firstDay").value;
    lastDay = document.getElementById("lastDay").value;

    // ajax로 검색결과의 상품 가져와서 목록 Update
    $.ajax({
        type: 'GET',
        url: '/api/cloths?searchType=' + searchType + '&searchInput=' + searchInput + '&brandId=' + brandId +
            '&mainCategoryId=' + mainCategoryId + '&subCategoryId=' + subCategoryId +
            "&icons=" + iconList + "&firstDay=" + firstDay + "&lastDay=" + lastDay,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const productListTBody = document.getElementById("productListTBody");
            productListTBody.innerHTML = '';

            // 1) 가져온 목록 개수 뿌려주는 ListView의 Head 부분
            const getPageCountText = document.getElementById("getPageCountText");
            getPageCountText.innerHTML = '검색 ' + result.pageObject.numberOfElements + '건 /'
                + ' 총 ' + result.pageObject.totalElements + '건';

            // 2) 가져온 목록 뿌려주는 ListView의 Body 부분
            result.contentList.forEach(data => {
                // tr 태그
                const tr = document.createElement('tr');
                tr.className = "text-center td-py10 td-px15";
                tr.id = data.clothId;
                // icon 체크
                let icon = '';
                if(data.mainCategory.mainName == 'onlySuncloth'){ icon += '<img src="/icon/only.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'best'){ icon += '<img src="/icon/best.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'hot'){ icon += '<img src="/icon/hot.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'minPrice'){ icon += '<img src="/icon/minPrice.png" alt="이미지 없음" width = "30px" height = "30px">'}
                if(data.cloth.regDate == new Date()){ icon += '<img src="/icon/minPrice.png" alt="이미지 없음" width = "30px" height = "30px">'}
                // td 태그 추가
                tr.innerHTML =  '<td><input type = "checkbox" name = "clothIds" value = "' + data.cloth.clothId + '"></td>' +
                                '<td>' +
                                    '<img src="/uploadMainImageView/' + data.cloth.clothId + '" alt="이미지 없음" width = "50px" height = "60px">' +
                                '</td>' +
                                '<td>' + data.cloth.clothId + '</td>' +
                                '<td>' + data.subCategory.subName + '</td>' +
                                '<td class="text-start">' + icon +
                                    '<a href="/host/productInput?clothId=' + data.cloth.clothId + '">' + data.cloth.clothName +
                                    '</a></td>' +
                                '<td>' + data.cloth.basePrice + '</td>' +
                                '<td>' + data.cloth.deliPrice + '</td>' +
                                '<td>' + data.cloth.regDate + '</td>' +
                                '<td><a href="/host/stockList?clothId=' + data.cloth.clothId +'"><input type = "button" value = "재고 관리" name = "csInput" class="whiteButton"/></a></td>';
                productListTBody.append(tr);
            })

            // 3) 페이지 이동 버튼 보여주는 ListView의 footer 부분
            const pagingBlock = document.getElementById("pagingBlock");
            // < 버튼 부분 추가
            let pagingStr = '<li class="page-item ' + ((1 === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                            '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (result.pageObject.pageable.pageNumber-1) + '">&lt;</a>\n' +
                            '</li>\n';
            // 1,2,... 버튼 부분 추가
            for (let i = result.startPage; i < result.endPage+1; i++) {
                pagingStr += '<li class="page-item ' + ((i === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                            '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (i - 1) + '">' + i + '</a>\n' +
                            '</li>\n';
            }
            // > 버튼 부분 추가
            pagingStr += '<li class="page-item ' + ((result.pageObject.totalPages === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                        '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (result.pageObject.pageable.pageNumber+1) + '">&gt;</a>\n' +
                        '</li>';
            pagingBlock.innerHTML = pagingStr;
        }, error:function(e) {
            alert("검색 조건이 올바르지 않습니다. 재확인 후 검색해주세요.");
        }
    });
}
// --- 페이지 넘기기 --- //
function clothPaging(page){
    // ajax로 검색결과의 상품 가져와서 목록 Update
    $.ajax({
        type: 'GET',
        url: '/api/cloths?searchType=' + searchType + '&searchInput=' + searchInput + '&brandId=' + brandId +
            '&mainCategoryId=' + mainCategoryId + '&subCategoryId=' + subCategoryId +
            "&icons=" + iconList + "&firstDay=" + firstDay + "&lastDay=" + lastDay + "&page=" + page,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const productListTBody = document.getElementById("productListTBody");
            productListTBody.innerHTML = '';

            // 1) 가져온 목록 개수 뿌려주는 ListView의 Head 부분
            const getPageCountText = document.getElementById("getPageCountText");
            getPageCountText.innerHTML = '검색 ' + result.pageObject.numberOfElements + '건 /'
                + ' 총 ' + result.pageObject.totalElements + '건';

            // 2) 가져온 목록 뿌려주는 ListView의 Body 부분
            result.contentList.forEach(data => {
                // tr 태그
                const tr = document.createElement('tr');
                tr.className = "text-center td-py10 td-px15";
                tr.id = data.clothId;
                // icon 체크
                let icon = '';
                if(data.mainCategory.mainName == 'onlySuncloth'){ icon += '<img src="/icon/only.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'best'){ icon += '<img src="/icon/best.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'hot'){ icon += '<img src="/icon/hot.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'minPrice'){ icon += '<img src="/icon/minPrice.png" alt="이미지 없음" width = "30px" height = "30px">'}
                if(data.cloth.regDate == new Date()){ icon += '<img src="/icon/minPrice.png" alt="이미지 없음" width = "30px" height = "30px">'}
                // td 태그 추가
                tr.innerHTML =  '<td><input type = "checkbox" name = "clothIds" value = "' + data.cloth.clothId + '"></td>' +
                    '<td>' +
                    '<img src="/uploadMainImageView/' + data.cloth.clothId + '" alt="이미지 없음" width = "50px" height = "60px">' +
                    '</td>' +
                    '<td>' + data.cloth.clothId + '</td>' +
                    '<td>' + data.subCategory.subName + '</td>' +
                    '<td class="text-start">' + icon +
                    '<a href="/host/productInput?clothId=' + data.cloth.clothId + '">' + data.cloth.clothName +
                    '</a></td>' +
                    '<td>' + data.cloth.basePrice + '</td>' +
                    '<td>' + data.cloth.deliPrice + '</td>' +
                    '<td>' + data.cloth.regDate + '</td>' +
                    '<td><a href="/host/stockList?clothId=' + data.cloth.clothId +'"><input type = "button" value = "재고 관리" name = "csInput" class="whiteButton"/></a></td>';
                productListTBody.append(tr);
            })

            // 3) 페이지 이동 버튼 보여주는 ListView의 footer 부분
            const pagingBlock = document.getElementById("pagingBlock");
            // < 버튼 부분 추가
            let pagingStr = '<li class="page-item ' + ((1 === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (result.pageObject.pageable.pageNumber-1) + '">&lt;</a>\n' +
                '</li>\n';
            // 1,2,... 버튼 부분 추가
            for (let i = result.startPage; i < result.endPage+1; i++) {
                pagingStr += '<li class="page-item ' + ((i === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                    '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (i - 1) + '">' + i + '</a>\n' +
                    '</li>\n';
            }
            // > 버튼 부분 추가
            pagingStr += '<li class="page-item ' + ((result.pageObject.totalPages === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (result.pageObject.pageable.pageNumber+1) + '">&gt;</a>\n' +
                '</li>';
            pagingBlock.innerHTML = pagingStr;
        }, error:function(e) {
            alert("페이지 기능 에러남");
        }
    });
}




