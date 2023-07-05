// *** 비밀번호 정규식 ***
// 1) 최소 하나 이상의 소문자, 하나의 대문자, 하나의 숫자, 하나의 특수문자, 최소 8 자리
// 2) 비밀번호 6 자리 이상, 모든 요구 사항을 충족하거나 숫자만 빼고 나머지를 충족하는 경우
// 3) 비밀번호 6 자리 이상, 최소 하나 이상의 대소문자, 하나의 숫자, 하나의 특수문자
let strongPassword = new RegExp('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.{8,})');
let mediumStrongPassword = new RegExp('((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.{6,}))|((?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9])(?=.{8,}))');
let mediumPassword = new RegExp('(((?=.*[a-z])|(?=.*[A-Z]))(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.{6,}))');
// *** 휴대폰 번호 정규식 *** //
let regPhone = /^01([0|1|2|7|8|9])?-([0-9]{3,4})?-([0-9]{4})$/;
// *** 예비 전화번호 정규식 *** //
let reservePhone = /^0([2|31|32|33|41|42|43|44|70])?-([0-9]{3,4})?-([0-9]{4})$/;
// *** 숫자 정규식 *** //
let numberCheck = /^[0-9]+$/;


// *** Register(회원가입) *** //
// ID 중복검사
function idCheck(id){
    const idForm = document.getElementById("IDForm");
    const idDiv = document.getElementById("IDdiv");

    $.ajax({
        url: '/api/userCheck/' + id,
        type: 'GET',
        success: (result) => {
            //AJAX 성공시 실행 코드
            if(id === ""){
                idDiv.previousElementSibling.className = "form-control is-invalid";
                idDiv.className = "invalid-feedback"
                idDiv.innerText = "입력해주세요.";
            } else if(id.length > 30){
                idDiv.previousElementSibling.className = "form-control is-invalid";
                idDiv.className = "invalid-feedback"
                idDiv.innerText = "id는 30자가 넘어가면 안됩니다.";
            } else if(result === ""){
                idDiv.previousElementSibling.className = "form-control is-valid";
                idDiv.className = "valid-feedback";
                idDiv.innerText = "사용가능한 아이디입니다.";
            } else {
                idDiv.previousElementSibling.className = "form-control is-invalid";
                idDiv.className = "invalid-feedback"
                idDiv.innerText = "중복된 아이디입니다. 사용이 불가능합니다.";
            }
        }
    });
}
// PWD 보안 강함, 보통, 약함
function pwdCheck(pwd){
    const pwdDiv = document.getElementById("pwdDiv");
    if(pwd === ""){
        pwdDiv.previousElementSibling.className = "form-control is-invalid";
        pwdDiv.className = "invalid-feedback"
        pwdDiv.style.color = "red";
        pwdDiv.textContent = "입력해주세요.";
    } else if(strongPassword.test(pwd)){
        pwdDiv.previousElementSibling.className = "form-control is-valid";
        pwdDiv.className = "valid-feedback";
        pwdDiv.style.color = "green";
        pwdDiv.textContent = "강함";
    } else if(mediumPassword.test(pwd)){
        pwdDiv.previousElementSibling.className = "form-control is-valid";
        pwdDiv.className = "valid-feedback";
        pwdDiv.style.color = "blue";
        pwdDiv.textContent = "보통";
    } else {
        pwdDiv.previousElementSibling.className = "form-control is-invalid";
        pwdDiv.className = "invalid-feedback"
        pwdDiv.style.color = "red";
        pwdDiv.textContent = "위험(영문, 특수문자, 숫자를 1가지 이상씩 넣어주세요)";
    }
}
// PWDChk 일치 확인
function pwdChkCheck(pwdChk){
    const pwd = document.getElementById("pwdInput");
    const pwdChkDiv = document.getElementById("pwdChkDiv");
    if(pwd.value !== pwdChk){
        pwdChkDiv.previousElementSibling.className = "form-control is-invalid";
        pwdChkDiv.className = "invalid-feedback"
        pwdChkDiv.style.color = "red";
        pwdChkDiv.textContent = "비밀번호가 일치하지 않습니다.";
    } else {
        pwdChkDiv.previousElementSibling.className = "form-control is-valid";
        pwdChkDiv.className = "valid-feedback";
        pwdChkDiv.style.color = "green";
        pwdChkDiv.textContent = "비밀번호가 일치합니다.";
    }
}

// SMS 인증 번호 전송
function SMSFormal(){
    const keyCheckForm = document.getElementById("keyCheckForm");
    const keyHiddenInput = document.getElementById("keyNumber");
    let hp = document.getElementById("hpInput").value;
    if(!regPhone.test(hp)){
        alert("번호입력이 잘못되었습니다. 다시 입력해주세요.(ex 010-1234-5678)");
    } else {
        $.ajax({
            url: '/smsApi/send-one/' + hp.replace(/-/g, ''),
            type: 'GET',
            success: (result) => {
                //AJAX 성공시 실행 코드
                alert(result); // 인증번호 가져온거 alert 띄워줌
                alert("인증번호가 발송되었습니다.");
                keyCheckForm.className = "";
                keyHiddenInput.value = result;
            }
        });
    }
}
// SMS 인증 번호 일치 확인
function SMSFormalCheck() {
    const keyHiddenInput = document.getElementById("keyNumber");
    const formalInput = document.getElementById("formalInput");
    const formalDiv = document.getElementById("formalDiv");
    if(keyHiddenInput.value === formalInput.value) {
        formalDiv.style.color = "green";
        formalDiv.textContent = "비밀번호가 일치합니다.";
    } else {
        formalDiv.style.color = "red";
        formalDiv.textContent = "비밀번호가 일치하지 않습니다.";
    }
}

// Email urlSelect 시 동작 함수
function urlSelected() {
    const urlSelect = document.getElementById("urlSelect");
    const urlCodeInput = document.getElementById("urlCodeInput");
    if(urlSelect.value === "0"){
        urlCodeInput.value = "";
        urlCodeInput.focus();
        return false;
    } else {
        urlCodeInput.value = urlSelect.value;
        return false;
    }
}

