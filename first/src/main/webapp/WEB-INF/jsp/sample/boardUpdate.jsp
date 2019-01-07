<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
</head>
<body>
	<form id="frm" name="frm" enctype="multipart/form-data">
		<table class="board_view">
			<colgroup>
				<col width="15%"/>
				<col width="35%"/>
				<col width="15%"/>
				<col width="35%"/>
			</colgroup>
			<caption>게시글 상세</caption>
			<tbody>
				<tr>
					<th scope="row">글 번호</th>
					<td>${map.IDX }<input type="hidden" id="IDX" name="IDX" value="${map.IDX }"></td>
					<th scope="row">조회수</th>
					<td>${map.HIT_CNT }</td>
				</tr>
				<tr>
					<th scope="row">작성자</th>
					<td>${map.CREA_ID }</td>
					<th scope="row">작성시간</th>
					<td>${map.CREA_DTM }</td>
				</tr>
				<tr>
					<th scope="row">제목</th>
					<td colspan="3">
						<input type="text" id="TITLE" name="TITLE" class="wdp_90" value="${map.TITLE }"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="view_text">
						<textarea rows="20" cols="100" title="내용" id="CONTENTS" name="CONTENTS">${map.CONTENTS }</textarea>
					</td>
				</tr>
				<tr>
                    <th scope="row">첨부파일</th>
                    <td colspan="3">
                        <div id="fileDiv">               	<!-- varStatus: forEach문에서 목록의 위치값을 알려줌 -->	
                            <c:forEach var="row" items="${list }" varStatus="var">
                                <p>										<!-- var.index: 0부터의 순서 -->
                                    <input type="hidden" id="IDX" name="IDX_${var.index }" value="${row.IDX }">
                                    <a href="#this" id="name_${var.index }" name="name_${var.index }">${row.ORIGINAL_FILE_NAME }</a>
                                    (${row.FILE_SIZE }kb)
                                    <input type="file" id="file_${var.index }" name="file_${var.index }">
                                    <a href="#this" class="btn" id="delete_${var.index }" name="delete_${var.index }">삭제</a>
                                </p>
                            </c:forEach>
                        </div>
                    </td>
                </tr>
			</tbody>
		</table>
	</form>
	
	<a href="#this" class="btn" id="addFile">파일 추가</a>
	<a href="#this" class="btn" id="list">목록으로</a>
	<a href="#this" class="btn" id="update">저장하기</a>
	<a href="#this" class="btn" id="delete">삭제하기</a>
	
	<%@ include file="/WEB-INF/include/include-body.jspf" %>
	<script type="text/javascript">
	var gfv_count = '${fn:length(list)+1}';
	
		$(document).ready(function(){
			$("#list").on("click", function(e){ //목록으로 버튼
				e.preventDefault();
				fn_openBoardList();
			});
			
			$("#update").on("click", function(e){ //저장하기 버튼
				e.preventDefault();
				fn_updateBoard();
			});
			
			$("#delete").on("click", function(e){ //삭제하기 버튼
				e.preventDefault();
				fn_deleteBoard();
			});
			
			$("#addFile").on("click", function(e){ //파일 추가 버튼
                e.preventDefault();
                fn_addFile();
            });
             //모든 a태그 중에 name이 delete로 시작하는 거
            $("a[name^='delete']").on("click", function(e){ //기존파일 삭제 버튼
                e.preventDefault();
                fn_deleteFile($(this));
            });
		});
		
		function fn_openBoardList(){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/openBoardList.do' />");
			comSubmit.submit();
		}
		
		function fn_updateBoard(){
			var comSubmit = new ComSubmit("frm");
			comSubmit.setUrl("<c:url value='/sample/updateBoard.do' />");
			comSubmit.submit();
		}
		
		function fn_deleteBoard(){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/deleteBoard.do' />");
			comSubmit.addParam("IDX", $("#IDX").val());
			comSubmit.submit();
		}
		
		function fn_addFile(){
			//fn_addFile()호출되면 밑에있는 2개다실행(물론,click시에만 {}실행). 즉 ++가 제일 마지막에 적용되야 ++적용전 만들어진 번호가 id에 들어가서 click이 가능.
			//++를 다른 곳에 넣으면 delete에 하나 더 큰 번호가 들어가서 그 번호에 해당하는 id가 없기 때문에 click이벤트가 아예 실행이 되지 않는다.
            var str = "<p><input type='file' id='file_"+(gfv_count)+"' name='file_"+(gfv_count)+"'> <a href='#this' class='btn' id='delete_"+(gfv_count)+"' name='delete_"+(gfv_count)+"'>"+(gfv_count)+"삭제</a></p>";
            $("#fileDiv").append(str);
           	$("#delete_"+(gfv_count++)).on("click", function(e){ //삭제 버튼
				e.preventDefault();
                fn_deleteFile($(this));
            });
            /* 따라서 이렇게 해도 동일기능 가능(맨 마지막에 ++해서 계속해서 번호가 바뀌도록 설정. delete방법이 위와 다르므로 문제없이 등록,삭제가능.)
            var str = "<p><input type='file' id='file_"+(gfv_count)+"' name='file_"+(gfv_count)+"'> <a href='#this' class='btn' id='delete_"+(gfv_count)+"' name='delete_"+(gfv_count++)+"'>삭제</a></p>";
            $("#fileDiv").append(str);
            $("a[name^='delete']").on("click", function(e){
                e.preventDefault();
                fn_deleteFile($(this));
            }); */
        }
         
        function fn_deleteFile(obj){
            obj.parent().remove();
        }
	</script>
</body>
</html>