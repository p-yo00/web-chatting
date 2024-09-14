<html>
<head>
    <title>User Information</title>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>

<button id="naver_id_login" onclick="getLoginUrl()">NAVER</button>

<script>
  function getLoginUrl() {
    axios.get('/login-url')
    .then(function (response) {
      location.href = response.data;
    })
    .catch(function () {
      alert("잠시후 다시 시도해주세요.")
    });
  }
</script>
</body>
</html>