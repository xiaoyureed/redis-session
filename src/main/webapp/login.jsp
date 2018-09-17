<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>login</title>
    <link href="http://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <style type="text/css">

    </style>
    <script src="http://cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
</head>
<body>

<div class="container">
    <p>debug ---- uri: ${uri}</p>
    <form id="login-form">
        <input type="text" placeholder="name" name="name" required autofocus>
        <input type="password" placeholder="pass" name="pass" required>
        <button type="button" id="login-btn">login</button>
    </form>
</div>

<script type="text/javascript">
    $('#login-btn').click(function () {
        $.post(
            '${uri}/login',
            $('#login-form').serialize(),
            function (result) {
                if (result && "200" === result.code) {
                    window.location.href = '${uri}/hello';
                }
                else {
                    console.log('login fail');
                    alert(result.msg)
                }
            }
        );
    });
</script>
</body>
</html>
