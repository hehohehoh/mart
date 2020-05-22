<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false"
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath }" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>타이틀</title>
	<style>
		table{ margin-left: 2%; width: 60%}
		.bar{ width: 20%; text-align: center; background-color: #FF9933;}
		#tr_btn_modify{display: none;}
	</style>
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">
		function readURL(input){
			if(input.files && input.files[0]){
				var reader = new FileReader();
				reader.onload = function(e){
					$('#preview').attr('src', e.target.result);
				}
				reader.readAsDataURL(input.files[0]);
			}
		}
		
		function backToList(obj){
			obj.action = "${contextPath}/board04/listArticles.do";
			obj.submit();
		}
		
		function fn_enable(obj){
			document.getElementById("i_title").disabled = false;
			document.getElementById("i_content").disabled = false;
			document.getElementById("i_imageFileName").disabled = false;
			document.getElementById("tr_btn").style.display = "none";
			document.getElementById("tr_btn_modify").style.display = "block";
			
		}
		
		function fn_modify_article(obj){
			obj.action = "${contextPath}/board04/modArticle.do";
			obj.submit();
		}
		
		function fn_remove_article(url, articleNO){
			var form = document.createElement("form");
			form.setAttribute("method", "post");
			form.setAttribute("action", url);
			
			var articleNOInput = document.createElement("input");
			articleNOInput.setAttribute("type", "hidden");
			articleNOInput.setAttribute("name", "articleNO");
			articleNOInput.setAttribute("value", articleNO);
			
			form.appendChild(articleNOInput);
			document.body.appendChild(form);
			form.submit();
		}
		
		function fn_reply_form(url, parentNO){
			var form = document.createElement("form");
			form.setAttribute("method", "post");
			form.setAttribute("action", url);
			
			var parentNOInput = document.createElement("input");
			parentNOInput.setAttribute("type", "hidden");
			parentNOInput.setAttribute("name", "parentNO");
			parentNOInput.setAttribute("value", parentNO);
			
			form.appendChild(parentNOInput);
			document.body.appendChild(form);
			form.submit();
		}
	</script>
</head>
<body>
	<form name="frmArticle" method="post" action="${contextPath }" enctype="multipart/form-data" >
	<table>
		<tr>
			<td class="bar">글번호</td>
			<td>
				<input type="hidden" name="articleNO" value="${article.articleNO }" />
				<input type="text" value="${article.articleNO }" disabled>
			</td>
		</tr>
		<tr>
			<td class="bar">작성자 아이디</td>
			<td><input type="text" value="${article.id }" name="writer" disabled></td>
		</tr>
		<tr>
			<td class="bar">글제목</td>
			<td><input type="text" value="${article.title }" name="title" id="i_title" disabled></td>
		</tr>
		<tr>
			<td class="bar">글내용</td>
			<td><textarea rows="20" cols="60" name="content" id="i_content" disabled>${article.content }</textarea></td>
		</tr>
		
		<c:if test="${not empty article.imageFileName && article.imageFileName != 'null' }" >
			<tr>
				<td class="bar" rowspan="2">이미지</td>
				<td>
					<input type="hidden" name="originalFileName" value="${article.imageFileName }" />
					<img src="${contextPath }/download.do?imageFileName=${article.imageFileName }&articleNO=${article.articleNO}" 
						id="preview" alt="이미지를 찾을 수 없습니다." width="300" height="400"><br>
				</td>
			</tr>
			<tr>
				<td>
					<input type="file" name="imageFileName" id="i_imageFileName" disabled onChange="readURL(this)" />
				</td>
			</tr>
		</c:if>
		<tr>
			<td class="bar">등록일자</td>
			<td><input type="text" value="<fmt:formatDate value='${article.writeDate }' pattern='YYYY/MM/dd HH:mm:ss'  />" disabled /></td>
		</tr>
		
		<tr id="tr_btn_modify">
			<td colspan="2" align="center">
				<input type="button" onclick="fn_modify_article(this.form)" value="수정완료">
				<input type="button" onclick="history.go(0);" value="취소">
			</td>
		</tr>
		<tr id="tr_btn">
			<td colspan="2" align="center">
				<input type="button" onclick="fn_enable(this.form)" value="수정하기">
				<input type="button" value="삭제하기"
					onClick="fn_remove_article('${contextPath}/board04/removeArticle.do', ${article.articleNO })" />
				<input type="button" onclick="backToList(this.form)" value="리스트로 돌아가기">
				<input type="button" value="답글쓰기"
					onClick="fn_reply_form('${contextPath }/board04/replyForm.do', ${article.articleNO})">
			</td>
		</tr>
	</table>
	</form>
</body>
</html>