<%--
  Created by IntelliJ IDEA.
  User: GM
  Date: 2018/2/20
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>创建客户</title>
</head>
<body>
    <h1>创建客户界面</h1>
    <form id="customer_form" enctype="multipart/form-data">
        <table>
            <tr>
                <td>客户名称:</td>
                <td><input type="text" name="name" value="${customer.name}">
                </td>
            </tr>
            <tr>
                <td>联系人:</td>
                <td><input type="text" name="contract" value="${customer.contract}">
                </td>
            </tr>

            <tr>
                <td>电话号码:</td>
                <td><input type="text" name="telephoen" value="${customer.telephone}">
                </td>
            </tr>

            <tr>
                <td>邮箱地址:</td>
                <td><input type="text" name="email" value="${customer.email}">
                </td>
            </tr>

            <tr>
                <td>照片:</td>
                <td><input type="text" name="photo" value="${customer.photo}">
                </td>
            </tr>

        </table>

        <button type="submit">保存</button>>
    </form>

<script src="${BASE}/asset/lib/jquery/jquery-3.3.1.min.js"></script>
<script src="${BASE}/asset/lib/jquery-form/jquery.form.min.js"></script>
<script>
    $(function(){
        $('customer.form').ajaxForm({
            type:'post',
            url:'${BASE}/customer_create',
            success:function(data){
                if(data){
                    location.herf = '${BASE}/customer';
                }
            }
        });
    });
</script>
</body>
</html>