/* checkbox 한번에 체크 함수 */
function allAgreeCheck() {
    const allAgree = document.getElementById("allAgree");
    const useAgree = document.getElementById("useAgree");
    const dataAgree = document.getElementById("dataAgree");
    if(allAgree.checked){
        useAgree.checked = true;
        dataAgree.checked = true;
    } else {
        useAgree.checked = false;
        dataAgree.checked = false;
    }
}
/* checkbox 제거 시에 all체크 제거 함수 */
function agreeCheck() {
    const allAgree = document.getElementById("allAgree");
    const useAgree = document.getElementById("useAgree");
    const dataAgree = document.getElementById("dataAgree");
    if(!useAgree.checked){ allAgree.checked = false; }
    else { allAgree.checked = dataAgree.checked; }
}

// 우편번호 버튼 클릭 기능
function addressSearch() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var fullAddr = ''; // 최종 주소 변수
            var extraAddr = ''; // 조합형 주소 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                fullAddr = data.roadAddress;

            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                fullAddr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
            if(data.userSelectedType === 'R'){
                //법정동명이 있을 경우 추가한다.
                if(data.bname !== ''){
                    extraAddr += data.bname;
                }
                // 건물명이 있을 경우 추가한다.
                if(data.buildingName !== ''){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('addressInput').value = data.zonecode; //5자리 새우편번호 사용
            document.getElementById('subAddressInput').value = fullAddr;

            // 커서를 상세주소 필드로 이동한다.
            document.getElementById('detailAddressInput').focus();
        }
    }).open();
}

// Register 폼 제출 전 확인 script
function registerCheck() {
    // 아이디 입력 확인(30자 이하여야함)
    if (document.getElementById("IDdiv").className === "invalid-feedback"){
        document.getElementById("IDdiv").innerText = "사용가능한 아이디여야합니다.";
        document.getElementById("idInput").focus();
        return false;
    }

    // 패스워드 입력 확인(강함, 보통인지)
    if (document.getElementById("pwdDiv").className === "invalid-feedback") {
        document.getElementById("pwdDiv").previousElementSibling.className = "form-control is-invalid";
        document.getElementById("pwdDiv").className = "invalid-feedback"
        document.getElementById("pwdDiv").style.color = "red";
        document.getElementById("pwdDiv").textContent = "패스워드는 강함/보통 정도여야합니다.";
        document.getElementById("pwdDiv").focus();
        return false;
    }

    // 패스워드 Chk 확인
    if(document.getElementById("pwdInput").value !== document.getElementById("pwdChkInput").value){
        document.getElementById("pwdChkDiv").previousElementSibling.className = "form-control is-invalid";
        document.getElementById("pwdChkDiv").className = "invalid-feedback"
        document.getElementById("pwdChkDiv").style.color = "red";
        document.getElementById("pwdChkDiv").textContent = "비밀번호가 일치하지 않습니다.";
        document.getElementById("pwdChkInput").focus();
        return false;
    }

    // 휴대폰 인증번호 확인
    if(document.getElementById("keyCheckForm").className === "d-none") { // 휴대폰 인증 확인
        alert("휴대폰 인증을 해주세요");
        document.getElementById("hpInput").focus();
        return false;
    } else if(document.getElementById("keyNumber").value !== document.getElementById("formalInput").value) { // 인증번호 확인
        alert("인증번호를 확인해주세요.");
        const formalDiv = document.getElementById("formalDiv");
        formalDiv.style.color = "red";
        formalDiv.textContent = "인증번호를 확인해주세요.";
        document.getElementById("formalInput").focus();
        return false;
    }

}


//*** Category(Search) ***//
// 상품 검색
function clothSearch() {
    const guestSearchText = document.getElementById("guestSearchText").value;
    window.location.href="/guest/category?category=search&searchText=" + guestSearchText;
}

