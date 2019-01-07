<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<!-- 매번 공통으로 사용할거라 조각코드로 빼놓고 include함 -->
<%@ include file="/WEB-INF/include/include-header.jspf" %>
</head>
<body>
	<h2>게시판 목록</h2>
    <table class="board_list">
        <colgroup>
            <col width="10%"/>
            <col width="*"/>
            <col width="15%"/>
            <col width="20%"/>
        </colgroup>
        <thead>
        	<tr>
        		<th scope="col">글번호</th>
        		<th scope="col">제목</th>
        		<th scope="col">조회수</th>
        		<th scope="col">작성일</th>
        	</tr>
        </thead>
        <tbody>
        	<%-- 
        	<!-- mvc게시판 만들때 request.setAttribute로 올려둔걸 쓸 때 foreach사용. struts에선 iterate사용. spring에선 jstl사용. -->
        	<c:choose>
        		<c:when test="${fn:length(list) > 0 }">
        			<c:forEach items="${list }" var="row">
        				<tr>
        					<td>${row.IDX }</td>
        					<td class="title">
        						<a href="#this" name="title">${row.TITLE }</a>
        						<input type="hidden" id="IDX" value="${row.IDX }">
        					</td>
        					<td>${row.HIT_CNT }</td>
        					<td>${row.CREA_DTM }</td>
        				</tr>
        			</c:forEach>
        		</c:when>
        		<c:otherwise>
        			<tr>
        				<td colspan="4">조회된 결과가 없습니다.</td>
        			</tr>
        		</c:otherwise>
        	</c:choose>
        	 --%>
        </tbody>
	</table>
	<div id="PAGE_NAVI"></div> <!-- 페이징 태그가 그려질 부분 -->
	<input type="hidden" id="PAGE_INDEX" name="PAGE_INDEX" /> <!-- 현재 페이지 번호가 저장 -->
	
	<br/>
	<a href="#this" class="btn" id="write">글쓰기</a>
	
	<%@ include file="/WEB-INF/include/include-body.jspf" %>
	<script type="text/javascript">
		$(document).ready(function() {
			fn_selectBoardList(1); //최초에 화면이 호출되면 1페이지의 내용을 조회
			$("#write").on("click", function(e) { //글쓰기 버튼
				//현재 이벤트의 기본 동작을 중단.(a태그의 기본 기능인 url이동 기능이 실행되지 않음. 즉, url에 #this가 붙지않음)
				//이게 없으면 a태그에 걸린 이벤트를 수행하면서도 a태그의 기본기능인 url로 이동하는 기능까지 모두 수행
				//또한 이게 없으면 이벤트가 전파되어 이걸 클릭했을 때 다른 기능이 같이 실행됨(said 강사님)
				//e.dtopPropagation();도 같은 기능이지만 url에 #this가 넘어가는걸 막지못한다.
				e.preventDefault();
				fn_openBoardWrite();
			});
			
			$("a[name='title']").on("click", function(e) {//제목
				e.preventDefault();
				//$(this): 게시글 제목<a>태그
				fn_openBoardDetail($(this));
			});
		});
		
		function fn_openBoardWrite() {
			//자바스크립트 객체 생성
			var comSubmit = new ComSubmit();
			//호출하고 싶은 주소를 입력 / JSTL 태그(c태그)를 이용해서 ContextPath를 자동으로 붙음.
			comSubmit.setUrl("<c:url value='/sample/openBoardWrite.do'/>");
			comSubmit.submit();
		}
		
		function fn_openBoardDetail(obj) {
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/openBoardDetail.do'/>");
			//obj.parent().find("#IDX").val(): <a> 태그의 부모 노드 내에서 IDX라는 값을 가진 태그를 찾아서, 그 태그의 값을 가쟈와라.
			comSubmit.addParam("IDX", obj.parent().find("#IDX").val());
			comSubmit.submit();
		}
		//pageNo: 호출하고자 하는 페이지 번호
		function fn_selectBoardList(pageNo) {
			var comAjax = new ComAjax();
			comAjax.setUrl("<c:url value='/sample/selectBoardList.do' />");
			//setCallback(): Ajax 요청이 완료된 후 호출될 함수의 이름을 지정
			comAjax.setCallback("fn_selectBoardListCallback");
			comAjax.addParam("PAGE_INDEX", pageNo); //현제 페이지
			comAjax.addParam("PAGE_ROW", 15); //한 페이지에 보여줄 행의 수
			comAjax.ajax();
		}
		//data: 서버에서 전송된 json 형식의 결과값
		function fn_selectBoardListCallback(data) {
			var total = data.TOTAL;
			var body = $("table>tbody");
			body.empty();
			if (total == 0) { //tbody태그의 c:otherwise태그와 동일한 기능
				var str = "<tr><td colspan='4'>조회된 결과가 없습니다.</td></tr>";
				body.append(str);
			} else {
				//var 변수명 = {} " 이렇게 선언을 하면 Object가 만들어지고, 거기에 각각 key와 value 형식으로 값을 추가할 수 있다.
				var params = {
					divId : "PAGE_NAVI",
					pageIndex : "PAGE_INDEX",
					totalCount : total,
					recordCount : 15,
					eventName : "fn_selectBoardList"
					
				};
				gfn_renderPaging(params);

				var str = "";
				//tbody태그의 c:forEach태그와 동일한 기능 수행
				//data.list(서버에서 보내준 데이터)이용해서 jQuery의 .each함수(선택한 요소가 여러 개일 때 각각에 대하여 반복하여 함수를 실행)로 HTML 태그를 만듦
				$.each(data.list, function(key, value) {
					str += "<tr>"
							+ "<td>" + value.IDX + "</td>"
							+ "<td class='title'>"
								+ "<a href='#this' name='title'>" + value.TITLE + "</a>"
								+ "<input type='hidden' id='IDX' value=" + value.IDX + ">"
							+ "</td>" 
							+ "<td>" + value.HIT_CNT + "</td>" 
							+ "<td>" + value.CREA_DTM + "</td>" 
							+ "</tr>";
				});
				body.append(str);

				$("a[name='title']").on("click", function(e) { //제목
					e.preventDefault();
					fn_openBoardDetail($(this));
				});
			}
		}
	</script>
</body>
</html>