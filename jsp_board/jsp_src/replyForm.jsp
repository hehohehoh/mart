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
	<title>Reply</title>
	<style>
		table{ margin:auto; }
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
	</script>
</head>
<body>
	<h1 style="text-align: center">답글 쓰기</h1>
	<form name="frmReply" method="post" action="${contextPath }/board04/addReply.do" encType="multipart/form-data">
		<table>
			<tr>
				<td align="right"> 글쓴이:&nbsp;</td>
				<td><input type="text" size="20" value="lee" disabled /></td>
			</tr>
			<tr>
				<td align="right"> 글제목:&nbsp;</td>
				<td><input type="text" size="40" name="title" maxlength="100" /></td>
			</tr>
			<tr>
				<td align="right" valign="top"><br> 글내용:&nbsp;</td>
				<td><textarea name="content" rows="10" cols="40" maxlength="2000"></textarea></td>
			</tr>
			<tr>
				<td align="right"> 이미지파일 첨부:&nbsp;</td>
				<td><input type="file" name="imageFileName" onchange="readURL(this);" /></td>
				<td><img id="preview" src="#" width=200 height=200 /></td>	
			</tr>
			<tr>
				<td></td>
				<td>
					<input type="submit" value="답글 반영하기" />
					<input type="button" value="취소" onClick="backToList(this.form)" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>