//*** ProductDetail ***//
// 1) 선택한 컬러에 해당하는 재고의 사이즈 목록 가져오기
function stockSizesGet() {
    const colorSelector = document.getElementById("colorSelector").value;
    const clothId = document.getElementById("clothId").value;

    $.ajax({
        type: 'get',
        url: '/api/stocks/size?colorCode=' + colorSelector + "&clothId=" + clothId,
        success: (result) => {
            //AJAX 성공시 실행 코드(* sizeSelector ID)
            const sizeSelector = document.getElementById("sizeSelector");

            // 1. sizeSelector Clear
            sizeSelector.innerHTML = `<option value = "0">사이즈 선택</option>`;
            // 2. sizeSelector Option 추가
            result.forEach(data => {
                const option = document.createElement('option');
                option.innerHTML = data.sizeName;
                option.value = data.sizeCode;
                sizeSelector.append(option);
            })
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}
// 2) 재고 가져오기
function stockGet() {
    const colorSelector = document.getElementById("colorSelector").value;
    const sizeSelector = document.getElementById("sizeSelector").value;
    const clothId = document.getElementById("clothId").value;

    // ajax로 Color/Size의 재고 가져와서 화면 보여주기
    $.ajax({
        type: 'GET',
        url: '/api/stocks/color/size?colorCode=' + colorSelector + '&sizeCode=' + sizeSelector + '&clothId=' + clothId,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            document.getElementById("buyBtn").value = result.stock.stockId;
            document.getElementById("cartAddBtn").value = result.stock.stockId;

            const stockOrderAmountSelectBox = document.getElementById("stockOrderAmountSelectBox");
            stockOrderAmountSelectBox.innerHTML
                  = '<tr class="border-top-1-ccc">' +
                    '    <td><b>color/size</b></td>' +
                    '    <td>' +
                    '        <input type = "hidden" id="stockId_' + result.stock.stockId + '" value = "' + result.stock.stockId + '">' +
                    '        <b>' + result.color.colorName + '</b>/<b>' + result.size.sizeName + '</b>' +
                    '    </td>' +
                    '</tr>' +
                    '<tr>' +
                    '    <td><b>수량</b></td>' +
                    '    <td><input type = "number" class="w-50 py-1" id = "count_' + result.stock.stockId + '" min = "1"' +
                    '               max = "' + result.stock.stockMaxCount + '"value = "1"' +
                    '               oninput = "detailPriceUpdate(this.value);"></td>' +
                    '</tr>' +
                    '<tr class="border-bottom-1-ccc">' +
                    '    <td><b>가격</b></td>' +
                    '    <td><input type = "hidden" id="stockSalePrice" value = "' + result.stock.salePrice + '">' +
                    '    <label id="stockTotalPrice">' + result.stock.salePrice + '원</label></td>' +
                    '</tr>';
        }, error:function(e) {
            alert("실패 에러남");
        }
    });
}
// 3) 가져온 재고 구매 수량 선택시 가격도 바꿔줌
function detailPriceUpdate(count){
    const stockTotalPrice = document.getElementById("stockTotalPrice");
    const stockSalePrice = document.getElementById("stockSalePrice").value;
    const totalPrice = stockSalePrice*count;
    stockTotalPrice.innerHTML = totalPrice + '원';
};
// 한개의 stock 정보를 가지고 주문페이지로 이동
function stockOrderFormMove(id) {
    if(id === ''){ alert("구매할 상품을 선택해주세요."); return false; }

    const stockId = document.getElementById("stockId_" + id); // 재고 목록들(stockIds)
    const count = document.getElementById("count_" + id); // 주문할 수량 가져오기(counts)
    let stockIdList = []; let countList = [];
    stockIdList.push(stockId.value); countList.push(count.value);

    window.location.href="/guest/orderForm?stockIds=" + stockIdList + "&counts=" + countList;
}
// checkbox 선택 stockList 가지고 주문페이지로 이동
function stockListOrderFormMove() {
    const cartIds = document.getElementsByName("cartIds"); // 화면에 있는 모든 Checkbox(cartNums)
    let stockIdList = [], countList = [];

    // checkBox에 체크되었는지 확인 후 stockIdList, countList 에 값 추가해주기
    for (let i = 0; i < cartIds.length; i++) {
        if (cartIds[i].checked === true) {
            let cartId = cartIds[i].value;
            const stockId = document.getElementById("stockId_" + cartId); // 재고 목록들(stockIds)
            const count = document.getElementById("count_" + cartId); // 주문할 수량 가져오기(counts)

            stockIdList.push(stockId.value);
            countList.push(count.value);
        }
    }

    if(stockIdList.length === 0) { alert("주문할 상품을 선택해주세요."); return false; }

    // 아임포트 토큰 가져오기 및 주문번호 금액 지정(보안 위해서 => 적립금을 지정해주어야해서 orderForm에서 DB 값 가져와서 처리하기로 함)
    // getAccessToken(saleTotalPrice);

    // 상품 주문 페이지로 이동
    window.location.href="/guest/orderForm?stockIds=" + stockIdList + "&counts=" + countList;

}

//*** I'mport(아임포트) 결제 API 관련 ***//
// 아임포트의 Token 가져오기 => 일회용 프록시 서버 사용해서 함
function getAccessToken(saleTotalPrice) {
    let urlLink = "https://api.iamport.kr/users/getToken";
    $.ajax({
        url: "https://cors-anywhere.herokuapp.com/" + urlLink,
        method: "post",
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify({
            imp_key: "", // REST API 키
            imp_secret: "" // REST API Secret
        }),
        success: (result) => {
            console.log("성공");
            console.log(result);

            const maxId = getOrderTableMaxId();

            // 아임포트 서버에 주문번호와 금액 DB 저장하기(인증 토큰, 주문번호, 결제 금액
            paymentPrepare(result.response.access_token, (maxId+1), saleTotalPrice);
        },
        error: function (e) {
            console.log("실패");
            console.log(e);
        }

    });
}
// 아임포트 서버에 주문번호와 금액 DB 저장하기
function paymentPrepare(accessToken, maxId, saleTotalPrice) {
    $.ajax({
        url: "https://cors-anywhere.herokuapp.com/https://api.iamport.kr/payments/prepare?_token=" + accessToken,
        method: "post",
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify({
            merchant_uid: maxId,    // 가맹점 주문번호
            amount: saleTotalPrice, // 결제 예정금액
        }),
        success: (result) => {
            console.log("성공");
            console.log(result);
        },
        error: function (e) {
            console.log("실패");
            console.log(e);
        }

    });
}

//*** Cart ***//
// 장바구니 추가
function cartAdd(id) {
    if(id === ''){ alert("장바구니에 추가할 상품을 선택해주세요."); return false; }

    const stockId = document.getElementById("stockId_" + id).value; // 재고 목록들(stockIds)
    const count = document.getElementById("count_" + id).value; // 주문할 수량 가져오기(counts)
    const formData = new FormData();
    formData.append("stockId", stockId);
    formData.append("count", count);

    // 메인 이미지 update 하기
    $.ajax({
        type:"POST",
        url: "/api/cart",
        processData: false,
        contentType: false,
        data: formData,
        success: function(result){
            alert("장바구니에 해당 상품이 추가되었습니다.");
            window.location.href="/guest/cartList";
        },
        err: function(err){
            alert("장바구니 추가 실패");
        }
    })
}
// 장바구니 삭제
function cartDel(cartId) {
    if(confirm(cartId + "번 장바구니를 정말 삭제하시겠습니까?")){
        // cart 1개 삭제하는 Ajax 함수
        $.ajax({
            type: 'DELETE',
            url: '/api/carts/' + cartId,
            success: (result) => {
                //AJAX 성공시 실행 코드
                alert(cartId + "번 장바구니가 삭제되었습니다.");
                // 화면에서 해당 요소 삭제해줌
                document.getElementById('tr_' + cartId).remove();
                // Total Price 업데이트
                totalPriceUpdate();
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    } else {
        alert(cartId + '번 삭제가 취소되었습니다.');
    }
}
// 장바구니 checkbox 선택목록 삭제 Or 장바구니 비우기
function cartAllOrCheckDel(switchStr) {
    let msg = '';
    if(switchStr === 'check'){ msg = "선택된 목록들을 정말 삭제하시겠습니까?" }
    else if(switchStr === 'all'){ msg = "장바구니를 정말 비우시겠습니까?" }

    if(confirm(msg)) {
        const cartIds = document.getElementsByName("cartIds"); // 화면에 있는 모든 Checkbox(cartNums)
        let cartDelList = []; // 삭제된 cartNums

        // checkBox에 체크되었는지 확인 후 삭제
        for (let i = 0; i < cartIds.length; i++) {
            if (switchStr === 'check') {
                if (cartIds[i].checked === true) {
                    let cartId = cartIds[i].value;
                    cartDelAjax(cartId);
                    cartDelList.push(cartId);
                }
            } else if (switchStr === 'all') {
                let cartId = cartIds[i].value;
                cartDelAjax(cartId);
                cartDelList.push(cartId);
            }
        }

        // 삭제되었음을 alert 띄워줌
        alert(cartDelList + "번 장바구니가 삭제되었습니다.");

        // 장바구니의 모든 목록을 삭제해준 것이라면 장바구니 목록이 없다는 Text를 띄워줌
        if(cartDelList.length === cartIds.length){
            document.getElementById("cartSizeZeroMsg").className = 'text-center';
        }

        // 화면에서 해당 요소 삭제해줌
        for (let i = 0; i < cartDelList.length; i++) {
            document.getElementById('tr_' + cartDelList[i]).remove();
        }

        // Total Price 업데이트
        totalPriceUpdate();

    }

    // stock 1개 삭제하는 Ajax 함수
    function cartDelAjax(cartId){
        $.ajax({
            type: 'DELETE',
            url: '/api/carts/' + cartId,
            success: (result) => {
                //AJAX 성공시 실행 코드
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    }
}
// 장바구니 checkBox 버튼 클릭 시 totalPrice 바꿔주기
function cartTotalPriceUpdate(checkType) {
    // 값을 넣어줄 Block 들
    const cartSelectTotalText = document.getElementById("cartSelectTotalText"); // 상품구매금액 0 + 배송비 0 = 합계: KRW 0
    const totalSalePriceView = document.getElementById("totalSalePriceView");   // Total 상품 금액
    const totalDeliPriceView = document.getElementById("totalDeliPriceView");   // Total 배송비 금액
    const totalPriceView = document.getElementById("totalPriceView");           // Total 구매 금액

    // 값을 가져올 Block 들
    const cartIds = document.getElementsByName("cartIds"); // 화면에 있는 모든 Checkbox(cartNums)
    let totalSalePrice = 0, totalDeliPrice = 0, totalPrice = 0;

    // 모두 체크가 되어있다면 전체 CheckBox 체크 해주기
    if(checkType === 'allCheck') { allCheck('cart'); }
    else { allCheckRemove('cart'); }

    // checkBox에 체크되었는지 확인 후 삭제
    for (let i = 0; i < cartIds.length; i++) {
        if(cartIds[i].checked === true){
            const salePrice = document.getElementById("salePrice_" + cartIds[i].value).textContent.split(' ')[1]; // 장바구니 상품 단가 금액
            const cartCount = document.getElementById("count_" + cartIds[i].value).value; // 장바구니 상품 수량
            const deliPrice = parseInt(document.getElementById("deliPrice_" + cartIds[i].value).textContent.replace(/원/, '')); // 장바구니 상품 배송비
            totalSalePrice  += salePrice * cartCount; // 상품단가 * 수량
            totalDeliPrice  += deliPrice;             // 상품 배송비
            totalPrice      += salePrice * cartCount + deliPrice;     // 상품 가격 + 배송비
        }
    }

    // Total 값 Text 띄워주기
    cartSelectTotalText.innerText = '상품구매금액 ' + totalSalePrice +
                                    ' + 배송비 ' + totalDeliPrice +
                                    ' = 합계: KRW ' + totalPrice;
    totalSalePriceView.innerText = 'KRW ' + totalSalePrice;
    totalDeliPriceView.innerText = '+ KRW ' + totalDeliPrice;
    totalPriceView.innerText = '= KRW ' + totalPrice;
}
// 장바구니 numberInput 버튼 값 변경 시 totalPrice 바꿔주기
function CountChangeUpdate(updateId, page) {
    const Count = document.getElementById(updateId).value;
    const salePrice = document.getElementById("salePrice_" + (updateId.split('_')[1])).textContent.split(' ')[1];
    const deliPrice = document.getElementById("deliPrice_" + (updateId.split('_')[1])).textContent.replace(/원/, '');
    const totalPrice = document.getElementById("totalPrice_" + (updateId.split('_')[1]));
    const plusPayBlock = document.getElementById("plusPay_" + (updateId.split('_')[1]));
    const plusPay = (plusPayBlock.textContent === '0원')?0:salePrice/100;
    totalPrice.innerText = 'KSW ' + (salePrice * Count + parseInt(deliPrice));
    plusPayBlock.innerText = (plusPay * Count) + '원';

    if(page === 'cartList') { cartTotalPriceUpdate('oneCheck'); }
    else if(page === 'orderForm') { orderTotalPriceUpdate(); }
}

//*** OrderForm ***//
// 주문 Form에서 수량 증감시 TotalPrice 업데이트
function orderTotalPriceUpdate() {
    // 값을 넣어줄 Block 들
    const orderSelectTotalText = document.getElementById("orderSelectTotalText");   // 상품구매금액 0 + 배송비 0 = 합계: KRW 0
    const totalOrderPriceView = document.getElementById("totalOrderPriceView");     // Total 상품 금액
    const totalUseMileageView = document.getElementById("totalUseMileageView");     // Total 배송비 금액
    const totalPriceView = document.getElementById("totalPriceView");               // Total 구매 금액
    const finishTotalPriceView = document.getElementById("finishTotalPriceView");   // 최종 Total 구매 금액

    // 값을 가져올 Block 들
    const stockIds = document.getElementsByName("stockIds"); // 화면에 있는 모든 Checkbox(cartNums)
    const useMileage = document.getElementById("useMileage").value; // 사용가능한 적립금 가져오기
    let totalSalePrice = 0, totalDeliPrice = 0, totalPrice = 0, mileage = 0;

    // checkBox에 체크되었는지 확인 후 삭제
    for (let i = 0; i < stockIds.length; i++) {
        const salePrice = document.getElementById("salePrice_" + stockIds[i].value).textContent.split(' ')[1]; // 상품 단가 금액
        const orderCount = parseInt(document.getElementById("count_" + stockIds[i].value).textContent); // 상품 수량
        const deliPrice = parseInt(document.getElementById("deliPrice_" + stockIds[i].value).textContent.replace(/원/, '')); // 상품 배송비
        totalSalePrice  += salePrice * orderCount;              // 상품단가 * 수량
        totalDeliPrice  += deliPrice;                           // 상품 배송비
        totalPrice      += salePrice * orderCount + deliPrice;  // 상품 가격 + 배송비
    }

    // Total 값 Text 띄워주기
    orderSelectTotalText.innerText = '상품구매금액 ' + totalSalePrice +
        ' + 배송비 ' + totalDeliPrice +
        ' = 합계: KRW ' + totalPrice;
    totalOrderPriceView.innerText = 'KRW ' + totalPrice;
    totalUseMileageView.innerText = '- KRW ' + (useMileage === ''?0:useMileage);
    totalPriceView.innerText = '= KRW ' + (totalPrice-useMileage);
    finishTotalPriceView.innerText = (totalPrice-useMileage) + '';
}
// 배송지 radioButton 별 작동 함수
function newDeliveryAddress() {
    document.getElementById("newReset").click();
}
function getMyDeliveryAddress(userId) {
    $.ajax({
        url: '/api/users/' + userId,
        type: 'GET',
        success: (result) => {
            //AJAX 성공시 실행 코드
            const deliName = document.getElementById("deliName");
            const addressInput = document.getElementById("addressInput");
            const subAddressInput = document.getElementById("subAddressInput");
            const detailAddressInput = document.getElementById("detailAddressInput");
            const baseHPInput = document.getElementById("baseHPInput");
            const secondHPInput = document.getElementById("secondHPInput");
            const thirdHPInput = document.getElementById("thirdHPInput");
            const idNameInput = document.getElementById("idNameInput");
            const urlCodeInput = document.getElementById("urlCodeInput");

            deliName.value = result.username;
            addressInput.value = result.addressNum;
            subAddressInput.value = result.addressSub;
            detailAddressInput.value = result.addressDetail;
            baseHPInput.value = result.hp.split('-')[0];
            secondHPInput.value = result.hp.split('-')[1];
            thirdHPInput.value = result.hp.split('-')[2];
            idNameInput.value = result.emailIdName;
            urlCodeInput.value = result.emailUrlCode;

        }
    });
}
// 결제 적립금 지정
function useMileageInput() {
    const useMileage = document.getElementById("useMileage"); // 사용가능한 적립금 가져오기
    const usableMileage = parseInt(document.getElementById("usableMileage").textContent); // 사용가능한 적립금 가져오기
    const totalOrderPriceView = parseInt(document.getElementById("totalOrderPriceView").textContent.split(' ')[1]);
    if(!numberCheck.test(useMileage.value) && useMileage.value != ''){
        alert("숫자만 입력 가능합니다.");
        useMileage.value = '';
    }
    if(useMileage.value > totalOrderPriceView){
        useMileage.value = totalOrderPriceView;
    }
    if(useMileage.value > usableMileage){
        useMileage.value = usableMileage;
    }

    orderTotalPriceUpdate();
}
function useMileageCheck() {
    const useMileage = document.getElementById("useMileage"); // 사용가능한 적립금 가져오기
    if(useMileage.value < 100 && useMileage.value !== '' && useMileage.value !== '0'){
        alert("적립금은 100원 이상부터 사용가능합니다.");
        useMileage.value = 0;
        orderTotalPriceUpdate();
    }
}

//*** OrderForm : 결제하기 ***//
// 0. 결제 방법 선택
function paymentMethod(method) {
    if(method === 'account') {
        document.getElementById("paySelectAccountInfo").className = 'fs-75 w-100';
        document.getElementById("paySelectCardInfo").className = 'fs-75 w-100 d-none';
    }
    else {
        document.getElementById("paySelectAccountInfo").className = 'fs-75 w-100 d-none';
        document.getElementById("paySelectCardInfo").className = 'fs-75 w-100';
    }
}
// 1. 결제 위변조 검증
function paymentsVerification() {
    const finishTotalPriceView = document.getElementById("finishTotalPriceView").textContent;
    const useMileage = document.getElementById("useMileage").value;

    const stockIds = document.getElementsByName("stockIds"); // 화면에 있는 모든 Checkbox(cartNums)
    const stockIdList = [], countList = [];

    for(let i = 0; i < stockIds.length; i++){
        const orderCount = parseInt(document.getElementById("count_" + stockIds[i].value).textContent); // 상품 수량

        stockIdList.push(stockIds[i].value);
        countList.push(orderCount);
    }

    const formData = new FormData();
    formData.append("stockIdList", stockIdList);
    formData.append("countList", countList);
    formData.append("finishTotalPriceView", finishTotalPriceView);
    formData.append("useMileage", useMileage);

    $.ajax({
        type: 'POST',
        url: '/api/order/payments_verification',
        processData: false,
        contentType: false,
        data: formData,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            if(result === "정상 결제시도"){ alert(result); getOrderTableMaxId(); }
            else { alert(result); }
        }, error:function(e) {
            alert("실패 에러남");
        }
    });
}
// 2. 주문 테이블의 Id 최대값 가져오기(주문번호로 사용 될 예정)
function getOrderTableMaxId() {
    // ajax로 Color/Size의 재고 가져와서 화면 보여주기
    $.ajax({
        type: 'GET',
        url: '/api/order/maxId',
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            requestPay(result);
        }, error:function(e) {
            alert("실패 에러남");
        }
    });
}
// 3. 결제 전 체크
function paymentCheck() {
    // FormData 정보들
    /** 배송관련 Data **/
    const deliName = document.getElementById("deliName");
    const addressNum = document.getElementById("addressInput");
    const addressSub = document.getElementById("subAddressInput");
    const addressDetail = document.getElementById("detailAddressInput");
    const hp = document.getElementById("baseHPInput").value + '-' +
        document.getElementById("secondHPInput").value + '-' +
        document.getElementById("thirdHPInput").value;
    const reserveHp = document.getElementById("baseTelInput").value + '-' +
        document.getElementById("secondTelInput").value + '-' +
        document.getElementById("thirdTelInput").value;
    const emailIdName = document.getElementById("idNameInput");
    const emailUrlCode = document.getElementById("urlCodeInput");
    const userMessage = document.getElementById("userMessage").value;
    const usePlus = document.getElementById("useMileage").value;
    const realPrice = document.getElementById("finishTotalPriceView").textContent;
    const totalMileage = document.getElementById("totalMileage").textContent.replace(/원/, '');
    const paymentsAgree = document.getElementById("paymentsAgree");

    // 결제 동의 체크 여부 확인
    if (paymentsAgree.checked === false) {
        alert("결제정보와 유의사항을 확인하시고 결제버튼 위에 동의를 체크해주세요.");
        return false;
    }
    // 받으시는 분 입력 확인
    if (deliName.value === ""){
        alert("배송정보 중 배송 받으시는 분에 대해서 입력하지 않으셨습니다. 입력 후 결제해주세요.");
        deliName.focus();
        return false;
    }
    // 우편번호 입력
    if (addressNum.value === ""){
        alert("배송정보 중 우편번호가 입력되지 않으셨습니다. 입력 후 결제해주세요.");
        addressNum.focus();
        return false;
    }
    // 배송 주소 정보 입력
    if (addressSub.value === ""){
        alert("배송정보 중 주소 정보가 입력되지 않으셨습니다. 입력 후 결제해주세요.");
        addressSub.focus();
        return false;
    }
    // 배송 주소 상세정보 입력
    if (addressDetail.value === ""){
        alert("배송정보 중 주소 상세 정보가 입력되지 않으셨습니다. 입력 후 결제해주세요.");
        addressDetail.focus();
        return false;
    }
    // 배송 전화번호 입력
    if (!regPhone.test(hp)) {
        alert("번호입력이 잘못되었습니다. 다시 입력해주세요.(ex 010-1234-5678)");
        document.getElementById("baseHPInput").focus();
        return false;
    }
    // 이메일 아이디
    if (emailIdName.value === ""){
        alert("배송정보 중 이메일 ID가 입력되지 않으셨습니다. 입력 후 결제해주세요.");
        emailIdName.focus();
        return false;
    }
    // 이메일 URL
    if (emailUrlCode.value === ""){
        alert("배송정보 중 이메일 url이 선택되지 않으셨습니다. 입력 후 결제해주세요.");
        emailUrlCode.focus();
        return false;
    }


    const formData = new FormData();
    formData.append("reserveHp", reserveHp);                          // 예비 전화번호
    formData.append("userMessage", userMessage);                      // 고객 메세지
    formData.append("usePlus", usePlus);                              // 사용 적립금
    formData.append("totalMileage", totalMileage);                    // 추가될 적립금
    formData.append("deliName", deliName.value);                      // 받는 사람
    formData.append("addressNum", addressNum.value);                  // 우편번호
    formData.append("addressSub", addressSub.value);                  // 배송 주소
    formData.append("addressDetail", addressDetail.value);            // 배송 상세주소
    formData.append("hp", hp);                                        // 핸드폰번호
    formData.append("emailIdName", emailIdName.value);                // 이메일 아이디
    formData.append("emailUrlCode", emailUrlCode.value);              // 이메일 URL
    formData.append("realPrice", realPrice);                          // 최종 결제 금액

    alert("체크 완료!!!");
    return formData;
}
// 4. 아임포트 결제 화면 실행
// IMPORT의 결제 시스템 API : 현재 등록된 PG정보가 없다고 뜸, 결제 구현 중 아직 해야할 것 많음 내일 꼭 다 끝낸다 //
const IMP = window.IMP;
IMP.init("imp81234382"); // 예: imp00000000
function requestPay(orderMaxId) {
    const finishTotalPriceView = document.getElementById("finishTotalPriceView").textContent;
    const stockIds = document.getElementsByName("stockIds"); // 화면에 있는 모든 Checkbox(cartNums)
    let orderStockListStr = "";
    let stockIdList = [], stockCountList = [];

    const formData = paymentCheck(); if(!formData){ return false; };

    // 주문 정보 Info(상품 이름, 상품 옵션<color, size>)
    for(let i = 0; i < stockIds.length; i++){
        const count = document.getElementById("count_" + stockIds[i].value).textContent;
        const clothName = document.getElementById("clothName_" + stockIds[i].value).textContent;
        const stockColorSize = document.getElementById("stockColorSize_" + stockIds[i].value).textContent;

        orderStockListStr += "(" + clothName + " " + stockColorSize + ") ";
        stockIdList.push(stockIds[i].value); stockCountList.push(count);
    }
    alert(orderStockListStr + " : " + finishTotalPriceView + "원");

    // 무통장 입금인지, 카드 결제인지 확인하기 위한 부분
    const paymentAccount = document.getElementById("account").checked;
    if(finishTotalPriceView === '0') {
        formData.append("merchantUid", "1-13" + orderMaxId);  // 주문번호
        formData.append("orderState", "결제완료");               // 주문상태
        formData.append("payOption", "mileage");              // 결제수단
        formData.append("stockIdList", stockIdList);                // 재고 번호 리스트
        formData.append("stockCountList", stockCountList);          // 재고별 주문 수량

        $.ajax({
            url: "/api/order",
            type : "POST",
            processData: false,
            contentType: false,
            data: formData,
            success: (result) => {
                //AJAX 성공시 실행 코드
                alert("결제 성공");
                window.location.href="/guest/orderList";
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    } else if(paymentAccount){
        const depositName = document.getElementById("depositName");
        const depositAccount = document.getElementById("depositAccount");
        if(depositName.value === "") { alert("입금자명이 입력되지 않았습니다. 입력 후 결제버튼을 눌러주세요."); depositName.focus(); return false; }
        if(depositAccount.value === "") { alert("입금계좌가 선택되지 않았습니다. 선택 후 결제버튼을 눌러주세요."); depositAccount.focus(); return false; }
        formData.append("depositName", depositName.value);          // 입금자명
        formData.append("depositAccount", depositAccount.value);    // 입금계좌
        formData.append("merchantUid", "1-13" + orderMaxId);  // 주문번호
        formData.append("orderState", "입금전");               // 주문상태
        formData.append("payOption", "account");              // 결제수단
        formData.append("stockIdList", stockIdList);                // 재고 번호 리스트
        formData.append("stockCountList", stockCountList);          // 재고별 주문 수량

        $.ajax({
            url: "/api/order",
            type : "POST",
            processData: false,
            contentType: false,
            data: formData,
            success: (result) => {
                //AJAX 성공시 실행 코드
                alert("결제 성공");
                window.location.href="/guest/orderList";
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    } else {
        // Import API 블러오기
        IMP.request_pay({
            pg: "kcp.INIpayTest",
            pay_method: "card",
            merchant_uid: "1-17" + orderMaxId,
            name: orderStockListStr,
            amount: finishTotalPriceView,
            buyer_email: formData.get("emailIdName") + "@" + formData.get("emailUrlCode"),
            buyer_name: "포트원 기술지원팀",
            buyer_tel: formData.get("hp"),
            buyer_addr: formData.get("addressSub"),
            buyer_postcode: formData.get("addressNum"),
        }, function (rsp) { // callback
            //rsp.imp_uid 값으로 결제 단건조회 API를 호출하여 결제결과를 판단합니다.
            if(rsp.success){
                console.log(rsp);
                formData.append("imp_uid", rsp.imp_uid);                    // 결제 고유번호
                formData.append("merchantUid", rsp.merchant_uid);           // 주문번호
                formData.append("orderState", "결제완료");              // 주문상태
                formData.append("payOption", rsp.pay_method);               // 결제수단
                formData.append("stockIdList", stockIdList);                // 재고 번호 리스트
                formData.append("stockCountList", stockCountList);          // 재고별 주문 수량
                $.ajax({
                    url: "/api/order",
                    type : "POST",
                    processData: false,
                    contentType: false,
                    data: formData,
                    success: (result) => {
                        //AJAX 성공시 실행 코드
                        alert("결제 성공");
                        window.location.href="/guest/orderList";
                    }, error:function(e) {
                        alert("error: " + e);
                    }
                });
            } else {
                alert("결제에 실패하였습니다. 에러 내용: " + rsp.error_msg);
            }
        });
    }
}


//**** Board 게시글 관련 스크립트 ****//
let searchType = '제목';                                        // 게시글 검색 타입 (제목, 내용, 작성자)
let searchInput = '';                                          // 게시글 검색 텍스트(Text)
let pageSize = 10;                                             // 게시글 페이지 당 글 갯수
let writeState = '', contentState = '';                        // 답글상태, 문의구분
let firstDay = '', lastDay = '';                               // 시작, 끝 날짜
let name = '';                                                 // Page Title Name
// --- 게시글 검색 버튼 클릭 --- //
function boardsGet(){
    // 검색 요소 Parameter 변수
    name = document.getElementById("title").value;
    searchType = document.getElementById("searchType").value;
    searchInput = document.getElementById("searchInput").value;
    pageSize = document.getElementById("pageSize").value;
    // 검색 요소 - Q&A일때만
    if(name === "Q&A"){
        writeState = document.getElementById("writeState").value;
        contentState = document.getElementById("contentState").value;
    }
    // 검색 요소 - 날짜 변수
    const dayType = document.getElementById("dayType").value;
    if(dayType !== ""){
        const todayDate = new Date(); const date = new Date(); date.setDate(todayDate.getDate() - dayType);
        lastDay = todayDate.toISOString().substring(0, 10); firstDay = date.toISOString().substring(0, 10);
    }

    // ajax로 검색결과의 상품 가져와서 목록 Update
    $.ajax({
        type: 'GET',
        url: '/api/boards?searchType=' + searchType + '&searchInput=' + searchInput + '&size=' + pageSize +
            '&writeState=' + writeState + '&contentState=' + contentState +
            "&firstDay=" + firstDay + "&lastDay=" + lastDay + "&boardState=" + name,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const boardListTBody = document.getElementById("boardListTBody");
            boardListTBody.innerHTML = '';

            // 1) 가져온 목록 개수 뿌려주는 ListView의 Head 부분
            const getPageCountText = document.getElementById("getPageCountText");
            getPageCountText.innerHTML = '검색 ' + result.pageObject.numberOfElements + '건 /'
                + ' 총 ' + result.pageObject.totalElements + '건';

            // 2) 가져온 목록 뿌려주는 ListView의 Body 부분
            result.contentList.forEach(data => {
                // tr 태그
                const tr = document.createElement('tr');
                tr.className = "td-py10 td-px15";
                tr.id = data.num;
                // Q&A일 경우
                let boardTdTopList = '', boardTdBottomList = '', refImage = '';
                if(data.board.boardState === "FAQ" || data.board.boardState === "Q&A")      // 게시글 분류/문의구분 : FAQ, Q&A
                { boardTdTopList += '<td>' + data.board.contentState + '</td>'; }
                if(data.board.boardState === "REVIEW" || data.board.boardState === "Q&A")   // 상품 이미지 : Review, Q&A
                { boardTdTopList += '<td><img src="/uploadBoardImageView/' + data.boardFileList.get(0).fileId + '" alt="" width = "50px" height = "60px"></td>'; }
                if(data.board.boardState === "Q&A")                                         // 게시글 답변상태 : Q&A
                { boardTdBottomList += '<td>' + data.board.writeState + '</td>'; }
                if(data.board.boardState === "REVIEW")                                      // 게시글 방문횟수 : Review
                { boardTdBottomList += '<td>' + data.board.readCnt + '</td>'; }
                if(data.board.refLevel > 0)                                                 // 답글 여부 확인
                { refImage += '&nbsp;<img src = "${project}ascloimage/re.png" width = "20" height = "15">'}
                // td 태그 추가
                tr.innerHTML =  '<!-- 게시글 NO : All -->' +
                                '<th scope="row">' + data.board.num + '</th>' +
                                boardTdTopList +
                                '<!-- 게시글 제목 : All -->' +
                                '<td><a href="/board/boardView?name=' + data.board.boardState + '&num=' + data.board.num + '">' +
                                         refImage + data.board.subject + '</a></td>' +
                                '<!-- 게시글 작성자 : All -->' +
                                '<td>' + data.user.username + '</td>' +
                                '<!-- 게시글 작성날짜 : All -->\n' +
                                '<td>' + data.board.regDate + '</td>' +
                                boardTdBottomList;
                boardListTBody.append(tr);
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
function boardPaging(page){
    name = document.getElementById("title").value;

    // ajax로 검색결과의 상품 가져와서 목록 Update
    $.ajax({
        type: 'GET',
        url: '/api/boards?searchType=' + searchType + '&searchInput=' + searchInput + '&size=' + pageSize +
            '&writeState=' + writeState + '&contentState=' + contentState +
            "&firstDay=" + firstDay + "&lastDay=" + lastDay + "&boardState=" + name,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const boardListTBody = document.getElementById("boardListTBody");
            boardListTBody.innerHTML = '';

            // 1) 가져온 목록 개수 뿌려주는 ListView의 Head 부분
            const getPageCountText = document.getElementById("getPageCountText");
            getPageCountText.innerHTML = '검색 ' + result.pageObject.numberOfElements + '건 /'
                + ' 총 ' + result.pageObject.totalElements + '건';

            // 2) 가져온 목록 뿌려주는 ListView의 Body 부분
            result.contentList.forEach(data => {
                // tr 태그
                const tr = document.createElement('tr');
                tr.className = "td-py10 td-px15";
                tr.id = data.num;
                // Q&A일 경우
                let boardTdTopList = '', boardTdBottomList = '', refImage = '';
                if(data.board.boardState === "FAQ" || data.board.boardState === "Q&A")      // 게시글 분류/문의구분 : FAQ, Q&A
                { boardTdTopList += '<td>' + data.board.contentState + '</td>'; }
                if(data.board.boardState === "REVIEW" || data.board.boardState === "Q&A")   // 상품 이미지 : Review, Q&A
                { boardTdTopList += '<td><img src="/uploadBoardImageView/' + data.boardFileList.get(0).fileId + '" alt="" width = "50px" height = "60px"></td>'; }
                if(data.board.boardState === "Q&A")                                         // 게시글 답변상태 : Q&A
                { boardTdBottomList += '<td>' + data.board.writeState + '</td>'; }
                if(data.board.boardState === "REVIEW")                                      // 게시글 방문횟수 : Review
                { boardTdBottomList += '<td>' + data.board.readCnt + '</td>'; }
                if(data.board.refLevel > 0)                                                 // 답글 여부 확인
                { refImage += '&nbsp;<img src = "${project}ascloimage/re.png" width = "20" height = "15">'}
                // td 태그 추가
                tr.innerHTML =  '<!-- 게시글 NO : All -->' +
                    '<th scope="row">' + data.board.num + '</th>' +
                    boardTdTopList +
                    '<!-- 게시글 제목 : All -->' +
                    '<td><a href="/board/boardView?name=' + data.board.boardState + '&num=' + data.board.num + '">' +
                    refImage + data.board.subject + '</a></td>' +
                    '<!-- 게시글 작성자 : All -->' +
                    '<td>' + data.user.username + '</td>' +
                    '<!-- 게시글 작성날짜 : All -->\n' +
                    '<td>' + data.board.regDate + '</td>' +
                    boardTdBottomList;
                boardListTBody.append(tr);
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
// --- Q&A, REVIEW 글쓰기 : 상품 선택하기 --- //
// 1. popup 창 열기 //
function withItemListPage() {
    /*
	 * window.open("파일명", "윈도우명", "창에 대한 속성")\
	 * url = "주소?속성 = " + 속성값;   -> get방식
	 */
    const url = "/popup/withItemsSelect";
    window.open(url, "withItem", "menubar=no, width=1000, height = 600");
}
// 2. popup 창에서 상품명 클릭 //
function withItemSelected(clothId) {
    opener.document.getElementById("imageDiv").innerHTML = '<img src="/uploadMainImageView/' + clothId + '" class="w-100 h-100"/>';
    opener.document.getElementById("clothId").value = clothId;
    opener.document.getElementById("clothName").innerText = document.getElementById("clothName_" + clothId).textContent;
    self.close();
}