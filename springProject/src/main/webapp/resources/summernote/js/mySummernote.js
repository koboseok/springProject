$(document).ready(function() {
    $('#summernote').summernote({
        width : 1000, // 에디터 넓이
        height: 600, // 에디터 높이
        lang : 'ko-KR',  // 언어 : 한국어 
        
        // 이미지 업로드 이벤트가 발생했을 때 ( 이미지가 업로드 되었을 때  ) callbacks 함수 수행
        callbacks : {
            onImageUpload : function(files){
                // files : 업로드된 이미지가 배열로 저장되어 있다.
                // editor == this : 이미지가 업로드된 섬머노트 에디터 
                
                sendFile(files[0], this)
            }
        }



    });
  });

  // 섬머노트에 업로드된 이미지를 비동기로 서버로 전송하여 저장하는 함수
  function sendFile(file, editor){

    var formData = new FormData();
    // FormData : form 태그 내부 값 전송을 위한 객체로 
    // 추가된 값을 k = v 형태로 쉽게 생성해주는 객체

    formData.append("uploadFile",file);

    $.ajax({
        url : "insertImage",
        type : "post",
        enctype : "multipart/form-data", // 파일 전송 형식으로 인코딩 지정 
        data : formData,
        contentType : false, // 서버로 전송되는 데이터 형식
        // 기본값 :  application/x-www-form-urlencoded; charset=utf-8
        //           == 텍스트 
        // false : 바이트 코드 있는 그대로 

        cache : false, // 메모리 저장 없이 바로 서버로 저장
        processData : false, // 서버로 전달되는 값을 쿼리스트링으로 전달할 경우 true, 아니면 false
                            // 파일 전송 시 false로 지정
        dataType : "json", // 자바의 객체와 js의 객체의 형태가 다르기 때문에 json포맷으로 설정
        success : function(at){
            // console.log(at);
            
            // 자바스크립트를 이용한 contextPath 얻어오는 방법 
            var contextPath = location.pathname.substring(0, window.location.pathname.indexOf("/",2));
            // 현재 주소에서 처음부터 두번째 / 까지만 짤라서 저장하는 변수 구문
            // localhost:8080/spring         board/2/insert

            // 저장된 이미지를 summernote 에디터에 반영(삽입)
            $(editor).summernote('editor.insertImage',contextPath + at.filePath + "/" + at.fileName);
                                                    // localhost:8080/spring + /resources/infoImages / 2020~~.jpg
            
        }
        


    });


  }